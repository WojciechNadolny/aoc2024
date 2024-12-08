package aoc2024.day01;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import aoc2024.DayResolver;
import aoc2024.util.PuzzleInputLoader;

import java.util.List;
import java.util.Objects;

class Day01ResolverTest {
	
	private DayResolver<Long> underTest;
	private List<String> puzzleInputLines;
	
	@BeforeEach
	void setUp() {
		underTest = new Day01Resolver();
		puzzleInputLines = PuzzleInputLoader.loadPublicExamplePuzzleInput(1);
	}
	
	@Test
	void resolvePart1_shouldReturnExpectedResult() {
		// Given
		long expectedPuzzleAnswer = 11;
		
		// When
		long actualPuzzleAnswer = underTest.resolvePart1(puzzleInputLines);
		
		// Then
		assert Objects.equals(actualPuzzleAnswer, expectedPuzzleAnswer);
	}
	
	@Test
	void resolvePart2_shouldReturnExpectedResult() {
		// Given
		long expectedPuzzleAnswer = 31;
		
		// When
		long actualPuzzleAnswer = underTest.resolvePart2(puzzleInputLines);
		
		// Then
		assert Objects.equals(actualPuzzleAnswer, expectedPuzzleAnswer);
	}
}