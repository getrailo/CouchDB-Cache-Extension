package railo.extension.io.cache.couchdb;

import org.jcouchdb.document.BaseDocument;

import railo.extension.io.cache.CouchDBCaster;
import railo.runtime.exp.PageException;


public class CacheDocument extends BaseDocument{

	/* railo specific properties */
	private Object data;
	private String lastAccessed;
	private String expires;
	private String idleTime;
	private String createdDate;
	private int hitcount = 0;
	private String updatedDate;
	
	
	public CacheDocument() {
		super();
	}

	public Object getData() throws PageException{
		return  CouchDBCaster.toRailoObject(data);
	}
	
	public void setData(Object data) {
		try{
			
			this.data = CouchDBCaster.toCacheObject(data);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
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
	public boolean isValid(){
		long expireTime = new Long(expires).longValue();
		long created = new Long(createdDate).longValue();
		long idle = new Long(idleTime).longValue();
		long lasthit = new Long(lastAccessed).longValue();
		
		long now = System.currentTimeMillis();
		if(expireTime>0 && expireTime+created<now){
				return false;
		}
		if(idle>0 && idle+lasthit<now){
				return false;
		}
		return true;
		
		
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

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

}
