package aoc2024.day16;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class ScoreTest {
	
	@ParameterizedTest
	@MethodSource("ofTurnTestParams")
	void ofTurn_shouldReturnExpectedScore(Heading from, Heading to, int expectedScore) {
		final int result = Score.ofTurn(from, to);
		assert result == expectedScore
				: String.format("Expected score %d but got %d while turning from %s to %s",
				expectedScore, result, from, to);
	}
	
	private static Stream<Arguments> ofTurnTestParams() {
		return Stream.of(
				Arguments.of(Heading.NORTH, Heading.NORTH, 0),
				Arguments.of(Heading.NORTH, Heading.SOUTH, 2000),
				Arguments.of(Heading.NORTH, Heading.EAST, 1000),
				Arguments.of(Heading.NORTH, Heading.WEST, 1000),
				Arguments.of(Heading.SOUTH, Heading.NORTH, 2000),
				Arguments.of(Heading.SOUTH, Heading.SOUTH, 0),
				Arguments.of(Heading.SOUTH, Heading.EAST, 1000),
				Arguments.of(Heading.SOUTH, Heading.WEST, 1000),
				Arguments.of(Heading.EAST, Heading.NORTH, 1000),
				Arguments.of(Heading.EAST, Heading.SOUTH, 1000),
				Arguments.of(Heading.EAST, Heading.EAST, 0),
				Arguments.of(Heading.EAST, Heading.WEST, 2000),
				Arguments.of(Heading.WEST, Heading.NORTH, 1000),
				Arguments.of(Heading.WEST, Heading.SOUTH, 1000),
				Arguments.of(Heading.WEST, Heading.EAST, 2000),
				Arguments.of(Heading.WEST, Heading.WEST, 0)
		);
	}
}