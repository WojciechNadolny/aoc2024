package aoc2024.day12;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

record Plot(int row, int column, char type, AtomicInteger regionId, Set<Side> fences) {
	
	boolean isAdjacent(Plot other) {
		boolean nextAtSameRow = this.row == other.row
				&& ((this.column == other.column - 1) || (this.column == other.column + 1));
		boolean nextAtSameColumn = this.column == other.column
				&& ((this.row == other.row - 1) || (this.row == other.row + 1));
		return nextAtSameRow || nextAtSameColumn;
	}
	
	@Override
	public String toString() {
		return String.format("%c@(%d,%d)", type, row, column);
	}
}
