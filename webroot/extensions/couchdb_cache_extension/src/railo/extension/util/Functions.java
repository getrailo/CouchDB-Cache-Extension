package railo.extension.util;

import java.lang.reflect.Method;

import railo.loader.engine.CFMLEngine;
import railo.loader.engine.CFMLEngineFactory;
import railo.runtime.PageContext;
import railo.runtime.exp.PageException;
import railo.runtime.util.Cast;
public class Functions {

	private static final String SERIALIZE_JSON_CLASS="railo.runtime.functions.conversion.SerializeJSON";
	private static final String DESERIALIZE_JSON_CLASS="railo.runtime.functions.conversion.DeSerializeJSON";
	
	private static final String SERIALIZE_CLASS="railo.runtime.functions.dynamicEvaluation.Serialize";
	private static final String EVALUATE_CLASS="railo.runtime.functions.dynamicEvaluation.Evaluate";
	
	private Method serializeJSON;
	private Method deserializeJSON;
	
	private Method serialize;
	private Method evaluate;
	
	private CFMLEngine engine;
	

	public String serializeJSON(Object var,boolean serializeQueryByColumns) throws PageException{
		return serializeJSON(pc(), var, serializeQueryByColumns);
	}
	
	public String serializeJSON(PageContext pc, Object var,boolean serializeQueryByColumns) throws PageException{
		Cast caster = engine.getCastUtil();
		try {
			if(serializeJSON==null){
				// Need ClassLoader from core
				ClassLoader cl = pc.getClass().getClassLoader();
			
				// load method
				Class clazz = cl.loadClass(SERIALIZE_JSON_CLASS);
				serializeJSON=clazz.getMethod("call", new Class[]{Object.class,boolean.class});
			}
			return caster.toString(serializeJSON.invoke(null, new Object[]{var,caster.toBoolean(serializeQueryByColumns)}));
		}
		catch(Throwable t){
			throw caster.toPageException(t);
		}
	}
	
	


	
	public String serialize(Object var) throws PageException{
		return serialize(pc(), var);
	}
	
	public String serialize(PageContext pc, Object var) throws PageException{
		Cast caster = engine.getCastUtil();
		try {
			if(serialize==null){
				// Need ClassLoader from core
				ClassLoader cl = pc.getClass().getClassLoader();
			
				// load method
				Class clazz = cl.loadClass(SERIALIZE_CLASS);
				serialize=clazz.getMethod("call", new Class[]{Object.class});
			}
			return caster.toString(serialize.invoke(null, new Object[]{var}));
		}
		catch(Throwable t){
			throw caster.toPageException(t);
		}
	}
	
	

	private PageContext pc() {
		return engine().getThreadPageContext();
	}
	private CFMLEngine engine() {
		if(engine==null)
			engine=CFMLEngineFactory.getInstance();
		return engine;
	}
}
