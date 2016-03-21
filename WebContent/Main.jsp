<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
  <meta charset="utf-8">
  <title>jQuery Functions</title>
  <link rel="stylesheet" href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
  <script src="//code.jquery.com/jquery-1.10.2.js"></script>
  <script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
  <link rel="stylesheet" href="/resources/demos/style.css">
  <script>
   
  $(function() {
    $( "#menu" ).menu();
  });
  
   $(function() {
    $( "#tabs" ).tabs();
  });
  
  </script>
</head>
<Frameset rows="15%,*">
<Frame src="Upper.jsp" name=TOP>
<Frameset cols="80%, 20%">
<Frame src="LeftEmpty.jsp" name=LEFT>
<Frame src="Right.jsp" name=RIGHT>
</frameset>

<body>

</body>
</frameset>
</html>


