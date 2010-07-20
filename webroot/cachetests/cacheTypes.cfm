
<cfscript>
	stItem = {name:"elvis", location:"usa"};
	arrItem = [1,3,5,6,7,6];
	qry = QueryNew("name,age", "string,int");
	for(i=0; i<10;i++){
		QueryAddRow(qry);
		QuerySetCell(qry,"name", "name_#i#");
		QuerySetCell(qry,"age", 30 + i);
	
	}
	
	stItem2 = Duplicate(stItem);
	stItem2.array = arrItem;
	
	cComp = CreateObject("component", "TestObject");
	cComp.setName("elvis");
	cComp.setAge(36);
	
	
	CachePut("struct", stItem);
	CachePut("array", arrItem);
	CachePut("query", qry);
	CachePut("mixed", stItem2);
	CachePut("obj", cComp);
/*
	dump(serialize(stItem));
	dump(arrItem);
	dump(qry);	
	dump(stItem2);
	dump(cComp);
*/
	my.Struct = CacheGet("struct");
	my.Array = CacheGet("array");
	my.Query = CacheGet("query");
	my.Mixed = CacheGet("mixed");
	my.Obj = CacheGet("obj");
	
</cfscript>


<cfdump var="#variables#" abort>
<cf_valueEquals left="#isStruct(variables.myStruct)#" right="true">
<cf_valueEquals left="#isArray(variables.myArray)#" right="true">
<cf_valueEquals left="#isquery(variables.myQuery)#" right="true">
<cf_valueEquals left="#isStruct(variables.myMixed)#" right="true">
<cf_valueEquals left="#isArray(variables.myMixed.array)#" right="true">
<cf_valueEquals left="#isObject(variables.myObj)#" right="true">