package railo.extension.io.cache.couchdb.tests;

import java.util.Date;

import org.jcouchdb.db.Database;
import org.jcouchdb.db.ServerImpl;
import org.svenson.JSON;
import org.svenson.JSONConfig;
import org.svenson.JSONParser;
import org.svenson.converter.DateConverter;
import org.svenson.converter.DefaultTypeConverterRepository;
import org.svenson.converter.JSONConverter;

import railo.extension.io.cache.couchdb.JSONConfigFactory;



public class CouchDBtest {

	private static String host = "localhost";
	private static int port = 5984;
	private static String database = "railo_cache_test";
	
	
	public static void main(String[] args) {
		
		ServerImpl myserver = new ServerImpl("localhost");
		JSONConfigFactory factory = new JSONConfigFactory();
		JSONConfig config = factory.createJSONConfig();
		
		
		
		Database db = new Database(myserver, database);
		db.setJsonConfig(config);

		long objectid = System.currentTimeMillis();
		CustomObject obj = new CustomObject(String.valueOf(objectid), new Date());
		obj.setId(String.valueOf(objectid));
		db.createDocument(obj);
		
		System.out.println("Saved object");
		CustomObject document = db.getDocument(CustomObject.class, String.valueOf(objectid)); //errors here
		System.out.println("retrieved object"); 
	}
	
	
}
