
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.net.*;


public class TestServlet extends HttpServlet {

Connection theConnection;
    public void doPost(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException
    {
        
		try{
		    /*Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");//Loading Sun's JDBC ODBC Driver
            theConnection = DriverManager.getConnection("jdbc:odbc:emaildb", "admin", ""); //Connect to emaildb Data source
		    Statement theStatement=theConnection.createStatement();
	        ResultSet theResult=theStatement.executeQuery("select * from emaillists"); //Select all records from emaillists table.
		*/
		
		request.setAttribute("message", "Hello, world");
		RequestDispatcher RD = getServletContext().getRequestDispatcher("test.jsp");
		RD.forward(request, response);
		}
		
		 catch(Exception e){
		//out.println(e.getMessage());//Print trapped error.
	}
		
		
	}	
	
	
}