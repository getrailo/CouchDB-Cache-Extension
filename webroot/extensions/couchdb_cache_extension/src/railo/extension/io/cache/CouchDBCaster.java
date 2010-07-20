package railo.extension.io.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.Map.Entry;

import railo.extension.io.cache.couchdb.CacheQuery;
import railo.loader.engine.CFMLEngineFactory;
import railo.runtime.Component;
import railo.runtime.exp.PageException;
import railo.runtime.type.Array;
import railo.runtime.type.Query;
import railo.runtime.type.Struct;
import railo.runtime.type.Collection.Key;
import railo.runtime.util.Cast;
import railo.runtime.util.Decision;

public class CouchDBCaster {

	private static Decision dec;
	private static Cast cas;

	public static Object toRailoObject(Object value) throws PageException {
		// Date
		/*
		 * if(value instanceof Date){ try {
		 * CFMLEngineFactory.getInstance().getCastUtil().toDateTime(value,
		 * null); } catch (Exception e) { return value; } //return DateTimeInew
		 * DateTimeI(((DateTime)value).getTime()); }
		 */

		if (value instanceof HashMap) {

			Struct struct = CFMLEngineFactory.getInstance().getCreationUtil()
					.createStruct();
			HashMap items = ((HashMap) value);

			Iterator iterator = items.keySet().iterator();

			for (Iterator iterator2 = iterator; iterator2.hasNext();) {
				Object object = (Object) iterator2.next();
				struct.put(object, items.get(object));

			}

			return struct;

		}
		if (value instanceof ArrayList) {
			Array array = CFMLEngineFactory.getInstance().getCreationUtil()
					.createArray();
			ArrayList items = (ArrayList) value;
			for (int i = 0; i < items.size(); i++) {
				array.append(items.get(i));
			}
			return array;
		}

		return value;
	}

	public static Object toCacheObject(Object value, Object defaultValue) {
		try {
			return toCacheObject(value);
		} catch (PageException e) {
			return defaultValue;
		}
	}

	public static Object toCacheObject(Object value) throws PageException {
		// Date
		// if(value instanceof DateTime){
		// return new Date(((DateTime)value).getTime());
		// }

		// if(cf instanceof Node) return toAMFObject((Node)cf);
		// if(cf instanceof Array) return
		// toAMFObject(ArrayAsList.toList((Array)cf));
		// if(cf instanceof Component) return toAMFObject((ComponentImpl)cf);
		if (value instanceof Query) {
			throw new RuntimeException("Query values have not been implemented yet");
			//return new CacheQuery((Query) value);

		}
		if (value instanceof Component){
			//Component cmp = (Component)value;
			throw new RuntimeException("Component values have not been implemented yet");
		}
		// if(cf instanceof Image) return toAMFObject((Image)cf);
		// if(cf instanceof Map) return toAMFObject((Map)cf);
		// if(cf instanceof Object[]) return toAMFObject((Object[])cf);
		// else if(value instanceof Struct) return new
		// StructWrap((Struct)value);//return toCacheObject((Struct)value);
		// else if(value instanceof Map) return toCacheObject((Map)value);

		// else if(dec().isArray(value)) return cas().toList(value, true);
		// else if(value instanceof List) return toCacheObject((List)value);
		// else if(value instanceof Array) return toCacheObject((Array)value);

		return value;
	}

	private static Object toCacheObject(List src) {
		ArrayList trg = new ArrayList(src.size());
		ListIterator it = src.listIterator();
		while (it.hasNext()) {
			trg.add(it.nextIndex(), it.next());
		}
		return trg;
	}

	private static Object toCacheObject(Array src) {
		ArrayList trg = new ArrayList();
		for (int i = 0; i < src.size(); i++)
			trg.add(i, src.get(i + 1, null));
		return trg;
	}

	private static Object toCacheObject(Struct sct) throws PageException {
		HashMap map = new HashMap();
		Key[] keys = sct.keys();
		for (int i = 0; i < keys.length; i++) {
			map.put(keys[i].getString(), toCacheObject(sct.get(keys[i], null)));
		}
		return map;
	}

	private static Object toCacheObject(Map _map) throws PageException {
		if (_map instanceof HashMap)
			return _map;
		if (_map instanceof Hashtable)
			return _map;
		if (_map instanceof WeakHashMap)
			return _map;

		HashMap map = new HashMap();
		Iterator it = _map.entrySet().iterator();
		Map.Entry entry;
		while (it.hasNext()) {
			entry = (Entry) it.next();
			map.put(entry.getKey(), toCacheObject(entry.getValue()));
		}
		return map;
	}

	private static Decision dec() {
		if (dec == null)
			dec = CFMLEngineFactory.getInstance().getDecisionUtil();
		return dec;
	}

	private static Cast cas() {
		if (cas == null)
			cas = CFMLEngineFactory.getInstance().getCastUtil();
		return cas;
	}
}
