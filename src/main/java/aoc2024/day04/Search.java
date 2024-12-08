package aoc2024.day04;

public enum Search {
	RIGHT(0, 1, 0, 1, 0, 4),
	DOWN_RIGHT(1, 1, 0, 4, 0, 4),
	DOWN(1, 0, 0, 4, 0, 1),
	DOWN_LEFT(1, -1, 0, 4, 3, 1),
	LEFT(0, -1, 0, 1, 3, 1),
	UP_LEFT(-1, -1, 3, 1, 3, 1),
	UP(-1, 0, 3, 1, 0, 1),
	UP_RIGHT(-1, 1, 3, 1, 0, 4);
	
	Search(int rowModifier, int columnModifier, int topMargin, int bottomMargin, int leftMargin, int rightMargin) {
		this.rowModifier = rowModifier;
		this.columnModifier = columnModifier;
		this.topMargin = topMargin;
		this.bottomMargin = bottomMargin;
		this.leftMargin = leftMargin;
		this.rightMargin = rightMargin;
	}
	
	final int rowModifier;
	final int columnModifier;
	final int topMargin;
	final int bottomMargin;
	final int leftMargin;
	final int rightMargin;
}
