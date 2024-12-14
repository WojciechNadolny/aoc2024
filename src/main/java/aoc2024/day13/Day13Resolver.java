package aoc2024.day13;

import aoc2024.DayResolver;
import aoc2024.util.PuzzleInputLoader;

import java.util.*;

public class Day13Resolver implements DayResolver<Long> {
	
	public static void main(String[] args) {
		Day13Resolver resolver = new Day13Resolver();
		List<String> personalSecretPuzzleInput = PuzzleInputLoader.loadPersonalSecretPuzzleInput(13);
		
		System.out.println("***  Part 1: Claw machines  ***");
		System.out.println("Fewest tokens to spend to win all possible prizes: " + resolver.resolvePart1(personalSecretPuzzleInput));
		
		System.out.println("***  Part 2: Claw machines with 10 quad higher prices  ***");
		System.out.println("Fewest tokens to spend to win all possible prizes: " + resolver.resolvePart2(personalSecretPuzzleInput));
	}
	
	public Long resolvePart1(List<String> puzzleInputLines) {
		List<Machine> machines = parseMachines(puzzleInputLines);
		return machines.stream()
				.map(this::getFewestTokensToSpendToWinPrizeByBruteForce)
				.filter(Objects::nonNull)
				.mapToLong(Long::longValue)
				.sum();
	}
	
	public Long resolvePart2(List<String> puzzleInputLines) {
		List<Machine> machines = parseMachines(puzzleInputLines);
		return machines.stream()
				.peek(machine -> machine.getPrize().addX(10000000000000L).addY(10000000000000L))
				.map(this::getFewestTokensToSpendToWinPrizeByLinearEquations)
				.filter(Objects::nonNull)
				.mapToLong(Long::longValue)
				.sum();
	}
	
	private List<Machine> parseMachines(List<String> puzzleInputLines) {
		List<Machine> machines = new ArrayList<>();
		MachineBuilder machineBuilder = new MachineBuilder();
		for (String line : puzzleInputLines) {
			if (line.isEmpty()) {
				continue;
			}
			String[] typeAndArguments = line.split(": X.");
			String[] arguments = typeAndArguments[1].split(", Y.");
			Position position = new Position(Integer.parseInt(arguments[0]), Integer.parseInt(arguments[1]));
			switch (typeAndArguments[0]) {
				case "Button A" -> machineBuilder.setButtonA(new Button(position, 3));
				case "Button B" -> machineBuilder.setButtonB(new Button(position, 1));
				case "Prize" -> {
					machineBuilder.setPrize(position);
					machines.add(machineBuilder.build());
					machineBuilder.reset();
				}
				default -> throw new IllegalStateException("Unexpected line: " + line);
			}
		}
		return machines;
	}
	
	private Long getFewestTokensToSpendToWinPrizeByBruteForce(Machine machine) {
		Long fewestTokensToSpendToWinPrize = null;
		for (int buttonAPresses = 0; buttonAPresses <= 100; buttonAPresses++) {
			machine.reset();
			machine.pressButtonA(buttonAPresses);
			for (int buttonBPresses = 0; buttonBPresses <= 100; buttonBPresses++) {
				if (machine.lost()) {
					break;
				}
				if (machine.won()) {
					assertAndPrintWin(machine, buttonAPresses, buttonBPresses);
					if (fewestTokensToSpendToWinPrize == null || fewestTokensToSpendToWinPrize > machine.getTokensUsed()) {
						fewestTokensToSpendToWinPrize = machine.getTokensUsed();
					}
					break;
				}
				machine.pressButtonB();
			}
		}
		
		System.out.format("Machine %d: The prize %s%n", machine.getId(), fewestTokensToSpendToWinPrize == null
				? "cannot be achieved"
				: "takes " + fewestTokensToSpendToWinPrize + " tokens");
		
		return fewestTokensToSpendToWinPrize;
	}
	
	/*
	 * Now that was a lot of fun and is worth a comment since it needed some math computation rather than programming.
	 * (sidenote: Friday, Dec 13th, "w grudniu po południu". What could go wrong?)
	 * By such high numbers brute forcing by executing huge number of additions would take vast amount of time
	 * despite low memory and operations complexity requirements. Of course this can be solved in many other ways.
	 * For clarity, let's start of modelling the situation in monospace:
	 *
	 *  ^                                 P
	 *  |              .             A
	 *  |                       A      B
	 *  |           .      A
	 *  |             A             B      .
	 *  |        B                    .
	 *  |   .                    A
	 * .|     B             A    :
	 *  |              A      .  :
	 *  |  B      A              :
	 *  |    A             .     :
	 *  +--------------------------------->
	 *
	 * Out of all possible sequences to achieve known prize coordinates (P) up from (0,0), each has same number of
	 * button A presses and button B presses. In the example above, starting of 5 button A presses and then 3 button B
	 * presses have drawn bottom edge of an area. Doing the opposite has drawn upper edge of that area. Mixing buttons'
	 * presses would give solutions to mark anywhere within the area and giving the same result in terms of tokens cost.
	 * Thus, sequence of pressing them doesn't really matter as long as offsets differ between buttons and are positive.
	 * Having analyzed the input, that's fortunately the case.
	 *
	 * Resulting 4 edges can be described by linear functions below. First two of them are simple as they start at (0,0)
	 * unlike the latter two that start at prize coordinates. The dots mark function lines exceeding area edges.
	 *
	 * bottom: f(x) = x * (buttonA.advanceY / buttonA.advanceX)
	 * left: f(x) = x * (buttonB.advanceY / buttonB.advanceX)
	 * top: f(x) = prize.y + (x - prize.x) * (buttonA.advanceY / buttonA.advanceX)
	 * right: f(x) = prize.y + (x - prize.x) * (buttonB.advanceY / buttonB.advanceX)
	 *
	 * At this point it's sufficient to find coordinates of bottom right corner of solution area (where we need to stop
	 * pressing button A and start pressing button B) by settling intercept between functions describing adjacent edges.
	 * That's a nice opportunity to recall equations rearrangements from high school and find both x and y of the
	 * intercept that way.
	 *
	 *      y = x * (buttonA.advanceY / buttonA.advanceX)
	 *      y = prize.y + (x - prize.x) * (buttonB.advanceY / buttonB.advanceX)
	 *                                          |
	 *                                          |  1. Substitute y in second equation with its counterpart from first one
	 *                                          V
	 *      x * (buttonA.advanceY / buttonA.advanceX) = prize.y + (x - prize.x) * (buttonB.advanceY / buttonB.advanceX)
	 *                                          |
	 *                                          |  2. Extract x from parenthesis at the right side of resulting equation
	 *                                          V
	 *      x * (buttonA.advanceY / buttonA.advanceX) = prize.y + x * (buttonB.advanceY / buttonB.advanceX) - prize.x * (buttonB.advanceY / buttonB.advanceX)
	 *                                          |
	 *                                          |  3. Move both x to the left side
	 *                                          V
	 *      x * (buttonA.advanceY / buttonA.advanceX) - x * (buttonB.advanceY / buttonB.advanceX) = prize.y - prize.x * (buttonB.advanceY / buttonB.advanceX)
	 *                                          |
	 *                                          |  4. Merge factors of multiplications with x
	 *                                          V
	 *      x * (buttonA.advanceY / buttonA.advanceX - buttonB.advanceY / buttonB.advanceX) = prize.y - prize.x * (buttonB.advanceY / buttonB.advanceX)
	 *                                          |
	 *                                          |  5. Divide both sides by resulting factor to get plain x value
	 *                                          V
	 *      x = (prize.y - prize.x * (buttonB.advanceY / buttonB.advanceX)) / (buttonA.advanceY / buttonA.advanceX - buttonB.advanceY / buttonB.advanceX)
	 *
	 * Hopefully I got it right :-D   (Update: My bad, result is rejected. This algorithm solves part 1 perfectly though...)
	 * If so, it feels like the perfect time to substitute variables with their known values. Winning the prize is only
	 * possible if resulting X-coordinate is integer without reminder. Y-coordinate is now easier to compute by
	 * applying known X-coordinate into first equation.
	 *
	 * Next step is to compute presses count of each button.
	 *      buttonAPresses = x / buttonA.advanceX
	 *      buttonBPresses = y / buttonB.advanceY
	 *
	 * And finally they can be converted to tokens cost same way as in part one.
	 */
	private Long getFewestTokensToSpendToWinPrizeByLinearEquations(Machine machine) {
		assert(machine.getButtonA().advance().getX() != machine.getButtonB().advance().getX());
		assert(machine.getButtonA().advance().getY() != machine.getButtonB().advance().getY());
		double buttonAAdvanceRatioYToX = (double) machine.getButtonA().advance().getY() / machine.getButtonA().advance().getX();
		double buttonBAdvanceRatioYToX = (double) machine.getButtonB().advance().getY() / machine.getButtonB().advance().getX();
		double interceptX = (machine.getPrize().getY() - machine.getPrize().getX() * buttonBAdvanceRatioYToX)
				/ (buttonAAdvanceRatioYToX - buttonBAdvanceRatioYToX);
		double interceptY = interceptX * buttonAAdvanceRatioYToX;
		Position intercept = new Position((long) interceptX, (long) interceptY);
		
		if (interceptX - intercept.getX() < 0.00000001 && interceptY - intercept.getY() < 0.00000001) {
			long buttonAPressesCount = (long) interceptX / machine.getButtonA().advance().getX();
			long buttonBPressesCount = (machine.getPrize().getY() - (long) interceptY) / machine.getButtonB().advance().getY();
			assertAndPrintWin(machine, buttonAPressesCount, buttonBPressesCount);
			return buttonAPressesCount * machine.getButtonA().tokensCost()
					+ buttonBPressesCount * machine.getButtonB().tokensCost();
		} else {
			System.out.format("On machine %d it would be best to switch from pressing button A to B at claw coordinates (%f,%f) " +
							"but apparently such coordinates are not both integers.%n",
					machine.getId(), interceptX, interceptY);
			return null;
		}
	}
	
	private void assertAndPrintWin(Machine machine, long buttonAPressesCount, long buttonBPressesCount) {
		assert machine.getPrize().getX() == buttonAPressesCount * machine.getButtonA().advance().getX()
				+ buttonBPressesCount * machine.getButtonB().advance().getX()
				: String.format("Machine %d claw calibration error: %d != %d*%d + %d*%d == %d",
						machine.getId(), machine.getPrize().getX(),
						buttonAPressesCount, machine.getButtonA().advance().getX(),
						buttonBPressesCount, machine.getButtonB().advance().getX(),
						buttonAPressesCount * machine.getButtonA().advance().getX()
								+ buttonBPressesCount * machine.getButtonB().advance().getX());
		assert machine.getPrize().getY() == buttonAPressesCount * machine.getButtonA().advance().getY()
				+ buttonBPressesCount * machine.getButtonB().advance().getY()
				: String.format("Machine %d claw calibration error: %d != %d*%d + %d*%d == %d",
						machine.getId(), machine.getPrize().getX(),
						buttonAPressesCount, machine.getButtonA().advance().getY(),
						buttonBPressesCount, machine.getButtonB().advance().getY(),
						buttonAPressesCount * machine.getButtonA().advance().getY()
								+ buttonBPressesCount * machine.getButtonB().advance().getY());
		System.out.format("Win at machine %d: X(%d) = A(%d*%d) + B(%d*%d), Y(%d) = A(%d*%d) + B(%d*%d)%n",
				machine.getId(),
				machine.getPrize().getX(),
				buttonAPressesCount, machine.getButtonA().advance().getX(),
				buttonBPressesCount, machine.getButtonB().advance().getX(),
				machine.getPrize().getY(),
				buttonAPressesCount, machine.getButtonA().advance().getY(),
				buttonBPressesCount, machine.getButtonB().advance().getY());
	}
}
