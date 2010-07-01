package railo.extension.io.cache.couchdb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.jcouchdb.db.Database;
import org.jcouchdb.document.Document;
import org.jcouchdb.document.ValueRow;
import org.jcouchdb.document.ViewResult;
import org.jcouchdb.exception.NotFoundException;


import railo.commons.io.cache.Cache;
import railo.commons.io.cache.CacheEntry;
import railo.commons.io.cache.CacheEntryFilter;
import railo.commons.io.cache.CacheKeyFilter;
import railo.commons.io.cache.exp.CacheException;
import railo.extension.util.Functions;

import railo.loader.engine.CFMLEngine;
import railo.loader.engine.CFMLEngineFactory;
import railo.runtime.PageContext;
import railo.runtime.config.Config;
import railo.runtime.exp.PageException;
import railo.runtime.type.Struct;
import railo.runtime.util.Cast;

public class CouchDBCache implements Cache{
	
	//private Map entries= new ReferenceMap();
	//private long missCount;
	//private int hitCount;
	/*
	 * 
	 * 
	 	CFMLEngine engine = CFMLEngineFactory.getInstance();
		PageContext pc = engine.getThreadPageContext();
		pc.evaluate("serializeJson(...)");
	 */
	private String cacheName = "";
	private  String host = "";
	private int port = 5984;
	private String database = "";
	
	private int hits;
	private int misses;

	
	
	public void init(String cacheName, Struct arguments) throws IOException {
		log("Starting up the Extensions 1, "  + cacheName + " args " + arguments);
	//		init(null, cacheName, arguments);
	}
	
	public void init(Config config ,String[] cacheName,Struct[] arguments){
		log("Starting up the Extensions 2, "  + cacheName + " args " + arguments);
		//init(config, cacheName, arguments);
	}

	public void init(Config config, String cacheName, Struct arguments) {
		log("Starting up the Extensions 3, "  + cacheName + " args " + arguments);
		
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
	
	public void put(String key, Object value, Long idleTime, Long until) {
		
		Object data = value;
		CFMLEngine engine = CFMLEngineFactory.getInstance();
		PageContext pc = engine.getThreadPageContext();
		
		try {
			pc.setVariable("mySerialize", data);
			data = pc.evaluate("serialize(mySerialize)");
			pc.setVariable("mySerialize", data);
			data = pc.evaluate("serializeJSON(mySerialize)");
			
		} catch (PageException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		//Get an item first, get it's revision and then save it, this will keep track of stuff much easier. 
		
		
		Database db = new Database(host, port, database);
		
		try {
			Map doc = new HashedMap();
			doc.put("_id", key);
			doc.put("data", data);
			doc.put("idleTime", idleTime);
			doc.put("until", until);
			doc.put("created", new Date());
			db.createOrUpdateDocument(doc);
			
			}
		catch (org.jcouchdb.exception.UpdateConflictException e) {
			
			Map docnew2 = db.getDocument(Map.class, key);
			db.delete(docnew2);
			Map doc = new HashedMap();
			doc.put("_id", key);
			doc.put("data", data);	
			doc.put("idleTime", idleTime);
			doc.put("until", until);
			db.createOrUpdateDocument(doc);
		}
		
		
	}
	
	public CouchDBCacheEntry getCacheEntry(String key, CacheEntry defaultValue) {
		try{
			return getCacheEntry(key);
		}
		catch(Exception nfe){
			return new CouchDBCacheEntry(new HashMap(),key, defaultValue, null, null);
		}
	}
	
	
	/* Go and get a cache entry, throws an IO Exception if not found */
	
	public CouchDBCacheEntry getCacheEntry(String key){
		Database db = new Database(host, port, database);
		Map docnew = new HashMap();
		Object retString = "";
			
		docnew = db.getDocument(Map.class, key);
		retString = docnew.get("data");	
		
		CFMLEngine engine = CFMLEngineFactory.getInstance();
		PageContext pc = engine.getThreadPageContext();
		try {
			pc.setVariable("myDeserializer", retString);
			retString = pc.evaluate("deserializejson(myDeserializer)");
			pc.setVariable("myDeserializer", retString);
			retString = pc.evaluate("evaluate(myDeserializer)");
		} catch (PageException e) {
			e.printStackTrace();
		}
		
		return new CouchDBCacheEntry(docnew,key, retString, null, null);
	}


	private Object deserialize(Object object){
		Object retString = "";
		CFMLEngine engine = CFMLEngineFactory.getInstance();
		PageContext pc = engine.getThreadPageContext();
		try {
			pc.setVariable("myDeserializer", retString);
			retString = pc.evaluate("deserializejson(myDeserializer)");
			pc.setVariable("myDeserializer", object);
			retString = pc.evaluate("evaluate(myDeserializer)");
		} catch (PageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retString;
	}
	
	private Object serialize(Object object){
		Object data = object;
		CFMLEngine engine = CFMLEngineFactory.getInstance();
		PageContext pc = engine.getThreadPageContext();
		
		try {
			pc.setVariable("mySerialize", data);
			data = pc.evaluate("serialize(mySerialize)");
			pc.setVariable("mySerialize", data);
			data = pc.evaluate("serializeJSON(mySerialize)");
			
		} catch (PageException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return data;
	}
	
	public boolean contains(String arg0) {
		throw new RuntimeException("method contains not implemented yet");
	}

	public List entries() {
		
		Database db = new Database(host, port, database);
		ViewResult vresult = db.listDocuments(null, null);
		
		List list=new ArrayList();
		Iterator it = vresult.getRows().iterator();
		while(it.hasNext()){
			ValueRow row = (ValueRow)it.next();
			list.add(getCacheEntry(row.getId()));
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
			
			CouchDBCacheEntry entry = getCacheEntry(row.getId());
			if(filter.accept(row.getId())){
				list.add(entry);
			}
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
		} catch (IOException e) {
			throw new CacheException(e.getMessage());
		}
		
	}

	/**
	 * @see railo.commons.io.cache.Cache#getValue(String, java.lang.Object)
	 */
	public Object getValue(String key, Object defaultValue) {
			Object retval = null;
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
			
		} catch (NotFoundException nfe){
			
		}
		
		//if the document exists, then we can delete it 
		return false;
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
			db.delete(row.getId(), (String)doc.get("_rev"));
			counter++;
		}
		return counter;
	}

	public int remove(CacheEntryFilter filter) {
		return remove(filter);
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
	
	private void log(String logStatement){
		
		System.out.println(logStatement);
		
	}

}
