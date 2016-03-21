package util;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

public class Result extends HttpServlet{
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException {
		
		//String c = request.getParameter("color");
		//Expert be = new Expert();
		//List result = be.getBrands(c);
		
		request.setAttribute("styles", "hello");
		
		RequestDispatcher view = request.getRequestDispatcher("../Progress.jsp");
		
		view.forward(request, response);
	}

}
