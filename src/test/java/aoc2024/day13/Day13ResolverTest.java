package aoc2024.day13;

import aoc2024.DayResolver;
import aoc2024.util.PuzzleInputLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

class Day13ResolverTest {
	
	private DayResolver<Long> underTest;
	private List<String> puzzleInputLines;
	
	@BeforeEach
	void setUp() {
		underTest = new Day13Resolver();
		puzzleInputLines = PuzzleInputLoader.loadPublicExamplePuzzleInput(13);
	}
	
	@Test
	void resolvePart1_shouldReturnExpectedResult() {
		// Given
		long expectedPuzzleAnswer = 480;
		
		// When
		long actualPuzzleAnswer = underTest.resolvePart1(puzzleInputLines);
		
		// Then
		assert Objects.equals(actualPuzzleAnswer, expectedPuzzleAnswer)
				: String.format("Actual puzzle answer is %d and doesn't match expected %d%n", actualPuzzleAnswer, expectedPuzzleAnswer);
	}
	
	@Test
	void resolvePart2_shouldReturnExpectedResult() {
		// Given
		long expectedPuzzleAnswer = 875318608908L;
		
		// When
		long actualPuzzleAnswer = underTest.resolvePart2(puzzleInputLines);
		
		// Then
		assert Objects.equals(actualPuzzleAnswer, expectedPuzzleAnswer)
				: String.format("Actual puzzle answer is %d and doesn't match expected %d%n", actualPuzzleAnswer, expectedPuzzleAnswer);
	}
}