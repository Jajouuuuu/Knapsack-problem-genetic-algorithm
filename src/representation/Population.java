package representation;

import algorithm.GeneticAlgorithm;

import java.util.*;

public class Population {
    private ArrayList<Bag> population;
    private TreeSet<Bag> orderedPopulation;

    /**
     * Constructor building an instance of representation.Population from a representation.Bag
     */
    public Population() {
        this.population = new ArrayList<Bag>();
        this.orderedPopulation = new TreeSet<Bag>();
    }

    /**
     * Adds a representation.Bag to the population.
     * @param bag, the representation.Bag to be added.
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
     * This method provides an iterator on the representation.Population of individuals.
     * This iterator makes it possible to iterate over individuals in
     * descending order of fitness.
     * @return an iterator on the representation.Population of individuals.
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

    public void mutation(List<Integer> maximumCost, double mutationFactor) {
        for (Bag bag : this.population) {
            if (Math.random() < mutationFactor) {
                int mutationSize = (int) (bag.content.size() * mutationFactor);
                for (int i = 0; i < mutationSize; i++) {
                    int index = (int) (Math.random() * bag.content.size());
                    if (bag.content.get(index) == 1) {
                        bag.removeBagObject(index);
                    } else {
                        if (bag.isValidAdd(index)) {
                            bag.addBagObject(index);
                        }
                    }
                }
                // Repair the bag si c'est pas un bag valid après la mutation TODO a checker si ça fonctionne correctement
                if (!bag.isValid(maximumCost)) {
                    bag.reparation(maximumCost);
                }
            }
        }
    }

    /**
     * Provides the best individual in the population.
     * @return the best individual (representation.Bag) in the population.
     */
    public Bag getBest() {
        Iterator<Bag> it = this.iterator();
        return it.next();
    }

    @Override
    public String toString() {
        return "representation.Population{" +
                "population=" + population;
    }

    public int getOrderedPopulationsize(){
        return orderedPopulation.size();
    }
}
