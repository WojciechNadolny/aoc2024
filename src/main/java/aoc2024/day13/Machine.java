package aoc2024.day13;

class Machine {
	
	private final int id;
	private final Button buttonA;
	private final Button buttonB;
	private final Position prize;
	private final Position clawPosition;
	private long tokensUsed;
	
	Machine(int id, Button buttonA, Button buttonB, Position prize) {
		this.id = id;
		this.buttonA = buttonA;
		this.buttonB = buttonB;
		this.prize = prize;
		this.clawPosition = new Position(0,0);
		this.tokensUsed = 0;
	}
	
	void pressButtonA(long times) {
		clawPosition
				.addX(buttonA.advance().getX() * times)
				.addY(buttonA.advance().getY() * times);
		tokensUsed += (long) buttonA.tokensCost() * times;
	}
	
	void pressButtonB() {
		clawPosition.add(buttonB.advance());
		tokensUsed += buttonB.tokensCost();
	}
	
	Button getButtonA() {
		return buttonA;
	}
	
	Button getButtonB() {
		return buttonB;
	}
	
	Position getPrize() {
		return prize;
	}
	
	boolean won() {
		return clawPosition.compareTo(prize) == 0;
	}
	
	boolean lost() {
		return clawPosition.compareTo(prize) > 0;
	}
	
	int getId() {
		return id;
	}
	
	long getTokensUsed() {
		return tokensUsed;
	}
	
	void reset() {
		clawPosition.reset();
		tokensUsed = 0;
	}
}
