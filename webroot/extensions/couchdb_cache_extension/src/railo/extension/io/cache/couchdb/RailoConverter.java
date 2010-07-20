package railo.extension.io.cache.couchdb;

import org.svenson.converter.TypeConverter;

import railo.extension.util.Functions;
import railo.runtime.exp.PageException;

public class RailoConverter implements TypeConverter {

		private Functions func = new Functions();
	
	 public RailoConverter() {}
	
	public Object fromJSON(Object in){
		try{
			return func.deserializeJSON(in.toString());
			
		}catch(Exception e){
			return "ERROR";
		}
	}

	public Object toJSON(Object in) {
		try{
			return func.serializeJSON(in, false);
			
		}catch(Exception e){
			return "ERROR";
		}
	}

}
