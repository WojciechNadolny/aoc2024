package aoc2024.day06;

class Guard {
	
	public Guard(int row, int column, Direction direction) {
		this.row = row;
		this.column = column;
		this.direction = direction;
	}
	
	private int row;
	private int column;
	private Direction direction;
	
	public int getRow() {
		return row;
	}
	
	public int getColumn() {
		return column;
	}
	
	public Direction getDirection() {
		return direction;
	}
	
	public void walk() {
		row += direction.rowMove;
		column += direction.columnMove;
	}
	
	public void turn() {
		direction = switch(direction) {
			case UP -> Direction.RIGHT;
			case RIGHT -> Direction.DOWN;
			case DOWN -> Direction.LEFT;
			case LEFT -> Direction.UP;
		};
	}
	
	@Override
	public String toString() {
		return String.format("Guard at row %d, column %d, heading %s", row, column, direction.toString().toLowerCase());
	}
	
	@Override
	public boolean equals(Object otherObject) {
		return otherObject instanceof Guard other
				&& this.row == other.row
				&& this.column == other.column
				&& this.direction == other.direction;
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
}
