package aoc2024.day14;

import java.util.function.Predicate;

public record SubSpace (String name, Predicate<Integer> xWithin, Predicate<Integer> yWithin) {
	
	boolean containsTile(int x, int y) {
		return xWithin.test(x) && yWithin.test(y);
	}
}
