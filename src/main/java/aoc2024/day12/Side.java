package aoc2024.day12;

enum Side {
	TOP( -1, 0),
	BOTTOM( 1, 0),
	LEFT( 0, -1),
	RIGHT( 0, 1);
	
	final int rowLook;
	final int columnLook;
	
	Side(int rowLook, int columnLook) {
		this.rowLook = rowLook;
		this.columnLook = columnLook;
	}
}
