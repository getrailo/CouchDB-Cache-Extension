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
	        JSONParser parser = new JSONParser();

	        
	        
	        DefaultTypeConverterRepository typeConverterRepository = new DefaultTypeConverterRepository();
	        typeConverterRepository.addTypeConverter(new DateConverter());
	        typeConverterRepository.addTypeConverter(new RailoConverter());
	        
	        ClassNameBasedTypeMapper typeMapper = new ClassNameBasedTypeMapper();
	        typeMapper.setBasePackage(CustomObject.class.getPackage().getName());
	        // we only want to have AppDocument instances
	        typeMapper.setEnforcedBaseType(CustomObject.class);
	        // we use the docType property of the AppDocument 
	        typeMapper.setDiscriminatorField("docType");
	        
	        // we only want to do the expensive look ahead if we're being told to
	        typeMapper.setPathMatcher(new SubtypeMatcher(CustomObject.class));
	        // we use the new sub parser.setTypeMapper(typeMapper);
	        parser.setTypeMapper(typeMapper);
	        parser.setTypeConverterRepository(typeConverterRepository);

	        JSON json = new JSON();
	        json.setTypeConverterRepository(typeConverterRepository);

	        return new JSONConfig(json, parser);
	    }

}
