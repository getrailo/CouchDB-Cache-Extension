package railo.extension.io.cache.couchdb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jcouchdb.db.Database;
import org.jcouchdb.document.ValueRow;
import org.jcouchdb.document.ViewResult;
import org.jcouchdb.exception.NotFoundException;
import org.jcouchdb.exception.UpdateConflictException;
import org.svenson.JSONConfig;


import railo.commons.io.cache.Cache;
import railo.commons.io.cache.CacheEntry;
import railo.commons.io.cache.CacheEntryFilter;
import railo.commons.io.cache.CacheKeyFilter;
import railo.commons.io.cache.exp.CacheException;

import railo.loader.engine.CFMLEngine;
import railo.loader.engine.CFMLEngineFactory;
import railo.runtime.config.Config;
import railo.runtime.type.Struct;
import railo.runtime.util.Cast;

public class CouchDBCache implements Cache{
	
	private String cacheName = "";
	private  String host = "";
	private int port = 5984;
	private String database = "";
	
	private int hits = 0;
	private int misses = 0;

	/**
	 * @deprecated this method is no longer used by railo and ignored as long the method <code>init(Config config, String cacheName, Struct arguments)</code> exists
	 */
	public void init(String cacheName, Struct arguments) throws IOException {
		//Not used at the moment
	}
	
	public void init(Config config ,String[] cacheName,Struct[] arguments){
		//Not used at the moment
	}

	public void init(Config config, String cacheName, Struct arguments) {
		
		this.cacheName = cacheName;
		CFMLEngine engine = CFMLEngineFactory.getInstance();
		Cast caster = engine.getCastUtil();
	
		try {
			this.host = caster.toString(arguments.get("host"));
			this.port = caster.toIntValue(arguments.get("port"));
			this.database = caster.toString(arguments.get("database"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void put(String key, Object value, Long idleTime, Long lifespan) {
		Long created = System.currentTimeMillis();
		long idle = idleTime==null?0:idleTime.longValue();
		long life = lifespan==null?0:lifespan.longValue();
		
		
		//String = liveTime==null?null:new Integer((int)liveTime.longValue()/1000);
		
		CacheDocument doc = new CacheDocument();
			doc.setId(key);
			
			
			doc.setData(value);
			doc.setCreatedDate(new Long(created).toString());
			doc.setLastAccessed(new Long(created).toString());
			doc.setIdleTime(String.valueOf(idle));
			doc.setExpires(String.valueOf(life));

			quickSave(doc);
		
	}
	
	private void quickSave(CacheDocument doc){
		Database db = new Database(host, port, database);
		
		//We should add the JSON type factory
		JSONConfigFactory factory = new JSONConfigFactory();
		JSONConfig config = factory.createJSONConfig();
		db.setJsonConfig(config);
		
		CacheDocument existingdoc = null;
			
		//Try and save it first. if that fails then we set the revision
			
		try{
			db.createDocument(doc);
			
		}catch (UpdateConflictException e) {
			existingdoc = db.getDocument(CacheDocument.class, doc.getId());
			doc.setUpdatedDate(new Long(System.currentTimeMillis()).toString());
			doc.setRevision(existingdoc.getRevision());
			db.updateDocument(doc);
		}
		catch (IllegalStateException e) {
			existingdoc = db.getDocument(CacheDocument.class, doc.getId());
			doc.setUpdatedDate(new Long(System.currentTimeMillis()).toString());
			doc.setRevision(existingdoc.getRevision());
			db.updateDocument(doc);
		}
		
		catch (Exception e) {
			e.printStackTrace();
		}
			
	}
	
	public CouchDBCacheEntry getCacheEntry(String key, CacheEntry defaultValue) {
		try{
			hits++;
			return getCacheEntry(key);
		}
		catch(Exception nfe){
			misses++;
			CacheDocument defaultdoc = new CacheDocument();
				defaultdoc.setData(defaultValue);
			return new CouchDBCacheEntry(defaultdoc);
		}
	}
	
	
	/* Go and get a cache entry, throws an IO Exception if not found */
	
	public CouchDBCacheEntry getCacheEntry(String key) throws CacheException{
		Database db = new Database(host, port, database);
		
		CacheDocument docnew = db.getDocument(CacheDocument.class, key);
		
		boolean available = docnew.isValid();
		long currenttime = System.currentTimeMillis();
		if(!available){
			throw new CacheException("there is no entry in cache with key ["+key+"]");
		}

		System.out.println("getting " + key);
		
		int objHitcount = docnew.getHitcount();
			docnew.setHitcount(objHitcount+1);
			docnew.setLastAccessed(new Long(System.currentTimeMillis()).toString());
			quickSave(docnew);
			
			
			
		return new CouchDBCacheEntry(docnew);		
		
	}


	public boolean contains(String item) {
		
		Database db = new Database(host, port, database);
		ViewResult vresult = db.listDocuments(null, null);
		
		List list=new ArrayList();
		Iterator it = vresult.getRows().iterator();
		while(it.hasNext()){
			ValueRow row = (ValueRow)it.next();
			if(row.getId().equalsIgnoreCase(item)){
				return true;
			}
			
		}
		
		return false;
	}

	public List entries() {
		
		Database db = new Database(host, port, database);
		ViewResult vresult = db.listDocuments(null, null);
		
		List list=new ArrayList();
		Iterator it = vresult.getRows().iterator();
		while(it.hasNext()){
			ValueRow row = (ValueRow)it.next();
			try{
				list.add(getCacheEntry(row.getId()));
			}
			catch (Exception e){}
			
		}
		
		return list;
	}

	public List entries(CacheKeyFilter filter) {
		
		Database db = new Database(host, port, database);
		ViewResult vresult = db.listDocuments(null, null);
		List list=new ArrayList();
		Iterator it = vresult.getRows().iterator();
		while(it.hasNext()){
			ValueRow row = (ValueRow)it.next();
			try{
				CouchDBCacheEntry entry = getCacheEntry(row.getId());
				if(filter.accept(row.getId())){
					list.add(entry);
				}
			}
			catch(Exception e){}
		}
		
		return list;
	}

	public List entries(CacheEntryFilter filter) {
		throw new RuntimeException("method entries(CacheEntryFilter filter) not implemented yet");
	}

	public Struct getCustomInfo() {
		Struct info=CFMLEngineFactory.getInstance().getCreationUtil().createStruct();
		return info;
	}

	public Object getValue(String key) throws CacheException {
		try {
			misses++;
			CouchDBCacheEntry el = getCacheEntry(key);
			if(el==null)throw new CacheException("there is no entry in cache with key ["+key+"]");
			misses--;
			hits++;
			return el.getValue();
		}
		catch(IllegalStateException ise) {
			throw new CacheException(ise.getMessage());
		}
		
	}

	/**
	 * @see railo.commons.io.cache.Cache#getValue(String, java.lang.Object)
	 */
	public Object getValue(String key, Object defaultValue) {
		try {
			return getValue(key);
		}
		catch(Exception e){
			return defaultValue;
		}
	}


	public long hitCount() {
		return hits;
	}

	

	public List keys() {
		Database db = new Database(host, port, database);
		ViewResult vresult = db.listDocuments(null, null);
		List list=new ArrayList();
		Iterator it = vresult.getRows().iterator();
		CouchDBCacheEntry entry;
		while(it.hasNext()){
			ValueRow row = (ValueRow)it.next();
			list.add(row.getId());
		}
		return list;
	}

	public List keys(CacheKeyFilter filter) {
		Database db = new Database(host, port, database);
		ViewResult vresult = db.listDocuments(null, null);
		List list=new ArrayList();
		Iterator it = vresult.getRows().iterator();
		CouchDBCacheEntry entry;
		while(it.hasNext()){
			ValueRow row = (ValueRow)it.next();
			if(filter.accept(row.getId())){
				list.add(row.getId());
				
			}
		}
		return list;
	}

	public List keys(CacheEntryFilter filter) {
		throw new RuntimeException("method keys(CacheEntryFilter filter) not implemented yet");
	}

	public long missCount() {
		return misses;
	}

	

	public boolean remove(String key) {
		Database db = new Database(host, port, database);
		try{
			Map doc = db. getDocument(Map.class, key);
			db.delete((String)doc.get("_id"), (String)doc.get("_rev"));
			return true;
			
		} catch (NotFoundException nfe){
			return false;
		}
	}

	public int remove(CacheKeyFilter filter) {
		Database db = new Database(host, port, database);
		ViewResult vr = db.listDocuments(null, null);
		List rows = vr.getRows();
		Iterator iterator = rows.iterator();
		int counter = 0;
		while (iterator.hasNext()) {
			ValueRow row = (ValueRow)iterator.next();
			Map doc = db.getDocument(Map.class, row.getId());
			if(filter.accept(row.getId())){
				db.delete(row.getId(), (String)doc.get("_rev"));
				counter++;
				
			}
		}
		return counter;
	}

	public int remove(CacheEntryFilter filter) {
		throw new RuntimeException("method values not implemented yet");
	}

	public List values() {
		throw new RuntimeException("method values not implemented yet");
	}

	public List values(CacheKeyFilter arg0) {
		throw new RuntimeException("method values not implemented yet");
	}

	public List values(CacheEntryFilter arg0) {
		throw new RuntimeException("method values not implemented yet");
	}
	


}
