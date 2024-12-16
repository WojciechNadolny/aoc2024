package aoc2024.day16;

class Score {
	
	static final int STEP = 1;
	static final int TURN = 1000;
	
	static int ofTurn(Heading from, Heading to) {
		return TURN * Math.max(
				Math.abs(from.rowMove - to.rowMove),
				Math.abs(from.columnMove - to.columnMove));
	}
}
