package railo.extension.io.cache.couchdb;

import java.util.Date;


import railo.commons.io.cache.CacheEntry;
import railo.loader.engine.CFMLEngineFactory;
import railo.runtime.type.Struct;

public class CouchDBCacheEntry implements CacheEntry {

	private CouchDBCacheDocument document;
	
	public CouchDBCacheEntry(CouchDBCacheDocument docnew) {
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

	public Object getValue() {
		return document.getData(); 
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
