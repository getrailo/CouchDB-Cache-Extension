package railo.extension.io.cache.couchdb.tests;

import java.util.Date;

import org.jcouchdb.document.BaseDocument;
import org.svenson.JSONProperty;
import org.svenson.converter.DateConverter;
import org.svenson.converter.JSONConverter;

public class CustomObject extends BaseDocument{

	

	private String name;
	private Date myDate;
	
	public CustomObject() {
		super();
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@JSONConverter( type = DateConverter.class)
	public Date getMyDate() {
		return myDate;
	}
	public void setMyDate(Date myDate) {
		this.myDate = myDate;
	}

	public CustomObject(String name, Date date) {
		this.name = name;
		this.myDate = date;
	}
	
	 @JSONProperty(value = "docType", readOnly = true)
	   public String getDocumentType()
	    {
	        return this.getClass().getSimpleName();
	    }
	
	

}
