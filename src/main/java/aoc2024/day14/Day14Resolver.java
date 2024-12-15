package aoc2024.day14;

import aoc2024.DayResolver;
import aoc2024.util.PuzzleInputLoader;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Day14Resolver implements DayResolver<Integer> {
	
	private final int spaceWidth;
	private final int spaceHeight;
	
	Day14Resolver(int spaceWidth, int spaceHeight) {
		this.spaceWidth = spaceWidth;
		this.spaceHeight = spaceHeight;
	}
	
	public static void main(String[] args) {
		Day14Resolver resolver = new Day14Resolver(101, 103);
		List<String> personalSecretPuzzleInput = PuzzleInputLoader.loadPersonalSecretPuzzleInput(14);
		
		System.out.println("***  Part 1: Multiplication of robots per four quadrants  ***");
		System.out.println("Space safety factor: " + resolver.resolvePart1(personalSecretPuzzleInput));
		
		System.out.println("***  Part 2: Christmas tree image detection  ***");
		System.out.println("The fewest number of seconds until Easter egg display: " + resolver.resolvePart2(personalSecretPuzzleInput));
	}
	
	public Integer resolvePart1(List<String> puzzleInputLines) {
		List<Robot> robots = parseRobots(puzzleInputLines);
		System.out.println("Robots initial state:");
		robots.forEach(System.out::println);
		System.out.println();
		simulateMotion(robots, 100);
		System.out.println("Robots state after 100 seconds:");
		robots.forEach(System.out::println);
		System.out.println();
		return computeSpaceSafetyFactor(robots);
	}
	
	public Integer resolvePart2(List<String> puzzleInputLines) {
		List<Robot> robots = parseRobots(puzzleInputLines);
		int mostCrowdedSecond = findMostCrowdedSecond(robots);
		simulateMotion(robots, mostCrowdedSecond);
		char[][] spaceImage = getSpaceImage(robots);
		System.out.println("Robots formed such a crowded image at second " + mostCrowdedSecond + ":");
		for (char[] imageRow : spaceImage) {
			System.out.println(imageRow);
		}
		System.out.println();
		return mostCrowdedSecond;
	}
	
	private List<Robot> parseRobots(List<String> puzzleInputLines) {
		AtomicInteger robotIdGenerator = new AtomicInteger(0);
		return puzzleInputLines.stream()
				.map(line -> line.replaceAll("(p=|v=)", ""))
				.map(line -> line.replace(" ", ","))
				.map(line -> line.split(","))
				.map(params -> new Robot(robotIdGenerator.incrementAndGet(),
						Integer.parseInt(params[0]), Integer.parseInt(params[1]),
						Integer.parseInt(params[2]), Integer.parseInt(params[3])))
				.toList();
	}
	
	private void simulateMotion(List<Robot> robots, int seconds) {
		robots.forEach(robot -> {
			robot.positionX = getWrappedTileIndex(robot.positionX + robot.velocityX * seconds, spaceWidth);
			robot.positionY = getWrappedTileIndex(robot.positionY + robot.velocityY * seconds, spaceHeight);
		});
	}
	
	private int getWrappedTileIndex(int tileIndex, int dimension) {
		while (tileIndex >= dimension) {
			tileIndex -= dimension;
		}
		while (tileIndex < 0) {
			tileIndex += dimension;
		}
		return tileIndex;
	}
	
	private int computeSpaceSafetyFactor(List<Robot> robots) {
		Map<String, List<Robot>> robotsByQuadrant = getRobotsByQuadrant(robots);
		System.out.println("Robots count by quadrant: ");
		robotsByQuadrant.forEach((quadrantName, quadrantRobots) ->
				System.out.format("%s (%d): %s%n", quadrantName, quadrantRobots.size(), quadrantRobots));
		
		return robotsByQuadrant.entrySet().stream()
				.filter(quadrantRobots -> !quadrantRobots.getKey().equals("elsewhere"))
				.map(Map.Entry::getValue)
				.mapToInt(List::size)
				.reduce((int1, int2) -> int1 * int2)
				.orElseThrow();
	}
	
	private Map<String, List<Robot>> getRobotsByQuadrant(List<Robot> robots) {
		List<SubSpace> quadrants = getQuadrants();
		Function<Robot, String> robotQuadrant = robot -> quadrants.stream()
				.filter(subSpace -> subSpace.containsTile(robot.positionX, robot.positionY))
				.findAny()
				.map(SubSpace::name)
				.orElse("elsewhere");
		return robots.stream()
				.collect(Collectors.groupingBy(robotQuadrant));
	}
	
	private List<SubSpace> getQuadrants() {
		int middleX = spaceWidth / 2;
		int middleY = spaceHeight / 2;
		Predicate<Integer> isToTheLeftFromMiddleX = x -> x >= 0 && x < middleX;
		Predicate<Integer> isAboveMiddleY = y -> y >= 0 && y < middleY;
		Predicate<Integer> isToTheRightFromMiddleX = x -> x > middleX && x < spaceWidth;
		Predicate<Integer> isBelowMiddleY = y -> y > middleY && y < spaceHeight;
		return List.of(
				new SubSpace("top-left", isToTheLeftFromMiddleX, isAboveMiddleY),
				new SubSpace("top-right", isToTheRightFromMiddleX, isAboveMiddleY),
				new SubSpace("bottom-left", isToTheLeftFromMiddleX, isBelowMiddleY),
				new SubSpace("bottom-right", isToTheRightFromMiddleX, isBelowMiddleY));
	}
	
	private int getRepositioningSeconds(List<Robot> robots) {
		int repositioningSeconds = 0;
		for (Robot robot : robots) {
			int initialPositionX = robot.positionX;
			int initialPositionY = robot.positionY;
			int second = 0;
			do {
				robot.positionX = getWrappedTileIndex(robot.positionX + robot.velocityX, spaceWidth);
				robot.positionY = getWrappedTileIndex(robot.positionY + robot.velocityY, spaceHeight);
				second++;
			} while (robot.positionX != initialPositionX || robot.positionY != initialPositionY);
			if (second > repositioningSeconds) {
				repositioningSeconds = second;
			}
		}
		System.out.println("Robots reposition every " + repositioningSeconds + " seconds.");
		return repositioningSeconds;
	}
	
	private char[][] getSpaceImage(List<Robot> robots) {
		char[][] image = new char[spaceHeight][spaceWidth];
		for (int row = 0; row < spaceHeight; row++) {
			for (int column = 0; column < spaceWidth; column++) {
				image[row][column] = ' ';
			}
		}
		robots.forEach(robot -> image[robot.positionY][robot.positionX] = 'R');
		return image;
	}
	
	private int findMostCrowdedSecond(List<Robot> robots) {
		int highestCrowdIndex = 0;
		int highestCrowdIndexSecond = -1;
		int repositioningSeconds = getRepositioningSeconds(robots);
		for (int second = 0; second < repositioningSeconds; second++) {
			int crowdIndex = 0;
			char[][] spaceImage = getSpaceImage(robots);
			for (int y = 1; y < spaceHeight - 1; y++) {
				for (int x = 1; x < spaceWidth - 1; x++) {
					if (spaceImage[y][x] == 'R') {
						crowdIndex += spaceImage[y][x - 1] == 'R' ? 1 : 0;
						crowdIndex += spaceImage[y][x + 1] == 'R' ? 1 : 0;
						crowdIndex += spaceImage[y - 1][x] == 'R' ? 1 : 0;
						crowdIndex += spaceImage[y + 1][x] == 'R' ? 1 : 0;
					}
				}
			}
			if (crowdIndex > highestCrowdIndex) {
				highestCrowdIndex = crowdIndex;
				highestCrowdIndexSecond = second;
			}
			simulateMotion(robots, 1);
		}
		return highestCrowdIndexSecond;
	}
	
//	/*
//	 * I was hoping that a christmas tree image would be perfectly symmetric.
//	 * That hope died as soon as I ran this method. None of images appeared symmetric.
//	 */
//	private void f(List<Robot> robots) {
//		int repositioningSeconds = getRepositioningSeconds(robots);
//		for (int second = 0; second < repositioningSeconds; second++) {
//			char[][] spaceImage = getSpaceImage(robots);
//			if (isSymmetric(spaceImage)) {
//				System.out.println("Robots formed such a symmetric image at second " + second);
//				for (char[] imageRow : spaceImage) {
//					System.out.println(imageRow);
//				}
//				System.out.println();
//			}
//			simulateMotion(robots, 1);
//		}
//	}
//
//	private boolean isSymmetric(char[][] spaceImage) {
//		for (char[] imageRow : spaceImage) {
//			for (int x = 0; x < spaceWidth / 2; x++) {
//				if (imageRow[x] != imageRow[spaceWidth - 1 - x]) {
//					return false;
//				}
//			}
//		}
//		return true;
//	}
//
//	/*
//	 * Finding lowest robots dispersal wasn't a right way to go as it is clearly said that MOST of robots form a tree.
//	 * Not all of them. So the rest might form a snow which doesn't differ from white noise present in each second.
//	 */
//	private int findSecondWithLowestDispersal(List<Robot> robots) {
//		double lowestDispersal = Integer.MAX_VALUE;
//		int lowestDispersalSecond = -1;
//		int repositioningSeconds = getRepositioningSeconds(robots);
//		for (int second = 0; second < repositioningSeconds; second++) {
//			int minX = spaceWidth, maxX = 0, minY = spaceHeight, maxY = 0;
//			for (Robot robot : robots) {
//				minX = Math.min(minX, robot.positionX);
//				maxX = Math.max(maxX, robot.positionX);
//				minY = Math.min(minY, robot.positionY);
//				maxY = Math.max(maxY, robot.positionY);
//			}
//			int dispersal = maxX - minX + maxY - minY;
//			if (dispersal < lowestDispersal) {
//				lowestDispersal = dispersal;
//				lowestDispersalSecond = second;
//			}
//			simulateMotion(robots, 1);
//		}
//		return lowestDispersalSecond;
//	}
}
