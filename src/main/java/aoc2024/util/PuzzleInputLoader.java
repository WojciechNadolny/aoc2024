package aoc2024.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class PuzzleInputLoader {
	
	private static final String PUZZLE_INPUT_PUBLIC_EXAMPLE_PATH = "src/test/resources/puzzleinput/day%02d.txt";
	private static final String PUZZLE_INPUT_PERSONAL_SECRET_PATH = "src/main/resources/puzzleinput/day%02d.txt";
	
	public static List<String> loadPublicExamplePuzzleInput(int day) {
		return loadPuzzleInput(day, PUZZLE_INPUT_PUBLIC_EXAMPLE_PATH);
	}
	
	public static List<String> loadPersonalSecretPuzzleInput(int day) {
		return loadPuzzleInput(day, PUZZLE_INPUT_PERSONAL_SECRET_PATH);
	}
	
	private static List<String> loadPuzzleInput(int day, String path) {
		Path puzzleInputPath = Paths.get(String.format(path, day));
		try {
			return Files.readAllLines(puzzleInputPath);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
