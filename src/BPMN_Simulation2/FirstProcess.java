package BPMN_Simulation2;

import java.util.ArrayList;
import java.util.Random;

import com.espertech.esper.client.EPServiceProvider;


public class FirstProcess implements Runnable{
	private EPServiceProvider epService;
	private String Path;
	private int ProductID; // ��ǰ ID
	private static int NumOfProduct = 10;
	private long InterArrivalTime = 0; // Product������ ������ ���� �ð� (Distribution)
	
	private ArrayList<Integer> CheckDuplicationPID = new ArrayList<Integer> ();
	
	public FirstProcess(EPServiceProvider epService) {
		this.epService = epService;
		this.Path = Path;
		
	}
	
	@Override
	public void run() {
		while(true){
			try{
			
				// ��ǰ �������� ID ����
				Random rand = new Random();
				int RandomProductID = rand.nextInt(NumOfProduct) + 1;
				ProductID = RandomProductID;
				
				// ������ ID�� �����Ǿ��ִ°� �ִ��� Ȯ��
				if (!CheckDuplicationPID.contains(ProductID)){
					CheckDuplicationPID.add(ProductID);
					
					Runnable Run = new SecondProcess(epService, ProductID);
					Thread t1= new Thread(Run);
					t1.start();
					InterArrivalTime = 3000;
				}
				else if(CheckDuplicationPID.size()==NumOfProduct){
					System.out.println("Complete FirstProcess");
					break;
				}
				//Thread.currentThread().getName().wait();
				//System.out.println("FirstProcess Thread Name : "+);
				Thread.sleep(InterArrivalTime);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}
	
}
