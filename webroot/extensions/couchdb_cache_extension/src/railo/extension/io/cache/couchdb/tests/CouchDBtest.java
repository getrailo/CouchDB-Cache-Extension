package railo.extension.io.cache.couchdb.tests;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.jcouchdb.db.Database;
import org.jcouchdb.document.Document;
import org.jcouchdb.document.ValueRow;
import org.jcouchdb.document.ViewResult;
import org.jcouchdb.exception.NotFoundException;
import org.jcouchdb.exception.UpdateConflictException;

import railo.extension.io.cache.couchdb.CouchDBCacheDocument;
import railo.extension.io.cache.couchdb.CouchDBCacheEntry;

import java.util.List;



public class CouchDBtest {

	private static String host = "localhost";
	private static int port = 5984;
	private static String database = "railo_cache_test";
	
	
	public static void main(String[] args) {

		//Our own specific couchDB Cache Document
		Database db = new Database(host, port, database);
		
		try {
			CouchDBCacheDocument doc2 = db.getDocument(CouchDBCacheDocument.class, "bubba");
		}
		catch(NotFoundException nfe){
			System.out.println("Document not found");
		}
		
		
		
	}
	
	
	private static Object deserialize(Object object){
		return object;
	}

}
