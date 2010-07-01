<cflock scope="server" timeout="1">


	<cfset cacheName="sample">
	<cfset cacheRemove(arrayToList(cacheGetAllIds()))>
	
	<cfset cachePut('abc','123')>
	<cfset cacheGetMetadata('abc')>
	
<cfif server.ColdFusion.ProductName EQ "railo"> 
	<cfset cachePut('abc','123')>
	<cfset cacheGetMetadata('abc')>
</cfif>
</cflock>