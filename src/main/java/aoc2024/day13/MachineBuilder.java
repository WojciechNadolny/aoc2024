package aoc2024.day13;

public class MachineBuilder {
	
	int id;
	Button buttonA;
	Button buttonB;
	Position prize;
	
	void setButtonA(Button buttonA) {
		assert this.buttonA == null : "Machine cannot have multiple A buttons";
		this.buttonA = buttonA;
	}
	
	void setButtonB(Button buttonB) {
		assert this.buttonB == null : "Machine cannot have multiple B buttons";
		this.buttonB = buttonB;
	}
	
	void setPrize(Position prize) {
		assert this.prize == null : "Machine cannot have multiple prizes";
		this.prize = prize;
	}
	
	public Machine build() {
		assert buttonA != null : "Machine is missing button A";
		assert buttonB != null : "Machine is missing button B";
		assert prize != null : "Machine is missing prize";
		return new Machine(++id, buttonA, buttonB, prize);
	}
	
	public void reset() {
		this.buttonA = null;
		this.buttonB = null;
		this.prize = null;
	}
}
