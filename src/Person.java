public class Person {
	
	Direction direction;
	boolean hasTorch;
	
	//Constructor for initialization
	public Person(){
		this.direction = Direction.Left;
		this.hasTorch = false;
	}
	
	public Person(Direction direction){
		this.direction = direction;
		this.hasTorch = false;
	}
	
	public Direction getDirection(){
		return direction;
	}
	
	public void setDirection(Direction direction){
		this.direction = direction;
	}
	
	public void setHasTorch(boolean hasTorch){
		this.hasTorch = hasTorch;
	}
	
	public boolean getHasTorch(){
		return hasTorch;
	}

}
