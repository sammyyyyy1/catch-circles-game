
public class Circle { //Class creates the Circle object
	private static int WINDOWHEIGHT = 500; //Declare variables
	private int xPosition;
	private int yPosition;
	private int circleTime;
  	private double circleSpeed;
  	
  	//Precondition: xPos, time, and speed have valid values
  	//Postcondition: instantiates a Circle object
  	public Circle (int xPos, int time, double speed) { //Constructor method
  		xPosition = xPos;
  		yPosition = (int)(0 - (time*speed)); //Set the y position of the circle to some negative value so that it falls until it reaches the play area
  		circleTime = time;
  		circleSpeed = speed;
  	}

	//Precondition: xPosition is initialized
	//Postcondition: returns xPosition
  	public int getXPos() {
  		return xPosition;
  	}

	//Precondition: yPosition is initialized
	//Postcondition: returns yPosition
  	public int getYPos() {
  		return yPosition;
  	}

	//Precondition: circleTime is initialized
	//Postcondition: returns circleTime
  	public int getTime() {
  		return circleTime;
  	}
  	
	//Precondition: circleSpeed is initialized
	//Postcondition: returns circleSpeed
  	public double getSpeed() {
  		return circleSpeed;
  	}  
  
	//Precondition: yPosition is initialized
	//Postcondition: returns yPosition
  	public void update() {
  		if (yPosition <= WINDOWHEIGHT) {
  			yPosition += circleSpeed;
  		}
  	}

  	//Precondition: circle is initialized
  	//Postcondition: return true if the Circle touches x and if the Circle touches y
  	//				otherwise return false
	public boolean caught(Circle circle, int x, int y) {
		if ((getXPos() >= x || getXPos()+50 >= x) && getXPos() <= x+75 && (getYPos()+50) >= y && getYPos() <= y+10) {
			return true;
		}
		return false;
	}
	
	//Precondition: circle is initialized
	//Postcondition: return true if the Circle's y position has passed the height of the window and thus cannot be caught
	//Precondition: return false if the Circle's y position has not passed the height of the window and thus can still be caught
	public boolean lost(Circle circle) {
		return getYPos() >= WINDOWHEIGHT;
	}
}
