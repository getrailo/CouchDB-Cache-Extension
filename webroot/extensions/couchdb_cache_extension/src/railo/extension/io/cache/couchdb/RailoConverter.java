package railo.extension.io.cache.couchdb;

import org.svenson.converter.TypeConverter;

import railo.extension.util.Functions;
import railo.runtime.exp.PageException;

public class RailoConverter implements TypeConverter {

		private Functions func = new Functions();
	
	 public RailoConverter() {}
	
	public Object fromJSON(Object in){
		try{
			System.out.println("about to convert " + in);
			Object ret =func.evaluate(in); 
			return ret;
			
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException("Error converting " + in + " to object");
		}
	}

	public Object toJSON(Object in) {
		try{
			return func.serialize(in);
			
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException("Error converting " + in + " to json");
		}
	}

}
