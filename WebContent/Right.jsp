<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
  <meta charset="utf-8">
  <title>jQuery UI Accordion - Default functionality</title>
  <link rel="stylesheet" href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
  <script src="//code.jquery.com/jquery-1.10.2.js"></script>
  <script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
  <link rel="stylesheet" href="/resources/demos/style.css">
  <script>
  $(function() {
    $( "#accordion" ).accordion();
  });
  $(function() {
	    $( "input[type=submit], a, button" )
	      .button()
	      .click(function( event ) {
	        event.preventDefault();
	      });
	  });
  
  </script>
  
  
</head>
<body>
 
 
 

<div id="accordion">
  <h3>Process Setup</h3>
  <div>
    <p>  Process Diagram retrieval  <br>
    
    <input type="submit" id="btnSubmit" name="submit" href="Right.jsp" target="LEFT"
	
	onClick=parent.frames['LEFT'].location='Left.jsp' value="A submit button"><!-- parent.frames['LEFT'] -->
    
    </p>
    </div>
  <h3>Time Setting</h3>
  <div>
    <p>
    Time Setting Information
    </p>
  </div>
  <h3>Control Flow</h3>
  <div>
    <p>
    Control flow of the process
    </p>
    <ul>
      <li>List item one</li>
      <li>List item two</li>
      <li>List item three</li>
    </ul>
  </div>
  <h3>Resources setting</h3>
  <div>
    <p>
    Resources setting
    </p>
  </div>
</div>
</body>
</html>
