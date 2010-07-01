package railo.extension.io.cache.couchdb.tests;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.jcouchdb.db.Database;
import org.jcouchdb.document.Document;
import org.jcouchdb.document.ValueRow;
import org.jcouchdb.document.ViewResult;

import railo.extension.io.cache.couchdb.CouchDBCacheEntry;

import java.util.List;



public class CouchDBtest {

	private static String host = "localhost";
	private static int port = 5984;
	private static String database = "railo_cache_test";
	
	
	public static void main(String[] args) {

		Database db = new Database(host, port, database);
		ViewResult vresult = db.listDocuments(null, null);
		
		List list=new ArrayList();
		
		Iterator it = vresult.getRows().iterator();
		CouchDBCacheEntry entry;
		while(it.hasNext()){
			ValueRow row = (ValueRow)it.next();
			Map doc = db.getDocument(Map.class, row.getId());
			db.delete(row.getId(), (String)doc.get("_rev"));
		}
		
		System.out.println("Test Passed!");
		
	}
	
	
	private static Object deserialize(Object object){
		return object;
	}

}
