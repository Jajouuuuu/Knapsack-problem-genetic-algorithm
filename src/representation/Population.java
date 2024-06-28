package representation;

import java.util.*;

/**
 * Représente une population d'objets de type Bag.
 * Une population contient une liste ordonnée et non ordonnée de sacs (Bag).
 */
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
     *
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
     *
     * @return the size of the population.
     */
    public int size() {
        return this.population.size();
    }

    /**
     * This methods returns the ith individual of the population.
     *
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
     *
     * @return an iterator on the representation.Population of individuals.
     */
    public Iterator<Bag> iterator() {
        return this.orderedPopulation.descendingIterator();
    }

    /**
     * Replaces the least fit individual of the population by a given individual.
     *
     * @param newBag the bag to be added to the population.
     */
    public void replace(Bag newBag) {
        Bag oldBag = this.orderedPopulation.first();
        this.orderedPopulation.remove(oldBag);
        this.population.remove(oldBag);
        this.add(newBag);
    }

    /**
     * Performs a flip mutation on the population of bags. For each bag, a number of items
     * are randomly selected and flipped (added or removed) based on the mutation factor.
     * If the resulting bag is not valid, it is repaired to ensure it meets the maximum cost constraints.
     *
     * @param maximumCost The list of maximum costs used to validate the bags.
     * @param mutationFactor The probability of a mutation occurring and the size of the mutation.
     */
    public void flipMutation(List<Integer> maximumCost, double mutationFactor) {
        for (Bag bag : this.population) {
            if (Math.random() < mutationFactor) {
                int mutationSize = (int) (bag.content.size() * mutationFactor ) + 1;
                for (int i = 0; i < mutationSize; i++) {
                    int index = (int) (Math.random() * bag.content.size());
                    // Flip the object at the selected index
                    if (bag.content.get(index) == 1) {
                        bag.removeBagObject(index);
                    } else {
                        if (bag.isValidAdd(index)) {
                            bag.addBagObject(index);
                        }
                    }
                }
                // Repair the bag if it's not valid after the mutation
                if (!bag.isValid(maximumCost)) {
                    bag.reparation(maximumCost);
                }
            }
        }
    }

    /**
     * Performs a swap mutation on the population of bags. For each bag, two items are
     * randomly selected and swapped based on the mutation factor. If the resulting
     * bag is not valid, it is repaired to ensure it meets the maximum cost constraints.
     *
     * @param maximumCost The list of maximum costs used to validate the bags.
     * @param mutationFactor The probability of a mutation occurring.
     */
    public void swapMutation(List<Integer> maximumCost, double mutationFactor) {
        for (Bag bag : this.population) {
            if (Math.random() < mutationFactor) {
                // Select two different random indices
                int index1 = (int) (Math.random() * bag.content.size());
                int index2;
                do {
                    index2 = (int) (Math.random() * bag.content.size());
                } while (index2 == index1);
                int contentIndex1 = bag.content.get(index1);
                int contentIndex2 = bag.content.get(index2);

                // Swap items if conditions are met
                if (contentIndex1 == 1 && contentIndex2 == 0 && bag.isValidAdd(index2)){
                    bag.removeBagObject(index1);
                    bag.addBagObject(index2);
                }
                else if (contentIndex1 == 0 && contentIndex2 == 1 && bag.isValidAdd(index1)){
                    bag.removeBagObject(index2);
                    bag.addBagObject(index1);
                }
                // Repair the bag if it's not valid after the mutation
                if (!bag.isValid(maximumCost)) {
                    bag.reparation(maximumCost);
                }
            }
        }
    }

    /**
     * Performs a scramble mutation on the population of bags. For each bag, a random subset
     * of items is selected, scrambled (shuffled), and then applied back to the bag's content
     * based on the mutation factor. If the resulting bag is not valid, it is repaired to ensure
     * it meets the maximum cost constraints.
     *
     * @param maximumCost The list of maximum costs used to validate the bags.
     * @param mutationFactor The probability of a mutation occurring and the size of the mutated subset.
     */
    public void scrambleMutation(List<Integer> maximumCost, double mutationFactor) {
        for (Bag bag : this.population) {
            if (Math.random() < mutationFactor) {
                // Select two different random indices
                int startIndex = (int) (Math.random() * bag.content.size());
                int endIndex;
                do {
                    endIndex = (int) (Math.random() * bag.content.size());
                } while (endIndex == startIndex | Math.abs(endIndex-startIndex+1) > bag.content.size()/2);
                // condition on size of scrambled subset to temper the effect of mutation
                if (startIndex > endIndex) {
                    int temp = startIndex;
                    startIndex = endIndex;
                    endIndex = temp;
                }

                // Scramble the sublist
                List<Integer> sublist = bag.content.subList(startIndex, endIndex + 1);
                List<Integer> scrambledSublist = new ArrayList<>(sublist);
                Collections.shuffle(scrambledSublist);

                // Apply the scrambled sublist back to the bag content
                for (int i = 0; i < scrambledSublist.size(); i++) {
                    if (scrambledSublist.get(i) == 1 && bag.content.get(startIndex + i) == 0) {
                        bag.addBagObject(startIndex + i);
                    }
                    else if (scrambledSublist.get(i) == 0 && bag.content.get(startIndex + i) == 1) {
                        bag.removeBagObject(startIndex + i);
                    }
                }
                // Repair the bag si c'est pas un bag valid après la mutation
                if (!bag.isValid(maximumCost)) {
                    bag.reparation(maximumCost);
                }
            }
        }
    }

    /**
     * Performs an inversion mutation on the population of bags. For each bag, a random subset
     * of items is selected, inverted (reversed), and then applied back to the bag's content
     * based on the mutation factor. If the resulting bag is not valid, it is repaired to ensure
     * it meets the maximum cost constraints.
     *
     * @param maximumCost The list of maximum costs used to validate the bags.
     * @param mutationFactor The probability of a mutation occurring and the size of the mutated subset.
     */
    public void inversionMutation(List<Integer> maximumCost, double mutationFactor) {
        for (Bag bag : this.population) {
            if (Math.random() < mutationFactor) {
                int startIndex = (int) (Math.random() * bag.content.size());
                int endIndex;
                do {
                    endIndex = (int) (Math.random() * bag.content.size());
                } while (endIndex == startIndex | Math.abs(endIndex-startIndex+1) > bag.content.size()/2);
                // condition on size of inverted subset to temper the effect of mutation

                if (startIndex > endIndex) {
                    int temp = startIndex;
                    startIndex = endIndex;
                    endIndex = temp;
                }

                // Invert the sublist
                List<Integer> sublist = bag.content.subList(startIndex, endIndex + 1);
                List<Integer> invertedSublist = new ArrayList<>(sublist);
                Collections.reverse(invertedSublist);

                // Apply the inverted sublist back to the bag content
                for (int i = 0; i < invertedSublist.size(); i++) {
                    if (invertedSublist.get(i) == 1 && bag.content.get(startIndex + i) == 0) {
                        bag.addBagObject(startIndex + i);
                    }
                    else if (invertedSublist.get(i) == 0 && bag.content.get(startIndex + i) == 1) {
                        bag.removeBagObject(startIndex + i);
                    }
                }
                // Repair the bag if it's not valid after the mutation
                if (!bag.isValid(maximumCost)) {
                    bag.reparation(maximumCost);
                }
            }
        }
    }

    /**
     * Provides the best individual in the population.
     *
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

    /**
     * Retourne la taille de la population ordonnée.
     *
     * @return La taille de la population ordonnée.
     */
    public int getOrderedPopulationsize(){
        return orderedPopulation.size();
    }
}
