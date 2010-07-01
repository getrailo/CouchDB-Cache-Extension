<cfcomponent>
	
	<cfscript>
		variables.name = "CouchDBCache";
		variables.id = "railo_couchdbcache";
		variables.jar = "CouchDBCache.jar"
		variables.driver = "CouchDBCache.cfc"
		variables.jars = "#variables.jar#,commons-beanutils-1.8.0.jar,httpclient-4.0.jar,httpcore-4.0.1.jar,jcouchdb-0.11.0-1.jar,slf4j-api-1.5.6.jar,slf4j-log4j12-1.5.6.jar,svenson-1.3.7.jar";
		
	</cfscript>
    
    <cffunction name="validate" returntype="void" output="no"
    	hint="called to validate values">
    	<cfargument name="error" type="struct">
        <cfargument name="path" type="string">
        <cfargument name="config" type="struct">
        <cfargument name="step" type="numeric">
        
    </cffunction>
    
    <cffunction name="install" returntype="string" output="no"
    	hint="called from Railo to install application">
    	<cfargument name="error" type="struct">
        <cfargument name="path" type="string">
        <cfargument name="config" type="struct">
        
        <cfif server.railo.version LT "3.1.1.017">
        	<cfset error.common="to install this extension you need at least Railo version [3.1.1.017], the current version is [#server.railo.version#]">
        <cfelseif hasExtension(variables.id)>
        	<cfset error.common="You already have ""#variables.name#"" installed">
		</cfif>
		<cfloop list="#variables.jars#" index="i">
		<cfadmin 
            action="updateJar"
            type="#request.adminType#"
            password="#session["password"&request.adminType]#"    
            jar="#path#lib/#i#">
		</cfloop>
            

        <cfadmin 
        	action="updateContext"
            type="#request.adminType#"
            password="#session["password"&request.adminType]#"
            source="#path#driver/#variables.driver#"
            destination="admin/cdriver/#variables.driver#">

        <cfreturn '#variables.name# is now successfully installed'>
    </cffunction>
    	
     <cffunction name="update" returntype="string" output="no"
    	hint="called from Railo to update a existing application">
    	<cfargument name="error" type="struct">
        <cfargument name="path" type="string">
        <cfargument name="config" type="struct">
        <cfset uninstall(path,config)>
		<cfreturn install(argumentCollection=arguments)>
    </cffunction>
    
    
    <cffunction name="uninstall" returntype="string" output="no"
    	hint="called from Railo to uninstall application">
    	<cfargument name="path" type="string">
        <cfargument name="config" type="struct">
        
		<cfloop list="#variables.jars#" index="i">
		<cfadmin 
            action="removeJar"
            type="#request.adminType#"
            password="#session["password"&request.adminType]#"    
            jar="#path#lib/#i#">
		</cfloop>

		<cfadmin 
        	action="removeContext"
            type="#request.adminType#"
            password="#session["password"&request.adminType]#"
            destination="admin/cdriver/#variables.driver#">
        
        <cfreturn '#variables.name# is now successfully removed'>
    </cffunction>
    
    <cffunction name="hasExtension" returntype="boolean">
        <cfargument name="name" type="string" required="yes">
        <cfset var extensions="">
        <cfadmin 
            action="getExtensions"
            type="#request.adminType#"
            password="#session["password"&request.adminType]#"
            returnVariable="extensions">
         <cfloop query="extensions">
            <cfif extensions.name EQ arguments.name>
                <cfreturn true>
            </cfif>
         </cfloop>
         <cfreturn false>
    </cffunction>
    
    
    
    
        
</cfcomponent>