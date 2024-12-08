package aoc2024.day07;

import aoc2024.DayResolver;
import aoc2024.util.PuzzleInputLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Day07Resolver implements DayResolver<Long> {
	
	public static void main(String[] args) {
		Day07Resolver resolver = new Day07Resolver();
		List<String> personalSecretPuzzleInput = PuzzleInputLoader.loadPersonalSecretPuzzleInput(7);
		
		System.out.println("***  Part 1: Sum od possible equations  ***");
		System.out.println("Calibration result: " + resolver.resolvePart1(personalSecretPuzzleInput));
		
		System.out.println("***  Part 2: Sum of possible equations with concatenations  ***");
		System.out.println("Calibration result: " + resolver.resolvePart2(personalSecretPuzzleInput));
	}
	
	public Long resolvePart1(List<String> puzzleInputLines) {
		return getCalibrationResult(puzzleInputLines, List.of("+", "*"));
	}
	
	public Long resolvePart2(List<String> puzzleInputLines) {
		return getCalibrationResult(puzzleInputLines, List.of("+", "*", "||"));
	}
	
	private long getCalibrationResult(List<String> inputLines, List<String> allowedOperators) {
		long calibrationResult = 0L;
		for (String inputLine : inputLines) {
			String[] equationParts = inputLine.split(": ");
			long result = Long.parseLong(equationParts[0]);
			List<Integer> numbers = Arrays.stream(equationParts[1].split(" "))
					.map(Integer::parseInt)
					.toList();
			List<String> operators = new ArrayList<>();
			if (numbers.size() > 1) {
				if (isPossibleEquation(result, numbers, operators, allowedOperators)) {
					System.out.println("Resolved equation: " + getTextualRepresentation(result, numbers, operators));
					calibrationResult += result;
				} else {
					System.out.println("Unable to resolve equation: " + getTextualRepresentation(result, numbers, null));
				}
			} else {
				if (result == numbers.getFirst()) {
					System.out.println("True common equation: " + result + " = " + result);
					calibrationResult += result;
				} else {
					System.out.println("False common equation: " + result + " = " + numbers.getFirst());
				}
			}
		}
		return calibrationResult;
	}
	
	private boolean isPossibleEquation(long result, List<Integer> numbers, List<String> operators, List<String> allowedOperators) {
		if (operators.size() < numbers.size() - 1) {
			for (String operator : allowedOperators) {
				List<String> enhancedOperators = new ArrayList<>(operators);
				enhancedOperators.add(operator);
				if (isPossibleEquation(result, numbers, enhancedOperators, allowedOperators)) {
					complementWithMissingOperators(operators, enhancedOperators);
					return true;
				}
			}
			return false;
		} else {
			return isTrueEquation(result, numbers, operators);
		}
	}
	
	private boolean isTrueEquation(long result, List<Integer> numbers, List<String> operators) {
		assert Objects.equals(numbers.size(), operators.size() + 1);
		long actualResult = numbers.getFirst();
		for (int operationIndex = 0; operationIndex < operators.size(); operationIndex++) {
			switch (operators.get(operationIndex)) {
				case "+":
					actualResult += numbers.get(operationIndex + 1);
					break;
				case "*":
					actualResult *= numbers.get(operationIndex + 1);
					break;
				case "||":
					actualResult = Long.parseLong("" + actualResult + numbers.get(operationIndex + 1));
					break;
				default:
					throw new IllegalArgumentException("Unexpected operator: " + operators.get(operationIndex));
			}
		}
		return Objects.equals(actualResult, result);
	}
	
	private void complementWithMissingOperators(List<String> targetOperators, List<String> sourceOperators) {
		for (int operatorIndex = targetOperators.size(); operatorIndex < sourceOperators.size(); operatorIndex++) {
			targetOperators.add(sourceOperators.get(operatorIndex));
		}
	}
	
	private String getTextualRepresentation(long result, List<Integer> numbers, List<String> operators) {
		StringBuilder textualRepresentation = new StringBuilder()
				.append(result)
				.append(" = ")
				.append(numbers.getFirst());
		for (int operationIndex = 1; operationIndex < numbers.size(); operationIndex++) {
			textualRepresentation
					.append(" ")
					.append(operators == null ? "?" : operators.get(operationIndex - 1))
					.append(" ")
					.append(numbers.get(operationIndex));
		}
		return textualRepresentation.toString();
	}
}
