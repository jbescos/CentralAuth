<!DOCTYPE html>
<%@ taglib uri="https://github.com/jbescos/CentralAuth/jsp/msg" prefix="cookie"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <title>Auth server</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta name="description" content="">
  <meta name="author" content="">

	<!--link rel="stylesheet/less" href="less/bootstrap.less" type="text/css" /-->
	<!--link rel="stylesheet/less" href="less/responsive.less" type="text/css" /-->
	<!--script src="js/less-1.3.3.min.js"></script-->
	<!--append ‘#!watch’ to the browser URL, then refresh the page. -->
	
	<link href="/server-auth/layoutit/src/css/bootstrap.min.css" rel="stylesheet">
	<link href="/server-auth/layoutit/src/css/style.css" rel="stylesheet">

  <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
  <!--[if lt IE 9]>
    <script src="js/html5shiv.js"></script>
  <![endif]-->

  <!-- Fav and touch icons -->
  <link rel="apple-touch-icon-precomposed" sizes="144x144" href="/server-auth/layoutit/src/img/apple-touch-icon-144-precomposed.png">
  <link rel="apple-touch-icon-precomposed" sizes="114x114" href="/server-auth/layoutit/src/img/apple-touch-icon-114-precomposed.png">
  <link rel="apple-touch-icon-precomposed" sizes="72x72" href="/server-auth/layoutit/src/img/apple-touch-icon-72-precomposed.png">
  <link rel="apple-touch-icon-precomposed" href="/server-auth/layoutit/src/img/apple-touch-icon-57-precomposed.png">
  <link rel="shortcut icon" href="/server-auth/layoutit/src/img/favicon.png">
  
	<script type="text/javascript" src="/server-auth/layoutit/src/js/jquery.min.js"></script>
	<script type="text/javascript" src="/server-auth/layoutit/src/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="/server-auth/layoutit/src/js/scripts.js"></script>
</head>

<body>
<div class="container">
	<div class="row clearfix">
		<div class="col-md-12 column">
			<div class="page-header">
				<h1>
					Central Auth <small>server</small>
				</h1>
			</div>
			<ul class="nav nav-tabs">
				<li>
					<a href="/server-auth/login.html">Login</a>
				</li>
				<li>
					<a href="/server-auth/rest/logout">Logout</a>
				</li>
				<li>
					<a href="/server-auth/layoutit/src/register.html">Register</a>
				</li>
				<li>
					<a href="/server-auth/layoutit/src/application.html">Add application</a>
				</li>
				<li>
					<a href="/server-auth/layoutit/src/roles.html">Add role</a>
				</li>
			</ul>
			<h3>
				You are login!
			</h3>
		</div>
	</div>
</div>
<cookie:iframes/>
</body>
</html>
