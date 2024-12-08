package aoc2024.day04;

import aoc2024.DayResolver;
import aoc2024.util.PuzzleInputLoader;

import java.util.List;

public class Day04Resolver implements DayResolver<Integer> {
	
	private int width;
	private int height;
	private char[][] wordSearch;
	
	public static void main(String[] args) {
		Day04Resolver resolver = new Day04Resolver();
		List<String> personalSecretPuzzleInput = PuzzleInputLoader.loadPersonalSecretPuzzleInput(4);
		
		System.out.println("***  Part 1: XMAS occurrences  ***");
		System.out.println("XMAS occurrences: " + resolver.resolvePart1(personalSecretPuzzleInput));
		
		System.out.println("***  Part 2: Diagonally crossed MAS occurrences  ***");
		System.out.println("Crossed MAS occurrences: " + resolver.resolvePart2(personalSecretPuzzleInput));
	}
	
	public Integer resolvePart1(List<String> puzzleInputLines) {
		parse(puzzleInputLines);
		int occurrences = 0;
		for (Search search : Search.values()) {
			occurrences += findXmas(search);
		}
		return occurrences;
	}
	
	public Integer resolvePart2(List<String> puzzleInputLines) {
		parse(puzzleInputLines);
		int occurrences = 0;
		for (int row = 1; row <= height - 2; row++) {
			for (int column = 1; column <= width - 2; column++) {
				if (isCenterOfCrossedMas(wordSearch, row, column)) {
					occurrences++;
				}
			}
		}
		return occurrences;
	}
	
	private void parse(List<String> puzzleInputLines) {
		height = puzzleInputLines.size();
		width = puzzleInputLines.getFirst().length();
		wordSearch = new char[height][width];
		for (int row = 0; row < height; row++) {
			assert puzzleInputLines.get(row).length() == width;
			wordSearch[row] = puzzleInputLines.get(row).toCharArray();
		}
	}
	
	private int findXmas(Search search) {
		int occurrences = 0;
		for (int row = search.topMargin; row <= height - search.bottomMargin; row++) {
			for (int column = search.leftMargin; column <= width - search.rightMargin; column++) {
				StringBuilder word = new StringBuilder();
				int nextRow = row;
				int nextColumn = column;
				for (int i = 0; i < "XMAS".length(); i++) {
					word.append(wordSearch[nextRow][nextColumn]);
					nextRow += search.rowModifier;
					nextColumn += search.columnModifier;
				}
				if ("XMAS".contentEquals(word)) {
					occurrences++;
				}
			}
		}
		return occurrences;
	}
	
	private boolean isCenterOfCrossedMas(char[][] array, int row, int column) {
		boolean potentialCenterOfCrossedMas = array[row][column] == 'A';
		if (potentialCenterOfCrossedMas) {
			boolean upLeftCounterparts = hasMAndSCounterparts(array, row, column, Search.DOWN_RIGHT, Search.UP_LEFT);
			boolean upRightCounterparts = hasMAndSCounterparts(array, row, column, Search.DOWN_LEFT, Search.UP_RIGHT);
			boolean downLeftCounterparts = hasMAndSCounterparts(array, row, column, Search.UP_RIGHT, Search.DOWN_LEFT);
			boolean downRightCounterparts = hasMAndSCounterparts(array, row, column, Search.UP_LEFT, Search.DOWN_RIGHT);
			boolean diagonalForward = upRightCounterparts || downLeftCounterparts;
			boolean diagonalBackward = upLeftCounterparts || downRightCounterparts;
			return diagonalForward && diagonalBackward;
		}
		return false;
	}
	
	private boolean hasMAndSCounterparts(char[][] array, int row, int column, Search direction1, Search direction2) {
		return array[row + direction1.rowModifier][column + direction1.columnModifier] == 'M'
				&& array[row + direction2.rowModifier][column + direction2.columnModifier] == 'S';
	}
}
