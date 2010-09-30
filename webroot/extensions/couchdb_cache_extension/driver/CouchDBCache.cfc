<cfcomponent extends="Cache">

    <cfset fields=array(
			field(	displayName="Server Host",
					name="host",
					defaultValue="localhost",
					required=true,
					description="The host that the CouchDB server is running on",
					type="text"
				),
			field(	displayName="Server Port",
					name="port",
					defaultValue="5984",
					required=true,
					description="The port that the CouchDB server is running on",
					type="text"
				),
					
			field(	displayName="Database",
					name="database",
					defaultValue="",
					required=true,
					description="The name of the database on the CouchDB server to use",
					type="text"
				)
		)>
				
		<!--- /*
			
					displayName	true	string	null	
					name	true	string	null	
					defaultValue	false	string		
					required	false	boolean	no	
					description	false	any		
					type	false	string	text	
					values	false	string	
						
							
		field("Server Address","server","false",true,"Sets whether elements are eternal. If eternal, timeouts are ignored and the element is never expired","checkbox","true"),

		field("Maximal elements in memory","maxelementsinmemory","10000",true,"Sets the maximum objects to be held in memory","text"),
		field("Memory Store Eviction Policy","memoryevictionpolicy","LRU,LFU,FIFO",true,"The algorithm to used to evict old entries when maximum limit is reached, such as LRU (least recently used), LFU (least frequently used) or FIFO (first in first out).","select"),
		field("Time to idle in seconds","timeToIdleSeconds","86400",true,"Sets the time to idle for an element before it expires. Is only used if the element is not eternal","time"),
		field("Time to live in seconds","timeToLiveSeconds","86400",true,"Sets the timeout to live for an element before it expires. Is only used if the element is not eternal","time"),
		
		//group("Disk","Hard disk specific settings"),
		field("Disk persistent","diskpersistent","true",true,"for caches that overflow to disk, whether the disk store persists between restarts of the Engine.","checkbox","true"),
		field("Overflow to disk","overflowtodisk","true",true,"for caches that overflow to disk, the disk cache persist between CacheManager instances","checkbox","true"),
		field("Maximal elements on disk","maxelementsondisk","10000000",true,"Sets the maximum number elements on Disk. 0 means unlimited","text")
		
		*/
		 --->
		
		
		

	<cffunction name="getClass" returntype="string">
    	<cfreturn "railo.extension.io.cache.couchdb.CouchDBCache">
    </cffunction>
    
	<cffunction name="getLabel" returntype="string" output="no">
    	<cfreturn "CouchDB">
    </cffunction>
	<cffunction name="getDescription" returntype="string" output="no">
    	<cfset var c="">
    	<cfsavecontent variable="c">
This is the CouchDB Cache implementation for Railo. This allows you to cache objects, primitives and queries in a CouchDB server that can be used as a cache. 
        </cfsavecontent>
    
    
    	<cfreturn trim(c)>
    </cffunction>
</cfcomponent>