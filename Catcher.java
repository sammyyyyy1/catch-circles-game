import java.awt.*; //imports
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;


public class Catcher extends JFrame implements KeyListener { //Class extends Frame and implements KeyListener
	private static int WINDOWWIDTH = 500; //Declare variables
	private static int WINDOWHEIGHT = 500;
	private static int CATCHERWIDTH = 75;
	private static int CATCHERHEIGHT = 10;
	private int xPosition;
	private int yPosition = 450;
	private double catcherSpeed;
	private int diff;
	private ArrayList<Circle> cr; //An ArrayList of circle objects
	private int score;
	private String showScore;
	private static boolean left = false, right = false; //Boolean variables for the movement of the Catcher
	private int highScore;
	private String showHighScore = "High Score: 0";
	private String showCombo = "0";
	private int lost; //Number of Circles not caught
	private int numCircles; //Number of total circles in this level
	private int caught;
	private int combo;
	private Image doubleImage; //Image doubleImage and Graphics doubleGraphics for the prevention of flickering
    private Graphics doubleGraphics;
    private static Clip music; //Music file
    private boolean gameEnd;
    
    //Precondition: xPos is in the window, sound is the name of a file in the folder
    //Postcondition: instantiates a Catcher object of type JFrame, initializes fields, creates KeyListener
	public Catcher (int xPos, int difficulty, int hScore, String sound) {
		super(sound);
		xPosition = xPos-37; //Positions the catcher to be in the middle of the screen
		cr = new ArrayList<Circle>();
		diff = difficulty;
		generateCircle(); //Call method to initialize cr
		score = 0;
		caught = 0;
		combo = 0;
		highScore = hScore;
		lost = 0;
		gameEnd = false;
		catcherSpeed = 5 + difficulty; //the catcherSpeed is increased with the difficulty
		left = false; right = false;
		playSound(sound); //Start the music
		addKeyListener(this); //Add the KeyListener for this object
	}

	//Precondition:
	//Postcondition: initializes ArrayList<Circle> cr in Catcher object
	public void generateCircle() {
		cr.add(new Circle((int)((Math.random()*400)+50), 0, 1+diff)); //Instantiate one circle object before the other ones
		if (diff == 1) { //Depending on the song (different lengths), decide how many circles to fit in the ArrayList
			numCircles = 215;
		}
		else if (diff == 2) {
			numCircles = 255;
		}
		else {
			numCircles = 230;
		}
		for (int i = 1; i < numCircles; i++) { //Instantiate a numCircles amount of Circle objects
			int tmpX = cr.get(i-1).getXPos();
			int xPos = generateCircleX(tmpX-200, tmpX+200); //Call method to generate a random integer for xPos
			if (xPos > 400) { //Update xPos so that it fits within the play area
				xPos = 400;
			}
			else if (xPos < 50) {
				xPos = 50;
			}
			cr.add(new Circle(xPos, i*50/diff, 1+diff)); //Instantiate Circle object
		}
	}
	
	//Precondition: min < max
	//Postcondition: return a random integer between min and max
	public int generateCircleX(int min, int max) {
		return (int)(Math.random()*(max-min)+min);
	}
	
	//Precondition: method is called with paintComponent is overloaded
	//Postcondition: paints Catcher and Circle objects, and updates their position and other variables
	public void paintComponent(Graphics g) {
		g.clearRect(0, 0, WINDOWWIDTH, WINDOWHEIGHT); //Clear the frame
		movement(); //Call method to update the position of the catcher
		g.setColor(Color.white); //Paint white rectangle
		g.fillRect(xPosition, yPosition, CATCHERWIDTH, CATCHERHEIGHT);
		for (int i = 0; i < cr.size(); i++) { //Loop for every object in cr
			if(i % 4 == 0) { //Circle objects will alternate colors
		        g.setColor(Color.red);
		    }
			else if (i % 3 == 0) {
				g.setColor(Color.blue);
			}
	    	else if (i % 2 == 0) {
		        g.setColor(Color.green);
		    }
	    	else {
	    		g.setColor(Color.yellow);
	    	}
			Circle circle = cr.get(i); //Create a temporary Circle object with identical fields to cr.get(i) in order to check its status
			if (!circle.caught(circle, getXPos(), getYPos())) { //Call Circle.caught(Circle circle, int x, int y) to see if the Circle has not yet touched the catcher
				g.drawOval(circle.getXPos(), circle.getYPos(), 50, 50); //If the Circle was not caught, paint it
				circle.update(); //Update the position of the Circle by calling Circle.update()
				if (circle.lost(circle)) { //Call Circle.lost(Circle circle) to see if the y value has fallen past the play area
					cr.remove(i); //If so, remove it from the ArrayList and update lost
					lost++;
					combo = 0; //Reset combo
				}
			}
			else { //If the object was caught, remove it from the ArrayList and update score
				cr.remove(i);
				score += 1+combo/3; //The score increases more with a bigger combo
				combo++;
				caught++;
			}
		}
		for (int i = 0; i < lost; i++) { //Paint X's for each circle lost
			drawString(g, "X", 450-(i*25), 50);
		}
		updateScore(); //Call method updateScore()
		drawString(g, showScore, 25, 50);
		drawString(g, showHighScore, 25, 75);
		drawString(g, showCombo, getXPos()+25-((showCombo.length()-1)*7), 400);
		pause(7); //Each frame is 7 milliseconds apart
        repaint();
        if (cr.size()==0 || lost==5) { //update gameEnd so that the next call of the overloaded paint will call this method if gameEnd is false
    		gameEnd = true;
    		
        }
	}
	
	//Precondition: method is called when paint is overloaded
	//Postcondition: paints relevant components and disposes if the game is over
	public void paint(Graphics g) {
		if (!gameEnd) { //Only call paintComponent if the game is not ended
	        doubleImage = createImage(getWidth(), getHeight());
	        doubleGraphics = doubleImage.getGraphics();
	        paintComponent(doubleGraphics);
	        g.drawImage(doubleImage, 0, 0, this);
		}
		else { //If the game has ended, print an ending message depending on how many circles were missed
			if (lost == 5) { 
				drawString(g, "You failed!", 325, 75);
			}
			else if (lost >= 1) {
				drawString(g, "Well done!", 325, 75);
			}
			else {
				drawString(g, "Perfect!", 365, 50);
			}
			pause(2000); //Wait 2 seconds, then stop the music, update the buttons in Main, and dispose
			music.stop();
			Main.enableButtons();
			dispose();
		}
    }
	
	//Precondition:
	//Postcondition: updates the showScore and showHighScore so that the overloaded print will show the player the new score
	public void updateScore() {
		showScore = "Score: " + String.valueOf(score);
		if (score > highScore) {
			highScore = score;
			Main.setHighScore(highScore, getDiff()); //Call method in Main so that the local variable in Main is updated
		}
		showHighScore = "High Score: " + String.valueOf(highScore);
		showCombo = String.valueOf(combo);
	}
	
	//Precondition: text is a valid String
	//Postcondition: draw a String with white color, font Verdana, bolded, and size 24
	public void drawString(Graphics g, String text, int x, int y) {
		g.setColor(Color.WHITE);
		g.setFont(new Font("Verdana", Font.BOLD, 24));
		for (String line:text.split("\n")) {
			g.drawString(line, x, y += g.getFontMetrics().getHeight());
		}
	}
	
	//Precondition:
	//Postcondition: updates the xPosition so that the overloaded print will move the catcher rectangle
	public void movement() {
		if(xPosition < 0) { //Keep rectangle from going off screen to the left
			xPosition = 1;
		}
		else if(xPosition > WINDOWWIDTH-CATCHERWIDTH) { //Keep rectangle from going off screen to the right
			xPosition = WINDOWWIDTH-CATCHERWIDTH;
		}
		else { //Move rectangle to the left or right by the catcherSpeed
			if (left) {
				xPosition -= catcherSpeed;
			}
			else if (right) {
				xPosition += catcherSpeed;
			}
		}	
	}

	//Precondition: sound is a valid file name
	//Postcondition: plays the specified song
	public static void playSound(String sound) {
		try {
			File file = new File(sound + ".wav");
			music = AudioSystem.getClip();
			music.open(AudioSystem.getAudioInputStream(file));
			music.start();
		}
		catch (Exception e) {
		}
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

	//Precondition: highScore is initialized
	//Postcondition: returns highScore
	public int getHighScore() {
		return highScore;
	}
	
	//Precondition: diff is initialized
	//Postcondition: returns diff
	public int getDiff() {
		return diff;
	}
	
	//Precondition: gameEnd is initialized
	//Postcondition: returns gameEnd
	public boolean getGameEnd() {
		return gameEnd;
	}
	
	//Precondition: score and numCircles are initialized
	//Postcondition: returns true if the player has passed the level, hence (score+4) accounts for the possible misses
	//				 returns false if player has failed or has not yet completed the level
	public boolean getWin() {
		return caught+4 >= numCircles;
	}
	
	//Precondition: cr is initialized
	//Postcondition: returns cr
	public ArrayList<Circle> getCircles() {
		return cr;
	}
	
	
	//Precondition: a key is typed
	//Postcondition:
	public void keyTyped(KeyEvent e) {
	}

	//Precondition: a key is pressed
	//Postcondition: update left or right so that the next call of movement() will update the catcher rectangle
	public void keyPressed(KeyEvent e) {
		if (e.getKeyChar() == 'a') {
			left = true;
		}
		else if (e.getKeyChar() == 'd') {
			right = true;
		}
	}

	//Precondition: a key is released
	//PostconditionL update left or right so that the next call of movement() will update the catcher rectangle
	public void keyReleased(KeyEvent e) {
		if (e.getKeyChar() == 'a') {
			left = false;
		}
		else if (e.getKeyChar() == 'd') {
			right = false;
		}
	}
	
	//Precondition: time > 0
	//Postcondition: pauses for time milliseconds
	public void pause(int time) {
		try {
			Thread.sleep(time);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}	
	}
	
	//Precondition: frame is initialized
	//Postcondition: sets attributes of frame
	public void run(Catcher frame) {
		frame.setSize(WINDOWWIDTH, WINDOWHEIGHT); //Set frame size
		frame.setLocationRelativeTo(null); //Center frame on screen
		frame.setResizable(false); //Impossible to change size of frame
		frame.setVisible(true); //Make frame visible
		frame.addWindowListener(new WindowAdapter() { //Frame can be closed, stop the music, update the buttons in Main, and dispose
			public void windowClosing(WindowEvent e) {
				gameEnd = true;
				music.stop();
				Main.enableButtons();
				dispose();
			}
		});
		frame.setBackground(Color.black); //Make background color black
	}
}