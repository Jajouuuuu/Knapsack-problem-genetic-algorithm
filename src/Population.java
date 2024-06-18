import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

public class Population {

    private ArrayList<Bag> population;
    private TreeSet<Bag> orderedPopulation;

    /**
     * Constructor building an instance of Population from a Bag
     */
    public Population() {
        this.population = new ArrayList<Bag>();
        this.orderedPopulation = new TreeSet<Bag>();
    }


    /**
     * Adds a Bag to the population.
     * @param bag, the Bag to be added.
     */
    public void add(Bag bag) {
        if(this.orderedPopulation.contains(bag)) {
            Bag bag1 = Bag.createRandomBag();
            this.population.add(bag1);
            this.orderedPopulation.add(bag1);
        }
        else {
            Bag copy = bag.copy();
            this.population.add(copy);
            this.orderedPopulation.add(copy);
        }
    }

    /**
     * Returns the size of the population.
     * @return the size of the population.
     */
    public int size() {
        return this.population.size();
    }

    /**
     * This methods returns the ith individual of the population.
     * @param i, the index of the desired element.
     * @return the ith individual of the population.
     */
    public Bag get(int i) {
        return this.population.get(i).copy();
    }


    /**
     * This method provides an iterator on the Population of individuals.
     * This iterator makes it possible to iterate over individuals in
     * descending order of fitness.
     * @return an iterator on the Population of individuals.
     */
    public Iterator<Bag> iterator() {
        return this.orderedPopulation.descendingIterator();
    }

    /**
     * Replaces the least fit individual of the population by a given individual.
     * @param newBag the bag to be added to the population.
     */
    public void replace(Bag newBag) {
        Bag oldBag = this.orderedPopulation.first();
        this.orderedPopulation.remove(oldBag);
        this.population.remove(oldBag);
        this.add(newBag);
    }

    //TODO mutation parce qu'elle se fait sur la population et pas sur un bag en particulier


    /**
     * Provides the best individual in the population.
     * @return the best individual (Bag) in the population.
     */
    public Bag getBest() {
        Iterator<Bag> it = this.iterator();
        return it.next();
    }


    @Override
    public String toString() {
        return "Population{" +
                "population=" + population;
    }
}
