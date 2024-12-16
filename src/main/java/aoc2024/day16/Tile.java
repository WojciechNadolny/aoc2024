package aoc2024.day16;

import java.util.Map;

record Tile(int row, int column, boolean viable, Map<Heading, Integer> scores) {
	
	@Override
	public String toString() {
		return (viable ? "free" : "wall") + "@(" + column + "," + row + ")";
	}
	
	@Override
	public boolean equals(Object object) {
		return object instanceof Tile that
				&& this.row == that.row
				&& this.column == that.column;
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
}
