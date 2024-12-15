package aoc2024.day13;

class Position implements Comparable<Position> {
	
	private long x;
	private long y;
	
	Position(long x, long y) {
		this.x = x;
		this.y = y;
	}
	
	long getX() {
		return x;
	}
	
	long getY() {
		return y;
	}
	
	Position addX(long x) {
		this.x += x;
		return this;
	}
	
	Position addY(long y) {
		this.y += y;
		return this;
	}
	
	Position add(Position that) {
		this.x += that.x;
		this.y += that.y;
		return this;
	}
	
	public void reset() {
		x = 0;
		y = 0;
	}
	
	@Override
	public int compareTo(Position that) {
		return (this.x == that.x && this.y == that.y)
				? 0
				: (this.x > that.x || this.y > that.y)
						? 1
						: -1;
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
}
