
public class Transition {
	int startState;
	int endState;
	char character;
	int movement;
	char write;
	
	public Transition(int _startState, int _endState, char _character, int _movement, char _write) {
		startState = _startState;
		endState = _endState;
		character = _character;
		movement = _movement;
		write = _write;
	}
}
