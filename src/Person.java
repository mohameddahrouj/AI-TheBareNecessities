


public class Person implements Comparable<Person> {

    private int crossingTime;
    private String name;

    public Person(String name, int crossingTime)
    {
        this.crossingTime = crossingTime;

        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public int getCrossingTime()
    {
        return  this.crossingTime;
    }

    public String toString()
    {
        return this.crossingTime + "";
    }

    public int compareTo(Person other) {
        return Integer.compare(this.crossingTime, other.crossingTime);
    }

}