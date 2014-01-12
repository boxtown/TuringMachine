import java.util.Collections;
import java.util.Stack;
import java.util.Vector;


public class Tape {
	static int MOVELEFT = 0;
	static int MOVERIGHT = 1;
	
	char head;
	Stack<String> left;
	Stack<String> right;
	
	public Tape() {
		head = 'B';
		left = new Stack<String>();
		right = new Stack<String>();
		left.push("B");
		right.push("B");
	}
	
	public Tape(String input) {
		head = input.charAt(0);
		left = new Stack<String>();
		right = new Stack<String>();
		left.push("B");
		right.push("B");
		for(int i = input.length()-1; i > 0; i--) {
			right.push(String.valueOf(input.charAt(i)));
		}
	}
	
	public void write(char c) {
		head = c;
	}
	
	public void move(int moveType) {
		if(moveType == MOVELEFT) moveLeft();
		else moveRight();
	}
	
	private void moveLeft() {
		char leftChar = left.pop().charAt(0);
		if(left.size() == 0) {
			left.push("B");
		}
		right.push(String.valueOf(head));
		head = leftChar;
	}
	
	private void moveRight() {
		char rightChar = right.pop().charAt(0);
		if(right.size() == 0) {
			right.push("B");
		}
		left.push(String.valueOf(head));
		head = rightChar;
	}
	
	public String printSelf() {
		Vector<String> leftVector = (Vector<String>) left.clone();
		Vector<String> rightVector = (Vector<String>) right.clone();
		Collections.reverse(rightVector);
		String tapeString = "";
		for(int i = 0; i < leftVector.size(); i++) {
			tapeString += leftVector.elementAt(i);
		}
		tapeString += "["+head+"]";
		for(int i = 0; i < rightVector.size(); i++) {
			tapeString += rightVector.elementAt(i);
		}
		return tapeString;
	}
}