package aoc2024.day16;

import java.util.EnumMap;

record Maze(int height, int width, Tile[][] map, Tile start, Tile destination) {
	
	static Maze of(char[][] map) {
		int height = map.length;
		int width = map[0].length;
		Tile[][] tiles = new Tile[height][width];
		Tile start = null;
		Tile destination = null;
		for (int row = 0; row < height; row++) {
			for (int column = 0; column < width; column++) {
				tiles[row][column] = switch (map[row][column]) {
					case '.' -> new Tile(row, column, true, new EnumMap<>(Heading.class));
					case '#' -> new Tile(row, column, false, null);
					case 'S' -> start = new Tile(row, column, true, new EnumMap<>(Heading.class));
					case 'E' -> destination = new Tile(row, column, true, new EnumMap<>(Heading.class));
					default -> throw new IllegalArgumentException("Unexpected tile symbol: " + map[row][column]);
				};
			}
		}
		return new Maze(height, width, tiles, start, destination);
	}
}
