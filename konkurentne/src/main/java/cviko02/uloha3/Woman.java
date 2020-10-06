package cviko02.uloha3;

import java.util.List;

public class Woman implements Runnable {

	private List<Man> men;

	public Woman(List<Man> men) {
		this.men = men;
	}

	public void run() {
		for (Man man : men) {
			synchronized (man) {
				System.out.println("žena: hodnotím " + man.getId() + ". chlapa: " + man.toString());
			}
		}
	}

}