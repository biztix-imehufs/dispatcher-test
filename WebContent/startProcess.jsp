<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>
</head>
<body>
<% session.removeAttribute("statusBean"); %> 
<jsp:useBean id="statusBean" scope="session"	
class="util.StatusBean"/>	
<% new Thread(statusBean).start(); %>  
<jsp:forward page="Progress.jsp"/> 
</body>
</html>