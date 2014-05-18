<!DOCTYPE html>
<%@ taglib uri="https://github.com/jbescos/CentralAuth/jsp/msg" prefix="cookie"%>
<html>
<head>
<meta charset="UTF-8">
<title>You are not logged</title>
</head>
<body>
<h1>You are not logged</h1>
	<a href="/server-auth/login.html"> Login </a>
	<a href="/server-auth/register.html"> Register </a>
	<a href="/server-auth/roles.html"> Add role </a>
	<a href="/server-auth/application.html"> Add application </a>
	<cookie:iframes/>
</body>
</html>