<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<jsp:useBean id="esperMain" scope="session"	
class="BPMN_Simulation1.EsperMains"/>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>BPMN Representation</title>
 <link rel="stylesheet" href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
  <script src="//code.jquery.com/jquery-1.10.2.js"></script>
  <script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
  <link rel="stylesheet" href="/resources/demos/style.css">
<script>

  $(function() {
	    $( "#dialog" ).dialog();
	  });
  </script>

<% if (!esperMain.isCompleted()) { %>  
<SCRIPT LANGUAGE="JavaScript">
//window.location("Popup.jsp",null,"height=200,width=400,status=yes,toolbar=no,menubar=no,location=no");
//window.location("Popup.jsp");
//setTimeout("location='Popup.jsp'", 1000);
</SCRIPT> 
<% } %> 

</head>
<body;"><!-- poponload() -->
<div id="dialog" title="Basic dialog">
<% session.removeAttribute("esperMain"); %> 

<% esperMain.execute(); %>  


<jsp:forward page="Popup.jsp"/> 
</div>

 <!-- <div id="dialog" title="Basic dialog">
  <p>This is the real time data display dialog</p>
  <%// if (!esperMain.isCompleted()) { %> 
<H1 ALIGN="CENTER">Please wait..while we process your request..</H1>
<%//out.println(esperMain.getInt()); %> 
<%// } else { %> 
<H1 ALIGN="CENTER">Put your page content here...</H1> 
<%// } %> 
</div>

<%// session.removeAttribute("esperMain"); %> 

<%// esperMain.execute(); %>   -->
<!--<jsp:forward page="Left.jsp"/> -->
</body>
</html>