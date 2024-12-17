package aoc2024.day17;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class CPU {
	
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
	
	String executeProgram(String program) {
		System.out.println("Executing program: " + program);
		String[] byteCode = program.split(",");
		do {
			Opcode opcode = Opcode.of(byteCode[ip]);
			int operand = getComboOperand(byteCode[ip + 1]);
			printState(program, opcode, operand);
			executeInstruction(opcode, operand);
			ip += 2;
		} while (ip < byteCode.length);
		System.out.println("CPU halted");
		return output.stream()
				.map(String::valueOf)
				.collect(Collectors.joining(","));
	}
	
	private void printState(String program, Opcode opcode, int operand) {
		System.out.print(new StringBuilder("Program = ").append(program)
				.append(",  A=").append(a)
				.append(",  B=").append(b)
				.append(",  C=").append(c)
				.append(",  IP=").append(ip)
				.append(",  Output=").append(output)
				.append(System.lineSeparator())
				.repeat(" ", 10 + ip * 2)
				.append("^  ").append(opcode).append(" ").append(operand).append(": "));
	}
	
	private int getComboOperand(String argument) {
		return switch (argument) {
			case "0" -> 0;
			case "1" -> 1;
			case "2" -> 2;
			case "3" -> 3;
			case "4" -> a;
			case "5" -> b;
			case "6" -> c;
			default -> throw new IllegalArgumentException("Unsupported argument: " + argument);
		};
	}
	
	private void executeInstruction(Opcode opcode, int operand) {
		switch (opcode) {
			case Opcode.adv -> adv(operand);
			case Opcode.bxl -> bxl(operand);
			case Opcode.bst -> bst(operand);
			case Opcode.jnz -> jnz(operand);
			case Opcode.bxc -> bxc(operand);
			case Opcode.out -> out(operand);
			case Opcode.bdv -> bdv(operand);
			case Opcode.cdv -> cdv(operand);
		}
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
	
	private void bxc(int ignored) {
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
	
	private enum Opcode {
		adv("0"),
		bxl("1"),
		bst("2"),
		jnz("3"),
		bxc("4"),
		out("5"),
		bdv("6"),
		cdv("7");
		
		private final String opcode;
		
		Opcode(String code) {
			opcode = code;
		}
		
		static Opcode of(String code) {
			for (Opcode opcode : Opcode.values()) {
				if (opcode.opcode.equals(code)) {
					return opcode;
				}
			}
			throw new IllegalArgumentException("Unsupported opcode byte: " + code);
		}
	}
}
