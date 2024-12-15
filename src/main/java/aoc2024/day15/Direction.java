package aoc2024.day15;

enum Direction {
	UP( -1, 0),
	DOWN( 1, 0),
	LEFT( 0, -1),
	RIGHT( 0, 1);
	
	final int rowMove;
	final int columnMove;
	
	Direction(int rowMove, int columnMove) {
		this.rowMove = rowMove;
		this.columnMove = columnMove;
	}
}
