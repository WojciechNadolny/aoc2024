package aoc2024.day12;

import java.util.*;

public record Region(int id, Set<Plot> plots) {
	
	int getArea() {
		return plots.size();
	}
	
	int getPerimeter() {
		return plots.stream()
				.map(Plot::fences)
				.mapToInt(Collection::size)
				.sum();
	}
	
	@Override
	public String toString() {
		return String.format("Region %d: area = %d, perimeter = %d, plots: %s",
				id, getArea(), getPerimeter(), plots);
	}
}
