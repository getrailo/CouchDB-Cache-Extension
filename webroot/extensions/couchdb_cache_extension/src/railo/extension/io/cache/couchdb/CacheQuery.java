package railo.extension.io.cache.couchdb;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



import railo.runtime.type.Query;

public class CacheQuery {

	private Query query;
	
	public CacheQuery() {
		super();
	}
	public CacheQuery(Query query) {
		this.query = query;
		
	}
	
	public String[] getColumns(){
		return query.getColumns();
	}
	public void setColumns(String[] columns){
		//We shall do this implementation in a moment
		
	}
	
	
	
	
}
