
<cfscript>
	stItem = {name:"elvis", location:"usa"};
	arrItem = [1,3,5,6,7,6];
	qry = QueryNew("name,age", "string,int");
	for(i=0; i<10;i++){
		QueryAddRow(qry);
		QuerySetCell(qry,"name", "name_#i#");
		QuerySetCell(qry,"age", 30 + i);
	
	}
	
	
	CachePut("struct", stItem);
	CachePut("array", arrItem);
	CachePut("query", qry);


	dump(stItem);
	dump(arrItem);
	dump(qry);	
	
	myStruct = CacheGet("struct");
	myArray = CacheGet("array");
	myQuery = CacheGet("query");
	dump(myStruct);
	dump(myArray);
	dump(myQuery);

</cfscript>



<cf_valueEquals left="#isStruct(variables.myStruct)#" right="true">
<cf_valueEquals left="#isArray(variables.myArray)#" right="true">
<cf_valueEquals left="#isquery(variables.myQuery)#" right="true">