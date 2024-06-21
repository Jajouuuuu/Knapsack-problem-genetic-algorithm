package algorithm;

import java.util.*;

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

    public GeneticAlgorithm(List<Integer> maximumCost, List<List<Integer>> costs, List<Integer> values, int k) {
        GeneticAlgorithm.maximumCost = maximumCost;
        this.values = values;
        this.costs = costs;
        this.BagObjects = new ArrayList<>();
        this.populationSize = 2 * k;
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

    // TODO : j'ai retiré le mutation rate comme on s'en servait pas, je pense que y'a du avoir un micmac avec mutationFactor
    // on peut le remettre si besoin, faudrait vérifier si cette fonction reste correcte
    // TODO : je comprends pas à quoi sert la targetValue ? comment on peut passer en paramètre l'optimale qu'on cherche ?
    public Bag solve(double elitistRate, int maxIt, double mutationFactor, int populationSize) {
        Population newPopulation = null;
        boolean stop = false;
        int cmp = 1;
        while (!stop) {
            System.out.println("It n°: " + cmp++);
            newPopulation = crossover(selection());

            newPopulation.mutation(maximumCost, mutationFactor);

            for (int i = 0; i < newPopulation.size(); i++) {
                if (!newPopulation.get(i).isValid(maximumCost)) {
                    newPopulation.get(i).reparation(maximumCost);
                }
            }

            Iterator<Bag> it = this.population.iterator();

            for (int j = 0; j < elitistRate * populationSize && j< population.getOrderedPopulationsize(); j++) {
                newPopulation.replace(it.next());
            }
            System.out.println("Best fitness : " + newPopulation.getBest().value);
            System.out.println("Best bag : " + newPopulation.getBest().content);
            stop = cmp >= maxIt;
            population = newPopulation;
        }
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