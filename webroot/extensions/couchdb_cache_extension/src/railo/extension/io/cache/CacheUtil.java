package railo.extension.io.cache;

import railo.commons.io.cache.Cache;
import railo.commons.io.cache.CacheEntry;
import railo.loader.engine.CFMLEngineFactory;
import railo.runtime.type.Struct;
import railo.runtime.type.dt.TimeSpan;

public class CacheUtil {

	public static Struct getInfo(CacheEntry ce) {
		Struct info=CFMLEngineFactory.getInstance().getCreationUtil().createStruct();
		info.setEL("created", ce.created());
		info.setEL("last_hit", ce.lastHit());
		info.setEL("last_modified", ce.lastModified());

		info.setEL("hit_count", new Double(ce.hitCount()));
		info.setEL("size", new Double(ce.size()));
		
		
		info.setEL("idle_time_span", toTimespan(ce.idleTimeSpan()));		
		info.setEL("live_time_span", toTimespan(ce.liveTimeSpan()));
		
		
		return info;
	}


	public static Struct getInfo(Cache c) {
		Struct info=CFMLEngineFactory.getInstance().getCreationUtil().createStruct();

		long value = c.hitCount();
		if(value>=0)info.setEL("hit_count", new Double(value));
		value = c.missCount();
		if(value>=0)info.setEL("miss_count", new Double(value));
		
		return info;
	}

	
	public static Object toTimespan(long timespan) {
		if(timespan==0)return "";
		TimeSpan ts = CFMLEngineFactory.getInstance().getCastUtil().toTimespan(new Double(timespan/(24D*60D*60D*1000)),null);
		if(ts==null)return "";
		return ts;
	}


	public static String toString(CacheEntry ce) {

		return "created:	"+ce.created()
		+"\nlast-hit:	"+ce.lastHit()
		+"\nlast-modified:	"+ce.lastModified()
		
		+"\nidle-time:	"+ce.idleTimeSpan()
		+"\nlive-time	:"+ce.liveTimeSpan()
		
		+"\nhit-count:	"+ce.hitCount()
		+"\nsize:		"+ce.size();
	}
}
