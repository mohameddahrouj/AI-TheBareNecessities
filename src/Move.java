import java.util.ArrayList;
import java.util.List;

/**
 * Used to store old and new indicies when generating possible children moves
 * @author Mohamed Dahrouj
 *
 */
public class Move {

    private List<Person> people;
    public int transportationTime;

    public Move(List<Person> people, int transportationTime)
    {
        this.people = new ArrayList<>(people);
        this.transportationTime = transportationTime;
    }

    public List<Person> getPeople()
    {
        return this.people;
    }
}
