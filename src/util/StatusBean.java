package util;

import java.io.Serializable;

public class StatusBean implements Runnable, Serializable {
	private boolean status;
	private StringBuilder sb;
	private String ss = "";

	public StatusBean() {
		status = false; // #1
		sb = new StringBuilder("<html>");
	}

	public synchronized boolean isCompleted() {
		return status;
	}

	public synchronized void setStatus(boolean status) {
		this.status = status;
	}

	public void run() {
		try {
			Thread.sleep(20000); // #2
			this.status = true; // #3
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public synchronized String getInt() {
		int a = (int) (Math.random() * 100);
		sb.append(Integer.toString(a));
		sb.append("<br/>");
		// ss.concat(Integer.toString(a)+"+");
		// return ss;
		return sb.toString().concat("</html>");
	}
}
