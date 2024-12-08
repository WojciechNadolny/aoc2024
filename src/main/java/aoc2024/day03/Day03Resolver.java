package aoc2024.day03;

import aoc2024.DayResolver;
import aoc2024.util.PuzzleInputLoader;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day03Resolver implements DayResolver<Long> {
	
	private static final String REGEX_MUL = "mul\\(\\d{1,3},\\d{1,3}\\)"; // e.g. mul(123,4)
	private static final String REGEX_DOS = "do\\(\\)"; // e.g. do()
	private static final String REGEX_DONT = "don't\\(\\)"; // e.g. don't()
	
	public static void main(String[] args) {
		Day03Resolver resolver = new Day03Resolver();
		List<String> personalSecretPuzzleInput = PuzzleInputLoader.loadPersonalSecretPuzzleInput(3);
		
		System.out.println("***  Part 1: mul() instructions  ***");
		System.out.println("Multiplications sum = " + resolver.resolvePart1(personalSecretPuzzleInput));
		
		System.out.println("***  Part 2: mul() instructions with do()s and don't()s  ***");
		System.out.println("Enabled multiplications sum = " + resolver.resolvePart2(personalSecretPuzzleInput));
	}
	
	public Long resolvePart1(List<String> puzzleInputLines) {
		long multiplicationsSum = 0L;
		Pattern pattern = Pattern.compile(REGEX_MUL);
		for (String line : puzzleInputLines) {
			Matcher matcher = pattern.matcher(line);
			while(matcher.find()) {
				long result = parseAndMultiply(line, matcher);
				multiplicationsSum += result;
				System.out.format("%s = %d --> accumulated to %d%n",
						line.substring(matcher.start(), matcher.end()), result, multiplicationsSum);
			}
		}
		return multiplicationsSum;
	}
	
	public Long resolvePart2(List<String> puzzleInputLines) {
		long multiplicationsSum = 0;
		boolean enabled = true;
		System.out.println("Instructions enabled");
		Pattern pattern = Pattern.compile("(" + REGEX_MUL + ")|(" + REGEX_DOS + ")|(" + REGEX_DONT + ")");
		for (String line : puzzleInputLines) {
			Matcher matcher = pattern.matcher(line);
			while(matcher.find()) {
				int startIndex = matcher.start();
				int endIndex = matcher.end();
				if (line.substring(startIndex).startsWith("do")) {
					enabled = !line.substring(startIndex).startsWith("don't");
					System.out.println("!!!  Instructions " + (enabled ? "enabled" : "disabled") + "  !!!");
				} else {
					if (enabled) {
						long result = parseAndMultiply(line, matcher);
						multiplicationsSum += result;
						System.out.format("%s = %d --> accumulated to %d%n", line.substring(startIndex, endIndex), result, multiplicationsSum);
					} else {
						System.out.println("Instruction " + line.substring(startIndex, endIndex) + " skipped");
					}
				}
			}
		}
		return multiplicationsSum;
	}
	
	private long parseAndMultiply(String line, Matcher matcher) {
		int startIndex = matcher.start();
		int endIndex = matcher.end();
		int commaIndex = line.indexOf(',', startIndex, endIndex);
		int factor1 = Integer.parseInt(line.substring(startIndex + 4, commaIndex));
		int factor2 = Integer.parseInt(line.substring(commaIndex + 1, endIndex - 1));
		return (long) factor1 * factor2;
	}
}
