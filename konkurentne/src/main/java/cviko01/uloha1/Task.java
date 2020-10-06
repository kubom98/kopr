package cviko01.uloha1;

public class Task implements Runnable {

	public void run() {
		for (int i = 1; i <= 10; i++) {
			System.out.println("Task: " + i);
		}
	}

}
