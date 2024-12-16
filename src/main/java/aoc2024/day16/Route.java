package aoc2024.day16;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

class Route {
	
	private final List<Step> steps;
	private Heading heading;
	
	Route(Step step) {
		this.steps = new ArrayList<>();
		addStep(step);
	}
	
	private Route(List<Step> steps, Heading heading) {
		this.steps = steps;
		this.heading = heading;
	}
	
	void addStep(Step step) {
		this.steps.add(step);
		this.heading = step.heading();
	}
	
	List<Step> getSteps() {
		return steps;
	}
	
	Heading getHeading() {
		return heading;
	}
	
	int getScore() {
		return steps.getLast().score();
	}
	
	boolean visited(Tile tile) {
		return this.steps.stream()
				.map(Step::tile)
				.anyMatch(tile::equals);
	}
	
	boolean endsOn(Tile tile) {
		return Objects.equals(this.steps.getLast().tile(), tile);
	}
	
	Route copy() {
		return new Route(new ArrayList<>(steps), heading);
	}
	
	static Route findWithLowestScore(List<Route> routes) {
		return routes.stream()
				.min(Comparator.comparing(Route::getScore)).orElseThrow();
	}
}
