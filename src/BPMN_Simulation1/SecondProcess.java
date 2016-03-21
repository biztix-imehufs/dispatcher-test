package BPMN_Simulation1;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.espertech.esper.client.EPServiceProvider;

public class SecondProcess implements Runnable {

	private ArrayList<String> SaveProcess = new ArrayList<String>(); // 각 제품
																		// 공정

	private EPServiceProvider epService;
	private volatile String ProductID; // 선택된 Product ID
	protected int numOfProcess = 0; // 공정이 몇 번째 인지 나타냄
	private long ProcessingTime = 1;
	private String EndPointID = "";
	protected Integer selectedParallelIndex = null;

	protected String beforeProcess = "d";
	private String Status = "Complete";
	private String UsingMachineID;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY hh:mm:ss");

	// Queue상황 고려 Variables
	private boolean isWaitingToBeNotified = false;
	private boolean EndCondition = false;
	private boolean IsWaitingStatus = false;
	private String waitingActivityID = "";

	private ArrayList<String> CheckingMachine = new ArrayList<>();
	private ConcurrentLinkedQueue<String> EachQueue;

	FactoryLine Event, StartEvent, EndEvent, QueueEvent; // 최종 출력할 Event
	String activityID;
	String Path; // XPDL 경로

	public SecondProcess(EPServiceProvider epService, String ProductID) {
		this.epService = epService;
		// this.Path = Path;
		this.ProductID = ProductID;
	}

	@Override
	// ESper Engine을 통해 돌리기
	public synchronized void run() {
		// 제품 ID에 대한 Event 생성
		MakeEvent me = new MakeEvent(ProductID, Path);
		
		while (!EndCondition) {
			while (!isWaitingToBeNotified) {
				try {
					String prodAndAct = ProductID.split("-",2)[0].concat(",")
							.concat(beforeProcess);
					List<String> parallelActs = new ArrayList<>();
					parallelActs.addAll(me.Data_Process.get(beforeProcess));
					int parallelActNum = parallelActs.size();

					if (!GlobalRes.passedProcesses.contains(prodAndAct)) {
						GlobalRes.passedProcesses.add(prodAndAct);
						
						if (parallelActs.size() > 1) { // AND operation detected

							for (int actIdx = 0; actIdx < parallelActNum; actIdx++) {

								String subProductID = ProductID.concat("-")
										.concat(String.valueOf(actIdx + 1));
								// create new subProduct thread
								GlobalRes.createNewSubProductThread(epService,
										subProductID, numOfProcess,
										beforeProcess, actIdx);
							}

							// terminate current thread since subthreads for
							// handling next processes (containing parallel
							// activities) were already created
							EndCondition = true;
							break;
						}
					}

					me.MakeTheEvent(
							beforeProcess,
							numOfProcess,
							selectedParallelIndex != null ? selectedParallelIndex
									: 0, IsWaitingStatus);
					selectedParallelIndex = null;

					Event = me.getEvent();
					beforeProcess = me.getBeforeProcess();
					//from this point forward, "beforeProcess" shall contain current activityID
					
					//this will be used for destroying multiple converging subthreads
					prodAndAct = ProductID.split("-",2)[0].concat(",")
							.concat(beforeProcess);
					GlobalRes.initiatedProcesses.add(prodAndAct);
					
					Status = me.getProductStatus();

					// 종료 Condition
					EndPointID = me.getEndPointID();
					
					if (beforeProcess.equals(EndPointID)) {

						StartEvent = Event;
						epService.getEPRuntime().sendEvent(StartEvent);
						EndCondition = true;

						System.out.println("Final Complete");
						break;
					}

					// Queue에 들어가있는 상황
					else if (Status.equals("Waiting")) {
						QueueEvent = Event;
						epService.getEPRuntime().sendEvent(QueueEvent);
						waitingActivityID = me.getWaitActivity();

						// get the reference of the global machines object
						CheckingMachine = me.getAvailableMachine();

						GlobalRes.addQueueElement(waitingActivityID, ProductID);

						isWaitingToBeNotified = true;

						// Processing Time
						/*
						 * ProcessingTime = (long) (me.getProcessTime() * 1000);
						 * System.out.println("Processing Time : " +
						 * me.getProcessTime());
						 */

						// let's wait patiently until any
						// machine-assigned-for-this-activity is ready..
						// System.out.println("\n->current ActivityID" +
						// WaitActivityID);
						// System.out.println("Current Num of registered machine groups: "+MachineRegistrar.getNumOfMachines());

						GlobalRes.registerQueingThread(QueueEvent.getProcessID(),
								ProductID);
						System.out.println("pausing "+QueueEvent.getProcessID()+" ~ "+ProductID);
						this.wait();
						System.out.println("resuming "+QueueEvent.getProcessID()+" ~ "+ProductID);
					}

					// Process 처리중인 상황
					//to avoid "deadlock", we will use the following condition.
					else {
						
						IsWaitingStatus = false;
						SaveProcess.add(beforeProcess);

						// EventListener로 보내기
						StartEvent = Event;
						epService.getEPRuntime().sendEvent(StartEvent);

						// Processing Time
						ProcessingTime = (long) (me.getProcessTime() * 1000);
						System.out.println("Processing Time : "
								+ me.getProcessTime());

						numOfProcess++;

						// simulating a long process which takes time as long as
						// "ProcessingTime" time unit
						Thread.sleep(ProcessingTime);

						Date nowDate = new Date();
						String EndTime = sdf.format(nowDate);
						Status = "Complete";

						EndEvent = new FactoryLine(ProductID,
								StartEvent.getProcess(),StartEvent.getProcessID(),
								StartEvent.getMachine(), EndTime, Status);
						
						// Product in Queue entered
						//
						epService.getEPRuntime().sendEvent(EndEvent);

						// Busy Machine 업데이트 (Idle machine Id 지욱
						UsingMachineID = me.getMachineID();
						if (UsingMachineID != null) {
							GlobalRes.machineInUse.remove(UsingMachineID);	
						}

						// updating the incoming-transition-number record when
						// an activity is completed
						String completedProcess = StartEvent.getProcessID();
						List<String> awaitingProcs = me
								.getAwaitingProcess(completedProcess);
						String compoundID = null;
						for(String awaitingProc : awaitingProcs){
							compoundID = ProductID.split("-",2)[0].concat(",")
									.concat(awaitingProc);
							Integer pre = GlobalRes.prereqCount.get(compoundID);
							if (pre == null)
								pre = 0;

							pre -= 1;
							GlobalRes.prereqCount.put(compoundID, pre);
							
							if(pre==0){
								GlobalRes.notifyNextRunnableInQueue(awaitingProc, null);
							}
						}
						
						// one process/activity is done, let's call
						// the next "product-in-queue".. =)
						GlobalRes.returnFlag(UsingMachineID);
						GlobalRes.notifyNextRunnableInQueue(completedProcess, UsingMachineID);
						
						if(awaitingProcs.size()==1){
							//it's an unnecessary parallel-branch-thread; just terminate it
							if(GlobalRes.initiatedProcesses.add(compoundID)){
								//normalize productID; if needed
								if(GlobalRes.SAXParsing.parallelConverging.contains(awaitingProcs.get(0))){
									char[] pc = ProductID.toCharArray();
									for(int c=pc.length-1;c>0;c--){
										if(pc[c]=='-') {
											String oldProdID = ProductID;
											ProductID = String.copyValueOf(pc, 0, c);
											me.setProductID(ProductID);
											GlobalRes.reregisterThread(oldProdID, ProductID);
											break;
										}
									}
								}
							} else {
								EndCondition = true;
								System.out.println("destroying "+completedProcess+" ~ "+ProductID);
								break;
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			while (isWaitingToBeNotified) {
				EachQueue = GlobalRes.QueueData.get(waitingActivityID);
				// edit: added safety-check -> "EachQueue.size()>0"
				if (EachQueue.size() > 0 && EachQueue.contains(ProductID)) {
					boolean canProceed = false;
					if(CheckingMachine.size()==0)
					{
						//machine X, usually just bridging process; such as: join_d_e
						canProceed = true;
					}
					else{
						for (int i = 0; i < CheckingMachine.size(); i++) {
							String m = CheckingMachine.get(i);
							if (!GlobalRes.machineInUse.contains(m)) {
								//there is a machine that can be used
								canProceed = true;
								//get the key in advance
								GlobalRes.obtainFlag(m);
								break;
							}
						}
					}
					if(canProceed){
						// removing the first element in "EachQueue"
						EachQueue.remove(ProductID);
						
						GlobalRes.printQueueData();

						IsWaitingStatus = true;
						isWaitingToBeNotified = false;
					}
				}
			}
		}

	}

}
