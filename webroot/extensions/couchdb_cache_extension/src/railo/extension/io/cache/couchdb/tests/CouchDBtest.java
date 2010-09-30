package railo.extension.io.cache.couchdb.tests;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jcouchdb.db.Database;
import org.jcouchdb.db.Options;
import org.jcouchdb.db.ServerImpl;
import org.jcouchdb.document.ValueRow;
import org.jcouchdb.document.ViewResult;
import org.svenson.JSON;
import org.svenson.JSONConfig;
import org.svenson.JSONParser;
import org.svenson.converter.DateConverter;
import org.svenson.converter.DefaultTypeConverterRepository;
import org.svenson.converter.JSONConverter;

import railo.extension.io.cache.couchdb.CacheDocument;
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

		
		
		Options opts = new Options();
		
		ViewResult result = db.query("_all_docs", CacheDocument.class, opts, null,null);
		
		List rows = result.getRows();
		for (Iterator iterator = rows.iterator(); iterator.hasNext();) {
			ValueRow object = (ValueRow) iterator.next();
			System.out.println(object.getId());
		}
		

		
	}
	
	
}
