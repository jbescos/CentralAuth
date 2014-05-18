<!DOCTYPE html>
<%@ taglib uri="https://github.com/jbescos/CentralAuth/jsp/msg" prefix="cookie"%>
<html>
<head>
<meta charset="UTF-8">
<title>You are not logged</title>
</head>
<body>
<h1>You are not logged</h1>
	<a href="./login.html"> Login </a>
	<a href="./register.html"> Register </a>
	<a href="./roles.html"> Add role </a>
	<a href="./application.html"> Add application </a>
	<cookie:iframes/>
</body>
</html>