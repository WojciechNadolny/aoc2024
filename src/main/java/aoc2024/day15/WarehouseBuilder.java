package aoc2024.day15;

import java.util.ArrayList;
import java.util.List;

class WarehouseBuilder {
	
	private final List<String> rows = new ArrayList<>();
	private Integer width = null;
	private Robot robot;
	
	public void addRow(String row) {
		if (width == null) {
			width = row.length();
		} else {
			assert row.length() == width;
		}
		rows.add(row);
	}
	
	void setRobot(Robot robot) {
		this.robot = robot;
	}
	
	Warehouse build() {
		int height = rows.size();
		char[][] map = new char[width][rows.size()];
		for (int row = 0; row < rows.size(); row++) {
			map[row] = rows.get(row).toCharArray();
		}
		return new Warehouse(height, width, map, robot);
	}
	
	Warehouse buildDoubleWidth() {
		int height = rows.size();
		width *= 2;
		char[][] map = new char[height][width];
		StringBuilder widenedMapRow = new StringBuilder();
		for (int row = 0; row < rows.size(); row++) {
			widenedMapRow.setLength(0);
			for (char figure : rows.get(row).toCharArray()) {
				switch(figure) {
					case Figure.ROBOT -> widenedMapRow.append(Figure.ROBOT).append(Figure.AIR);
					case Figure.BOX -> widenedMapRow.append(Figure.WIDE_BOX_LEFT).append(Figure.WIDE_BOX_RIGHT);
					case Figure.WALL -> widenedMapRow.append(Figure.WALL).append(Figure.WALL);
					case Figure.AIR -> widenedMapRow.append(Figure.AIR).append(Figure.AIR);
					default -> throw new IllegalStateException("Unexpected figure: " + figure);
				}
			}
			map[row] = widenedMapRow.toString().toCharArray();
		}
		return new Warehouse(height, width, map, robot);
	}
}
