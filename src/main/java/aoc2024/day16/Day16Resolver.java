package aoc2024.day16;

import aoc2024.DayResolver;
import aoc2024.util.PuzzleInputLoader;

import java.util.*;
import java.util.stream.Stream;

public class Day16Resolver implements DayResolver<Integer> {
	
	public static void main(String[] args) {
		DayResolver<Integer> resolver = new Day16Resolver();
		List<String> personalSecretPuzzleInput = PuzzleInputLoader.loadPersonalSecretPuzzleInput(16);
		
		System.out.println("***  Part 1: Searching maze for a route with lowest weight  ***");
		System.out.println("The lowest score a Reindeer could possibly get: " + resolver.resolvePart1(personalSecretPuzzleInput));
		
		System.out.println("***  Part 2: Do the same both ways  ***");
		System.out.println("Tiles that are part of at least one of the best paths through the maze: " + resolver.resolvePart2(personalSecretPuzzleInput));
	}
	
	public Integer resolvePart1(List<String> puzzleInputLines) {
		char[][] map = parseMap(puzzleInputLines);
		Maze maze = Maze.of(map);
		Route route = findRouteWithLowestScore(maze);
		
		printMapWithRoutes(map, route);
		return route.getScore();
	}
	
	public Integer resolvePart2(List<String> puzzleInputLines) {
		char[][] map = parseMap(puzzleInputLines);
		Maze maze = Maze.of(map);
		Route there = findRouteWithLowestScore(maze);
		
		map[maze.start().row()][maze.start().column()] = 'E';
		map[maze.destination().row()][maze.destination().column()] = 'S';
		maze = Maze.of(map);
		Route back = findRouteWithLowestScore(maze);
		
		printMapWithRoutes(map, there, back);
		return (int) Stream.of(there.getSteps(), back.getSteps())
				.flatMap(List::stream)
				.map(Step::tile)
				.distinct()
				.count();
	}
	
	private char[][] parseMap(List<String> puzzleInputLines) {
		int height = puzzleInputLines.size();
		int width = puzzleInputLines.getFirst().length();
		char[][] map = new char[height][width];
		for (int rowIndex = 0; rowIndex < height; rowIndex++) {
			char[] rowChars = puzzleInputLines.get(rowIndex).toCharArray();
			assert rowChars.length == width;
			map[rowIndex] = rowChars;
		}
		return map;
	}
	
	private Route findRouteWithLowestScore(Maze maze) {
		//noinspection OptionalGetWithoutIsPresent
		Step start = tryToMakeAStep(Heading.EAST, maze.start(), -Score.STEP).get();
		List<Route> routes = new ArrayList<>();
		routes.add(new Route(start));
		Route routeWithLowestScore;
		do {
			discoverNextStep(routes, maze);
			routeWithLowestScore = Route.findWithLowestScore(routes);
		} while (!routes.isEmpty() && !routeWithLowestScore.endsOn(maze.destination()));
		return routeWithLowestScore;
	}
	
	private Optional<Step> tryToMakeAStep(Heading heading, Tile tile, int currentScore) {
		boolean sensible = false;
		for (Heading headingOption : Heading.values()) {
			int optionScore = currentScore + Score.ofTurn(heading, headingOption) + Score.STEP;
			int previousScore = tile.scores().getOrDefault(headingOption, Integer.MAX_VALUE);
			if (optionScore < previousScore) {
				sensible = true;
				tile.scores().put(headingOption, optionScore);
			}
		}
		return sensible
				? Optional.of(new Step(heading, tile, tile.scores().get(heading)))
				: Optional.empty();
	}
	
	private void discoverNextStep(List<Route> routes, Maze maze) {
		Route routeWithLowestScore = Route.findWithLowestScore(routes);
		List<Step> sensibleSteps = findPossibleOptions(routeWithLowestScore, maze).entrySet().stream()
				.map(option -> tryToMakeAStep(option.getKey(), option.getValue(),
						routeWithLowestScore.getScore() + Score.ofTurn(routeWithLowestScore.getHeading(), option.getKey())))
				.filter(Optional::isPresent)
				.map(Optional::get)
				.toList();
		if (sensibleSteps.isEmpty()) {
			routes.remove(routeWithLowestScore);
			return;
		}
		for (int stepIndex = 1; stepIndex < sensibleSteps.size(); stepIndex++) {
			Route newRoute = routeWithLowestScore.copy();
			newRoute.addStep(sensibleSteps.get(stepIndex));
			routes.add(newRoute);
		}
		routeWithLowestScore.addStep(sensibleSteps.getFirst());
	}
	
	private Map<Heading, Tile> findPossibleOptions(Route route, Maze maze) {
		Tile lastTile = route.getSteps().getLast().tile();
		Map<Heading, Tile> possibleOptions = new HashMap<>();
		for (Heading headingOption : Heading.values()) {
			int nextRow = lastTile.row() + headingOption.rowMove;
			int nextColumn = lastTile.column() + headingOption.columnMove;
			Tile tile = maze.map()[nextRow][nextColumn];
			if (tile.viable() && !route.visited(tile)) {
				possibleOptions.put(headingOption, tile);
			}
		}
		return possibleOptions;
	}
	
	private void printMapWithRoutes(char[][] map, Route... routes) {
		for (Route route : routes) {
			for (int stepIndex = 1; stepIndex < route.getSteps().size() - 1; stepIndex++) {
				Step step = route.getSteps().get(stepIndex);
				Tile tile = step.tile();
				map[tile.row()][tile.column()] = step.heading().symbol;
			}
		}
		System.out.println("Way chosen by reindeer:");
		for (int rowIndex = 0; rowIndex < map.length; rowIndex++) {
			for (int columnIndex = 0; columnIndex < map[rowIndex].length; columnIndex++) {
				System.out.print(" " + map[rowIndex][columnIndex]);
			}
			System.out.println();
		}
		System.out.println();
	}
}
