<!DOCTYPE html>
<%@ taglib uri="https://github.com/jbescos/CentralAuth/jsp/msg" prefix="cookie"%>
<html>
<head>
<meta charset="UTF-8">
<title>You are logged!</title>
</head>
<body>
	<h1>You have been logged!</h1>
	<a href="./rest/logout"> Logout </a>
	<a href="./roles.html"> Add role </a>
	<a href="./application.html"> Add application </a>
	<cookie:iframes/>
</body>
</html>