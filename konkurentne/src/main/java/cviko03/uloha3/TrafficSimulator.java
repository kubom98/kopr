package cviko03.uloha3;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;

public class TrafficSimulator {

	public static void main(String[] args) {
		Map<String, Point> cars = new HashMap<String, Point>();
		for (int i = 0; i < 10; i++) {
			cars.put("car" + i, new Point(((int) (20 * Math.random()) * 2), ((int) (20 * Math.random())) * 2));
		}
		TrafficTracker trafficTracker = new TrafficTracker(cars);
		for (int i = 0; i < 10; i++) {
			Thread thread = new Thread(new Car("car" + i, trafficTracker));
			thread.start();
		}
		Thread intruderThread = new Thread(new Intruder(trafficTracker));
		intruderThread.start();
		while (true) {
			Map<String, Point> locations = trafficTracker.getLocations();
			try {
				for (String carName : locations.keySet()) {
					Point point = locations.get(carName).getCopy();
					int x = 0;
					int y = 0;
					// 1
					// synchronized (point) {
					x = point.getX();
					y = point.getY();
					// }
					// 2
					// int[] array = point.getXY();
					// x = array[0];
					// y = array[1];
					System.out.print(carName + ":[" + x + "," + y + "],");
					if (Math.abs(x) % 2 != Math.abs(y) % 2) {
						System.err.println("Nekonzistentny bod: " + carName + ":[" + x + "," + y + "]");
					}
				}
			} catch (ConcurrentModificationException e1) {
				System.err.println("main: niekto mi pod rukami zmazal auto");
			}
			System.out.println();
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
