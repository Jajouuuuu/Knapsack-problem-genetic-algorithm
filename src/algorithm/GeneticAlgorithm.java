package algorithm;

import java.io.IOException;
import java.util.*;
import java.util.function.BiConsumer;
import representation.Bag;
import representation.BagObject;
import representation.Couple;
import representation.Population;
import testsCases.TestProblem;

/**
 * Classe représentant un algorithme génétique pour résoudre le problème de sac à dos multidimensionnel.
 */
public class GeneticAlgorithm {

    // TODO : faudrait revoir nos visibilités, j'ai mit pas mal de chose en public jsp si c'ets une bonne chose ?
    public List<Integer> values;
    public Population population;
    public int populationSize;
    public int iterations;
    public Bag bag;
    public int optiFindAt;

    /**
     * Constructeur de l'algorithme génétique.
     *
     * @param bag le sac initial
     * @param k taille de la population
     */
    public GeneticAlgorithm(Bag bag, int k) {
        this.values = new ArrayList<>();
        this.bag = bag;
        for (BagObject obj : Bag.bagObjects) {
            this.values.add(obj.value);
        }
        this.populationSize = 2 * k;
        this.iterations = 0;
        this.population = new Population();
        for (int i = 0; i < populationSize; i++) {
            Bag newBag = Bag.createRandomBag();
            population.add(newBag);
        }
        System.out.println("here");
        System.out.println(this.populationSize);
    }

    // TODO : je crois que lui on peut le virer non c'était pas ça l'uniform crossover ? @PepperMint5
    /**
     * Effectue un croisement entre les couples de parents.
     *
     * @param parents les couples de parents
     * @return une nouvelle population résultant du croisement
     */
    public Population crossover(ArrayList<Couple> parents) {
        Population res = new Population();
        for (Couple parent : parents) {
            Bag child1 = new Bag();
            Bag child2 = new Bag();
            for (int j = 0; j < parents.get(0).father.content.size(); j++) {
                if (Math.random() < 0.5) {
                    child1.addBagObject(parent.father.content.get(j));
                    child2.addBagObject(parent.mother.content.get(j));
                } else {
                    child1.addBagObject(parent.mother.content.get(j));
                    child2.addBagObject(parent.father.content.get(j));
                }
            }
            res.add(child1);
            res.add(child2);
        }
        return res;
    }

    /**
     * Effectue un croisement à un point (Single-point crossover).
     * Il s'agit d'une des méthodes les plus simples et
     * les plus couramment utilisées pour le croisement dans les algorithmes génétiques.
     * Il s'applique particulièrement bien aux représentations binaires et autres structures linéaires.
     * Le principe du single-point crossover est de sélectionner un point unique de crossover dans les génomes
     * des parents, puis d'échanger les segments de gènes après ce point pour créer deux nouveaux enfants.
     *
     * @param parents les couples de parents
     * @return une nouvelle population résultant du croisement à un point
     */
    public Population singlePointCrossover(ArrayList<Couple> parents) {
        Population res = new Population();
        for (Couple parent : parents) {
            Bag child1 = new Bag();
            Bag child2 = new Bag();
            int length = parent.mother.content.size();
            int crossoverPoint = (int) (Math.random() * length);
            for (int i = 0; i < length; i++) {
                if (i < crossoverPoint) {
                    child1.addBagObject(parent.father.content.get(i));
                    child2.addBagObject(parent.mother.content.get(i));
                } else {
                    child1.addBagObject(parent.mother.content.get(i));
                    child2.addBagObject(parent.father.content.get(i));
                }
            }
            res.add(child1);
            res.add(child2);
        }
        return res;
    }

    /**
     * Effectue un croisement à deux points (Two-point crossover).
     * Même logique que pour le onePointCrossover mais on casse en deux points les chaînes.
     *
     * @param parents les couples de parents
     * @return une nouvelle population résultant du croisement à deux points
     */
    public Population twoPointCrossover(ArrayList<Couple> parents) {
        Population res = new Population();
        for (Couple parent : parents) {
            if (Math.random() < 0.5) {
                Bag child1 = new Bag();
                Bag child2 = new Bag();
                int length = parent.mother.content.size();
                int crossoverPoint1 = (int) (Math.random() * length);
                int crossoverPoint2 = (int) (Math.random() * length);
                if (crossoverPoint1 > crossoverPoint2) {
                    int temp = crossoverPoint1;
                    crossoverPoint1 = crossoverPoint2;
                    crossoverPoint2 = temp;
                }
                for (int i = 0; i < length; i++) {
                    if (i < crossoverPoint1 || i >= crossoverPoint2) {
                        child1.addBagObject(parent.father.content.get(i));
                        child2.addBagObject(parent.mother.content.get(i));
                    } else {
                        child1.addBagObject(parent.mother.content.get(i));
                        child2.addBagObject(parent.father.content.get(i));
                    }
                }
                res.add(child1);
                res.add(child2);
            } else {
                res.add(parent.mother);
                res.add(parent.father);
            }
        }
        return res;
    }

    /**
     * Effectue un croisement uniforme (Uniform crossover).
     * C'est une méthode de croisement utilisée dans les algorithmes génétiques où
     * chaque gène de l'enfant est choisi indépendamment entre les deux parents selon une probabilité uniforme.
     * Cela permet de mélanger les gènes des parents de manière plus aléatoire et plus équilibrée par rapport aux méthodes
     * de crossover à point unique ou à deux points.
     *
     * @param parents les couples de parents
     * @return une nouvelle population résultant du croisement uniforme
     */
    public Population uniformCrossover(ArrayList<Couple> parents) {
        Population res = new Population();
        for (Couple parent : parents) {
            if (Math.random() < 0.5) {
                Bag child1 = new Bag();
                Bag child2 = new Bag();
                for (int j = 0; j < parents.get(0).father.content.size(); j++) {
                    if (Math.random() < 0.5) {
                        child1.addBagObject(parent.father.content.get(j));
                        child2.addBagObject(parent.mother.content.get(j));
                    } else {
                        child1.addBagObject(parent.mother.content.get(j));
                        child2.addBagObject(parent.father.content.get(j));
                    }
                }
                res.add(child1);
                res.add(child2);
            } else {
                res.add(parent.mother);
                res.add(parent.father);
            }
        }
        return res;
    }

    /**
     * Sélectionne la méthode de croisement à utiliser en fonction du nom spécifié.
     *
     * @param crossOver le nom de la méthode de croisement
     * @return une nouvelle population résultant du croisement
     */
    private Population applyCrossover(String crossOver) {
        switch (crossOver) {
            case "crossover":
                return crossover(selection());
            case "singlePointCrossover":
                return singlePointCrossover(selection());
            case "twoPointCrossover":
                return twoPointCrossover(selection());
            case "uniformCrossover":
                return uniformCrossover(selection());
            default:
                throw new IllegalArgumentException("Unsupported crossover method: " + crossOver);
        }
    }

    /**
     * Sélectionne la méthode de mutation à utiliser en fonction du nom spécifié.
     *
     * @param newPopulation la nouvelle population
     * @param mutationMethod le nom de la méthode de mutation
     * @param mutationFactor le facteur de mutation
     */
    private void applyMutation(Population newPopulation, String mutationMethod, double mutationFactor) {
        switch (mutationMethod) {
            case "flipMutation":
                newPopulation.flipMutation(Bag.maximumCost, mutationFactor);
                break;
            case "swapMutation":
                newPopulation.swapMutation(Bag.maximumCost, mutationFactor);
                break;
            case "scrambleMutation":
                newPopulation.scrambleMutation(Bag.maximumCost, mutationFactor);
                break;
            case "inversionMutation":
                newPopulation.inversionMutation(Bag.maximumCost, mutationFactor);
                break;
            default:
                throw new IllegalArgumentException("Unsupported mutation method: " + mutationMethod);
        }
    }

    /**
     * Résout le problème de sac à dos en utilisant un algorithme génétique.
     *
     * @param elitistRate le taux d'élitisme
     * @param mutationFactor le facteur de mutation
     * @param mutationMethod la méthode de mutation
     * @param crossOver la méthode de croisement
     * @param progressCallback callback pour suivre la progression
     * @return le meilleur sac trouvé
     */
    public Bag solve(double elitistRate, double mutationFactor, String mutationMethod, String crossOver,
                     BiConsumer<Integer, Population> progressCallback) throws IOException {
        Population newPopulation = null;
        boolean stop = false;
        boolean optimalFound = false;
        int cmp = 1;
        while (!stop) {
            System.out.println("It n°: " + cmp++);
            System.out.println(population.getBest());
            newPopulation = applyCrossover(crossOver);
            applyMutation(newPopulation, mutationMethod, mutationFactor);
            for (int i = 0; i < newPopulation.size(); i++) {
                if (!newPopulation.get(i).isValid(Bag.maximumCost)) {
                    newPopulation.get(i).reparation(Bag.maximumCost);
                }
            }
            Iterator<Bag> it = this.population.iterator();
            for (int j = 0; j < elitistRate * this.populationSize && j < population.getOrderedPopulationsize(); j++) {
                newPopulation.replace(it.next());
            }
            if (cmp % 10 == 0 && progressCallback != null) {
                progressCallback.accept(cmp, newPopulation);
            }
            population = newPopulation;
            stop = (population.getBest().cost.stream().mapToInt(Integer::intValue).sum() >= population.getBest().value ||  cmp >= 100);
            // TODO : Attention ici c'est juste pour nos check ! ça permets de savoir exactement à quelle itérations on trouve l'opti comme on connait notre résultat à trouver
            if(population.getBest().value == TestProblem.readFromFile("src/testsCases/mknap3.txt").getOptimal() && !optimalFound){
                this.optiFindAt = cmp;
                optimalFound = true;
            }
        }
        this.iterations = cmp;
        return newPopulation.getBest();
    }

    /**
     * Sélectionne des couples de parents pour le croisement.
     *
     * @return une liste de couples sélectionnés
     */
    private ArrayList<Couple> selection(){
        ArrayList<Couple> res = new ArrayList<>();
        RandomSelector randomSelector = new RandomSelector();
        Bag runningIndividu;
        Bag parent1;
        Bag parent2;
        for (int i = 0; i < population.size(); i++){
            runningIndividu = population.get(i);
            randomSelector.add(runningIndividu.value);
        }
        for (int i=0; i<population.size()/2; i++){
            parent1 = population.get(randomSelector.randomChoice());
            parent2 = population.get(randomSelector.randomChoice());
            while(parent2.hasSameContent(parent1)){
                parent2 = population.get(randomSelector.randomChoice());
            }
            res.add(new Couple(parent1,parent2));
        }
        return res;
    }

    @Override
    public String toString() {
        return "algorithm.GeneticAlgorithm{" +
                "values=" + values +
                ", costs=" + Bag.maximumCost +
                ", BagObjects=" + Bag.bagObjects +
                ", population=" + population +
                ", populationSize=" + populationSize +
                '}';
    }
}
