<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<jsp:useBean id="esperMain" scope="session"	
class="BPMN_Simulation1.EsperMains"/>
<HTML>
<HEAD>
<TITLE>Progress</TITLE>
<% if (!esperMain.isCompleted()) { %>  
<SCRIPT LANGUAGE="JavaScript">
setTimeout("location='ProgressEsper.jsp'", 1000);
</SCRIPT> 
<% } %> 

<% if (!esperMain.isCompleted()) { %>
<%out.println(esperMain.getInt()); %> <br/> </html>
<% } else { %> 
<H1 ALIGN="CENTER">Put your page content here...</H1> 
<% } %> 
</BODY> 
</HTML> 