package aoc2024.day06;

enum Direction {
	UP('^', -1, 0),
	DOWN('v', 1, 0),
	LEFT('<', 0, -1),
	RIGHT('>', 0, 1);
	
	Direction(char marker, int rowMove, int columnMove) {
		this.marker = marker;
		this.rowMove = rowMove;
		this.columnMove = columnMove;
	}
	
	final char marker;
	final int rowMove;
	final int columnMove;
}
