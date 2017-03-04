import java.util.Comparator;

public class StateComparator implements Comparator<State>
{
    public int compare(State board, State board2)
    {
        if(board.findCost() > board2.findCost())
            return 1;
        else if(board.findCost() < board2.findCost())
            return -1;
        else
            return 0;
    }

}