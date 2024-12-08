package aoc2024.day01;

import aoc2024.DayResolver;
import aoc2024.util.PuzzleInputLoader;

import java.util.ArrayList;
import java.util.List;

public class Day01Resolver implements DayResolver<Long> {
	
	private final List<Integer> leftList = new ArrayList<>();
	private final List<Integer> rightList = new ArrayList<>();
	
	public static void main(String[] args) {
		Day01Resolver resolver = new Day01Resolver();
		List<String> personalSecretPuzzleInput = PuzzleInputLoader.loadPersonalSecretPuzzleInput(1);
		
		System.out.println("***  Part 1: Total distance  ***");
		System.out.println("Total distance = " + resolver.resolvePart1(personalSecretPuzzleInput));
		
		System.out.println("***  Part 2: Similarity score  ***");
		System.out.println("Similarity score = " + resolver.resolvePart2(personalSecretPuzzleInput));
	}
	
	public Long resolvePart1(List<String> puzzleInputLines) {
		parse(puzzleInputLines);
		leftList.sort(Integer::compareTo);
		rightList.sort(Integer::compareTo);
		assert leftList.size() == rightList.size();
		
		long totalDistance = 0;
		for (int i = 0; i < leftList.size(); i++) {
			int leftLocationId = leftList.get(i);
			int rightLocationId = rightList.get(i);
			int distance = Math.abs(leftList.get(i) - rightList.get(i));
			totalDistance += distance;
			System.out.format("Distance between %d and %d is %d, accumulated to total %d\n",
					leftLocationId, rightLocationId, distance, totalDistance);
		}
		return totalDistance;
	}
	
	public Long resolvePart2(List<String> puzzleInputLines) {
		parse(puzzleInputLines);
		long similarityScore = 0L;
		for (Integer number : leftList) {
			long occurrences = rightList.stream()
					.filter(number::equals)
					.count();
			if (occurrences == 0) {
				System.out.format("Number %d from left list does not appear in the right list, " +
						"so the similarity code remains unchanged%n", number);
			} else {
				long multiplicationResult = number * occurrences;
				similarityScore += multiplicationResult;
				System.out.format("Number %d from left list appears %d times in the right list, " +
						"so the similarity code increases by %d * %d = %d to %d%n",
						number, occurrences, number, occurrences, multiplicationResult, similarityScore);
			}
		}
		return similarityScore;
	}
	
	private void parse(List<String> puzzleInputLines) {
		puzzleInputLines.forEach(line -> {
			String[] numberStrings = line.split("\\s+");
			leftList.add(Integer.parseInt(numberStrings[0]));
			rightList.add(Integer.parseInt(numberStrings[1]));
		});
	}
}
