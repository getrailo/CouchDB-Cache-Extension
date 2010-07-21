package railo.extension.io.cache.couchdb;

import org.svenson.ClassNameBasedTypeMapper;
import org.svenson.JSON;
import org.svenson.JSONConfig;
import org.svenson.JSONParser;
import org.svenson.converter.DateConverter;
import org.svenson.converter.DefaultTypeConverterRepository;
import org.svenson.matcher.SubtypeMatcher;

import railo.extension.io.cache.couchdb.tests.CustomObject;

public class JSONConfigFactory {

	  public JSONConfigFactory() {
		super();
	}

	public JSONConfig createJSONConfig()
	    {
	       

	        
	        
	        DefaultTypeConverterRepository typeConverterRepository = new DefaultTypeConverterRepository();
	        typeConverterRepository.addTypeConverter(new DateConverter());
	     //  typeConverterRepository.addTypeConverter(new RailoConverter());
	        
	        
	        JSONParser parser = new JSONParser();
	        parser.setTypeConverterRepository(typeConverterRepository);

	        JSON json = new JSON();
	        json.setTypeConverterRepository(typeConverterRepository);

	        return new JSONConfig(json, parser);
	    }

}
