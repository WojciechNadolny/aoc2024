package aoc2024.day05;

import aoc2024.DayResolver;
import aoc2024.util.PuzzleInputLoader;

import java.util.*;

public class Day05Resolver implements DayResolver<Integer> {
	
	private final List<List<String>> pageOrderingRules = new ArrayList<>();
	private final List<List<String>> pageNumbers = new ArrayList<>();
	
	public static void main(String[] args) {
		Day05Resolver resolver = new Day05Resolver();
		List<String> personalSecretPuzzleInput = PuzzleInputLoader.loadPersonalSecretPuzzleInput(5);
		
		System.out.println("***  Part 1: Sum of middle page numbers among updates in correct order  ***");
		System.out.println("Middle page numbers sum: " + resolver.resolvePart1(personalSecretPuzzleInput));
		
		System.out.println("***  Part 2: Sum of middle page numbers among updates in incorrect order  ***");
		System.out.println("Middle page numbers sum: " + resolver.resolvePart2(personalSecretPuzzleInput));
	}
	
	public Integer resolvePart1(List<String> puzzleInputLines) {
		parse(puzzleInputLines);
		Map<String, Set<String>> succession = discoverSuccession(pageOrderingRules);
		System.out.println("Page number successors: " + succession);
		
		int middlePageNumbersSum = 0;
		for (List<String> update : pageNumbers) {
			if (hasCorrectOrder(update, succession)) {
				String middlePageNumber = update.get(update.size() / 2);
				middlePageNumbersSum += Integer.parseInt(middlePageNumber);
				System.out.format("Added middle page number: %s from update %s%n", middlePageNumber, update);
			} else {
				System.out.format("Skipped update %s with incorrect order%n", update);
			}
		}
		return middlePageNumbersSum;
	}
	
	public Integer resolvePart2(List<String> puzzleInputLines) {
		parse(puzzleInputLines);
		Map<String, Set<String>> succession = discoverSuccession(pageOrderingRules);
		System.out.println("Page number successors: " + succession);
		
		int middlePageNumbersSum = 0;
		for (List<String> update : pageNumbers) {
			if (!hasCorrectOrder(update, succession)) {
				List<String> ordered = order(update, succession);
				String middlePageNumber = update.get(update.size() / 2);
				middlePageNumbersSum += Integer.parseInt(middlePageNumber);
				System.out.format("Added middle page number: %s from update %s after ordering %s%n", middlePageNumber, ordered, update);
			} else {
				System.out.format("Skipped update %s with correct order%n", update);
			}
		}
		return middlePageNumbersSum;
	}
	
	private void parse(List<String> puzzleInputLines) {
		for (String line : puzzleInputLines) {
			if (line.contains("|")) {
				pageOrderingRules.add(Arrays.asList(line.split("\\|")));
			}
			if (line.contains(",")) {
				pageNumbers.add(Arrays.asList(line.split(",")));
			}
		}
	}
	
	private Map<String, Set<String>> discoverSuccession(List<List<String>> pageOrderingRules) {
		Map<String, Set<String>> succession = new HashMap<>();
		for (List<String> pageOrderingRule : pageOrderingRules) {
			succession.compute(pageOrderingRule.getFirst(), (predecessor, successors) -> {
				successors = Optional.ofNullable(successors).orElseGet(HashSet::new);
				successors.add(pageOrderingRule.getLast());
				return successors;
			});
		}
		return succession;
	}
	
	private boolean hasCorrectOrder(List<String> update, Map<String, Set<String>> succession) {
		for (int updateIndex = 1; updateIndex < update.size(); updateIndex++) {
			String lastPageNumber = update.get(updateIndex - 1);
			if (!succession.containsKey(lastPageNumber) || !succession.get(lastPageNumber).contains(update.get(updateIndex))) {
				System.out.format("Incorrect order in update %s at element[%d] (%s)%n", update, updateIndex, update.get(updateIndex));
				return false;
			}
		}
		return true;
	}
	
	private List<String> order(List<String> update, Map<String, Set<String>> succession) {
		Comparator<String> pageNumbersComparator = (pageNumber1, pageNumber2) ->
				Objects.equals(pageNumber1, pageNumber2)
						? 0
						: Optional.ofNullable(succession.get(pageNumber1))
								.map(successor -> successor.contains(pageNumber2))
								.orElse(false)
								? -1
								: succession.get(pageNumber2).contains(pageNumber1)
										? 1
										: 0;
		
		update.sort(pageNumbersComparator);
		return update;
	}
	
// This method including pages preorder led to cyclic references as full ruling appeared to introduce them
//	public Integer resolvePart1(List<String> puzzleInputLines) {
//		parse(puzzleInputLines);
//		Map<String, Set<String>> before = new HashMap<>();
//		Map<String, Set<String>> after = new HashMap<>();
//		HashSet<String> unordered = new HashSet<>();
//		List<String> ordered = new ArrayList<>();
//		for (List<String> pageOrderingRule : pageOrderingRules) {
//			before.compute(pageOrderingRule.getFirst(), (predecessor, successors) -> merge(pageOrderingRule.getLast(), successors));
//			after.compute(pageOrderingRule.getLast(), (successor, predecessors) -> merge(pageOrderingRule.getFirst(), predecessors));
//			unordered.add(pageOrderingRule.getFirst());
//			unordered.add(pageOrderingRule.getLast());
//		}
//		System.out.println("Page number successors: " + before);
//		System.out.println("Page number predecessors: " + after);
//
//		String first = findFirst(before, after);
//		ordered.add(first);
//		unordered.remove(first);
//
//		String last = first;
//		while (!unordered.isEmpty()) {
//			String next = findNext(last, before, after, ordered)
//					.orElseGet(() -> unordered.iterator().next());
//			ordered.add(next);
//			unordered.remove(next);
//			last = next;
//		}
//		complement(before, after);
//		System.out.println("Ordered page numbers: " + ordered);
//
//		int middlePageNumbersSum = 0;
//		for (List<String> update : pageNumbers) {
//			if (hasCorrectOrder(update, ordered)) {
//				String middlePageNumber = update.get(update.size() / 2);
//				middlePageNumbersSum += Integer.parseInt(middlePageNumber);
//				System.out.println("Added middle page number: " + middlePageNumber + " from update " + update);
//			} else {
//				System.out.println("Skipped update " + update + " with incorrect order");
//			}
//		}
//		return middlePageNumbersSum;
//	}
//
//	private Set<String> merge(String value, Set<String> values) {
//		if (values == null) {
//			values = new HashSet<>();
//		}
//		values.add(value);
//		return values;
//	}
//
//	private String findFirst(Map<String, Set<String>> before, Map<String, Set<String>> after) {
//		return before.keySet().stream()
//				.filter(value -> !after.containsKey(value))
//				.findAny().orElseThrow();
//	}
//
//	private Optional<String> findNext(String nextTo, Map<String, Set<String>> before, Map<String, Set<String>> after, List<String> excluding) {
//		List<String> successors = before.keySet().stream()
//				.filter(predecessor -> !excluding.contains(predecessor))
//				.filter(after::containsKey)
//				.filter(successor -> after.get(successor).contains(nextTo))
//				.toList();
//		System.out.println("Looking for next to " + nextTo + " of " + successors);
//		return successors.stream()
//				.filter(successor -> after.get(successor).stream()
//						.noneMatch(successors::contains))
//				.findAny();
//	}
//
//	private void complement(Map<String, Set<String>> before, Map<String, Set<String>> after) {
//		for (Map.Entry<String, Set<String>> successors : after.entrySet()) {
//			supplement(successors.getValue(), after, 0);
//		}
//		for (Map.Entry<String, Set<String>> predecessors : before.entrySet()) {
//			supplement(predecessors.getValue(), before, 0);
//		}
//	}
//
//	private void supplement(Set<String> knownRelatives, Map<String, Set<String>> totalRelatives, int level) {
//		System.out.format("%s->%s%n", Strings.repeat(" ", level), knownRelatives);
//		for (String relative : knownRelatives) {
//			Optional.ofNullable(totalRelatives.get(relative)).ifPresent(nestedRelatives ->
//					supplement(nestedRelatives, totalRelatives, level + 1));
//		}
//	}
//
//	private boolean hasCorrectOrder(List<String> update, List<String> correctOrder) {
//		int orderIndex = 0;
//		int updateIndex = 0;
//		loop:
//		while (updateIndex < update.size()) {
//			while (orderIndex < correctOrder.size()) {
//				if (update.get(updateIndex).equals(correctOrder.get(orderIndex))) {
//					updateIndex++;
//					orderIndex++;
//					continue loop;
//				}
//				orderIndex++;
//			}
//			return false;
//		}
//		return true;
//	}
}
