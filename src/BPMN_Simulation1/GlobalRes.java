package BPMN_Simulation1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;

import com.espertech.esper.client.EPServiceProvider;

/**
 * This class holds the (global) reference to machines object.
 * (each activity will have one (single) global list-of-machines object).
 * 
 * @author Yohan
 *
 */
public class GlobalRes {
	private static ConcurrentHashMap<String, Boolean> semaphoreFlags = new ConcurrentHashMap<>();
	public static SAXParserData SAXParsing = new SAXParserData();
	private static Set<String> nonProcessActivities = new HashSet<>();
	public static void initialize(){
		nonProcessActivities.add("start");
		nonProcessActivities.add("end");
		nonProcessActivities.add("split");
		nonProcessActivities.add("join");
		nonProcessActivities.add("select");
		nonProcessActivities.add("parallel");
		GlobalRes.SAXParsing.ReadData();
	}
	
	//just moving this variable from "SecondProcess" class to this class for organizing things a bit..
	public static ConcurrentSkipListSet<String> machineInUse = new ConcurrentSkipListSet<>(); // list of machine in use by the running processes
	public static ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> QueueData = new ConcurrentHashMap<>();
	public static ConcurrentHashMap<String, Integer> prereqCount = new ConcurrentHashMap<>();
	public static ConcurrentLinkedQueue<String> passedProcesses = new ConcurrentLinkedQueue<>();
	public static ConcurrentSkipListSet<String> initiatedProcesses = new ConcurrentSkipListSet<>();
	
//	public static ConcurrentHashMap<String, List<String>> sensitiveTimestamps = new ConcurrentHashMap<>();
	
	private static HashMap<String, ArrayList<MyThread>> qThreads = new HashMap<>();
	private static HashMap<String, MyThread> listThreads = new HashMap<>();
	//listThreads ini key-nya harus dibuat compound; ndak hanya nilai int dari ProductID, tapi juga dengan string of activityID 
	
	public static synchronized void registerQueingThread(String activityID, String runnableProductID){
		ArrayList<MyThread> qt = qThreads.get(activityID);
		if(qt==null){
			qt = new ArrayList<>();
			qThreads.put(activityID, qt);
		}
		qt.add(listThreads.get(runnableProductID));
	}
	
	public static synchronized void notifyNextRunnableInQueue(String activityID, String machineID){
		LinkedList<String> otherActs;
		if(machineID==null || machineID.equalsIgnoreCase("x")){
			otherActs = new LinkedList<>();
		}else{
			otherActs = GlobalRes.SAXParsing.getActivities(machineID);
			otherActs.remove(activityID);
		}
		
		//the same activityID (activity type) is preferred;
		//in real world practice, it would prevent delay due to (potential) tools-changing
		otherActs.add(0, activityID);
		ArrayList<MyThread> q = qThreads.get(activityID);
			
		for(String a: otherActs){
			q = qThreads.get(a);
			if(q!=null && q.size()>0){
				q.get(0).doNotify();
				q.remove(0);
				break;
			}
		}
	}
	
	
	
	public static synchronized void createNewProductThread(EPServiceProvider epService, String ProductID){
		SecondProcess sp = new SecondProcess(epService, ProductID);
		MyThread t1 = new MyThread(sp);
		listThreads.put(ProductID, t1);
		t1.start();
	}
	
	public static synchronized void createNewSubProductThread(EPServiceProvider epService, String ProductID, int numOfProcess, String beforeProcess, int selParallelIdx){
		SecondProcess sp = new SecondProcess(epService, ProductID);
		sp.numOfProcess = numOfProcess;
		sp.beforeProcess = beforeProcess;
		sp.selectedParallelIndex = selParallelIdx;
		//with this, the thread of the subProduct will start where it stopped-in-the-middle-of-the-path
		
		MyThread t1 = new MyThread(sp);
		listThreads.put(ProductID, t1);
		t1.start();
	}
	
	public static synchronized void reregisterThread(String oldProdID, String newProductID){
		MyThread t = listThreads.get(oldProdID);
		listThreads.remove(t);
		listThreads.put(newProductID, t);
	}
	
	public static boolean isProcessActivity(String actName){
		if(actName.trim().isEmpty())
			return false;
		
		String target = actName.toLowerCase();
		for(String s: nonProcessActivities) {
			if(target.contains(s)){
				return false;
			}
		}
		return true;
	}
	
	public static synchronized boolean obtainFlag(String machine){
		if (machine==null || machine.equalsIgnoreCase("x")) return true;
		
		Boolean flag = semaphoreFlags.get(machine);
		if(flag==null || flag){
			semaphoreFlags.put(machine, false);
			return true;
		}
		
		return false;
	}
	
	public static synchronized void returnFlag(String machine){
		if(machine==null || machine.equalsIgnoreCase("x")) return;
		
		semaphoreFlags.put(machine, true);
	}
	
	public static synchronized void addQueueElement(String k, String v){
		ConcurrentLinkedQueue<String> li = QueueData.get(k);
		if(li==null){
			li = new ConcurrentLinkedQueue<>();
			QueueData.put(k, li);
		}
		li.add(v);
	}
	
	public static synchronized void printQueueData(){
		System.out.println("Update Queue : ".concat(QueueData.toString()));
	}
}


