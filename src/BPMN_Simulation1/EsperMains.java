package BPMN_Simulation1;

import org.json.simple.JSONObject;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;


public class EsperMains {
	public static StringBuilder notif = new StringBuilder();
	public static JSONObject jobj = new JSONObject();
	public boolean status;
	private StringBuilder sb ;
	public void execute(){
		GlobalRes.initialize();
		
		// �����ϱ�
		Configuration config = new Configuration();
		config.addEventTypeAutoName("BPMN_Simulation1");
		
		// Query �� �����
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider(config);
		String epl = "select * from FactoryLine";// output all every 2 seconds
		EPStatement statement = epService.getEPAdministrator().createEPL(epl);
		
		// ����ϱ�
		EventListener listener = new EventListener(notif,jobj);
		statement.addListener(listener);
	
		//
		status = false;
		
		// ����� Event �����ϱ�
		Runnable r2 = new FirstProcess(epService);
		Thread t1= new Thread(r2);
		t1.start();
	}
	public static void main(String[] args){
		GlobalRes.initialize();
		
		// �����ϱ�
		Configuration config = new Configuration();
		config.addEventTypeAutoName("BPMN_Simulation1");
		
		// Query �� �����
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider(config);
		String epl = "select * from FactoryLine";// output all every 2 seconds
		EPStatement statement = epService.getEPAdministrator().createEPL(epl);
		
		// ����ϱ�
		EventListener listener = new EventListener(notif);
		statement.addListener(listener);
	
		
		// ����� Event �����ϱ�
		Runnable r2 = new FirstProcess(epService);
		Thread t1= new Thread(r2);
		t1.start();
				
	}
	
	public synchronized boolean isCompleted() {
		return status;
		}
	
	public synchronized String getInt()
	{
		return notif.toString();
	}
	
	public String getString()
	{
		return notif.toString();
	}
	
	public JSONObject getObject()
	{
		return jobj;
	}
}
