package aoc2024.day07;

import aoc2024.DayResolver;
import aoc2024.util.PuzzleInputLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

class Day07ResolverTest {
	
	private DayResolver<Long> underTest;
	private List<String> puzzleInputLines;
	
	@BeforeEach
	void setUp() {
		underTest = new Day07Resolver();
		puzzleInputLines = PuzzleInputLoader.loadPublicExamplePuzzleInput(7);
	}
	
	@Test
	void resolvePart1_shouldReturnExpectedResult() {
		// Given
		long expectedPuzzleAnswer = 3749;
		
		// When
		long actualPuzzleAnswer = underTest.resolvePart1(puzzleInputLines);
		
		// Then
		assert Objects.equals(actualPuzzleAnswer, expectedPuzzleAnswer);
	}
	
	@Test
	void resolvePart2_shouldReturnExpectedResult() {
		// Given
		long expectedPuzzleAnswer = 11387;
		
		// When
		long actualPuzzleAnswer = underTest.resolvePart2(puzzleInputLines);
		
		// Then
		assert Objects.equals(actualPuzzleAnswer, expectedPuzzleAnswer);
	}
}