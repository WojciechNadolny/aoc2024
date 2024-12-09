package aoc2024.day09;

import aoc2024.DayResolver;
import aoc2024.util.PuzzleInputLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Day09Resolver implements DayResolver<Long> {
	
	private List<Integer> diskBlocks;
	
	public static void main(String[] args) {
		Day09Resolver resolver = new Day09Resolver();
		List<String> personalSecretPuzzleInput = PuzzleInputLoader.loadPersonalSecretPuzzleInput(9);
		
		System.out.println("***  Part 1: Free space defragmentation  ***");
		System.out.println("Defragmented disk checksum: " + resolver.resolvePart1(personalSecretPuzzleInput));
		
		System.out.println("***  Part 2: Files defragmentation  ***");
		System.out.println("Defragmented disk checksum: " + resolver.resolvePart2(personalSecretPuzzleInput));
	}
	
	public Long resolvePart1(List<String> puzzleInputLines) {
		parseDiskMap(puzzleInputLines.getFirst());
		defragmentFreeSpace();
		return computeChecksum();
	}
	
	
	public Long resolvePart2(List<String> puzzleInputLines) {
		parseDiskMap(puzzleInputLines.getFirst());
		moveDefragmentedFilesCloseToBeginning();
		return computeChecksum();
	}
	
	private void parseDiskMap(String diskMap) {
		diskBlocks = new ArrayList<>();
		int fileId = 0;
		boolean occupied = true;
		for (char blocksCountChar : diskMap.toCharArray()) {
			int blocksCount = blocksCountChar - '0';
			if (occupied) {
				for (int fileBlock = 0; fileBlock < blocksCount; fileBlock++) {
					diskBlocks.add(fileId);
				}
				fileId++;
			} else {
				for (int fileBlock = 0; fileBlock < blocksCount; fileBlock++) {
					diskBlocks.add(null);
				}
			}
			occupied = !occupied;
		}
		int filesCount = fileId;
		long occupiedBlocks = diskBlocks.stream()
				.filter(Objects::nonNull)
				.count();
		System.out.format("%d files occupy %d of %d disk blocks%n", filesCount, occupiedBlocks, diskBlocks.size());
	}
	
	private void defragmentFreeSpace() {
		int sourceIndex = diskBlocks.size() - 1;
		int destinationIndex = 0;
		while (sourceIndex > destinationIndex) {
			Integer fileIdAtSourceBlock = diskBlocks.get(sourceIndex);
			Integer fileIdAtDestinationBlock = diskBlocks.get(destinationIndex);
			
			// Seek rightmost file block
			if (fileIdAtSourceBlock == null) {
				sourceIndex--;
				continue;
			}
			
			// Seek leftmost free block
			if (fileIdAtDestinationBlock != null) {
				destinationIndex++;
				continue;
			}
			
			// Move file block to free block by exchange
			diskBlocks.set(destinationIndex, fileIdAtSourceBlock);
			diskBlocks.set(sourceIndex, null);
		}
		System.out.println("Done free space defragmentation");
	}
	
	private void moveDefragmentedFilesCloseToBeginning() {
		int sourceEndIndex = diskBlocks.size() - 1;
		while (sourceEndIndex > 0) {
			// Seek end of rightmost file
			Integer fileIdAtSourceBlock = diskBlocks.get(sourceEndIndex);
			if (fileIdAtSourceBlock == null) {
				sourceEndIndex--;
				continue;
			}
			
			// Seek beginning of rightmost file
			int sourceStartIndex = sourceEndIndex;
			while (sourceStartIndex > 0 && Objects.equals(diskBlocks.get(sourceStartIndex - 1), fileIdAtSourceBlock)) {
				sourceStartIndex--;
			}
			
			// Seek leftmost span of free space to fit the file
			int currentFileSize = sourceEndIndex - sourceStartIndex + 1;
			final int fileStartIndex = sourceStartIndex;
			Optional<Integer> gapStartIndexOptional = findLeftMostGapOfSizeBefore(currentFileSize, sourceStartIndex);
			
			// Move whole file if such free space was found
			gapStartIndexOptional.ifPresent(integer -> moveFile(fileIdAtSourceBlock, currentFileSize, fileStartIndex, integer));
			
			// Continue with file to the left
			sourceEndIndex = sourceStartIndex - 1;
		}
		System.out.println("Done files defragmentation");
	}
	
	private Optional<Integer> findLeftMostGapOfSizeBefore(int targetGapSize, int beforeBlockIndex) {
		int destinationIndex = 0;
		int currentGapSize = 0;
		while (currentGapSize < targetGapSize && destinationIndex < beforeBlockIndex) {
			if (diskBlocks.get(destinationIndex) == null) {
				currentGapSize++;
			} else {
				currentGapSize = 0;
			}
			destinationIndex++;
		}
		return Optional.ofNullable(currentGapSize == targetGapSize
				? (destinationIndex - currentGapSize)
				: null);
	}
	
	private void moveFile(int fileId, int fileSize, int fileStartIndex, int gapStartIndex) {
		for (int block = 0; block < fileSize; block++) {
			diskBlocks.set(gapStartIndex + block, fileId);
			diskBlocks.set(fileStartIndex + block, null);
		}
	}
	
	private long computeChecksum() {
		long checksum = 0;
		for (int blockIndex = 0; blockIndex < diskBlocks.size(); blockIndex++) {
			Integer fileIdAtBlockIndex = diskBlocks.get(blockIndex);
			if (fileIdAtBlockIndex != null) {
				checksum += (long) blockIndex * diskBlocks.get(blockIndex);
			}
		}
		return checksum;
	}
}
