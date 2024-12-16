package aoc2024.day16;

enum Heading {
	NORTH(-1, 0, '^'),
	SOUTH(1, 0, 'v'),
	EAST(0, 1, '>'),
	WEST(0, -1, '<');
	
	final int rowMove;
	final int columnMove;
	final char symbol;
	
	Heading(int rowMove, int columnMove, char symbol) {
		this.rowMove = rowMove;
		this.columnMove = columnMove;
		this.symbol = symbol;
	}
}
