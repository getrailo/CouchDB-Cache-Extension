<cfcomponent>
	<cffunction name="getInfo" access="remote" returntype="struct">
    	<cfset var info=struct()>
        <cfset info.title="Local Extension Provider ("&cgi.HTTP_HOST&")">
        <cfset info.mode="develop">
        <cfset info.description="Personal development extensions">
        <cfset info.image="http://localhost:8888/extensions/logo.jpg">
        <cfset info.url="http://"&cgi.HTTP_HOST>
    	<cfreturn info>
    </cffunction>
    
    
	<cffunction name="getDownloadDetails" access="remote" returntype="struct" output="no">
    	<cfargument name="type" required="yes" type="string">
        <cfargument name="serverId" required="yes" type="string">
        <cfargument name="webId" required="yes" type="string">
        <cfargument name="appId" required="yes" type="string">
        <cfargument name="serialNumber" required="no" type="string" default="">
        <cffile action="APPEND" file="#expandPath("extensionInstalled.txt")#" output="type: #arguments.type# serverid: #arguments.serverID# webId: #arguments.webID#">
        <cftry>
			<cfif len(serialNumber)>
                <cfset serialNumber=decrypt(serialNumber,"wfsfr456","cfmx_compat","Hex")>
            </cfif>
            <cfcatch></cfcatch>
    	</cftry>
        <cfset var data=struct()>
        <cfset data.error=false>
        


        <!--- allowed to download extension --->
        <cfif structKeyExists(variables.allowed,arguments.appId) and arrayFind(variables.allowed[arguments.appId],arguments.serverId)>
        	<cftry>
            	<cfset var app=getApplication(arguments.appId)>
            	<cfset var dir="">
                <cfdirectory directory="#expandPath('ext/'&app.name)#" action="list" name="dir" filter="*.zip" recurse="no">
                
                <cfset data.message="">
                <!--- TODO replace this with a cfcontent call ---->
                <cfset data.url=getInfo().url&'/ext/'&app.name&'/'&dir.name>
                
                <cfcatch>
                	<cfset data.error=true>
                    <cfset data.message=cfcatch.Message>
                    <cfset data.url="">
              	</cfcatch>
            </cftry>
        <!--- NOT allowed to download extension --->
        <cfelse>
        	<cfsavecontent variable="data.message">
			<cfoutput>
<b>The Railo Extension store will be available soon.</b><br>
In the meantime you can still purchase extensions, by <a href="http://www.getrailo.com/index.cfm/contact-us/" target="_blank">contacting us</a> and we shall provide  
you instructions on how to currently obtain paid extensions.
            <br><br>
            </cfoutput>
            </cfsavecontent>
            
            <cfset data.url="">
        </cfif>
        <cfreturn data>
    </cffunction> 
	
	
    
    <cffunction name="getApplication" access="private" returntype="query">
    	<cfargument name="appId" required="yes" type="string">
		<cfset var apps=listApplications()>
        <cfloop query="apps">
        	<cfif arguments.appId EQ apps.id>
            <cfreturn querySlice(apps,apps.currentrow,1)>
            </cfif> 
        </cfloop>
        <cfthrow message="app for id #appId# not found">
    </cffunction>  
    
    <cffunction name="listApplications" access="remote" returntype="query">
		<cfset var apps=queryNew('type,id,name,label,description,version,category,image,download,paypal,author,codename,video,support,documentation,forum,mailinglist,network,created')>
            <cfset populateCOM(apps)>
        <cfreturn apps>
    </cffunction>    
    
    
    
	<cffunction name="populateCOM" access="private" returntype="void">
    	<cfargument name="apps" type="query" required="yes">
        <cfset var exp="this extension is experimental and will no longer work with the final release of railo 3.1, it is not allowed to use this extension in a productve enviroment.">
        
        <cfset var rootURL=getInfo().url & "/extensions/">
        <cfset var zipFileLocation = "" />
		<cfset var extensions = "" />
		<cfset var c = "" />
		<cfset var colList = apps.columnlist>
		
		
		<cfdirectory action="list" directory="ext/" filter="*.zip" name="extensions">
		<cfloop query="extensions">
	    	<cfset var zipFileLocation = 'ext/#extensions.name#'>
			<cffile action="read" file="zip://#expandPath(zipFileLocation)#!/config.xml" variable="config">
			<cfset info = XMLParse(config)>
	        <cfset QueryAddRow(apps)>
	      	<cfset QuerySetCell(apps,'download',rootURL & zipFileLocation)>
			<cfloop array="#info.config.info.XmlChildren#" index="c">
				<cfif ListFindNoCase(colList, c.XMLName)>
			        <cfset QuerySetCell(apps,c.XMLName, c.XMLtext)>
				</cfif>
			</cfloop>
       
		</cfloop>
	</cffunction>
</cfcomponent>
