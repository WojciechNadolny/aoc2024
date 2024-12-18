package aoc2024.day17;

import java.util.ArrayList;
import java.util.List;

class CPU {
	
	private static final String[] MNEMONIC = new String[] {"adv", "bxl", "bst", "jnz", "bxc", "out", "bdv", "cdv"};
	
	private int a;
	private int b;
	private int c;
	private int ip;
	private final List<Integer> output = new ArrayList<>();
	
	void setA(int a) {
		System.out.println("Register A set to " + (this.a = a));
	}
	
	void setB(int b) {
		System.out.println("Register B set to " + (this.b = b));
	}
	
	void setC(int c) {
		System.out.println("Register C set to " + (this.c = c));
	}
	
	private void setIP(int ip) {
		this.ip = ip - 2;
		System.out.println("Instruction pointer set to " + ip);
	}
	
	int getA() {
		return a;
	}
	
	int getB() {
		return b;
	}
	
	int getC() {
		return c;
	}
	
	List<Integer> execute(int[] bytecode) {
		do {
			int opcode = bytecode[ip];
			int operand = bytecode[ip + 1];
			printState(bytecode, opcode, operand);
			executeInstruction(opcode, operand);
			ip += 2;
		} while (ip < bytecode.length);
		return output;
	}
	
	private void executeInstruction(int opcode, int operand) {
		switch (opcode) {
			case 0 -> adv(getCombo(operand));
			case 1 -> bxl(operand);
			case 2 -> bst(getCombo(operand));
			case 3 -> jnz(operand);
			case 4 -> bxc();
			case 5 -> out(getCombo(operand));
			case 6 -> bdv(getCombo(operand));
			case 7 -> cdv(getCombo(operand));
		}
	}
	
	private int getCombo(int argument) {
		return switch (argument) {
			case 0, 1, 2, 3 -> argument;
			case 4 -> a;
			case 5 -> b;
			case 6 -> c;
			default -> throw new IllegalArgumentException("Unsupported argument: " + argument);
		};
	}
	
	private void adv(int operand) {
		int result = a / (int) Math.pow(2, operand);
		System.out.format("%d / (2 ^ %d) = %d   ", a, operand, result);
		setA(result);
	}
	
	private void bxl(int operand) {
		int result = b ^ operand;
		System.out.format("%d XOR %d = %d   ", b, operand, result);
		setB(result);
	}
	
	private void bst(int operand) {
		int result = operand % 8;
		System.out.format("%d %% 8 = %d   ", operand, result);
		setB(result);
	}
	
	private void jnz(int operand) {
		if (a != 0) {
			setIP(operand);
		} else {
			System.out.println("Instruction pointer left unchanged");
		}
	}
	
	private void bxc() {
		int result = b ^ c;
		System.out.format("%d XOR %d = %d   ", b, c, result);
		setB(result);
	}
	
	private void out(int operand) {
		int result = operand % 8;
		output.add(result);
		System.out.format("%d %% 8 = %d   Appended %d to output%n", operand, result, result);
	}
	
	private void bdv(int operand) {
		int result = a / (int) Math.pow(2, operand);
		System.out.format("%d / (2 ^ %d) = %d   ", a, operand, result);
		setB(result);
	}
	
	private void cdv(int operand) {
		int result = a / (int) Math.pow(2, operand);
		System.out.format("%d / (2 ^ %d) = %d   ", a, operand, result);
		setC(result);
	}
	
	private void printState(int[] bytecode, int opcode, int operand) {
		String mnemonic = opcode >= 0 && opcode < MNEMONIC.length ? MNEMONIC[opcode] : "???";
		StringBuilder debuggerOutput = new StringBuilder("Bytecode =  ").append(bytecode[0]);
		for (int i = 1; i < bytecode.length; i++) {
			debuggerOutput.append(",").append(bytecode[i]);
		}
		debuggerOutput.append(" ");
		debuggerOutput.setCharAt(10 + ip * 2, '[');
		debuggerOutput.setCharAt(14 + ip * 2, ']');
		debuggerOutput
				.append(",  A=").append(a)
				.append(",  B=").append(b)
				.append(",  C=").append(c)
				.append(",  IP=").append(ip)
				.append(",  Output=").append(output)
				.append(System.lineSeparator())
				.repeat(" ", 11 + ip * 2)
				.append("^  ").append(mnemonic).append(" ").append(operand).append(": ");
		System.out.print(debuggerOutput);
	}
}
