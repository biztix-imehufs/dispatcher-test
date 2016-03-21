package BPMN_Simulation1;

import org.json.simple.JSONObject;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

public class EventListener implements UpdateListener{

	public StringBuilder notif;
	public ActivityMap activityMap;
	public JSONObject jObj;
	public EventListener(StringBuilder notif){
		//notif = new StringBuilder();
		this.notif = notif;
		this.activityMap = new ActivityMap();
	}
	public EventListener(StringBuilder notif, JSONObject jobj){
		//notif = new StringBuilder();
		this.notif = notif;
		this.activityMap = new ActivityMap();
		this.jObj = jobj;
	}
	@Override
	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		
    	
    	EventBean[] event = new EventBean[newEvents.length];
    	
    	for(int i=0; i<newEvents.length; i++){
    		event[i] = newEvents[i];
    	}
				
		//System.out.println(event1);
    	String out = null;
    	String out1=null;
    	
		for(int i =0; i< newEvents.length; i++){
			String caseid = String.valueOf(event[i].get("productID")).split("-")[0];
			String activity = (String) event[i].get("process");
			if (!GlobalRes.isProcessActivity(activity))
				continue;
			String machine = (String) event[i].get("machine");
			String starttime="", endtime="";
			String status =(String) event[i].get("status");
			if(status.equalsIgnoreCase("Start"))
			{
				starttime = (String) event[i].get("time");
				//add instance
		    	activityMap.addInstance(caseid, activity, status, machine,starttime);
			}
			else if((status.equalsIgnoreCase("Complete")))
			{
				endtime =(String) event[i].get("time");
				//add instance
		    	//activityMap.addInstance(caseid, activity, status, machine, endtime);
			}
		System.out.println("productID : "+event[i].get("productID")+
							"    process : "+event[i].get("process") + 
							"	 Machine : "+event[i].get("machine") + 
							"	 Status : "+event[i].get("status") + 
							"    Time : " + event[i].get("time"));
		out = event[i].get("time")+
				" -- productID : "+event[i].get("productID")+
				"    process : "+event[i].get("process") + 
				"	 Machine : "+event[i].get("machine") + "\n";
    	out1 = "activity :"+event[i].get("process");
    	
    	
    	//add activity
    	//activityMap.addEvent((String)event[i].get("process"), (String)event[i].get("status"));
    	
		}
		
		
		//notif.append(out1).append("\n");
		notif.setLength(0);
		notif.append(activityMap.toString());
		jObj=activityMap.getObject();
		
	}
	
	
}
