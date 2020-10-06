package cviko03.uloha1;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class TrafficTracker {

	private final Map<String, Point> locations;

	public TrafficTracker(Map<String, Point> locations) {
		this.locations = copyMap(locations);
	}

	public Map<String, Point> getLocations() {
		return copyMap(locations);
	}

	private synchronized Map<String, Point> copyMap(Map<String, Point> map) {
		Map<String, Point> newMap = new HashMap<String, Point>();
		for (Entry<String, Point> entry : map.entrySet()) {
			newMap.put(entry.getKey(), new Point(entry.getValue()));
		}
		return newMap;
	}

	public Point getLocation(String id) {
		Point loc = locations.get(id);
		return loc == null ? null : new Point(loc);
	}

	public synchronized void setLocation(String id, int x, int y) {
		Point loc = (Point) locations.get(id);
		if (loc == null)
			throw new IllegalArgumentException("No such ID: " + id);
		loc.setX(x);
		loc.setY(y);
	}
}
