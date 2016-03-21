package BPMN_Simulation1;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.json.simple.JSONObject;

public class ActivityMap {


	//process instance
	
	// activity with status
	public HashMap<String, Integer> activity = null;
	//public HashMap<ArrayList<String>, Integer> instance = null;
	public HashMap<String, Integer> actToAct = null;
	//public ArrayList<String> ins = new ArrayList<String>();
	public ArrayList<String> trace = null;
	public ArrayList<String> traceActStat = null;
	public HashMap<String, ArrayList<String>> caseDetail = null;
	public HashMap<String, ArrayList<String>> caseOnlyComplete = null;
	public JSONObject obj = null;
	public Set<String> actList = new HashSet<String>();
	public Set<String> actAbb = new HashSet<String>();
	public boolean first=false;
	public boolean first1=false;
	
	
	SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	public ActivityMap()
	{
		this.activity = new HashMap<String, Integer>();
		this.caseDetail = new HashMap<String, ArrayList<String>>();
		this.trace = new ArrayList<String>();
		this.caseOnlyComplete = new HashMap<String, ArrayList<String>>();
		this.traceActStat = new ArrayList<String>();
		this.actToAct = new HashMap<String, Integer>();
		this.obj = new JSONObject();
		this.actList = new HashSet<String>();
		this.actAbb= new HashSet<String>();
	}
	public void addEvent(String name, String status)
	{
		int count=1;
		String namestatus = name+";"+status;
		System.out.println("name status repositoryyyyyy "+namestatus);
		if(activity.containsKey(namestatus))
		{
			count = activity.get(namestatus);
			count++;
			activity.put(namestatus, count);
			//JsonObj(namestatus, count);
		}else
		{
		activity.put(namestatus, count);
		}
		//JsonObj(namestatus, count);
	}
	
	public void addInstance(String c, String name, String status, String machine, String time)
	{
		
		ArrayList<String> temp = new ArrayList<String>();
		String insDetail = name+";"+status+";"+machine+";"+time;
		
		
		addEvent(name, status);
		//for only complete
		addOnlyComplete(c, name, status);
		
		
		if(caseDetail.containsKey(c))
		{
			temp  = caseDetail.get(c);
			temp.add(insDetail);
			caseDetail.put(c, temp);
			//instance.put(insDetail, count);
			//JsonObj(insDetail, count);
		}else
		{
			String startDetail = "Start"+";"+"start"+";"+""+";"+time;
			addEvent("Start","start");
			temp.add(startDetail);
			temp.add(insDetail);
			caseDetail.put(c, temp);
		}
		//instance.put(insDetail, count);
		//JsonObj(insDetail, count);
	}
	
	public void addOnlyComplete(String c, String name, String status)
	{
		ArrayList<String> temp = new ArrayList<String>();
		ArrayList<String> temp1 = new ArrayList<String>();
		String onlyComplete = name+";"+status;
		
		if(status.equalsIgnoreCase("start")){
			
		if(caseOnlyComplete.containsKey(c.trim()))
		{
			
			temp  = caseOnlyComplete.get(c.trim());
			int size = temp.size();
			System.out.println("size: "+size);
			String n1=null, n2=null;
			/*if(size==2)
			{
				n1 = temp.get(size-2).split(";")[0];
				n2 = temp.get(size-1).split(";")[0];
				System.out.println("is it okay "+n1+" -- "+n2);
				
			}*/
			
				
				n2 = temp.get(size-1).split(";")[0];
			System.out.println("name ------------------------------------------:"+n2);
			temp.add(onlyComplete);
			caseOnlyComplete.put(c.trim(), temp);
			//instance.put(insDetail, count);
			//JsonObj(insDetail, count);
			
			addActToAct(n2, name);
			
			int count = checkCount(n2, name);
			addtoJSON(n2, name, count);
			
		}else
		{

			temp.add("Start"+";"+"start");
			temp.add(onlyComplete);
			caseOnlyComplete.put(c.trim(), temp);
			addActToAct("Start", name);
			int count = checkCount("Start", name);
			addtoJSON("Start", name, count);
			
			
		}
		}
		
	}
	
	public void addActToAct(String n1, String n2)
	{
		String name = n1+";"+n2;
		int count=1;
		System.out.println("check nameeeeeee "+name);
		if(actToAct.containsKey(name))
		{
			count = actToAct.get(name);
			count++;
			actToAct.put(name, count);
			
			
		}else
		{
			actToAct.put(name, count);
			
		}
	}
	
	public int checkCount(String n1, String n2)
	{
		int count=0;
		String name = n1+";"+n2;
		System.out.println("check counttttttt :"+name);
		if(actToAct.containsKey(name))
		{
			count = actToAct.get(name);
						
		}
		return count;
	}
	
	public void addtoJSON(String name1,String name2, int c )
	{
		//check the number of activity
		String namestatus1 = name1+";"+"Start";
		String namestatus2 = name2+";"+"Start";
		System.out.println("add to JSON----------------- "+namestatus1+"; " +namestatus2);
		System.out.println("activity "+activity.get(namestatus1));
		System.out.println("activity "+activity.get(namestatus2));
		int c1 = activity.get(namestatus1);
		int c2 = activity.get(namestatus2);
		System.out.println("add to JSON444444444----------------- "+c1+" " +c2);
		
		JSONObject obj2 = new JSONObject();
		String nm1 = null;
		
		//check the length of string of each activity
		String nm2 = null;
		//if no name1 and name2 in the activity list, add it!
		if(!actList.contains(name1))
		actList.add(name1);
		if(!actList.contains(name2))
		actList.add(name2);
		
		if(name1.length()>20){
			nm1 = name1.substring(0, 20);
			if(actAbb.contains(nm1) && !actList.contains(name1))
			nm1 += "-1";
		}
		     
		else{
			nm1 = name1;
		}
		if(name2.length()>20){
			nm2 = name2.substring(0, 20);
			if(actAbb.contains(nm2)&& !actList.contains(name2))
			nm2 += "-1";
			
		}
			else{
			nm2 = name2;
			}
		System.out.println("Testing add to JSON");
		if(!first1){
			obj2.put("consumers",2);
			obj2.put("count", c1);
			//obj2.put("inputQueue", "start");
			//obj2.put("inputThroughput", 50);
			obj.put(nm1, obj2);
			obj2.clear();
			//first1=true;
			/*obj2.put("consumers",2);
			obj2.put("count", c);
			obj2.put("inputQueue", "Start");
			obj2.put("inputThroughput", 50);
			obj.put(nm1, obj2);
			
			obj2.clear();*/
		}
		
		
		if (obj.containsKey(nm2) ) {
			obj2.put("consumers",2);
			obj2.put("count", c2);
			obj2.put("inputQueue", nm1);
			obj2.put("inputThroughput", c);
			obj.put(nm2, obj2);
		}else
		{
		
		
		obj2.put("consumers",2);
		obj2.put("count", c2);
		obj2.put("inputQueue", nm1);
		obj2.put("inputThroughput", c);
		obj.put(nm2, obj2);
		}

		/*if(obj.containsKey(name2))
		{
			
		}
		else
		{
			
		}*/
		
		System.out.println(nm1 + " -- "+ obj.toJSONString());
		//System.out.println(name2 + " -- "+ obj.toJSONString());
	}
	
	public void timediff(String s1, String s2 )
	{
		Date d1 = null;
	    Date d2 = null;
	    try {
			d1 = format.parse(s1);
			d2 = format.parse(s2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
	
	
	
	public String toString()
	{
		return obj.toJSONString();
	}
	
	public JSONObject getObject()
	{
		return obj;
	}
}
