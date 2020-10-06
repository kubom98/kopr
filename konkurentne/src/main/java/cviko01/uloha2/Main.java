package cviko01.uloha2;

public class Main {

	public static void main(String[] args) {
		Task task = new Task();
		
		Thread thread = new Thread(task);
		thread.start();
		thread.setName("First");
		
		Thread thread2 = new Thread(task);
		thread2.start();
		thread2.setName("Second");
		
		System.out.println("Main ends.");
	}

}
