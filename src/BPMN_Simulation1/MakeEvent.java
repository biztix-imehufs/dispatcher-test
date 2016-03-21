package BPMN_Simulation1;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

// 횄횩째징 횉횠쩐횩횉횘째횒 : 째짖 Machine 쩍횄째짙 �횜쩐챤쩐횩횉횚.
public class MakeEvent {

	Multimap<String, String> Data_Activity = ArrayListMultimap.create(); // Activity
	Multimap<String, String> Data_ActivityTime = ArrayListMultimap.create(); // 째짖
																				// Activity�횉
																				// 째횋쨍짰쨈횂
																				// 쩍횄째짙
																				// ->
																				// 째짖
																				// Machine
																				// 횇쨍�횙�쨍쨌횓
																				// 쨔횢짼찾쩐횩횉횚
	Multimap<String, String> Data_ActivityMachine = ArrayListMultimap.create(); // 째짖
																				// Activity쩔징
																				// 횉횠쨈챌횉횕쨈횂
																				// Machine

	Multimap<String, String> Data_Machine = ArrayListMultimap.create(); // �체횄쩌
																		// Machine
	Multimap<String, String> Data_Process = ArrayListMultimap.create(); // �체횄쩌
																		// Process

	ArrayList<String> StartPoint = new ArrayList<String>(); // 째첩횁짚�횉 횄쨔 쨘횓쨘횖
															// Activity

	HashMap<String, Integer> prereqTransNumList;
	List<Transition> transitionList;

	private int TheNumOfProcess;
	private volatile String ProductID;
	private String activityID;
	private String WaitingActivity;
	private ArrayList<String> availableMachine = new ArrayList<String>();
	private String MachineID;
	private String ProcessTime, MinTime, MaxTime;

	private FactoryLine event;
	private double ProcessingTime;
	private String EndPointID;
	private String beforeProcess; // Before Process
	private String Status;
	private boolean CheckWait = false;

	String SelectActivity;
	String SelectMachine;

	Random rand = new Random();
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY hh:mm:ss");

	String Path;

	public MakeEvent(String RandomProductID, String Path) {
		this.Path = Path; // by the way, what is this "Path" for? - so far it's
							// not used -

		Data_Activity = GlobalRes.SAXParsing.getActivity();
		Data_ActivityTime = GlobalRes.SAXParsing.getActivityTime();
		Data_ActivityMachine = GlobalRes.SAXParsing.getActivityMachine();

		Data_Machine = GlobalRes.SAXParsing.getMachine();
		Data_Process = GlobalRes.SAXParsing.getProcess();

		StartPoint = GlobalRes.SAXParsing.getStartPoint();
		prereqTransNumList = GlobalRes.SAXParsing.getIncomingTransNum();
		transitionList = GlobalRes.SAXParsing.getTransitionList();

		this.ProductID = RandomProductID;
	}

	public void MakeTheEvent(String BFProcess, int numOfProcess,
			int andProcessIdx, boolean CheckStatusWait) {
		beforeProcess = BFProcess;
		this.TheNumOfProcess = numOfProcess;
		MachineID = null;
		Status = "Start";
		// System.out.println(BeforeProcess);
		Date nowDate = new Date();

		// Queue쩔징 �횜쨈횢째징 쨀짧쩔횂 쨩처횊짼
		if (CheckStatusWait) {
//			beforeProcess = activityID;
		}

		// 횄쨔 째첩횁짚 Activity 쩌짹횇횄
		else if (numOfProcess == 0) {
			// set a random activity as the starting node
			// when considering AND operation, only an independent activity can
			// be picked as starting point
			int RandomStartPoint = rand.nextInt(StartPoint.size());
			activityID = (String) StartPoint.get(RandomStartPoint);
//			beforeProcess = activityID;
			// System.out.println("Test");
		}

		// 2쨔첩횂째 째첩횁짚 Activity 쩌짹횇횄
		else {
			Set<String> keys = Data_Process.keySet();
			int RandomProcess;

			for (String key : keys) {
				// System.out.println("Test");
				if (key.equals(beforeProcess)) {

					// RandomProcess =
					// rand.nextInt(Data_Process.get(key).size()); // �횑쨘횓쨘횖
					// Capacity횉횠째찼

					ArrayList<String> ToArrayProcess = new ArrayList<>();
					ToArrayProcess.addAll(Data_Process.get(key));

					String[] xorProcesses = ToArrayProcess.get(andProcessIdx)
							.split(",");
					RandomProcess = rand.nextInt(xorProcesses.length);
					activityID = xorProcesses[RandomProcess];
					
					//Setting prerequisiteTransCount
					//-------------------------------------------------------
					String compoundID = ProductID.split("-",2)[0].concat(",")
							.concat(activityID);
					Integer prereqNum = getPrereqTransNum(activityID);
					Integer existingRec = GlobalRes.prereqCount
							.get(compoundID);
					if (existingRec == null)
						existingRec = 0;
					if (prereqNum != null)
						existingRec += prereqNum;
					GlobalRes.prereqCount.put(compoundID, existingRec);
					//----------------------------------------------------------

					break;
				}

			}
		}
		beforeProcess = activityID;

		// Activity ID -> Name�쨍쨌횓 �체횊짱
		Set<String> Act = Data_Activity.keySet();
		for (String ActKey : Act) {
			if (ActKey.equals(activityID)) {
				ArrayList<String> ActivityList = new ArrayList<>();
				ActivityList.addAll(Data_Activity.get(ActKey));

				SelectActivity = (String) ActivityList.get(0);

				if (SelectActivity.equals("End")) {
					EndPointID = activityID;
//					beforeProcess = activityID;
					Status = "Complete";
				}
				break;
			}
		}

		// Processing Time 짹쨍횉횕짹창
		Set<String> AT = Data_ActivityTime.keySet();
		for (String key : AT) {
			if (key.equals(activityID)) {

				ArrayList<String> ATList = new ArrayList<>();
				ATList.addAll(Data_ActivityTime.get(key));

				ProcessTime = (String) ATList.get(0);
				MinTime = (String) ATList.get(1);
				MaxTime = (String) ATList.get(2);

				double MinT = Double.parseDouble(MinTime);
				double MaxT = Double.parseDouble(MaxTime);
				double FinalTime = (Math.floor(Math.random()
						* (MaxT - MinT + 1)) + MinT); // Uniform 째챔쨩챗

				ProcessingTime = 2d;//FinalTime;2d
				break;
			}
		}

		// SelectActivity쩔징 횉횠쨈챌횉횕쨈횂 Machine 쩌짹횇횄
		Set<String> keys = Data_ActivityMachine.keySet();
		ArrayList<String> ToArrayMachine = new ArrayList<>();
		for (String key : keys) {
			if (key.equals(activityID)) {
				ToArrayMachine.addAll(Data_ActivityMachine.get(key));
//				beforeProcess = activityID;

				// 쨩챌쩔챘째징쨈횋횉횗 Machine�횑 �횜�쨩쨋짠
				for (String machineCandidate: ToArrayMachine) {
					if (GlobalRes.machineInUse.add(machineCandidate)) {
//						ConcurrentLinkedQueue<String> q = GlobalRes.QueueData.get(activityID);
//						if (//CheckStatusWait || // Queue쩔징 �횜쨈첩 Item
//														// 쩔챙쩌짹쩌첩�짠
//								// Queue쩔징 �횜�쨩 쩍횄 Machine seize 쨍첩횉횚.
//								q==null || q.isEmpty()) {
							MachineID = machineCandidate;
							break;
//						}
					}
				}
			}
		}
		
		String Time = sdf.format(nowDate);
		
		// 쨩챌쩔챘째징쨈횋횉횗 Machine�횑 쩐첩�쨩쨋짠
		/**
		 * Also conduct a checking whether the PREREQUISITE
		 * activities of the activity-executed-next are all DONE
		 */
		
		String compoundID = ProductID.split("-",2)[0].concat(",").concat(activityID);
		Integer prereqNum = GlobalRes.prereqCount.get(compoundID);
		if(prereqNum==null) prereqNum = 0;
		boolean isPrereqDone = prereqNum==0;
		if (!isPrereqDone ||
				(MachineID == null && Status.equals("Start") &&
				GlobalRes.isProcessActivity(SelectActivity))) {
			System.out.println("preReq: "+prereqNum+" | "+activityID);
			WaitingActivity = activityID;

			availableMachine.addAll(ToArrayMachine);
			ProcessingTime = 1d;
			Status = "Waiting";
			SelectMachine = "Wait";
			event = new FactoryLine(ProductID, SelectActivity, activityID,
					SelectMachine, Time, Status);
			return;
		}

		// Machine ID - > Name�쨍쨌횓 �체횊짱
		Set<String> Mach = Data_Machine.keySet();
		for (String MachKey : Mach) {
			if (MachKey.equals(MachineID)) {
				ArrayList<String> MachineList = new ArrayList<>();
				MachineList.addAll(Data_Machine.get(MachKey));

				SelectMachine = (String) MachineList.get(0);
				break;
			}
		}

		if (!GlobalRes.isProcessActivity(SelectActivity)) {
			SelectMachine = "X";
			ProcessingTime = 0d;
		}
		event = new FactoryLine(ProductID, SelectActivity, activityID, SelectMachine, Time,
				Status);

	}

	/**
	 * Get the activityID of the next process which is waiting for the
	 * completion of the given process.
	 * 
	 * @return
	 */
	public List<String> getAwaitingProcess(String completedProcess) {
		List<String> awaitingProcessList = new ArrayList<>();
		for (Transition t : transitionList) {
			if (t.From.equals(completedProcess)) {
				 awaitingProcessList.add(t.To);
			}
		}
		return awaitingProcessList;
	}

	public FactoryLine getEvent() {
		return event;
	}

	public double getProcessTime() {
		return ProcessingTime;
	}

	public String getEndPointID() {
		return EndPointID;
	}

	public String getBeforeProcess() {
		return beforeProcess;
	}

	public String getMachineID() {
		return MachineID;
	}

	public String getProductStatus() {
		return Status;
	}

	public String getWaitActivity() {
		return WaitingActivity;
	}

	public Integer getPrereqTransNum(String actID) {
		return prereqTransNumList.get(actID);
	}
	
	public synchronized void setProductID(String productID){
		this.ProductID = productID;
	}

	/**
	 * note: this is "not" getting "unused" machine(s), but getting "assigned"
	 * machine(s) for a particular activity (regardless whether they are
	 * currently being used or not)
	 */
	public ArrayList<String> getAvailableMachine() {
		return availableMachine;
	}
}