/*Samuel Zheng and Minwoo Hwang
June 23, 2021
The program runs a game in which the player moves a 'catcher' to catch falling circles
*/

import javax.swing.*; //imports
import java.awt.*;
import java.awt.event.*;

public class Main extends JFrame implements ActionListener { //Class extends JFrame and implements ActionListener
	private static int WINDOWWIDTH = 500; //Declare variables
	private static int WINDOWHEIGHT = 500;
	private static Main frame;
	private static Container c;
	private static JButton b1, b2, b3;
	private JLabel l1, l2, l3, l4;
	private static Catcher ct;
	private static int highScore1 = 0, highScore2 = 0, highScore3 = 0;
	private static int currentLevel;
	
	//Precondition:
	//Postcondition: instantiates a Main object to a variable of type JFrame
	public Main() {
		super("Catch!");
		printScreen();
	}
	
	//Precondition:
	//Postcondition: initializes fields of Main object and prints buttons and labels
	public void printScreen() {
		c = getContentPane();
	    c.setBackground(Color.black);
		l1 = new JLabel("Catch!");
	    l2 = new JLabel("  [A] LEFT | [D] RIGHT");
	    l3 = new JLabel("      MISS 5 = FAIL");
	    l4 = new JLabel("COMBO = MORE SCORE");
		l1.setForeground(Color.yellow);
	    l2.setForeground(Color.white);
	    l3.setForeground(Color.red);
	    l4.setForeground(Color.cyan);
	    l1.setFont(new Font("Verdana", Font.BOLD, 36));
	    l2.setFont(new Font("Verdana", Font.PLAIN, 16));
	    l3.setFont(new Font("Verdana", Font.PLAIN, 16));
	    l4.setFont(new Font("Verdana", Font.PLAIN, 16));
		b1 = new JButton("Easy: Sasageyo");
		b2 = new JButton("Normal: A New Journey");
		b3 = new JButton("Hard: Harumachi Clover");
		c.setLayout(null);
		c.add(l1);
		c.add(l2);
		c.add(l3);
		c.add(l4);
		c.add(b1);
		c.add(b2);
		c.add(b3);
		l1.setSize(150, 100);
		l1.setLocation(WINDOWWIDTH/2-75, 75);
		l2.setSize(200, 100);
		l2.setLocation(WINDOWWIDTH/2-100, 125);
		l3.setSize(200, 100);
		l3.setLocation(WINDOWWIDTH/2-100, 150);
		l4.setSize(200, 100);
		l4.setLocation(WINDOWWIDTH/2-100, 175);
		b1.setSize(200,40);
		b1.setLocation(WINDOWWIDTH/2-100, 250);
		b2.setSize(200,40);
		b2.setLocation(WINDOWWIDTH/2-100, 300);
		b3.setSize(200,40);
		b3.setLocation(WINDOWWIDTH/2-100, 350);
		b1.addActionListener(this);
		b2.addActionListener(this);
		b3.addActionListener(this);
	}

	//Precondition: one of buttons b1, b2, or b3, are pressed
	//Postcondition: instantiates the Catcher object with parameters depending on the button pressed, buttons are disabled
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==b1) {
			ct = new Catcher(WINDOWWIDTH/2, 1, highScore1, "sasageyo");
			ct.run(ct);
			currentLevel = 1;
		}
		else if (e.getSource()==b2) {
			ct = new Catcher(WINDOWWIDTH/2, 2, highScore2, "a new journey");
			ct.run(ct);
			currentLevel = 2;
		}
		else {
			ct = new Catcher(WINDOWWIDTH/2, 3, highScore3, "harumachi clover");
			ct.run(ct);
			currentLevel = 3;
		}
		b1.setEnabled(false);
		b2.setEnabled(false);
		b3.setEnabled(false);
	}
	
	//Precondition: Catcher class calls method when Catcher.highScore is updated, 1 <= level <= 3
	//Postcondition: updates correct high score variable depending on level parameter
	public static void setHighScore(int high, int level) {
		if (level==1) {
			highScore1 = high;
		}
		else if (level==2) {
			highScore2 = high;
		}
		else if (level==3) {
			highScore3 = high;
		}
	}
	
	//Precondition: Catcher class calls method when Catcher.gameEnd is true, or window is closed
	//Postcondition: updates button colors and enables them
	public static void enableButtons() {
		b1.setEnabled(true);
		b2.setEnabled(true);
		b3.setEnabled(true);
		if (currentLevel == 1) { //To update the color of the button, determine which level was just played
			if (!ct.getWin() && b1.getBackground()!=Color.green) { //Set the color to red if ct.getWin() is false and the level is not already completed
				b1.setBackground(Color.red);
			}
			else {
				b1.setBackground(Color.green);
			}
		}
		else if (currentLevel == 2) {
			if (!ct.getWin() && b2.getBackground()!=Color.green) {
				b2.setBackground(Color.red);
			}
			else {
				b2.setBackground(Color.green);
			}
		}
		else {
			if (!ct.getWin() && b3.getBackground()!=Color.green) {
				b3.setBackground(Color.red);
			}
			else {
				b3.setBackground(Color.green);
			}
		}
		
	}
	
	//Precondition:
	//Postcondition: creates frame and sets its attributes
	public static void main(String[] args) {
		frame = new Main();
		frame.setSize(WINDOWWIDTH, WINDOWHEIGHT); //Set frame size
		frame.setLocationRelativeTo(null); //Center frame on screen
		frame.setResizable(false); //Impossible to change size of frame
		frame.setVisible(true); //Make frame visible
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //Frame can be closed, terminate program
	}
	
}

