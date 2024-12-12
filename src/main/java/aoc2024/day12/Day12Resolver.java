package aoc2024.day12;

import aoc2024.DayResolver;
import aoc2024.util.PuzzleInputLoader;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Day12Resolver implements DayResolver<Long> {
	
	public static void main(String[] args) {
		Day12Resolver resolver = new Day12Resolver();
		List<String> personalSecretPuzzleInput = PuzzleInputLoader.loadPersonalSecretPuzzleInput(12);
		
		System.out.println("***  Part 1: Garden regions fencing price  ***");
		System.out.println("Total price of fencing all regions: " + resolver.resolvePart1(personalSecretPuzzleInput));
		
		System.out.println("***  Part 2: Garden regions fencing price with discount for straight fence lines  ***");
		System.out.println("Total price of fencing all regions: " + resolver.resolvePart2(personalSecretPuzzleInput));
	}
	
	public Long resolvePart1(List<String> puzzleInputLines) {
		Garden garden = parseGardenMap(puzzleInputLines);
		putFencesWhereApplicable(garden);
		Map<Integer, Region> regions = groupIntoRegions(garden);
		return regions.values().stream()
				.mapToLong(region -> (long) region.getArea() * region.getPerimeter())
				.sum();
	}
	
	public Long resolvePart2(List<String> puzzleInputLines) {
		Garden garden = parseGardenMap(puzzleInputLines);
		putFencesWhereApplicable(garden);
		Map<Integer, Region> regions = groupIntoRegions(garden);
		return regions.values().stream()
				.mapToLong(region -> (long) region.getArea() * getSidesCount(region))
				.sum();
	}
	
	private Garden parseGardenMap(List<String> puzzleInputLines) {
		int mapRowsCount = puzzleInputLines.size();
		int mapColumnsCount = puzzleInputLines.getFirst().length();
		char[][] map = new char[mapRowsCount][mapColumnsCount];
		for (int row = 0; row < mapRowsCount; row++) {
			assert puzzleInputLines.get(row).length() == mapColumnsCount;
			map[row] = puzzleInputLines.get(row).toCharArray();
		}
		
		System.out.println("Garden map");
		for (char[] row : map) {
			System.out.println(String.valueOf(row));
		}
		
		Plot[][] plotsMatrix = new Plot[mapRowsCount][mapColumnsCount];
		for (int row = 0; row < mapRowsCount; row++) {
			for (int column = 0; column < mapColumnsCount; column++) {
				plotsMatrix[row][column] = new Plot(row, column, map[row][column], new AtomicInteger(0), new HashSet<>());
			}
		}
		return new Garden(plotsMatrix, mapRowsCount, mapColumnsCount);
	}
	
	private void putFencesWhereApplicable(Garden garden) {
		for (int row = 0; row < garden.rowsCount(); row++) {
			for (int column = 0; column < garden.columnsCount(); column++) {
				for (Side side : Side.values()) {
					if (plantAtCoordinatesNeedsFenceAtSide(row, column, side, garden)) {
						garden.plotsMatrix()[row][column].fences().add(side);
					}
				}
			}
		}
	}
	
	private boolean plantAtCoordinatesNeedsFenceAtSide(int row, int column, Side side, Garden garden) {
		int rowLook = row + side.rowLook;
		int columnLook = column + side.columnLook;
		return !isPositionWithinGardenArea(rowLook, columnLook, garden)
				|| garden.plotsMatrix()[row][column].type() != garden.plotsMatrix()[rowLook][columnLook].type();
	}
	
	
	private boolean isPositionWithinGardenArea(int row, int column, Garden garden) {
		return row >= 0 && row < garden.rowsCount()
				&& column >= 0 && column < garden.columnsCount();
	}
	
	private Map<Integer, Region> groupIntoRegions(Garden garden) {
		Map<Integer, Region> regions = new HashMap<>();
		AtomicInteger regionIdGenerator = new AtomicInteger(0);
		for (int row = 0; row < garden.rowsCount(); row++) {
			for (int column = 0; column < garden.columnsCount(); column++) {
				Plot plot = garden.plotsMatrix()[row][column];
				if (plot.regionId().get() == 0) {
					Region region = new Region(regionIdGenerator.incrementAndGet(), new HashSet<>());
					recurrentlySpreadRegionFromPlot(region, plot, garden);
					regions.put(region.id(), region);
				}
			}
		}
		
		System.out.println("Regions:");
		regions.values().forEach(System.out::println);
		
		return regions;
	}
	
	private void recurrentlySpreadRegionFromPlot(Region region, Plot plot, Garden garden) {
		assert plot.regionId().get() == 0;
		plot.regionId().set(region.id());
		region.plots().add(plot);
		for (Side side : Side.values()) {
			if (!plot.fences().contains(side)) {
				Plot adjacentPlot = garden.plotsMatrix()[plot.row() + side.rowLook][plot.column() + side.columnLook];
				if (adjacentPlot.regionId().get() == 0) {
					recurrentlySpreadRegionFromPlot(region, adjacentPlot, garden);
				}
			}
		}
	}
	
	int getSidesCount(Region region) {
		List<List<Plot>> sides = new ArrayList<>();
		for (Side edge : Side.values()) {
			List<Plot> edgePlots = new ArrayList<>(region.plots().stream()
					.filter(plot -> plot.fences().contains(edge))
					.toList());
			while (!edgePlots.isEmpty()) {
				Plot plot = edgePlots.getFirst();
				List<Plot> side = recurrentlyFindAdjacentPlotsAtSameSide(plot, edgePlots);
				sides.add(side);
				System.out.format("Side (%s): %s%n", edge, side);
			}
		}
		return sides.size();
	}
	
	private List<Plot> recurrentlyFindAdjacentPlotsAtSameSide(Plot plot, List<Plot> borderPlots) {
		List<Plot> side = new ArrayList<>();
		side.add(plot);
		borderPlots.remove(plot);
		borderPlots.stream()
				.filter(plot::isAdjacent)
				.forEach(adjacentPlot ->
						side.addAll(recurrentlyFindAdjacentPlotsAtSameSide(adjacentPlot, borderPlots)));
		return side;
	}
}
