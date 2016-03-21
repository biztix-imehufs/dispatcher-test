package BPMN_Simulation2;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
// �߰� �ؾ��Ұ� : �� Machine �ð� �־����.
public class MakeEvent {

	Multimap<String, String> Data_Activity = ArrayListMultimap.create(); // Activity
	Multimap<String, String> Data_ActivityTime = ArrayListMultimap.create(); // �� Activity�� �ɸ��� �ð� -> �� Machine Ÿ������ �ٲ����
	Multimap<String, String> Data_ActivityMachine = ArrayListMultimap.create(); // �� Activity�� �ش��ϴ� Machine

	Multimap<String, String> Data_Machine = ArrayListMultimap.create(); // ��ü Machine
	Multimap<String, String> Data_Process = ArrayListMultimap.create(); // ��ü Process

	ArrayList<String> StartPoint = new ArrayList<String>(); // ������ ù �κ� Activity
	
	private int TheNumOfProcess;
	private int ProductID;
	private String AcitivtyID;
	private String WaitingActivity;
	private ArrayList<String> CheckingAvailableMachine = new ArrayList<String>();
	private String MachineID;
	private String ProcessTime, MinTime, MaxTime;
	
	private FactoryLine event; 
	private double ProcessingTime;
	private String EndPointID;
	private String BeforeProcess; // Before Process
	private String Status;
	private boolean CheckWait=false;
	
	private ArrayList<String> UsingMachine = new ArrayList<String>();
	
	String SelectActivity;
	String SelectMachine;
		
	Random rand = new Random();
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY hh:mm:ss");
	
	String Path;
	
	public MakeEvent(int RandomProductID, String Path) {
		this.Path = Path;
		
		SAXParserData SAXParsing = new SAXParserData();
		SAXParsing.ReadData();

		Data_Activity = SAXParsing.getActivity();
		Data_ActivityTime = SAXParsing.getActivityTime();
		Data_ActivityMachine = SAXParsing.getActivityMachine();

		Data_Machine = SAXParsing.getMachine();
		Data_Process = SAXParsing.getProcess();

		StartPoint = SAXParsing.getStartPoint();
		
		this.ProductID = RandomProductID;		
		
				
	}
	
	public void MakeTheEvent(String BFProcess, int TheNumOfProcess, boolean CheckStatusWait){
		BeforeProcess = BFProcess;
		UsingMachine = SecondProcess.UsingMachine;
		this.TheNumOfProcess = TheNumOfProcess;
		MachineID = null;
		Status = "Start";
		//System.out.println(BeforeProcess);
		Date nowDate = new Date();
		
		// Queue�� �ִٰ� ���� ��Ȳ
		if(CheckStatusWait==true){
			BeforeProcess = AcitivtyID;
		}
		
		// ù ���� Activity ����
		else if(TheNumOfProcess==0){
			System.out.println("size>> "+StartPoint.size());
			int RandomStartPoint = rand.nextInt(StartPoint.size());
			AcitivtyID = (String) StartPoint.get(RandomStartPoint);
			BeforeProcess = AcitivtyID;
			//System.out.println("Test");
		}
		
		// 2��° ���� Activity ����
		else{
			
			Set<String> keys = Data_Process.keySet();
			int RandomProcess;
			
			for (String key : keys) {
				//System.out.println("Test");
				if (key.equals(BeforeProcess)) {
					
					RandomProcess = rand.nextInt(Data_Process.get(key).size());		// �̺κ�  Capacity�ذ�
					
					ArrayList ToArrayProcess = new ArrayList();
					ToArrayProcess.addAll(Data_Process.get(key));

					AcitivtyID = (String) ToArrayProcess.get(RandomProcess);
					
					break;
				}

			}
		}

		// Activity ID -> Name���� ��ȯ
		Set<String> Act = Data_Activity.keySet();
		for (String ActKey : Act) {
			if (ActKey.equals(AcitivtyID)) {
				ArrayList ActivityList = new ArrayList();
				ActivityList.addAll(Data_Activity.get(ActKey));

				SelectActivity = (String) ActivityList.get(0);
				
				if (SelectActivity.equals("End")) {
					EndPointID = AcitivtyID;
					BeforeProcess = AcitivtyID;
					Status = "Complete";
				}
				break;
			}
		}
		
		// Processing Time ���ϱ�
		Set<String> AT = Data_ActivityTime.keySet();
		for (String key : AT) {
			if (key.equals(AcitivtyID)) {

				ArrayList ATList = new ArrayList();
				ATList.addAll(Data_ActivityTime.get(key));

				ProcessTime = (String) ATList.get(0);
				MinTime = (String) ATList.get(1);
				MaxTime = (String) ATList.get(2);

				double MinT = Double.parseDouble(MinTime);
				double MaxT = Double.parseDouble(MaxTime);
				double FinalTime = (Math.floor(Math.random()
						* (MaxT - MinT + 1)) + MinT); // Uniform ���

				ProcessingTime = FinalTime;
				break;
			}
		}

		// SelectActivity�� �ش��ϴ� Machine ����
		Set<String> keys = Data_ActivityMachine.keySet();
		for (String key : keys) {
			if (key.equals(AcitivtyID)) {
				ArrayList ToArrayMachine = new ArrayList();
				ToArrayMachine.addAll(Data_ActivityMachine.get(key));
				BeforeProcess = AcitivtyID;
				
				// ��밡���� Machine�� ������
				for(int i=0; i<ToArrayMachine.size(); i++){
					if(!UsingMachine.contains(ToArrayMachine.get(i))){
						
						// Queue�� �ִ� Item �켱����
						if(CheckStatusWait==true){
							MachineID = (String) ToArrayMachine.get(i);
							UsingMachine.add(MachineID);
							
							break;
						}
						
						// Queue�� ���� �� Machine seize ����.
						else if(SecondProcess.QueueData.get(AcitivtyID).isEmpty()==true){
							MachineID = (String) ToArrayMachine.get(i);
							UsingMachine.add(MachineID);
							
							break;
						}
					}
				}
				
				// ��밡���� Machine�� ������
				if(MachineID == null && Status.equals("Start")){
					WaitingActivity = AcitivtyID;
					CheckingAvailableMachine.addAll(ToArrayMachine);
					ProcessingTime = 0;
					String Time = sdf.format(nowDate);
					Status = "Waiting";
					SelectMachine = "Wait";
					event = new FactoryLine(ProductID, SelectActivity, SelectMachine, Time, Status);
					return;
				}
				
			}
		}

		// Machine ID - > Name���� ��ȯ
		Set<String> Mach = Data_Machine.keySet();
		for (String MachKey : Mach) {
			if (MachKey.equals(MachineID)) {
				ArrayList MachineList = new ArrayList();
				MachineList.addAll(Data_Machine.get(MachKey));

				SelectMachine = (String) MachineList.get(0);
				break;
			}
		}

		String Time = sdf.format(nowDate);
		if(SelectActivity.equals("Start")||SelectActivity.equals("End")){
			SelectMachine = "X";
		}
		event = new FactoryLine(ProductID, SelectActivity, SelectMachine, Time, Status);
					
	}
	
	public FactoryLine getEvent(){
		return event;
	}
	
	public double getProcessTime(){
		return ProcessingTime;
	}

	public String getEndPointID(){
		return EndPointID;
	}
	
	public String getBeforeProcess(){
		return BeforeProcess;
	}
	
	public String getMachineID(){
		return MachineID;
	}
	
	public String getProductStatus(){
		return Status;
	}
		
	public String getWatiActivity(){
		return WaitingActivity;
	}
	
	public ArrayList<String> getAvailableMachine(){
		return CheckingAvailableMachine;
	}
}