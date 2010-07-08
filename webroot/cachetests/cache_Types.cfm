
<cfset CachePut("struct", {name:"elvis", location:"usa"})>

<cfset elvis = CacheGet("struct")>

