package aoc2024.day17;

import aoc2024.DayResolver;
import aoc2024.util.PuzzleInputLoader;

import java.util.List;

public class Day17Resolver implements DayResolver<String> {
	
	public static void main(String[] args) {
		DayResolver<String> resolver = new Day17Resolver();
		List<String> personalSecretPuzzleInput = PuzzleInputLoader.loadPersonalSecretPuzzleInput(17);
		
		System.out.println("***  Part 1: 3-bit CPU  ***");
		System.out.println("Comma-separated program output: " + resolver.resolvePart1(personalSecretPuzzleInput));
	}
	
	public String resolvePart1(List<String> puzzleInputLines) {
		CPU cpu = new CPU();
		cpu.setA(Integer.parseInt(puzzleInputLines.get(0).substring("Register A: ".length())));
		cpu.setB(Integer.parseInt(puzzleInputLines.get(1).substring("Register B: ".length())));
		cpu.setC(Integer.parseInt(puzzleInputLines.get(2).substring("Register C: ".length())));
		return cpu.executeProgram(puzzleInputLines.get(4).substring("Program: ".length()));
	}
	
	public String resolvePart2(List<String> puzzleInputLines) {
		throw new UnsupportedOperationException();
	}
}
