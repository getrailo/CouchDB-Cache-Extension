<!--- We can read the congfig here --->
<cfset exp="this extension is experimental and will no longer work with the final release of railo 3.1, it is not allowed to use this extension in a productve enviroment.">
<cfset rootURL="http://#CGI.server_name#:#CGI.server_port#/extensions/">
<cfset zipFileLocation = 'ext/couchdb-cache.zip'>
<cffile action="read" file="zip://#expandPath(zipFileLocation)#!/config.xml" variable="config">
<cfset info = XMLParse(config)>

<cfset providerWS =  CreateObject("webservice", "#rootURL#ExtensionProvider.cfc?wsdl")>
<cfset pInfo = providerWS.getInfo()>


<cfadmin action="updateExtension" type="server" password="universe"
		 provider="#rootURL#ExtensionProvider.cfc"
		 id="#info.config.info.id.XMLtext#"
		 version="#info.config.info.version.XMLtext#" 
		 name="#info.config.info.version.XMLtext#" label="#info.config.info.label.XMLtext#"
		 description="#info.config.info.version.XMLtext#" category="#info.config.info.category.XMLtext#"
		 author="markdrew"
		 codename="#info.config.info.version.XMLtext#"
		 image="#pInfo.image#"
		 video=""
		 support=""
		 documentation=""
		 forum=""
		 mailinglist=""
		 network=""
		 _type="#info.config.info.type.XMLtext#"
/>		
<!--- 
<cfadmin action="restart" type="server" password="universe" />
 --->