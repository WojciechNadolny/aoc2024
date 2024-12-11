package aoc2024.day11;

import aoc2024.DayResolver;
import aoc2024.util.PuzzleInputLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day11Resolver implements DayResolver<Long> {
	
	private static final String STONES_DELIMITER = " ";
	
	private Map<Integer, Map<String, Long>> lookup;
	
	public static void main(String[] args) {
		Day11Resolver resolver = new Day11Resolver();
		List<String> personalSecretPuzzleInput = PuzzleInputLoader.loadPersonalSecretPuzzleInput(11);
		
		System.out.println("***  Part 1: Physics-defying stones after 25 blinks  ***");
		System.out.println("Stones after 25 blinks: " + resolver.resolvePart1(personalSecretPuzzleInput));
		
		System.out.println("***  Part 2: Physics-defying stones after 75 blinks  ***");
		System.out.println("Stones after 75 blinks: " + resolver.resolvePart2(personalSecretPuzzleInput));
	}
	
	public Long resolvePart1(List<String> puzzleInputLines) {
		assert puzzleInputLines.size() == 1;
		String currentStonesArrangement = puzzleInputLines.getFirst();
		System.out.println("Original stones arrangement: " + currentStonesArrangement);
		
		// Compute reactions to blinks iteratively
		for (int blink = 0; blink < 25; blink++) {
			currentStonesArrangement = getStonesArrangementAfterBlink(currentStonesArrangement);
			System.out.format("Stones count after blink %d: %s   %s%n", blink+1, getStonesCount(currentStonesArrangement), currentStonesArrangement);
		}
		return getStonesCount(currentStonesArrangement);
	}
	
	public Long resolvePart2(List<String> puzzleInputLines) {
		assert puzzleInputLines.size() == 1;
		String stonesArrangement = puzzleInputLines.getFirst();
		System.out.println("Original stones arrangement: " + stonesArrangement);
		
		int blinksCount = 75;
		initializeLookup(blinksCount);
		
		// Compute reactions to blinks recursively
		long stonesCount = 0;
		for (String stone : stonesArrangement.split(STONES_DELIMITER)) {
			stonesCount += getStonesCountLookingAtStoneAfterAnotherBlinksCount(stone, blinksCount);
		}
		return stonesCount;
	}
	
	private long getStonesCount(String currentStonesArrangement) {
		return currentStonesArrangement.split(STONES_DELIMITER).length;
	}
	
	private String getStonesArrangementAfterBlink(String currentStonesArrangement) {
		List<String> newStonesArrangement = new ArrayList<>();
		for (String stone : currentStonesArrangement.split(STONES_DELIMITER)) {
			newStonesArrangement.add(applyStoneRules(stone));
		}
		return String.join(" ", newStonesArrangement);
	}
	
	private String applyStoneRules(String stone) {
		if (stone.equals("0")) {
			return "1";
		}
		if (stone.length() % 2 == 0) {
			return splitStone(stone);
		}
		return String.valueOf(Long.parseLong(stone) * 2024);
	}
	
	private String splitStone(String stone) {
		int halfIndex = stone.length() / 2;
		String firstHalf = stone.substring(0, halfIndex);
		String secondHalf = stone.substring(halfIndex, stone.length()); // Redundant second argument left for brevity
		return String.format("%d%s%d", Long.parseLong(firstHalf), STONES_DELIMITER, Long.parseLong(secondHalf));
	}
	
	private void initializeLookup(int blinksCount) {
		lookup = new HashMap<>();
		for (int blink = 1; blink <= blinksCount; blink++) {
			lookup.put(blink, new HashMap<>());
		}
	}
	
	private long getStonesCountLookingAtStoneAfterAnotherBlinksCount(String stone, int blinksCount) {
		// After last blink
		if (blinksCount == 1) {
			if (stone.equals("0")) {
				return 1;
			} else if (stone.length() % 2 == 0) {
				return 2;
			} else {
				return 1;
			}
		}
		
		// After any previous blink
		// If such stone's multiplication already calculated recursively after current blink then reuse its result
		Long stonesCountFromLookup = lookup.get(blinksCount).get(stone);
		if (stonesCountFromLookup != null) {
			return stonesCountFromLookup;
		}
		
		// No such stone appeared after current blink so calculate its multiplication recursively
		long stoneRecursiveMultiplicationResult;
		if (stone.equals("0")) {
			stoneRecursiveMultiplicationResult = getStonesCountLookingAtStoneAfterAnotherBlinksCount("1", blinksCount - 1);
		} else if (stone.length() % 2 == 0) {
			String[] newStones = splitStone(stone).split(STONES_DELIMITER);
			stoneRecursiveMultiplicationResult = getStonesCountLookingAtStoneAfterAnotherBlinksCount(newStones[0], blinksCount - 1)
					+ getStonesCountLookingAtStoneAfterAnotherBlinksCount(newStones[1], blinksCount - 1);
		} else {
			String newStone = String.valueOf(Long.parseLong(stone) * 2024);
			stoneRecursiveMultiplicationResult = getStonesCountLookingAtStoneAfterAnotherBlinksCount(newStone, blinksCount - 1);
		}
		
		// Save result for this stone's multiplication after current blink in lookup map
		lookup.get(blinksCount).put(stone, stoneRecursiveMultiplicationResult);
		
		return stoneRecursiveMultiplicationResult;
	}
}
