package aoc2024.day08;

import aoc2024.DayResolver;
import aoc2024.util.PuzzleInputLoader;

import java.util.List;

public class Day08Resolver implements DayResolver<Integer> {
	
	private char[][] antennasMap;
	private int mapHeight;
	private int mapWidth;
	private boolean[][] antinodesMap;
	
	public static void main(String[] args) {
		Day08Resolver resolver = new Day08Resolver();
		List<String> personalSecretPuzzleInput = PuzzleInputLoader.loadPersonalSecretPuzzleInput(8);
		
		System.out.println("***  Part 1: Unique locations with antinodes  ***");
		System.out.println("Count of unique locations with antinodes: " + resolver.resolvePart1(personalSecretPuzzleInput));
		
		System.out.println("***  Part 2: Unique locations with antinodes including resonant harmonics  ***");
		System.out.println("Count of unique locations with antinodes including resonant harmonics: " + resolver.resolvePart2(personalSecretPuzzleInput));
	}
	
	public Integer resolvePart1(List<String> puzzleInputLines) {
		parseMap(puzzleInputLines);
		printMap();
		antinodesMap = new boolean[mapHeight][mapWidth];
		for (int antenna1Row = 0; antenna1Row < mapHeight; antenna1Row++) {
			for (int antenna1Column = 0; antenna1Column < mapWidth; antenna1Column++) {
				for (int antenna2Row = 0; antenna2Row < mapHeight; antenna2Row++) {
					for (int antenna2Column = 0; antenna2Column < mapWidth; antenna2Column++) {
						if (antennasMap[antenna1Row][antenna1Column] == '.' || (antenna1Row == antenna2Row && antenna1Column == antenna2Column)) {
							continue;
						}
						if (antennasMap[antenna1Row][antenna1Column] == antennasMap[antenna2Row][antenna2Column]) {
							int antinodeRow = antenna1Row + (antenna1Row - antenna2Row);
							int antinodeColumn = antenna1Column + (antenna1Column - antenna2Column);
							if (isPositionWithinMapArea(antinodeRow, antinodeColumn)) {
								antinodesMap[antinodeRow][antinodeColumn] = true;
							}
						}
					}
				}
			}
		}
		printMapWithAntinodes();
		return countUniqueAntinodesLocationsCount();
	}
	
	public Integer resolvePart2(List<String> puzzleInputLines) {
		parseMap(puzzleInputLines);
		printMap();
		antinodesMap = new boolean[mapHeight][mapWidth];
		for (int antenna1Row = 0; antenna1Row < mapHeight; antenna1Row++) {
			for (int antenna1Column = 0; antenna1Column < mapWidth; antenna1Column++) {
				for (int antenna2Row = 0; antenna2Row < mapHeight; antenna2Row++) {
					for (int antenna2Column = 0; antenna2Column < mapWidth; antenna2Column++) {
						if (antennasMap[antenna1Row][antenna1Column] == '.' || (antenna1Row == antenna2Row && antenna1Column == antenna2Column)) {
							continue;
						}
						if (antennasMap[antenna1Row][antenna1Column] == antennasMap[antenna2Row][antenna2Column]) {
							int resonantHarmonicRows = antenna1Row - antenna2Row;
							int resonantHarmonicColumns = antenna1Column - antenna2Column;
							int antinodeRow = antenna1Row;
							int antinodeColumn = antenna1Column;
							while (isPositionWithinMapArea(antinodeRow, antinodeColumn)) {
								antinodesMap[antinodeRow][antinodeColumn] = true;
								antinodeRow += resonantHarmonicRows;
								antinodeColumn += resonantHarmonicColumns;
							}
						}
					}
				}
			}
		}
		printMapWithAntinodes();
		printMapWithoutAntennas();
		return countUniqueAntinodesLocationsCount();
	}
	
	private void parseMap(List<String> puzzleInputLines) {
		mapHeight = puzzleInputLines.size();
		mapWidth = puzzleInputLines.getFirst().length();
		antennasMap = new char[mapHeight][mapWidth];
		for (int row = 0; row < mapWidth; row++) {
			assert puzzleInputLines.get(row).length() == mapWidth;
			antennasMap[row] = puzzleInputLines.get(row).toCharArray();
		}
	}
	
	private void printMap() {
		System.out.println("Original map");
		for (char[] row : antennasMap) {
			System.out.println(row);
		}
		System.out.println();
	}
	
	private void printMapWithAntinodes() {
		System.out.println("Map with antinodes");
		for (int row = 0; row < mapHeight; row++) {
			StringBuilder rowBuilder = new StringBuilder();
			for (int column = 0; column < mapWidth; column++) {
				char antennasChar = antennasMap[row][column];
				boolean shouldPrintAntinode = antinodesMap[row][column] && (antennasChar == '.');
				rowBuilder.append(shouldPrintAntinode ? '#' : antennasChar);
			}
			System.out.println(rowBuilder);
		}
		System.out.println();
	}
	
	private void printMapWithoutAntennas() {
		System.out.println("Map with antinodes without antennas");
		for (int row = 0; row < mapHeight; row++) {
			StringBuilder rowBuilder = new StringBuilder();
			for (int column = 0; column < mapWidth; column++) {
				rowBuilder.append(antinodesMap[row][column] ? '#' : '.');
			}
			System.out.println(rowBuilder);
		}
		System.out.println();
	}
	
	private boolean isPositionWithinMapArea(int row, int column) {
		return row >= 0 && row < mapHeight && column >= 0 && column < mapWidth;
	}
	
	private Integer countUniqueAntinodesLocationsCount() {
		int uniqueAntinodesLocationsCount = 0;
		for (boolean[] antinodesRow : antinodesMap) {
			for (boolean locationContainsAntinodes : antinodesRow) {
				if (locationContainsAntinodes) {
					uniqueAntinodesLocationsCount++;
				}
			}
		}
		return uniqueAntinodesLocationsCount;
	}
}
