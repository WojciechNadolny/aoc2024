package aoc2024.day06;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Objects;
import java.util.stream.Stream;

class GuardTest {
	
	private static final int INITIAL_ROW = 100;
	private static final int INITIAL_COLUMN = 200;
	
	@ParameterizedTest
	@MethodSource("walkTestParams")
	void walk_shouldAlterRowOrColumnNumberAsExpected(Direction direction, int expectedRow, int expectedColumn) {
		// Given
		final Guard underTest = new Guard(INITIAL_ROW, INITIAL_COLUMN, direction);
		
		// When
		underTest.walk();
		
		// Then
		assert underTest.getRow() == expectedRow;
		assert underTest.getColumn() == expectedColumn;
		assert underTest.getDirection() == direction;
	}
	
	@ParameterizedTest
	@MethodSource("turnTestParams")
	void turn_shouldRotate90DegreesToTheRightOnly(Direction originalDirection, Direction expectedDirection) {
		// Given
		final Guard underTest = new Guard(INITIAL_ROW, INITIAL_COLUMN, originalDirection);
		
		// When
		underTest.turn();
		
		// Then
		assert underTest.getRow() == INITIAL_ROW;
		assert underTest.getColumn() == INITIAL_COLUMN;
		assert underTest.getDirection() == expectedDirection;
	}
	
	@Test
	void equals_shouldBeEqual() {
		// Given
		final Guard guard1 = new Guard(INITIAL_ROW, INITIAL_COLUMN, Direction.UP);
		final Guard guard2 = new Guard(INITIAL_ROW, INITIAL_COLUMN, Direction.UP);
		
		// When
		boolean result1 = guard1.equals(guard2);
		boolean result2 = guard2.equals(guard1);
		
		// Then
		assert result1;
		assert result2;
	}
	
	@ParameterizedTest
	@MethodSource("notEqualTestParams")
	void equals_shouldNotBeEqual(Guard guard1, Guard guard2) {
		assert !guard1.equals(guard2);
	}
	
	@Test
	void toString_shouldReturnExpectedString() {
		// Given
		final Guard guard = new Guard(INITIAL_ROW, INITIAL_COLUMN, Direction.UP);
		final String expected = "Guard at row 100, column 200, heading up";
		
		// When
		String result = guard.toString();
		
		// Then
		assert Objects.equals(result, expected);
	}
	
	private static Stream<Arguments> walkTestParams() {
		return Stream.of(
				Arguments.of(Direction.UP, INITIAL_ROW - 1, INITIAL_COLUMN),
				Arguments.of(Direction.DOWN, INITIAL_ROW + 1, INITIAL_COLUMN),
				Arguments.of(Direction.LEFT, INITIAL_ROW, INITIAL_COLUMN - 1),
				Arguments.of(Direction.RIGHT, INITIAL_ROW, INITIAL_COLUMN + 1)
		);
	}
	
	private static Stream<Arguments> turnTestParams() {
		return Stream.of(
				Arguments.of(Direction.UP, Direction.RIGHT),
				Arguments.of(Direction.RIGHT, Direction.DOWN),
				Arguments.of(Direction.DOWN, Direction.LEFT),
				Arguments.of(Direction.LEFT, Direction.UP)
		);
	}
	
	public static Stream<Arguments> notEqualTestParams() {
		return Stream.of(
				Arguments.of(
						new Guard(INITIAL_ROW, INITIAL_COLUMN, Direction.UP),
						new Guard(INITIAL_ROW + 1, INITIAL_COLUMN, Direction.UP)),
				Arguments.of(
						new Guard(INITIAL_ROW, INITIAL_COLUMN, Direction.UP),
						new Guard(INITIAL_ROW, INITIAL_COLUMN + 1, Direction.UP)),
				Arguments.of(
						new Guard(INITIAL_ROW, INITIAL_COLUMN, Direction.UP),
						new Guard(INITIAL_ROW, INITIAL_COLUMN, Direction.DOWN))
		);
	}
}