import java.util.ArrayList;

/**
 * Commodity transportation problem
 * Invalid states are stored, not generated. Generated states are checked for
 * validity against the invalidStates 2D-array, and removed from the successors
 * array list if invalid.
 * State Representation:
 * A state is defined as a 4-bit string with Direction enums representing whether a person
 * is on the left or right of the river.
 * 
 * @author Mohamed Dahrouj
 * 
 */

import java.util.*;

public class CTPState implements State {

    private ArrayList<Person> leftSide;
    private ArrayList<Person> rightSide;
    private Direction torch;
    private int time;
    private int stateValue;
    private int level;
    private Heuristic heuristic;
    private State previousState;

    public CTPState(ArrayList<Person> leftSide, ArrayList<Person> rightSide, Direction torch, int time, int level, State prevState, Heuristic heuristic)
    {
        this.leftSide = new ArrayList<>(leftSide);
        this.rightSide = new ArrayList<>(rightSide);
        this.time = time;
        this.torch = torch;
        this.level = level;
        this.previousState = prevState;
        this.heuristic = heuristic;
        setValue();
    }

    public boolean isSolved()
    {
        return this.leftSide.isEmpty();
    }

    @Override
    public void setStateValue(int value)
    {
        this.stateValue = value;
    }
    
    public ArrayList<Person> getLeftSide(){
    	return leftSide;
    }
    
    public ArrayList<Person> getRightSide(){
    	return rightSide;
    }

    private ArrayList<Move> generateAllMoves()
    {
        if(torch.equals(Direction.Left))
             return generateAllLeftToRightMoves();

        return generateAllRightToLeftMoves();
    }

    private ArrayList<Move> generateAllLeftToRightMoves()
    {
    	ArrayList<Move> moves = new ArrayList<>();
        for(int i = 0; i<this.leftSide.size() -1; i++) {
            Person person1 = this.leftSide.get(i);
            for (int j = i + 1; j < this.leftSide.size(); j++) {
                Person person2 = this.leftSide.get(j);
                int transportationTime = getTransportationTime(person1,person2);
                ArrayList<Person> people = new ArrayList<>();
                people.add(person1);
                people.add(person2);
                moves.add(new Move(people,transportationTime));
            }
        }
        return moves;
    }

    private ArrayList<Move> generateAllRightToLeftMoves()
    {
    	ArrayList<Move> moves = new ArrayList<>();
        for(int i = 0; i<this.rightSide.size() -1; i++) {
            Person person1 = this.rightSide.get(i);
            int transportationTime = person1.getCrossingTime();
            ArrayList<Person> people = new ArrayList<>();
            people.add(person1);
            moves.add(new Move(people, transportationTime));

        }
        return moves;
    }

    private void setValue() {

        int heuristicValue;
        if (this.heuristic.equals(Heuristic.MANHATTAN))
            heuristicValue = getManDist();
        else if (this.heuristic.equals(Heuristic.OUTOFPLACE))
            heuristicValue = getOutOfPlace();
        else if (this.heuristic.equals(Heuristic.AVERAGE))
            heuristicValue = getAverageHeuristic();
        else
        	heuristicValue = 1;
        
        this.stateValue = heuristicValue + this.level;
    }

    public int getStateValue()
    {
        return this.stateValue;
    }

    @Override
    public void setPreviousState(State previousState)
    {
        this.previousState = previousState;
    }

    private CTPState applyMove(Move move, State prevState)
    {
        Move transportationMove = move;

        if(this.torch.equals(Direction.Left))
        {
            CTPState newState = new CTPState(
                    this.leftSide,
                    this.rightSide,
                    Direction.Right,
                    this.time,
                    this.level + transportationMove.transportationTime,
                    prevState,
                    this.heuristic);
            newState.leftSide.removeAll(transportationMove.getPeople());
            newState.rightSide.addAll(transportationMove.getPeople());
            newState.time += transportationMove.transportationTime;
            newState.setValue();
            return newState;
        }
        else {
            CTPState newState = new CTPState(
                    this.leftSide,
                    this.rightSide,
                    Direction.Left,
                    this.time,
                    this.level + transportationMove.transportationTime,
                    prevState,
                    this.heuristic);
            newState.rightSide.removeAll(transportationMove.getPeople());
            newState.leftSide.addAll(transportationMove.getPeople());
            newState.time += transportationMove.transportationTime;
            newState.setValue();
            return newState;
        }
    }

    private int getTransportationTime(Person person1, Person person2)
    {
        int person1CrossingTime = person1.getCrossingTime();
        int person2CrossingTime = person2.getCrossingTime();

        if(person1CrossingTime > person2CrossingTime)
        {
            return person1CrossingTime;
        }

        return person2CrossingTime;
    }

    private String getTorchLocation()
    {
        if(torch.equals(Direction.Left))
            return "Left";
        return "Right";
    }

    @Override
    public State getPreviousState()
    {
        return this.previousState;
    }

	@Override
	public boolean isGoal() {
		return this.leftSide.isEmpty();
	}

	@Override
	public ArrayList<State> generateChildren() {
        ArrayList<State> productionSystemHashSet = new ArrayList<>();

        for(Move move: generateAllMoves())
        {
            productionSystemHashSet.add(applyMove(move, this));
        }

        return productionSystemHashSet;
	}

	// f = g + h(n)
	@Override
	public double findCost() {

		return stateValue;
	}

	@Override
	public int getOutOfPlace() {
		
		//Max crossing time
        int max = 0;

        for (Person person: leftSide) {

            int crossingTime = person.getCrossingTime();
            if(crossingTime > max)
                max = crossingTime;
        }

        return max;
	}

	@Override
	public int getManDist() {
		
		// N crossing time 
		int n = this.leftSide.size()/2;
        int counter = this.leftSide.size()-1;
        int sumN = 0;
        //Sort to increasing order
        Collections.sort(this.leftSide);

        while(n>0)
        {
            sumN+= this.leftSide.get(counter).getCrossingTime();
            counter--;
            n--;
        }

        return sumN;
	}

	@Override
	public int getAverageHeuristic() {
		return (getOutOfPlace() + getManDist())/2;
	}

	@Override
	public void printState() {
		
		StringBuilder builder = new StringBuilder();
        builder.append("Left: ");
        for (Person person: this.leftSide) {
            builder.append(person.getCrossingTime() + " ");
        }

        builder.append("\nRight: ");

        for (Person person: this.rightSide) {
            builder.append(person.getCrossingTime() + " ");
        }
        
        builder.append("\nTorch Location: " + getTorchLocation());
        builder.append("\nTime Elapsed: " + this.time);
        System.out.println(builder);
		
	}
	
	public String toString() {
		
		StringBuilder builder = new StringBuilder();
        builder.append("Left: ");
        for (Person person: this.leftSide) {
            builder.append(person.getCrossingTime() + " ");
        }

        builder.append("\nRight: ");

        for (Person person: this.rightSide) {
            builder.append(person.getCrossingTime() + " ");
        }
        
        builder.append("\nTorch Location: " + getTorchLocation());
        builder.append("\nTime Elapsed: " + this.time);
        builder.append("\n");
        builder.append("\n");
        return builder.toString();
		
	}

	@Override
	public int getTimeTaken() {
		return time;
	}
}