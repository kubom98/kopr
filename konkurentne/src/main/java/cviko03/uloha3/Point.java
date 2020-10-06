package cviko03.uloha3;

public class Point {

	private int x;
	private int y;

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Point(Point ip) {
		this.x = ip.getX();
		this.y = ip.getY();
	}

	public synchronized int getX() {
		return x;
	}

	public synchronized int getY() {
		return y;
	}

	public synchronized int[] getXY() {
		return new int[] { x, y };
	}

	public synchronized void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public synchronized Point getCopy() {
		return new Point(x, y);
	}
}
