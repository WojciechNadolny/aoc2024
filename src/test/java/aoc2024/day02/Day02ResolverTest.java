package aoc2024.day02;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import aoc2024.DayResolver;
import aoc2024.util.PuzzleInputLoader;

import java.util.List;
import java.util.Objects;

class Day02ResolverTest {
	
	private DayResolver<Integer> underTest;
	private List<String> puzzleInputLines;
	
	@BeforeEach
	void setUp() {
		underTest = new Day02Resolver();
		puzzleInputLines = PuzzleInputLoader.loadPublicExamplePuzzleInput(2);
	}
	
	@Test
	void resolvePart1_shouldReturnExpectedResult() {
		// Given
		int expectedPuzzleAnswer = 2;
		
		// When
		int actualPuzzleAnswer = underTest.resolvePart1(puzzleInputLines);
		
		// Then
		assert Objects.equals(actualPuzzleAnswer, expectedPuzzleAnswer);
	}
	
	@Test
	void resolvePart2_shouldReturnExpectedResult() {
		// Given
		int expectedPuzzleAnswer = 4;
		
		// When
		int actualPuzzleAnswer = underTest.resolvePart2(puzzleInputLines);
		
		// Then
		assert Objects.equals(actualPuzzleAnswer, expectedPuzzleAnswer);
	}
}