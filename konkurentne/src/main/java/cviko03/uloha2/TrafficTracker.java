package cviko03.uloha2;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TrafficTracker {

	private final ConcurrentMap<String, Point> locations;

	public TrafficTracker(Map<String, Point> locations) {
		this.locations = new ConcurrentHashMap<String, Point>(locations);
	}

	public Map<String, Point> getLocations() {
		return Collections.unmodifiableMap(locations);
	}

	public Point getLocation(String id) {
		Point loc = locations.get(id);
		return loc == null ? null : new Point(loc);
	}

	public void setLocation(String id, int x, int y) {
		Point loc = locations.replace(id, new Point(x, y));
		if (loc == null)
			throw new IllegalArgumentException("No such ID: " + id);
	}
}
