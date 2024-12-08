package aoc2024.day06;

import aoc2024.DayResolver;
import aoc2024.util.PuzzleInputLoader;

import java.util.*;

public class Day06Resolver implements DayResolver<Integer> {
	
	private char[][] map;
	private int mapHeight;
	private int mapWidth;
	
	public static void main(String[] args) {
		Day06Resolver resolver = new Day06Resolver();
		List<String> personalSecretPuzzleInput = PuzzleInputLoader.loadPersonalSecretPuzzleInput(6);
		
		System.out.println("***  Part 1: Distinct guard positions  ***");
		System.out.println("Distinct positions count: " + resolver.resolvePart1(personalSecretPuzzleInput));
		
		System.out.println("***  Part 2: Make guard stuck in a loop  ***");
		System.out.println("Enprisoning obstructions count: " + resolver.resolvePart2(personalSecretPuzzleInput));
	}
	
	public Integer resolvePart1(List<String> puzzleInputLines) {
		parse(puzzleInputLines);
		printMap();
		Guard guard = findGuard();
		while (isPositionWithinMap(guard.getRow(), guard.getColumn())) {
			System.out.println(guard);
			while (reachedObstruction(guard, map)) {
				guard.turn();
			}
			map[guard.getRow()][guard.getColumn()] = 'X';
			guard.walk();
			if (isPositionWithinMap(guard.getRow(), guard.getColumn())) {
				map[guard.getRow()][guard.getColumn()] = guard.getDirection().marker;
			}
		}
		
		System.out.println("Guard reached the edge of universe! " + guard);
		printMap();
		return countDistinctPositions();
	}
	
	public Integer resolvePart2(List<String> puzzleInputLines) {
		parse(puzzleInputLines);
		Guard guard = findGuard();
		int enprisoningObstructionsCount = 0;
		for (int row = 0; row < mapHeight; row++) {
			for (int column = 0; column < mapWidth; column++) {
				if (map[row][column] != '#'
						&& !(row == guard.getRow() && column == guard.getColumn())
						&& obstructionAtThisPlaceWouldMakeGuardStuckInALoop(row, column, guard)) {
					map[row][column] = 'O';
					enprisoningObstructionsCount++;
				}
			}
		}
		
		printMap();
		return enprisoningObstructionsCount;
	}
	
	private void parse(List<String> puzzleInputLines) {
		mapHeight = puzzleInputLines.size();
		mapWidth = puzzleInputLines.getFirst().length();
		map = new char[mapHeight][mapWidth];
		for (int row = 0; row < mapWidth; row++) {
			assert puzzleInputLines.get(row).length() == mapWidth;
			map[row] = puzzleInputLines.get(row).toCharArray();
		}
	}
	
	private void printMap() {
		for (char[] row : map) {
			System.out.println(row);
		}
		System.out.println();
	}
	
	private Guard findGuard() {
		for (int row = 0; row < mapHeight; row++) {
			for (int column = 0; column < mapWidth; column++) {
				for (Direction direction : Direction.values()) {
					if (map[row][column] == direction.marker) {
						return new Guard(row, column, direction);
					}
				}
			}
		}
		throw new IllegalArgumentException("Guard not found on given map");
	}
	
	private boolean isPositionWithinMap(int row, int column) {
		return row >= 0 && row < mapHeight && column >= 0 && column < mapWidth;
	}
	
	private boolean reachedObstruction(Guard guard, char[][] map) {
		int nextRow = guard.getRow() + guard.getDirection().rowMove;
		int nextColumn = guard.getColumn() + guard.getDirection().columnMove;
		return isPositionWithinMap(nextRow, nextColumn) && map[nextRow][nextColumn] == '#';
	}
	
	private int countDistinctPositions() {
		int distinctPositionsCount = 0;
		for (int row = 0; row < mapHeight; row++) {
			for (int column = 0; column < mapWidth; column++) {
				if (map[row][column] == 'X') {
					distinctPositionsCount++;
				}
			}
		}
		return distinctPositionsCount;
	}
	
	private boolean obstructionAtThisPlaceWouldMakeGuardStuckInALoop(int row, int column, Guard guard) {
		Guard shadow = new Guard(guard.getRow(), guard.getColumn(), guard.getDirection());
		char[][] playground = mapCopy();
		playground[row][column] = '#';
		List<Guard> shadows = new ArrayList<>();
		
		while (isPositionWithinMap(shadow.getRow(), shadow.getColumn())) {
			while (reachedObstruction(shadow, playground)) {
				shadow.turn();
			}
			shadow.walk();
			if (shadows.contains(shadow)) {
				System.out.format("Obstruction at row %d and column %d would make the guard stuck in a loop%n", row, column);
				return true;
			}
			shadows.add(new Guard(shadow.getRow(), shadow.getColumn(), shadow.getDirection()));
			playground[guard.getRow()][guard.getColumn()] = 'X';
		}
		System.out.format("Obstruction at row %d and column %d would not make the guard stuck in a loop%n", row, column);
		return false;
	}
	
	private char[][] mapCopy() {
		char[][] mapCopy = new char[mapHeight][mapWidth];
		for (int row = 0; row < mapHeight; row++) {
			System.arraycopy(map[row], 0, mapCopy[row], 0, mapWidth);
		}
		return mapCopy;
	}
}
