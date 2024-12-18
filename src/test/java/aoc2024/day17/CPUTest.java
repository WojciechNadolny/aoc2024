package aoc2024.day17;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CPUTest {
	
	private CPU underTest;
	
	@BeforeEach
	void setUp() {
		underTest = new CPU();
	}
	
	@Test
	void execute_shouldSetRegisterBBasedOnRegisterC() {
		// Given
		underTest.setC(9);
		int expectedRegisterBValue= 1;
		
		// When
		underTest.execute(new int[] {2,6});
		
		// Then
		assertEquals(expectedRegisterBValue, underTest.getB());
	}
	
	@Test
	void execute_shouldOutputResultBasedOnRegisterA() {
		// Given
		underTest.setA(10);
		List<Integer> expectedROutput = List.of(0,1,2);
		
		// When
		List<Integer> output = underTest.execute(new int[] {5,0,5,1,5,4});
		
		// Then
		assertEquals(expectedROutput, output);
	}
	
	@Test
	void execute_shouldOutputResultBasedOnRegisterAAndClearRegisterA() {
		// Given
		underTest.setA(2024);
		List<Integer> expectedROutput = List.of(4,2,5,6,7,7,7,7,3,1,0);
		int expectedRegisterAValue = 0;
		
		// When
		List<Integer> output = underTest.execute(new int[] {0,1,5,4,3,0});
		
		// Then
		assertEquals(expectedROutput, output);
		assertEquals(expectedRegisterAValue, underTest.getA());
	}
	
	@Disabled("Given example test is against given rule: Combo operand 7 is reserved and will not appear in valid programs.")
	@Test
	void execute_shouldResetRegisterBBasedOnRegisterB() {
		// Given
		underTest.setB(29);
		int expectedRegisterBValue = 26;
		
		// When
		underTest.execute(new int[] {1,7});
		
		// Then
		assertEquals(expectedRegisterBValue, underTest.getB());
	}
	
	@Test
	void execute_shouldResetRegisterBBasedOnRegistersBAndC() {
		// Given
		underTest.setB(2024);
		underTest.setC(43690);
		int expectedRegisterBValue = 44354;
		
		// When
		underTest.execute(new int[] {4,0});
		
		// Then
		assertEquals(expectedRegisterBValue, underTest.getB());
	}
}