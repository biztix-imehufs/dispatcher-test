<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<jsp:useBean id="statusBean" scope="session"
class="util.StatusBean"/> 
<HTML>
<HEAD>
<TITLE>Progress</TITLE>
<% if (!statusBean.isCompleted()) { %>  
<SCRIPT LANGUAGE="JavaScript">
setTimeout("location='Progress.jsp'", 1000);
</SCRIPT> 
<% } %> 
</HEAD> 
<BODY> 
<% if (!statusBean.isCompleted()) { %> 
<H1 ALIGN="CENTER">Please wait..while we process your request..</H1>
<html> <%out.println(statusBean.getInt()); %> <br/> </html>
<% } else { %> 
<H1 ALIGN="CENTER">Put your page content here...</H1> 
<% } %> 
</BODY> 
</HTML> 