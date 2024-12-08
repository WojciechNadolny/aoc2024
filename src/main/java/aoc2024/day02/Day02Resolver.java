package aoc2024.day02;

import aoc2024.DayResolver;
import aoc2024.util.PuzzleInputLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day02Resolver implements DayResolver<Integer> {
	
	public static void main(String[] args) {
		Day02Resolver resolver = new Day02Resolver();
		List<String> personalSecretPuzzleInput = PuzzleInputLoader.loadPersonalSecretPuzzleInput(2);
		
		System.out.println("***  Part 1: Safe levels  ***");
		System.out.println("Safe reports count = " + resolver.resolvePart1(personalSecretPuzzleInput));
		
		System.out.println("***  Part 2: Safe levels with one removable  ***");
		System.out.println("Safe reports count = " + resolver.resolvePart2(personalSecretPuzzleInput));
	}
	
	public Integer resolvePart1(List<String> reports) {
		int safeReportsCount = 0;
		for (String report : reports) {
			List<String> levels = new ArrayList<>(Arrays.asList(report.split(" ")));
			if (isSafeReport(levels, 0, false)) {
				safeReportsCount++;
			}
		}
		return safeReportsCount;
	}
	
	public Integer resolvePart2(List<String> reports) {
		int safeReportsCount = 0;
		for (String report : reports) {
			List<String> levels = new ArrayList<>(Arrays.asList(report.split(" ")));
			if (isSafeReport(levels, 1, false)) {
				safeReportsCount++;
			}
		}
		return safeReportsCount;
	}
	
	private boolean isSafeReport(List<String> levels, int removableCount, boolean recurrentCall) {
		Boolean ascending = null;
		Integer lastLevel = null;
		for (String levelString : levels) {
			int level = Integer.parseInt(levelString);
			if (lastLevel != null) { // skip first
				
				// On duplicate
				if (level == lastLevel) { // on duplicate
					return handleUnsafe(levels, level, removableCount, recurrentCall);
				}
				
				// Infer trend
				if (ascending == null) {
					ascending = level > lastLevel;
				}
				
				// Safety check on each level
				boolean unsafeAscending = ascending && (level < lastLevel || level > lastLevel + 3);
				boolean unsafeDescending = !ascending && (level > lastLevel || level < lastLevel - 3);
				if (unsafeAscending || unsafeDescending) {
					return handleUnsafe(levels, level, removableCount, recurrentCall);
				}
			}
			lastLevel = level;
		}
		System.out.format("Report safe: %s%n", levels);
		return true;
	}
	
	private boolean handleUnsafe(List<String> levels, int level, int removableCount, boolean recurrentCall) {
		if (removableCount == 0) {
			if (!recurrentCall) {
				System.out.format("Report %s unsafe at level %d%n", levels, level);
			}
			return false;
		} else {
			return tryRemovals(levels);
		}
	}
	
	private boolean tryRemovals(List<String> levels) {
		for (int removalIndex = 0; removalIndex < levels.size(); removalIndex++) {
			List<String> reportWithRemoval = new ArrayList<>(levels);
			//noinspection SuspiciousListRemoveInLoop
			reportWithRemoval.remove(removalIndex);
			if (isSafeReport(reportWithRemoval, 0, true)) {
				System.out.format("Report %s safe with removed #%d element (%s) from %s%n", levels, removalIndex, levels.get(removalIndex), levels);
				return true;
			}
		}
		System.out.format("Report %s unsafe despite removal attempts from%n", levels);
		return false;
	}
}
