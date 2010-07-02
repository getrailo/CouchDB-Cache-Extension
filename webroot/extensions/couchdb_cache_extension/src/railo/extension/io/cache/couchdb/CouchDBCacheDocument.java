package railo.extension.io.cache.couchdb;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.jcouchdb.document.Attachment;
import org.jcouchdb.document.BaseDocument;
import org.svenson.converter.DateConverter;
import org.svenson.converter.JSONConverter;


public class CouchDBCacheDocument extends BaseDocument{

	/* railo specific properties */
	private Long idletime ;
	private Long until;
	
	
	private String created;
	private int hitcount = 0;
	private Object data;
	
	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public int getHitcount() {
		return hitcount;
	}

	public void setHitcount(int hitcount) {
		this.hitcount = hitcount;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
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

}
