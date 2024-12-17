package aoc2024.day17;

import aoc2024.DayResolver;
import aoc2024.util.PuzzleInputLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

class Day17ResolverTest {
	
	private DayResolver<String> underTest;
	private List<String> puzzleInputLines;
	
	@BeforeEach
	void setUp() {
		underTest = new Day17Resolver();
		puzzleInputLines = PuzzleInputLoader.loadPublicExamplePuzzleInput(17);
	}
	
	@Test
	void resolvePart1_shouldReturnExpectedResult() {
		// Given
		String expectedPuzzleAnswer = "4,6,3,5,6,3,5,2,1,0";
		
		// When
		String actualPuzzleAnswer = underTest.resolvePart1(puzzleInputLines);
		
		// Then
		assert Objects.equals(actualPuzzleAnswer, expectedPuzzleAnswer);
	}
	
	@Test
	void resolvePart2_shouldReturnExpectedResult() {
		// Given
		String expectedPuzzleAnswer = null; // TODO Update
		
		// When
		String actualPuzzleAnswer = underTest.resolvePart2(puzzleInputLines);
		
		// Then
		assert Objects.equals(actualPuzzleAnswer, expectedPuzzleAnswer);
	}
}