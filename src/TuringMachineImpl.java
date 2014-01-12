import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Scanner;


public class TuringMachineImpl {
	
	int startState;
	int currentState;
	HashSet<Integer> acceptingStates;
	Hashtable<Integer, ArrayList<Transition>> transitions;
	
	public TuringMachineImpl() {
		startState = 0;
		currentState = startState;
		acceptingStates = new HashSet<Integer>();
		transitions = new Hashtable<Integer, ArrayList<Transition>>();
	}
	
	public void setStartState(int state) {
		startState = state;
		currentState = startState;
	}
	
	public void addAcceptingState(int state) {
		acceptingStates.add(new Integer(state));
	}
	
	public void addTransition(Transition t) {
		Integer tStart = new Integer(t.startState);
		if(transitions.containsKey(tStart)) {
			transitions.get(tStart).add(t);
		}
		else {
			transitions.put(tStart, new ArrayList<Transition>());
			transitions.get(tStart).add(t);
		}
	}
	
	/* No transitions out means rejecting state */
	public boolean isRejectingState(int state) {
		return !transitions.containsKey(new Integer(state));
	}
	
	public boolean isAcceptingState(int state) {
		return acceptingStates.contains(new Integer(state));
	}
	
	public int simulate(String input) {
		currentState = startState;
		Tape tape;
		if(input.equals("Empty")) {
			tape = new Tape();
		}
		else {
			tape = new Tape(input);
		}
		String status = "State: "+Integer.toString(currentState)+" Head: "+String.valueOf(tape.head)+" Tape: "+tape.printSelf();
		System.out.println(status);
		while(!isAcceptingState(currentState) && !isRejectingState(currentState)) {
			Integer state = new Integer(currentState);
			char head = tape.head;
			ArrayList<Transition> possibleTransitions = transitions.get(state);
			Transition t = null;
			
			for(int i = 0; i < possibleTransitions.size(); i++) {
				if(head == possibleTransitions.get(i).character) {
					t = possibleTransitions.get(i);
					break;
				}
			}
			
			if(t == null) return 0;
			
			tape.write(t.write);
			tape.move(t.movement);
			currentState = t.endState;
			status = "State: "+Integer.toString(currentState)+" Head: "+String.valueOf(tape.head)+" Tape: "+tape.printSelf();
			System.out.println(status);
		}
		if(isAcceptingState(currentState)) return 1;
		else return 0;
	}
	
	public static Transition createTransition(String transitionString) throws IllegalArgumentException {
		boolean matches = transitionString.matches("^[(][0-9]+[,].[)] [(][0-9]+[,].[,][LR][)]$");
		if(!matches) throw new IllegalArgumentException("String not valid transition format");
		
		String[] args = transitionString.split("[(]|[)]");
		String[] from = args[1].split("[,]");
		String[] to = args[3].split("[,]");
		
		int startState = Integer.parseInt(from[0]);
		char character = from[1].charAt(0);
		int endState = Integer.parseInt(to[0]);
		char write = to[1].charAt(0);
		int movement;
		if(to[2].equals("L")) movement = Tape.MOVELEFT;
		else movement = Tape.MOVERIGHT;
		
		Transition t = new Transition(startState, endState, character, movement, write);
		return t;
	}
	
	public static HashSet<Integer> getStartingStates(String states) throws IllegalArgumentException {
		boolean matches = states.matches("^[0-9 ]+");
		if(!matches) throw new IllegalArgumentException("String not valid accepting states format");
		
		String[] acceptingStatesString = states.split(" ");
		HashSet<Integer> acceptingStates = new HashSet<Integer>();
		
		for(int i = 0; i < acceptingStatesString.length; i++) {
			int state = Integer.parseInt(acceptingStatesString[i]);
			acceptingStates.add(new Integer(state));
		}
		
		return acceptingStates;
	}
	
	public static void main(String[] args) {
		
		File file = new File("TM.txt");
		TuringMachineImpl M = new TuringMachineImpl();
		
		try {
			String line;
			BufferedReader in = new BufferedReader(new FileReader(file));
			
			/* Attempt to read in accepting states */
			line = in.readLine();
			if(line == null) throw new EOFException("Read EOF without accepting states, transitions, or input");
			while(line.length() == 0) {
				line = in.readLine();
				if(line == null) throw new EOFException("Read EOF without accepting states, transitions, or input");
			}
			M.acceptingStates.addAll(getStartingStates(line));
			
			/* Attempt to read in transitions */
			while((line = in.readLine()) != null) {
				if(line.length() == 0) continue;
				
				System.out.println(line);
				Transition t = createTransition(line);
				M.addTransition(t);
			}
			
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("File: "+args[0]+" not found.");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("IO Error reading file: "+args[0]);
		}
		
		Scanner in = new Scanner(System.in);
		while(true) {
			System.out.print("Enter input:");
			String input = in.next();
			if(input.equals("q") || input.equals("exit")) break;
			if(M.simulate(input) == 1) System.out.println("Accept");
			else System.out.println("Reject");
		}
	}
}
