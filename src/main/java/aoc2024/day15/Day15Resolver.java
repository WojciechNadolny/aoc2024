package aoc2024.day15;

import aoc2024.DayResolver;
import aoc2024.util.PuzzleInputLoader;

import java.util.*;

public class Day15Resolver implements DayResolver<Long> {
	
	public static void main(String[] args) {
		Day15Resolver resolver = new Day15Resolver();
		List<String> personalSecretPuzzleInput = PuzzleInputLoader.loadPersonalSecretPuzzleInput(15);
		
		System.out.println("***  Part 1: Sokoban game  ***");
		System.out.println("Final sum of all boxes' GPS coordinates: " + resolver.resolvePart1(personalSecretPuzzleInput));
		
		System.out.println("***  Part 2: Sokoban game with double width except the robot  ***");
		System.out.println("Final sum of all boxes' GPS coordinates: " + resolver.resolvePart2(personalSecretPuzzleInput));
	}
	
	public Long resolvePart1(List<String> puzzleInputLines) {
		Warehouse warehouse = parse(puzzleInputLines, false);
		play(warehouse, false);
		return computeBoxesCoordinatesSum(warehouse, false);
	}
	
	public Long resolvePart2(List<String> puzzleInputLines) {
		Warehouse warehouse = parse(puzzleInputLines, true);
		play(warehouse, true);
		return computeBoxesCoordinatesSum(warehouse, true);
	}
	
	private Warehouse parse(List<String> puzzleInputLines, boolean doubleWidth) {
		WarehouseBuilder warehouseBuilder = new WarehouseBuilder();
		RobotBuilder robotBuilder = new RobotBuilder();
		boolean warehouseUnderConstruction = true;
		for (int row = 0; row < puzzleInputLines.size(); row++) {
			String line = puzzleInputLines.get(row);
			if (warehouseUnderConstruction) {
				if (!line.isEmpty()) {
					warehouseBuilder.addRow(line);
					if (line.contains(String.valueOf(Figure.ROBOT))) {
						robotBuilder.setRow(row);
						robotBuilder.setColumn(line.indexOf(Figure.ROBOT) * (doubleWidth ? 2 : 1));
					}
				} else {
					warehouseUnderConstruction = false;
				}
			} else {
				robotBuilder.addLineOfCode(line);
			}
		}
		Robot robot = robotBuilder.build();
		warehouseBuilder.setRobot(robot);
		
		return doubleWidth
				? warehouseBuilder.buildDoubleWidth()
				: warehouseBuilder.build();
	}
	
	private void play(Warehouse warehouse, boolean doubleWidth) {
		Robot robot = warehouse.robot();
		char[][] playground = new char[warehouse.height()][warehouse.width()];
		int movesCount = 1;
		while (robot.direction.hasNext()) {
			Direction direction = robot.direction.next();
			if (doubleWidth) {
				copy(warehouse.map(), playground); // Use spare map as a transaction that can be rolled back
			}
			boolean moved = moveIfPossible(robot.row, robot.column, direction, doubleWidth ? playground : warehouse.map());
			if (moved) {
				robot.row += direction.rowMove;
				robot.column += direction.columnMove;
				if (doubleWidth) { // Commit push
					copy(playground, warehouse.map());
				}
			}
			System.out.println(movesCount++ + ": Robot " + (moved ? "moved " : "could not move ") + direction);
			for (char[] mapRow : warehouse.map()) {
				System.out.println(new String(mapRow));
			}
			System.out.println();
		}
	}
	
	private boolean moveIfPossible(int row, int column, Direction direction, char[][] map) {
		char figure = map[row][column];
		return switch (figure) {
			case Figure.WALL -> false; // Cannot move
			case Figure.AIR -> true; // Will flow around backwards
			case Figure.ROBOT, Figure.BOX -> moveRecognizedFigureIfPossible(figure, row, column, direction, map); // Ability depends on occupancy behind
			case Figure.WIDE_BOX_LEFT, Figure.WIDE_BOX_RIGHT -> switch (direction) {
				case Direction.LEFT, Direction.RIGHT -> moveWideBoxHorizontallyIfPossible(figure, row, column, direction, map); // Ability might depend on occupancy behind the other half of the box
				case Direction.UP, Direction.DOWN -> moveWideBoxVerticallyIfPossible(figure, row, column, direction, map); // Ability depends on occupancy above or below both halves of the box
			};
			default -> throw new IllegalStateException("Unexpected figure: " + figure);
		};
	}
	
	private boolean moveRecognizedFigureIfPossible(char figure, int row, int column, Direction direction, char[][] map) {
		int nextRow = row + direction.rowMove;
		int nextColumn = column + direction.columnMove;
		boolean moved = moveIfPossible(nextRow, nextColumn, direction, map);
		if (moved) {
			moveByFiguresExchangeWithTheAir(figure, row, column, nextRow, nextColumn, map);
		}
		return moved;
	}
	
	private void moveByFiguresExchangeWithTheAir(char figure, int row, int column, int nextRow, int nextColumn, char[][] map) {
		map[row][column] = map[nextRow][nextColumn];
		map[nextRow][nextColumn] = figure;
	}
	
	private boolean moveWideBoxHorizontallyIfPossible(char thisHalfFigure, int row, int column, Direction direction, char[][] map) {
		boolean heading;
		char otherHalfFigure;
		int otherHalfColumn;
		switch (thisHalfFigure) {
			case Figure.WIDE_BOX_LEFT -> {
				heading = direction == Direction.LEFT;
				otherHalfFigure = Figure.WIDE_BOX_RIGHT;
				otherHalfColumn = column + 1;
			}
			case Figure.WIDE_BOX_RIGHT -> {
				heading = direction == Direction.RIGHT;
				otherHalfFigure = Figure.WIDE_BOX_LEFT;
				otherHalfColumn = column - 1;
			}
			default -> throw new IllegalArgumentException("Not a wide box half-figure: " + thisHalfFigure);
		}
		assert map[row][otherHalfColumn] == otherHalfFigure;
		boolean moved = moveRecognizedFigureIfPossible(heading ? thisHalfFigure : otherHalfFigure,
				row, heading ? column : otherHalfColumn, direction, map);
		if (moved) {
			moveByFiguresExchangeWithTheAir(thisHalfFigure, row, column, row, otherHalfColumn, map);
		}
		return moved;
	}
	
	private boolean moveWideBoxVerticallyIfPossible(char thisHalfFigure, int row, int column, Direction direction, char[][] map) {
		int leftHalfColumn, rightHalfColumn;
		switch (thisHalfFigure) {
			case Figure.WIDE_BOX_LEFT -> {
				assert map[row][column + 1] == Figure.WIDE_BOX_RIGHT;
				leftHalfColumn = column;
				rightHalfColumn = column + 1;
			}
			case Figure.WIDE_BOX_RIGHT -> {
				assert map[row][column - 1] == Figure.WIDE_BOX_LEFT;
				leftHalfColumn = column - 1;
				rightHalfColumn = column;
			}
			default -> throw new IllegalArgumentException("Not a wide box half-figure: " + thisHalfFigure);
		}
		boolean moved = moveRecognizedFigureIfPossible(Figure.WIDE_BOX_LEFT, row, leftHalfColumn, direction, map);
		if (moved) {
			moved = moveRecognizedFigureIfPossible(Figure.WIDE_BOX_RIGHT, row, rightHalfColumn, direction, map);
		}
		return moved;
	}
	
	private Long computeBoxesCoordinatesSum(Warehouse warehouse, boolean doubleWidth) {
		long gpsCoordinatesSum = 0;
		for (int row = 1; row < warehouse.height() - 1; row++) {
			for (int column = 1; column < warehouse.width() - 1; column++) {
				if (warehouse.map()[row][column] == (doubleWidth ? Figure.WIDE_BOX_LEFT : Figure.BOX)) {
					gpsCoordinatesSum += 100L * row + column;
				}
			}
		}
		return gpsCoordinatesSum;
	}
	
	private void copy(char[][] sourceMap, char[][] targetMap) {
		for (int row = 0; row < sourceMap.length; row++) {
			System.arraycopy(sourceMap[row], 0, targetMap[row], 0, sourceMap[row].length);
		}
	}
}
