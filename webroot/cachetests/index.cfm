<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>Cache Tests</title>
	<meta name="generator" content="TextMate http://macromates.com/">
	<meta name="author" content="Mark Drew">
	<!-- Date: 2010-05-24 -->
</head>
<body>

<cfdirectory action="list" directory="#expandPath(".")#" filter="*.cfm" name="cachetests">
	
<cfloop query="cachetests">
	<cfinclude template="#name#">
	
</cfloop>	

</body>
</html>
