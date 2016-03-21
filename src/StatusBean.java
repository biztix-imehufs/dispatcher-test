
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class StatusBean extends HttpServlet implements Runnable, Serializable {
private boolean status;
private int count=1;

public StatusBean() {
status = false; //#1
}

public synchronized boolean isCompleted() {
return status;
}

public synchronized void setStatus(boolean status) {
this.status = status;
}

public void run() {
	try{
		Thread.sleep(20000); //#2
		this.status = true; //#3
		while (count<10) {
		       System.out.print("<h1>"+2+"</h1>");
		       System.out.flush();
		       count++;
		}
		}catch(InterruptedException e){
		e.printStackTrace();
	}
}

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
