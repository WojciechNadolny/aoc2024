package aoc2024;

import java.util.List;

public interface DayResolver<T> {
	
	T resolvePart1(List<String> puzzleInputLines);
	T resolvePart2(List<String> puzzleInputLines);
}
