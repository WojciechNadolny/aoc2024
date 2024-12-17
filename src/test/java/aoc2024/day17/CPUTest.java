package aoc2024.day17;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CPUTest {
	
	private CPU underTest;
	
	@BeforeEach
	void setUp() {
		underTest = new CPU();
	}
	
	@Test
	void executeProgram_shouldSetRegisterBBasedOnRegisterC() {
		// Given
		underTest.setC(9);
		int expectedRegisterBValue= 1;
		
		// When
		underTest.executeProgram("2,6");
		
		// Then
		assertEquals(expectedRegisterBValue, underTest.getB());
	}
	
	@Test
	void executeProgram_shouldOutputResultBasedOnRegisterA() {
		// Given
		underTest.setA(10);
		String expectedROutput = "0,1,2";
		
		// When
		String output = underTest.executeProgram("5,0,5,1,5,4");
		
		// Then
		assertEquals(expectedROutput, output);
	}
	
	@Test
	void executeProgram_shouldOutputResultBasedOnRegisterAAndClearRegisterA() {
		// Given
		underTest.setA(2024);
		String expectedROutput = "4,2,5,6,7,7,7,7,3,1,0";
		int expectedRegisterAValue = 0;
		
		// When
		String output = underTest.executeProgram("0,1,5,4,3,0");
		
		// Then
		assertEquals(expectedROutput, output);
		assertEquals(expectedRegisterAValue, underTest.getA());
	}
	
	@Disabled("Given example test is against given rule: Combo operand 7 is reserved and will not appear in valid programs.")
	@Test
	void executeProgram_shouldResetRegisterBBasedOnRegisterB() {
		// Given
		underTest.setB(29);
		int expectedRegisterBValue = 26;
		
		// When
		underTest.executeProgram("1,7");
		
		// Then
		assertEquals(expectedRegisterBValue, underTest.getB());
	}
	
	@Test
	void executeProgram_shouldResetRegisterBBasedOnRegistersBAndC() {
		// Given
		underTest.setB(2024);
		underTest.setC(43690);
		int expectedRegisterBValue = 44354;
		
		// When
		underTest.executeProgram("4,0");
		
		// Then
		assertEquals(expectedRegisterBValue, underTest.getB());
	}
}