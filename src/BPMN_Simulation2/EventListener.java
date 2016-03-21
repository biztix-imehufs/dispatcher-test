package BPMN_Simulation2;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

public class EventListener implements UpdateListener{

	public StringBuilder notif;
	public EventListener(StringBuilder notif){
		//notif = new StringBuilder();
		this.notif = notif;
	}
	@Override
	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		
    	
    	EventBean[] event = new EventBean[newEvents.length];
    	
    	for(int i=0; i<newEvents.length; i++){
    		event[i] = newEvents[i];
    	}
				
		//System.out.println(event1);
    	String out = null;
		for(int i =0; i< newEvents.length; i++){
		System.out.println("productID : "+event[i].get("productID")+
							"    process : "+event[i].get("process") + 
							"	 Machine : "+event[i].get("machine") + 
							"	 Status : "+event[i].get("status") + 
							"    Time : " + event[i].get("time"));
		
		out = event[i].get("time")+
				" -- productID : "+event[i].get("productID")+
				"    process : "+event[i].get("process") + 
				"	 Machine : "+event[i].get("machine") + "\n";
				 
		}
		notif.append(out).append("\n");//System.getProperty("line.separator")
    	
	}
	
	
}
