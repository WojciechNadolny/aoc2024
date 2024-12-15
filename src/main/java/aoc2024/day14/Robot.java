package aoc2024.day14;

class Robot {
	
	final int id;
	int positionX;
	int positionY;
	int velocityX;
	int velocityY;
	
	Robot(int id, int positionX, int positionY, int velocityX, int velocityY) {
		this.id = id;
		this.positionX = positionX;
		this.positionY = positionY;
		this.velocityX = velocityX;
		this.velocityY = velocityY;
	}
	
	@Override
	public String toString() {
		return String.format("Robot %d: p=(%d, %d), v=(%d, %d)", id, positionX, positionY, velocityX, velocityY);
	}
}
