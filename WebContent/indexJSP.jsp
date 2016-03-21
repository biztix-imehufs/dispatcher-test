<!DOCTYPE HTML>
<html>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<head>
	<title>Server-Sent Events Servlet example</title>
	<style>
		body {
			font-family: sans-serif;
		}
	</style>
</head>
<body>
 
	Time: <span id="foo"></span>
	
	<br><br>
	<button onclick="start()">Start</button>
 
	<script LANGUAGE="javaScript" type="text/javascript">
	function start() {


		var eventSource = new EventSource("HelloServlet");
		eventSource.onmessage = function(event) {
			document.getElementById('foo').innerHTML = event.data;
		};
	}
	</script>
</body>
</html>