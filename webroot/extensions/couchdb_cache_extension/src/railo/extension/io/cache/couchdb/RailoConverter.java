package railo.extension.io.cache.couchdb;

import java.util.HashMap;

import org.svenson.converter.TypeConverter;

import railo.extension.util.Functions;
import railo.runtime.exp.PageException;
import railo.runtime.type.Struct;

public class RailoConverter implements TypeConverter {

		private Functions func = new Functions();
	
	 public RailoConverter() {}
	
	public Object fromJSON(Object in){
		
		try{
			if(in instanceof HashMap){
				
				String inString = in.toString();
				if(inString.startsWith("{")){
					return func.deserializeJSON(inString);
				}
				
			}
		
		
			Object ret =func.evaluate(in); 
			return ret;
			
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException("Error converting " + in + " to object");
		}
	}

	public Object toJSON(Object in) {
		
		//Do we serialise everything? Maybe not?
		
		if(in instanceof Struct){
			try {
				return func.serializeJSON(in, false);
			} catch (PageException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try{
			return func.serialize(in);
			
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException("Error converting " + in + " to json");
		}
	}

}
