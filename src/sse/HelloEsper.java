package sse;

import java.io.IOException;
import java.io.PrintWriter;
 


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import BPMN_Simulation1.EsperMains;
 
public class HelloEsper extends HttpServlet {
 
	String notif=null;
	JSONObject jobj = null;
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
 
		response.setContentType("text/event-stream");
		response.setCharacterEncoding("UTF-8");
		
		EsperMains esperMain = new EsperMains();
		esperMain.execute();
 
		PrintWriter writer = response.getWriter();
		
		while(!esperMain.isCompleted())
		{
			notif = esperMain.getInt();
			jobj = esperMain.getObject();
			writer.write("data: "+notif+"\n\n");
			//writer.write("obj: "+jobj+"\n\n");
			writer.flush();
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		writer.close();
		/*for (int i = 0; i < 20; i++) {
 
			writer.write("data: "+ System.currentTimeMillis() +"\n\n");
			writer.flush();
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		writer.close();*/
	}
 
}