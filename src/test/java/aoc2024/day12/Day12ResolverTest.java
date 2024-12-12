package aoc2024.day12;

import aoc2024.DayResolver;
import aoc2024.util.PuzzleInputLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

class Day12ResolverTest {
	
	private DayResolver<Long> underTest;
	private List<String> puzzleInputLines;
	
	@BeforeEach
	void setUp() {
		underTest = new Day12Resolver();
		puzzleInputLines = PuzzleInputLoader.loadPublicExamplePuzzleInput(12);
	}
	
	@Test
	void resolvePart1_shouldReturnExpectedResult() {
		// Given
		long expectedPuzzleAnswer = 1930;
		
		// When
		long actualPuzzleAnswer = underTest.resolvePart1(puzzleInputLines);
		
		// Then
		assert Objects.equals(actualPuzzleAnswer, expectedPuzzleAnswer);
	}
	
	@Test
	void resolvePart2_shouldReturnExpectedResult() {
		// Given
		long expectedPuzzleAnswer = 1206;
		
		// When
		long actualPuzzleAnswer = underTest.resolvePart2(puzzleInputLines);
		
		// Then
		assert Objects.equals(actualPuzzleAnswer, expectedPuzzleAnswer);
	}
}