<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<HEAD>
<jsp:useBean id="esperMain" scope="session"	
class="BPMN_Simulation1.EsperMains"/>
<TITLE>Progress</TITLE>
<script type="text/javascript">
    
    function popup(url) {
    var width = 600;
    var height = 500;
    var left = (screen.width - width) / 2;
    var top = (screen.height - height) / 2;
    var params = 'width=' + width + ', height=' + height;
    params += ', top=' + top + ', left=' + left;
    params += ', directories=no';
    params += ', location=no';
    params += ', menubar=no';
    params += ', resizable=no';
    params += ', scrollbars=no';
    params += ', status=no';
    params += ', toolbar=no';
    newwin = window.open(url, 'windowname5', params);
    return false;
}


//]]>
</script>



<% if (!esperMain.isCompleted()) { %>  
<SCRIPT LANGUAGE="javaScript">
//setTimeout(popup('`test/login.html'), 5000);
//popup('dagredemo1.jsp');
/* setInterval(function() {
	popup('dagredemo1.jsp');
    
  }, 1000); */
//setTimeout(popup('dagredemo1.jsp'), 1000);
//setTimeout("location='TestingGraph.jsp'", 1000);
</SCRIPT> 
<% } %> 







</HEAD> 

<body>
	

<% //session.removeAttribute("esperMain"); %> 

<% esperMain.execute(); %>  
<!--<jsp:forward page="dagredemo1.jsp"/>
<!--<jsp:forward page="TestingGraph.jsp"/>-->
<!--<jsp:forward page="ProgressEsper.jsp"/> -->


</body>
</html>