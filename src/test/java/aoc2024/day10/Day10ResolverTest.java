package aoc2024.day10;

import aoc2024.DayResolver;
import aoc2024.util.PuzzleInputLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

class Day10ResolverTest {
	
	private DayResolver<Integer> underTest;
	private List<String> puzzleInputLines;
	
	@BeforeEach
	void setUp() {
		underTest = new Day10Resolver();
		puzzleInputLines = PuzzleInputLoader.loadPublicExamplePuzzleInput(10);
	}
	
	@Test
	void resolvePart1_shouldReturnExpectedResult() {
		// Given
		int expectedPuzzleAnswer = 36;
		
		// When
		int actualPuzzleAnswer = underTest.resolvePart1(puzzleInputLines);
		
		// Then
		assert Objects.equals(actualPuzzleAnswer, expectedPuzzleAnswer);
	}
	
	@Test
	void resolvePart2_shouldReturnExpectedResult() {
		// Given
		int expectedPuzzleAnswer = 81;
		
		// When
		int actualPuzzleAnswer = underTest.resolvePart2(puzzleInputLines);
		
		// Then
		assert Objects.equals(actualPuzzleAnswer, expectedPuzzleAnswer);
	}
}