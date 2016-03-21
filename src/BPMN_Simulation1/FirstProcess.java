package BPMN_Simulation1;

import java.util.ArrayList;
import java.util.Random;

import com.espertech.esper.client.EPServiceProvider;

public class FirstProcess implements Runnable {
	private EPServiceProvider epService;
//	private String Path;
	private int ProductID; // ��ǰ ID
	private static int NumOfProduct = 100;
	private long InterArrivalTime = 0; // Product������ ������ ���� �ð�
										// (Distribution)

	private ArrayList<Integer> uniqueProductID = new ArrayList<Integer>();

	public FirstProcess(EPServiceProvider epService) {
		this.epService = epService;
//		this.Path = Path;
	}

	@Override
	public void run() {
		while (uniqueProductID.size() < NumOfProduct) {
			try {

				// ��ǰ �������� ID ����
				Random rand = new Random();
				int RandomProductID = rand.nextInt(NumOfProduct) + 1;
				ProductID = RandomProductID;

				// ������ ID�� �����Ǿ��ִ°� �ִ��� Ȯ��
				if (!uniqueProductID.contains(ProductID)) {
					uniqueProductID.add(ProductID);

					GlobalRes.createNewProductThread(epService, String.valueOf(ProductID));
					
					/**
					 * What is InterArrivalTime?
					 * I assume this is "only" used for the unreliable queue mechanism using "Thread.sleep".
					 * Thus, I comment it now and you may remove it if my "assumption" is correct.
					 * Otherwise, please adjust accordingly.
					 */
					// InterArrivalTime = 3000;
				} else if (uniqueProductID.size() == NumOfProduct) {
					System.out.println("Complete FirstProcess");
				}
				
				/**
				 *  --> Waiting for Activity A to complete?
				 *  I assume this is going to be replaced by wait-notify mechanism.
				 *  (It's done in SecondProcess class)
				 */
				// Thread.sleep(InterArrivalTime);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

}
