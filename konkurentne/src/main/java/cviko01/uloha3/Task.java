package cviko01.uloha3;

public class Task implements Runnable {

	private Counter counter = new Counter();

	public void run() {
		String name = Thread.currentThread().getName();
		synchronized (counter) {
			for (int i = 1; i <= 10000; i++) {
				System.out.println(name + ": " + counter.getNext());
			}
		}
	}

}
