package aoc2024.day03;

import aoc2024.DayResolver;
import aoc2024.util.PuzzleInputLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

class Day03ResolverTest {
	
	private DayResolver<Long> underTest;
	private List<String> puzzleInputLines;
	
	@BeforeEach
	void setUp() {
		underTest = new Day03Resolver();
		puzzleInputLines = PuzzleInputLoader.loadPublicExamplePuzzleInput(3);
	}
	
	@Test
	void resolvePart1_shouldReturnExpectedResult() {
		// Given
		long expectedPuzzleAnswer = 161;
		
		// When
		long actualPuzzleAnswer = underTest.resolvePart1(puzzleInputLines);
		
		// Then
		assert Objects.equals(actualPuzzleAnswer, expectedPuzzleAnswer);
	}
	
	@Test
	void resolvePart2_shouldReturnExpectedResult() {
		// Given
		puzzleInputLines = List.of("xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))");
		long expectedPuzzleAnswer = 48;
		
		// When
		long actualPuzzleAnswer = underTest.resolvePart2(puzzleInputLines);
		
		// Then
		assert Objects.equals(actualPuzzleAnswer, expectedPuzzleAnswer);
	}
}