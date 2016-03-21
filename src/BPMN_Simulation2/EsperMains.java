package BPMN_Simulation2;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Expert;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;


public class EsperMains {//extends HttpServlet
	//public static void main(String[] args){
	public static StringBuilder notif = new StringBuilder();
	public boolean status;
	private StringBuilder sb ;
	
	public void execute(){
		// 구성하기
		Configuration config = new Configuration();
		config.addEventTypeAutoName("BPMN_Simulation");
		
		// Query 문 만들기
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider(config);
		String epl = "select * from FactoryLine";// output all every 2 seconds
		EPStatement statement = epService.getEPAdministrator().createEPL(epl);
		
		// 출력하기
		EventListener listener = new EventListener(notif);
		statement.addListener(listener);
		
		//
		status = false;
	
		// 출력할 Event 생성하기
		Runnable r2 = new FirstProcess(epService);
		Thread t1= new Thread(r2);
		t1.start();
	}
	
	public static void main(String[] args){
		Configuration config = new Configuration();
		config.addEventTypeAutoName("BPMN_Simulation");
		
		// Query 문 만들기
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider(config);
		String epl = "select * from FactoryLine";// output all every 2 seconds
		EPStatement statement = epService.getEPAdministrator().createEPL(epl);
		
		// 출력하기
		//EventListener listener = new EventListener(notif);
		EventListener listener = new EventListener(notif);
		statement.addListener(listener);
		
		//
		//status = false;
	
		// 출력할 Event 생성하기
		Runnable r2 = new FirstProcess(epService);
		Thread t1= new Thread(r2);
		t1.start();
				
	}
	
	public synchronized boolean isCompleted() {
		return status;
		}
	
	public synchronized String getInt()
	{
		int a = (int) (Math.random() * 100);
		//sb.append(Integer.toString(a));
		//sb.append("\n");
		//ss.concat(Integer.toString(a)+"+");
		//return ss;
		return notif.toString();
	}
	
	/*public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
				
		new Thread(new Runnable() {
			public void run() {
				while(true)
				try {
					Thread.sleep(1000);
					
					//System.out.println(" †††††† "+notif.toString());
					Thread.currentThread().getName();
					
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}).start();
		
		request.setAttribute("output", notif);
		//((HttpServletResponse) request).sendRedirect("../resultEsper.jsp");
		RequestDispatcher view = request.getRequestDispatcher("result.jsp");
		view.forward(request, response);
		//view.forward(request, response);
		
				String c = request.getParameter("color");
				Expert be = new Expert();
				List result = be.getBrands(c);
				
				request.setAttribute("styles", result);
				
				
				
				view.forward(request, response);
			}*/
}
