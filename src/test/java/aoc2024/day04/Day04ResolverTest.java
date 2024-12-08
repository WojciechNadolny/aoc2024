package aoc2024.day04;

import aoc2024.DayResolver;
import aoc2024.util.PuzzleInputLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

class Day04ResolverTest {
	
	private DayResolver<Integer> underTest;
	private List<String> puzzleInputLines;
	
	@BeforeEach
	void setUp() {
		underTest = new Day04Resolver();
		puzzleInputLines = PuzzleInputLoader.loadPublicExamplePuzzleInput(4);
	}
	
	@Test
	void resolvePart1_shouldReturnExpectedResult() {
		// Given
		int expectedPuzzleAnswer = 18;
		
		// When
		int actualPuzzleAnswer = underTest.resolvePart1(puzzleInputLines);
		
		// Then
		assert Objects.equals(actualPuzzleAnswer, expectedPuzzleAnswer);
	}
	
	@Test
	void resolvePart2_shouldReturnExpectedResult() {
		// Given
		int expectedPuzzleAnswer = 9;
		
		// When
		int actualPuzzleAnswer = underTest.resolvePart2(puzzleInputLines);
		
		// Then
		assert Objects.equals(actualPuzzleAnswer, expectedPuzzleAnswer);
	}
}