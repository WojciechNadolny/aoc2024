package aoc2024.day15;

import java.util.Iterator;
import java.util.List;

class Robot {
	
	int row;
	int column;
	final Iterator<Direction> direction;
	
	Robot(int row, int column, List<Direction> moves) {
		this.row = row;
		this.column = column;
		this.direction = moves.iterator();
	}
}
