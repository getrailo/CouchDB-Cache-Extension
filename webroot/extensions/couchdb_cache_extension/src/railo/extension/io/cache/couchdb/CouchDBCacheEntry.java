package railo.extension.io.cache.couchdb;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.jcouchdb.document.Attachment;
import org.jcouchdb.document.Document;

import railo.commons.io.cache.CacheEntry;
import railo.extension.io.cache.CacheUtil;
import railo.loader.engine.CFMLEngineFactory;
import railo.runtime.type.Struct;

public class CouchDBCacheEntry implements CacheEntry {

	private String key;
	private Object value;
	private long idleTime;
	private long until;
	private long created;
	private long modifed;
	private long accessed;
	private int hitCount;
	
	private String _id;
	private Map document;
	
	
	
	public CouchDBCacheEntry(Map document,String key, Object value, Long idleTime, Long until) {
		this.document = document;
		this.key=key;
		this.value=value;
		this.idleTime=idleTime==null?Long.MIN_VALUE:idleTime.longValue();
		this.until=until==null?Long.MIN_VALUE:until.longValue();
		created=modifed=accessed=System.currentTimeMillis();
		hitCount=1;
		
	}
	public Struct getCustomInfo() {
		Struct info=CFMLEngineFactory.getInstance().getCreationUtil().createStruct();
		info.setEL("_rev", document.get("_rev"));
		return info;
	}
	
	public Date created() {
		Date myDate = new Date();		
		return myDate;
	}

	public String getKey() {
		return this.key;
	}

	public Object getValue() {
		return this.value;
	}

	public int hitCount() {
		throw new RuntimeException("method hitCount not implemented yet");
	}

	public long idleTimeSpan() {
		throw new RuntimeException("method idleTimeSpan not implemented yet");
	}

	public Date lastHit() {
		throw new RuntimeException("method lastHit not implemented yet");
	}

	public Date lastModified() {
		throw new RuntimeException("method lastModified not implemented yet");
	}

	public long liveTimeSpan() {
		throw new RuntimeException("method liveTimeSpan not implemented yet");
	}

	public long size() {
		throw new RuntimeException("method size not implemented yet");	}
	
	
}
