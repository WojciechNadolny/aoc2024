package aoc2024.day15;

import java.util.ArrayList;
import java.util.List;

class RobotBuilder {
	
	private Integer row = null;
	private Integer column = null;
	private final List<Direction> directions = new ArrayList<>();
	
	void setRow(Integer row) {
		this.row = row;
	}
	
	void setColumn(Integer column) {
		this.column = column;
	}
	
	public void addLineOfCode(String lineOfCode) {
		for (char instruction : lineOfCode.toCharArray()) {
			directions.add(switch (instruction) {
				case '^' -> Direction.UP;
				case 'v' -> Direction.DOWN;
				case '<' -> Direction.LEFT;
				case '>' -> Direction.RIGHT;
				default -> throw new IllegalStateException("Unexpected instruction: " + instruction);
			});
		}
	}
	
	public Robot build() {
		return new Robot(row, column, directions);
	}
}
