package BPMN_Simulation1;

public class FactoryLine {
	private String id;
	private String process;
	private String processID;
	private String machine;
	private String time;
	private String status;
	//private String status;
	
	public FactoryLine(String SelectID, String SelectActivity, String activityID, String SelectMachine, String Time, String Status){
		this.id = SelectID;
		this.process = SelectActivity;
		this.processID = activityID;
		this.machine = SelectMachine;
		this.time = Time;
		this.status = Status;
		//this.status = Status;
	}
	
	public String getProductID(){
		return id;
	}
	
	public String getProcess(){
		return process;
	}
	
	public String getProcessID(){
		return processID;
	}
	
	public String getMachine(){
		return machine;
	}
	
	public String getTime(){
		return time;
	}
	
	public String getStatus(){
		return status;
	}
}
