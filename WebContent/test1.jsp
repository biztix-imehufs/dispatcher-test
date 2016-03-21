<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR" 
    import="org.json.simple.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Untitled Document</title>

</head>

<body>

testing page
<h1></h1>
<%

JSONObject obj = new JSONObject();
JSONObject obj2 = new JSONObject();
obj2.put("consumer",new Integer(2));
obj2.put("count", new Integer (200));

obj.put("identifier", obj2);

JSONObject obj3 = new JSONObject();

obj3.put("consumer",2);
obj3.put("count", 50);
obj3.put("inputQueue", "identifier");
obj3.put("inputThroughput", 50);
obj.put("lost-and-found", obj3);

/* obj.put("age", new Integer(40));
 
JSONArray list1 = new JSONArray();
list1.add("HTML");
list1.add("XML");
 
obj.put("web", list1);

JSONArray list2 = new JSONArray();
list2.add("Java");
list2.add("C");

obj.put("desktop", list2); */

String s = obj.toJSONString();
%>

<script>
var workers=<%=s%>;
var worke = {
	    "identifier": {
	      "consumers": 2,
	      "count": 20
	    },
	    "lost-and-found": {
	      "consumers": 1,
	      "count": 1,
	      "inputQueue": "identifier",
	      "inputThroughput": 50
	    },
	    "monitor": {
	      "consumers": 1,
	      "count": 0,
	      "inputQueue": "identifier",
	      "inputThroughput": 50
	    },
	    "meta-enricher": {
	      "consumers": 4,
	      "count": 9900,
	      "inputQueue": "identifier",
	      "inputThroughput": 50
	    },
	    "geo-enricher": {
	      "consumers": 2,
	      "count": 1,
	      "inputQueue": "meta-enricher",
	      "inputThroughput": 50
	    },
	    "elasticsearch-writer": {
	      "consumers": 0,
	      "count": 9900,
	      "inputQueue": "geo-enricher",
	      "inputThroughput": 50
	    }
	  };
	  
for (var id in workers) {
alert(workers[id].count);
}

</script>

</body>

</html>