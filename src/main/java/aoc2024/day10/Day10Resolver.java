package aoc2024.day10;

import aoc2024.DayResolver;
import aoc2024.util.PuzzleInputLoader;

import java.util.*;

public class Day10Resolver implements DayResolver<Integer> {
	
	private char[][] map;
	private int mapHeight;
	private int mapWidth;
	
	public static void main(String[] args) {
		Day10Resolver resolver = new Day10Resolver();
		List<String> personalSecretPuzzleInput = PuzzleInputLoader.loadPersonalSecretPuzzleInput(10);
		
		System.out.println("***  Part 1: Trailheads scores sum  ***");
		System.out.println("Sum of the scores of all trailheads: " + resolver.resolvePart1(personalSecretPuzzleInput));
		
		System.out.println("***  Part 2: Trailheads rating sum  ***");
		System.out.println("Sum of rating of all trailheads: " + resolver.resolvePart2(personalSecretPuzzleInput));
	}
	
	public Integer resolvePart1(List<String> puzzleInputLines) {
		parseMap(puzzleInputLines);
		printMap();
		List<List<Position>> trails = findTrails();
		return calculateTrailheadScoresSum(trails);
	}
	
	public Integer resolvePart2(List<String> puzzleInputLines) {
		parseMap(puzzleInputLines);
		printMap();
		List<List<Position>> trails = findTrails();
		return calculateTrailheadRatingSum(trails);
	}
	
	private void parseMap(List<String> puzzleInputLines) {
		mapHeight = puzzleInputLines.size();
		mapWidth = puzzleInputLines.getFirst().length();
		map = new char[mapHeight][mapWidth];
		for (int row = 0; row < mapWidth; row++) {
			assert puzzleInputLines.get(row).length() == mapWidth;
			map[row] = puzzleInputLines.get(row).toCharArray();
		}
	}
	
	private void printMap() {
		System.out.println("Topographic map");
		for (char[] row : map) {
			System.out.println(row);
		}
		System.out.println();
	}
	
	private List<List<Position>> findTrails() {
		List<List<Position>> trails = new ArrayList<>();
		for (int row = 0; row < mapHeight; row++) {
			for (int column = 0; column < mapWidth; column++) {
				if (map[row][column] == '0') {
					Position startingPosition = new Position(row, column, '0');
					List<Position> candidTrail = List.of(startingPosition);
					trails.addAll(findTrailContinuations(candidTrail));
				}
			}
		}
		return trails;
	}
	
	private List<List<Position>> findTrailContinuations(List<Position> trailSoFar) {
		Position currentPosition = trailSoFar.getLast();
		if (currentPosition.height() == '9') {
			return List.of(trailSoFar);
		}
		
		List<List<Position>> trails = new ArrayList<>();
		for (Direction direction : Direction.values()) {
			int nextRow = currentPosition.row() + direction.rowMove;
			int nextColumn = currentPosition.column() + direction.columnMove;
			char nextHeight = (char) (currentPosition.height() + 1);
			if (isPositionWithinMap(nextRow, nextColumn) && map[nextRow][nextColumn] == nextHeight) {
				Position nextStep = new Position(nextRow, nextColumn, nextHeight);
				List<Position> candidTrail = new ArrayList<>(trailSoFar);
				candidTrail.add(nextStep);
				trails.addAll(findTrailContinuations(candidTrail));
			}
		}
		return trails;
	}
	
	private boolean isPositionWithinMap(int row, int column) {
		return row >= 0 && row < mapHeight && column >= 0 && column < mapWidth;
	}
	
	private Map<Position, Collection<Position>> getReachablePositionsByTrailhead(List<List<Position>> trails, boolean unique) {
		Map<Position, Collection<Position>> reachablePositionsByTrailhead = new HashMap<>();
		for (List<Position> trail : trails) {
			Position startPosition = trail.getFirst();
			Position reachablePosition = trail.getLast();
			reachablePositionsByTrailhead.compute(startPosition, (position, reachablePositions) -> {
				reachablePositions = Optional.ofNullable(reachablePositions)
						.orElseGet(unique ? HashSet::new : ArrayList::new);
				reachablePositions.add(reachablePosition);
				return reachablePositions;
			});
		}
		return reachablePositionsByTrailhead;
	}
	
	private int calculateTrailheadScoresSum(List<List<Position>> trails) {
		Map<Position, Collection<Position>> uniqueReachablePositionsByTrailhead = getReachablePositionsByTrailhead(trails, true);
		return uniqueReachablePositionsByTrailhead.values().stream()
				.map(Collection::size)
				.mapToInt(Integer::intValue)
				.sum();
	}
	
	private int calculateTrailheadRatingSum(List<List<Position>> trails) {
		Map<Position, Collection<Position>> nonUniqueReachablePositionsByTrailhead = getReachablePositionsByTrailhead(trails, false);
		return nonUniqueReachablePositionsByTrailhead.values().stream()
				.map(Collection::size)
				.mapToInt(Integer::intValue)
				.sum();
	}
}
