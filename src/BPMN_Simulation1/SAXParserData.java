package BPMN_Simulation1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class SAXParserData {
	
	ArrayList<String> TotalTimeList = new ArrayList<>();
	ArrayList<String> ProcessTimeList = new ArrayList<>();
	ArrayList<String> MaxTimeList = new ArrayList<>();
	ArrayList<String> MinTimeList = new ArrayList<>();
	ArrayList<String> StartPoint = new ArrayList<>();										// 맨 처음 시작되는 Activity들
	Set<String> parallelConverging;
	Set<String> parallel;
	Set<String> nonParallelDiverging;
	Set<String> nonParallelConverging;
	Set<String> nonParallel;
	List<Transition> TransitionList;
	
	Multimap<String, String> Activity = ArrayListMultimap.create();				// 총 Activity
	Multimap<String, String> ActivityTime = ArrayListMultimap.create();			// 각 Activity 시간 (Key : 해당 Activity, Value : Processing Time, Min, Max)
	Multimap<String, String> ActivityMachine = ArrayListMultimap.create();		// 각 Activity의 Machine들 (Key : 해당 Activity, Value : Machine)

	Multimap<String, String> Machine = ArrayListMultimap.create();				// 전체 Machine
	Multimap<String, String> Process = ArrayListMultimap.create();				// 전체 Process (Key : 해당 Activity, Value : 다음 번 갈 수 있는 Activity)
	
	HashMap<String, Integer> incomingTransNum = new HashMap<>();
	
	public void ReadData() {

		try {
			parallelConverging = new HashSet<>();
			parallel = new HashSet<>();
			nonParallelDiverging = new HashSet<>();
			nonParallelConverging = new HashSet<>();
			nonParallel = new HashSet<>();
			
			SAXParserFactory parserFactor = SAXParserFactory.newInstance();
			SAXParser parser = parserFactor.newSAXParser();
			SAXHandler handler = new SAXHandler();
			parser.parse("C://Users//USER//workspaceLuna//DispatcherTest//withGateway.xpdl ", handler);//order
			// parser.parse(ClassLoader.getSystemResourceAsStream("xml/employee.xml"),
			// handler);

			TransitionList = handler.TransitionList;
			
			// Printing the list of employees obtained from XML --> what kind of employee? outdated comment?
			for (Time emp : handler.TimeList) {
				TotalTimeList.add(emp.ActivityTime);
			}
						
			// ProcessTime, MaxTime, MinTime input Data (parsing) from XPDL
			for(int i=0; i<TotalTimeList.size()-2;i++){
				int j = i + 1;
				int k = i + 2;
				
				ProcessTimeList.add(TotalTimeList.get(i));
				MinTimeList.add(TotalTimeList.get(j));
				MaxTimeList.add(TotalTimeList.get(k));
				
				i = k;
			}
			
			//System.out.println(ProcessTimeList);
			//System.out.println(MinTimeList);
			//System.out.println(MaxTimeList);
			
			int i = 0;
			//System.out.println(ProcessTimeList);
			for (Activity emp : handler.ActivityList) {
				Activity.put(emp.Id, emp.act);
				if (GlobalRes.isProcessActivity(emp.act)) {
					ActivityMachine.putAll(emp.Id, emp.machine);
					ActivityTime.put(emp.Id, ProcessTimeList.get(i));
					ActivityTime.put(emp.Id, MinTimeList.get(i));
					ActivityTime.put(emp.Id, MaxTimeList.get(i));
					System.out.println("i ="+i);
					System.out.println(ActivityTime);
					i++;
				}				
			}
			
			//System.out.println(ActivityTime);
			
			for (Machine emp : handler.MachineList) {
				Machine.put(emp.Id, emp.Machine);
			}

			for (Transition emp : TransitionList) {
				Process.put(emp.From, emp.To);
				if (!StartPoint.contains(emp.From))
					StartPoint.add(emp.From);
				
				//Distinguishing whether the a certain branching-transition is a converging or diverging one
				if(parallel.contains(emp.To)){
					parallelConverging.add(emp.To);
				}
				else if(nonParallel.contains(emp.From)){
					nonParallelDiverging.add(emp.From);
				}else if(nonParallel.contains(emp.To)){
					nonParallelConverging.add(emp.To);
				}
			}

			//to avoid any process having start-in-the-middle conflict
			for (int j = 0; j < Process.values().size(); j++) {
				for (int j1 = 0; j1 < StartPoint.size(); j1++) {
					if (Process.containsValue(StartPoint.get(j1))) {
						StartPoint.remove(StartPoint.get(j1));
					}
				}
			}
			
			/**
			 * Tidying-up the transition list (properly distinguish AND and XOR).
			 * For XOR, the destinations will be stored as one string with "," delimiter.
			 * For AND, each destination will be stored as a single string in the multimap as usual.
			 */
			
			for(String from: nonParallelDiverging){
				List<String> toArray = new ArrayList<>();
				toArray.addAll(Process.get(from));
				StringBuilder xorTos = new StringBuilder();
				String prefix="";
				for(String to : toArray){
					xorTos.append(prefix).append(to.trim());
					prefix=",";
				}
				Process.removeAll(from);
				Process.put(from, xorTos.toString());
			}
			
			/**
			 * Adjusting the number of prerequisite-counter of a xor-converging node to 1.
			 */
			for(String to: nonParallelConverging){
				incomingTransNum.put(to, 1);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public Multimap<String, String> getActivity(){
		return Activity;
	}
	
	public Multimap<String, String> getActivityTime(){
		return ActivityTime;
	}
	
	public Multimap<String, String> getActivityMachine(){
		return ActivityMachine;
	}
	
	public LinkedList<String> getActivities(String machineID){
		LinkedList<String> al = new LinkedList<>();
		for(String key : ActivityMachine.keySet()){
			if(ActivityMachine.get(key).contains(machineID)){
				al.add(key);
			}
		}
		return al;
	}
	
	public Multimap<String, String> getMachine(){
		return Machine;
	}
	
	public Multimap<String, String> getProcess(){
		return Process;
	}
	
	public ArrayList<String> getStartPoint(){
		return StartPoint;
	}
	
	public HashMap<String, Integer> getIncomingTransNum(){
		return incomingTransNum;
	}
	
	public List<Transition> getTransitionList(){
		return TransitionList;
	}
	
	// SAXHandler Class
	class SAXHandler extends DefaultHandler {

		List<Activity> ActivityList = new ArrayList<>();
		List<Machine> MachineList = new ArrayList<>();
		List<Time> TimeList = new ArrayList<>();
		List<Transition> TransitionList = new ArrayList<>();
		
		
		Activity proc = null;
		Machine mach = null;
		Time ProcTime = null;
		Transition Tran = null;

		String content = null;
		String ProcessTime;
		String MaxTime, MinTime;

		@Override
		// Triggered when the start of tag is found.
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {

			switch (qName) {
			// Create a new Employee object when the start tag is found
			case "Activity":
				proc = new Activity();
				proc.Id = attributes.getValue("Id");
				proc.act = attributes.getValue("Name");
				break;
			case "Participant":
				mach = new Machine();
				mach.Id = attributes.getValue("Id");
				mach.Machine = attributes.getValue("Name");
				break;
			case "Route":
				String mv = attributes.getValue("MarkerVisible");
				if("true".equals(mv)){
					nonParallel.add(proc.Id);
//					String direction = attributes.getValue("GatewayDirection");
//					if("Diverging".equals(direction) && proc!=null){ //actually proc should never be null; unless the XML is invalid
//						nonParallelDiverging.add(proc.Id);
//					}
//					else if("Converging".equals(direction) && proc!=null){ //actually proc should never be null; unless the XML is invalid
//						nonParallelConverging.add(proc.Id);
//					}
				}else{
					String gt = attributes.getValue("GatewayType");
					if("Parallel".equals(gt)){
						parallel.add(proc.Id);
//						String direction = attributes.getValue("GatewayDirection");
//						if("Converging".equals(direction)&& proc!=null){
//							parallelConverging.add(proc.Id);
//						}
					}
				}
				break;
			case "ExtendedAttribute":
				ProcessTime = attributes.getValue("Value");
				
				if (ProcessTime != null) {
					ProcTime = new Time();
					ProcTime.ActivityTime = ProcessTime;
				}
				break;
			case "Transition":
				Tran = new Transition();
				Tran.From = attributes.getValue("From");
				Tran.To = attributes.getValue("To");
				break;
			}

		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			switch (qName) {
			// Add the employee to list once end tag is found
			case "Activity":
				ActivityList.add(proc);
				break;
			// For all other end tags the employee has to be updated.
			case "Performer":
				proc.machine.add(content);
				break;
			case "Participant":
				MachineList.add(mach);
				break;
			case "ExtendedAttribute":
				if (ProcessTime != null) {
					TimeList.add(ProcTime);
				}
				break;
			case "Transition":
				TransitionList.add(Tran);
				
				/**
				 * Recording the number of incoming transitions to a particular activity;
				 * so that it is executed correctly, right after all the prerequisite activities are done (i.e., in parallel activities flow).
				 */
				Integer curIncoming = incomingTransNum.get(Tran.To);
				if(curIncoming==null){
					curIncoming=1;
				}
				else{
					curIncoming+=1;
				}
				incomingTransNum.put(Tran.To, curIncoming);
				break;
				
			/*
			 * case "name": proc.name = content; break; case "gender":
			 * proc.gender = content; break; case "role": proc.role = content;
			 * break;
			 */
			}
		}

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			content = String.copyValueOf(ch, start, length).trim();
		}
		
		
	}
}
