package representation;

/**
 * This class represents a couple of Bag.
 * The two elements of the couple represents parents 
 * of individuals in a genetic algorithm. 
 * @author Documented by Hugo Gilbert, a large part of the code 
 * was written by David Eck and Julien Lesca
 *
 */
public class Couple {
    public Bag mother, father;

    public Couple(Bag m, Bag f) {
        this.mother = m.copy();
        this.father = f.copy();
    }
}