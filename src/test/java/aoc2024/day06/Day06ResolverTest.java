package aoc2024.day06;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import aoc2024.DayResolver;
import aoc2024.util.PuzzleInputLoader;

import java.util.List;
import java.util.Objects;

class Day06ResolverTest {
	
	private DayResolver<Integer> underTest;
	private List<String> puzzleInputLines;
	
	@BeforeEach
	void setUp() {
		underTest = new Day06Resolver();
		puzzleInputLines = PuzzleInputLoader.loadPublicExamplePuzzleInput(6);
	}
	
	@Test
	void resolvePart1_shouldReturnExpectedResult() {
		// Given
		int expectedPuzzleAnswer = 41;
		
		// When
		int actualPuzzleAnswer = underTest.resolvePart1(puzzleInputLines);
		
		// Then
		assert Objects.equals(actualPuzzleAnswer, expectedPuzzleAnswer);
	}
	
	@Test
	void resolvePart2_shouldReturnExpectedResult() {
		// Given
		int expectedPuzzleAnswer = 6;
		
		// When
		int actualPuzzleAnswer = underTest.resolvePart2(puzzleInputLines);
		
		// Then
		assert Objects.equals(actualPuzzleAnswer, expectedPuzzleAnswer);
	}
}