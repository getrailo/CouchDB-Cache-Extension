package railo.extension.io.cache.couchdb;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.jcouchdb.document.Attachment;
import org.jcouchdb.document.BaseDocument;
import org.svenson.converter.DateConverter;
import org.svenson.converter.JSONConverter;

import railo.extension.io.cache.CacheCaster;
import railo.loader.engine.CFMLEngine;
import railo.loader.engine.CFMLEngineFactory;
import railo.runtime.PageContext;
import railo.runtime.exp.PageException;


public class CouchDBCacheDocument extends BaseDocument{

	/* railo specific properties */
	private Object data;
	private String lastAccessed;
	private String expires;
	private String idleTime;
	private String createdDate;
	private Boolean eternal;
	private int hitcount = 0;
	
	
	public Object getData() {
		return  CacheCaster.toRailoObject(data);
	}
	
	public void setData(Object data) {
		this.data = data;
	}

	

	public void setIdleTime(String idleTime) {
		this.idleTime = idleTime;
	}

	public String getIdleTime() {
		return idleTime;
	}

	public void setHitcount(int hitcount) {
		this.hitcount = hitcount;
	}

	public int getHitcount() {
		return hitcount;
	}

	//This checks whether the current Item is actually available to use
	public boolean isAvailable(){
		long currentTime = System.currentTimeMillis();
		long expireTime = new Long(expires).longValue() + new Long(createdDate).longValue();
		long idleExpireTime = new Long(idleTime).longValue() + new Long(lastAccessed).longValue();
		
		Date expireDate = new Date(expireTime);
		Date idleExpireDate = new Date(idleExpireTime);
		Date currentDate = new Date(currentTime);
		
		//Make them into dates so we can really see the details
		
		boolean notExpired = expireTime >= currentTime;
		boolean notTimedOut = idleExpireTime >= currentTime;
		
		if(eternal){
			return true;
		}
		
		if(notTimedOut && notExpired){
			return true;
		}
		
		return false;
		
	}

	public long getTimeToIdle() {
		// TODO Auto-generated method stub
		return 0;
	}

	public long getTimeToLive() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getLastAccessed() {
		return lastAccessed;
	}

	public void setLastAccessed(String lastAccessed) {
		this.lastAccessed = lastAccessed;
	}

	public String getExpires() {
		return expires;
	}

	public void setExpires(String expires) {
		this.expires = expires;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public void setEternal(Boolean eternal) {
		this.eternal = eternal;
	}

	public Boolean getEternal() {
		return eternal;
	}


	


	
	
	/*


	public int getHitcount() {
		return hitcount;
	}

	public void setHitcount(int hitcount) {
		this.hitcount = hitcount;
	}



	public void setIdletime(Long idletime) {
		this.idletime = idletime;
	}

	public Long getIdletime() {
		return idletime;
	}

	public void setUntil(Long until) {
		this.until = until;
	}

	public Long getUntil() {
		return until;
	}
	
	public Boolean getEternal() {
		return eternal;
	}

	public void setEternal(Boolean eternal) {
		this.eternal = eternal;
	}

	public Integer getIdle() {
		return idle;
	}

	public void setIdle(Integer idle) {
		this.idle = idle;
	}

	public Integer getLive() {
		return live;
	}

	public void setLive(Integer live) {
		this.live = live;
	}


	*/
}
