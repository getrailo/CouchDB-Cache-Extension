package railo.extension.io.cache.couchdb;

import java.util.Date;


import railo.commons.io.cache.CacheEntry;
import railo.loader.engine.CFMLEngineFactory;
import railo.runtime.exp.PageException;
import railo.runtime.type.Struct;

public class CouchDBCacheEntry implements CacheEntry {

	private CacheDocument document;
	
	public CouchDBCacheEntry(CacheDocument docnew) {
		this.document = docnew;
	}
	public Struct getCustomInfo() {
		Struct info=CFMLEngineFactory.getInstance().getCreationUtil().createStruct();
		info.setEL("_rev", document.getRevision());
		return info;
	}
	
	public Date created() {
		Date myDate = new Date(new Long(document.getCreatedDate()));		
		return myDate;
	}

	public String getKey() {
		return document.getId();
	}

	public Object getValue(){
		try{
			return document.getData(); 
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public int hitCount() {
		return document.getHitcount();
		
	}

	public long idleTimeSpan() {
		return new Long(document.getIdleTime());
	}

	public Date lastHit() {
		return new Date(new Long(document.getLastAccessed()));
	}

	public Date lastModified() {
		return new Date(new Long(document.getLastAccessed()));
	}

	public long liveTimeSpan() {
		return new Long(document.getExpires());
	}

	public long size() {
		return 0;
	}
	
	
}
