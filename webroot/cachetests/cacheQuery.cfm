<cfquery name="getThings" cachedwithin="#CreateTimeSpan(0,0,0,10)#" datasource="myMura" result="myThing">
	SELECT * FROM tcontent
</cfquery>
<cfquery name="getThings" cachedwithin="#CreateTimeSpan(0,0,0,10)#" datasource="myMura" result="myThing">
	SELECT * FROM tcontent
</cfquery>
<cf_valueEquals left="#myThing.cached#" right="true">
