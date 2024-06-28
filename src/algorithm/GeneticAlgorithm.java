package algorithm;

import java.util.*;
import java.util.function.BiConsumer;

import representation.Bag;
import representation.BagObject;
import representation.Couple;
import representation.Population;

public class GeneticAlgorithm {
    // TODO : faudrait peut-être refacto les visibilités de nos attributs ? Je suis pas sûr qu'on ai besoin d'attributs public parttout ?
    public static List<Integer> maximumCost;
    public List<Integer> values;

    // TODO : check si on a réellement besoin des coûts ici où si on peut les mettre directement dans la classe Bag ?
    public List<List<Integer>> costs;
    public List<BagObject> BagObjects;
    public Population population;
    public int populationSize;
    public int iterations;

    public GeneticAlgorithm(List<Integer> maximumCost, List<List<Integer>> costs, List<Integer> values, int k) {
        GeneticAlgorithm.maximumCost = maximumCost;
        this.values = values;
        this.costs = costs;
        this.BagObjects = new ArrayList<>();
        this.populationSize = 2 * k;
        this.iterations = 0;
        for (int i = 0; i < values.size(); i++) {
            BagObjects.add(new BagObject(i, costs.get(i), values.get(i)));
        }
        Bag.initializeObjects(BagObjects);
        population = new Population();
        for (int i = 0; i < populationSize; i++) {
            Bag bag = Bag.createRandomBag();
            //reparation(bag);
            population.add(bag);
        }
        System.out.println("here");
        System.out.println(this.populationSize);
    }

    // TODO : check si on passe la proba de cross over en paramètre ?

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


    /*Le crossover à un point (Single-point crossover) est l'une des méthodes les plus simples et
    * les plus couramment utilisées pour le croisement dans les algorithmes génétiques.
    * Il s'applique particulièrement bien aux représentations binaires et autres structures linéaires.
    * Le principe du single-point crossover est de sélectionner un point unique de crossover dans les génomes
    * des parents, puis d'échanger les segments de gènes après ce point pour créer deux nouveaux enfants.
    *  */
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

    /*Même chose que le onePointCrossover mais on casse en deux points les chaines*/
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


/*Uniform crossover
Le crossover uniforme (Uniform Crossover) est une méthode de croisement utilisée dans les algorithmes génétiques où
chaque gène de l'enfant est choisi indépendamment entre les deux parents selon une probabilité uniforme.
Cela permet de mélanger les gènes des parents de manière plus aléatoire et plus équilibrée par rapport aux méthodes
de crossover à point unique ou à deux points.*/

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



    // TODO : j'ai retiré le mutation rate comme on s'en servait pas, je pense que y'a du avoir un micmac avec mutationFactor
    // on peut le remettre si besoin, faudrait vérifier si cette fonction reste correcte
    // TODO : je comprends pas à quoi sert la targetValue ? comment on peut passer en paramètre l'optimale qu'on cherche ?
    public Bag solve(double elitistRate, int maxIt, double mutationFactor, String mutationMethod, String crossOver,
                     BiConsumer<Integer, Population> progressCallback) {
        Population newPopulation = null;
        boolean stop = false;
        int cmp = 1;
        while (!stop) {
            System.out.println("It n°: " + cmp++);
            switch (crossOver) {
                case "crossover":
                    newPopulation = crossover(selection());
                    break;
                case "singlePointCrossover":
                    newPopulation = singlePointCrossover(selection());
                    break;
                case "twoPointCrossover":
                    newPopulation = twoPointCrossover(selection());
                    break;
                case "uniformCrossover":
                    newPopulation = uniformCrossover(selection());
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported method type: " + mutationMethod);
            }

            switch (mutationMethod) {
                case "flipMutation":
                    newPopulation.flipMutation(maximumCost, mutationFactor);
                    break;
                case "swapMutation":
                    newPopulation.swapMutation(maximumCost, mutationFactor);
                    break;
                case "scrambleMutation":
                    newPopulation.scrambleMutation(maximumCost, mutationFactor);
                    break;
                case "inversionMutation":
                    newPopulation.inversionMutation(maximumCost, mutationFactor);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported method type: " + mutationMethod);
            }

            for (int i = 0; i < newPopulation.size(); i++) {
                if (!newPopulation.get(i).isValid(maximumCost)) {
                    newPopulation.get(i).reparation(maximumCost);
                }
            }

            Iterator<Bag> it = this.population.iterator();

            for (int j = 0; j < elitistRate * this.populationSize && j < population.getOrderedPopulationsize(); j++) {
                newPopulation.replace(it.next());
            }

            if (cmp % 10 == 0 && progressCallback != null) {
                progressCallback.accept(cmp, newPopulation);
            }

            stop = cmp >= maxIt;
            population = newPopulation;
        }
        this.iterations = cmp;
        return newPopulation.getBest();
    }

    private ArrayList<Couple> selection(){
        ArrayList<Couple> res = new ArrayList<>();
        RandomSelector randomSelector = new RandomSelector();
        Bag runningIndividu;
        Bag parent1;
        Bag parent2;
        for (int i = 0; i<population.size(); i++){
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
                ", costs=" + costs +
                ", BagObjects=" + BagObjects +
                ", population=" + population +
                ", populationSize=" + populationSize +
                '}';
    }
}
