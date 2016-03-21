package BPMN_Simulation2;

import java.util.ArrayList;
import java.util.Random;

import com.espertech.esper.client.EPServiceProvider;


public class FirstProcess implements Runnable{
	private EPServiceProvider epService;
	private String Path;
	private int ProductID; // 제품 ID
	private static int NumOfProduct = 10;
	private long InterArrivalTime = 0; // Product공정에 들어오는 간격 시간 (Distribution)
	
	private ArrayList<Integer> CheckDuplicationPID = new ArrayList<Integer> ();
	
	public FirstProcess(EPServiceProvider epService) {
		this.epService = epService;
		this.Path = Path;
		
	}
	
	@Override
	public void run() {
		while(true){
			try{
			
				// 제품 랜덤으로 ID 생성
				Random rand = new Random();
				int RandomProductID = rand.nextInt(NumOfProduct) + 1;
				ProductID = RandomProductID;
				
				// 기존에 ID가 생성되어있는게 있는지 확인
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
