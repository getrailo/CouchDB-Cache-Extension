package railo.extension.io.cache.couchdb.tests;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.jcouchdb.db.Database;
import org.jcouchdb.document.Document;
import org.jcouchdb.document.ValueRow;
import org.jcouchdb.document.ViewResult;
import org.jcouchdb.exception.NotFoundException;
import org.jcouchdb.exception.UpdateConflictException;
import org.svenson.JSONParser;
import org.svenson.converter.DateConverter;
import org.svenson.converter.DefaultTypeConverterRepository;
import org.svenson.converter.TypeConverterRepository;

import railo.extension.io.cache.couchdb.CouchDBCacheDocument;
import railo.extension.io.cache.couchdb.CouchDBCacheEntry;

import java.util.List;



public class CouchDBtest {

	private static String host = "localhost";
	private static int port = 5984;
	private static String database = "railo_cache_test";
	
	
	public static void main(String[] args) {
		
		
		Database db = new Database(host, port, database);
		
		CouchDBCacheDocument doc = new CouchDBCacheDocument();

		doc.setId("testTime");
		doc.setCreatedDate(new Long(System.currentTimeMillis()).toString());
		doc.setExpires(new Long(System.currentTimeMillis() + 1000).toString());
		
		
		//Try getting the document first
		try{
			CouchDBCacheDocument docsaved = db.getDocument(CouchDBCacheDocument.class, doc.getId());
			db.delete(docsaved);
			
		}
		catch (Exception e){
		}
		db.createOrUpdateDocument(doc);

		
		//Now get the document
		CouchDBCacheDocument docsaved2 = db.getDocument(CouchDBCacheDocument.class, doc.getId());
		
		
		System.out.println("Done " + docsaved2.getCreatedDate() + " " + doc.getCreatedDate().equals(docsaved2.getCreatedDate()));
		
	}
	
	
	private static Object deserialize(Object object){
		return object;
	}

}
