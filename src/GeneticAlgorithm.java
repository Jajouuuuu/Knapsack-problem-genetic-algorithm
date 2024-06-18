import java.security.cert.PolicyNode;
import java.util.*;

public class GeneticAlgorithm {

    public static List<Integer> maximumCost;
    List<Integer> values;

    // je ne crois pas qu'il faille d'attribut cost ici. On évalue cout des sacs individuellement
    List<List<Integer>> costs;
    List<BagObject> BagObjects;
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
          /*  List<Integer> present = new ArrayList<>();
            for (int j = 0; j < BagObjects.size(); j++) {
                present.add(Math.random() < 0.5 ? 1 : 0);
            }*/
            Bag bag = Bag.createRandomBag();
            //reparation(bag);
            population.add(bag);
        }
        System.out.println("initial population : " + population);
        System.out.println("initial best : " + population.getBest());
    }

    // On fait le cross-over où le choix se base sur une proba de 1/2 (je suis pas sur sur de moi là je comprends pas les notes que j'ai
    // prise pendant l'explication du prof ...) -> c'est bien de faire 1/2, on pourrait faire autre proportion en mettant ce parametre
    // en argument de la fonction, mais c'est pas le plus important des parametre
    public Population crossover(ArrayList<Couple> parents) {
        Population res = new Population();
/*        List<Integer> contentChild1 = new ArrayList<>();
        List<Integer> contentChild2 = new ArrayList<>();

        for (int i = 0; i < parent1.content.size(); i++) {
            if (Math.random() < 0.5) {
                contentChild1.add(parent1.content.get(i));
                contentChild2.add(parent2.content.get(i));
            } else {
                contentChild1.add(parent2.content.get(i));
                contentChild2.add(parent1.content.get(i));
            }
        }

        Bag child1 = new Bag(parent1.BagObjects, contentChild1);
        Bag child2 = new Bag(parent2.BagObjects, contentChild2);
        return new Bag[]{child1, child2};*/
        for(int i = 0; i<parents.size(); i++){
            Bag child1 = new Bag();
            Bag child2 = new Bag();
            for (int j = 0; j < parents.get(0).father.content.size(); j++) {
                if(Math.random()<0.5){
                    child1.addBagObject(parents.get(i).father.content.get(j));
                    child2.addBagObject(parents.get(i).mother.content.get(j));
                }
                else{
                    child1.addBagObject(parents.get(i).mother.content.get(j));
                    child2.addBagObject(parents.get(i).father.content.get(j));
                }
            }
            res.add(child1);
            res.add(child2);
        }
        return res;
    }

    // là c'est pour remplacer le ac parents si y'a une meilleure fitness
    public void replacement(Bag parent1, Bag parent2, Bag child1, Bag child2) {
        // je me trompe peut etre, mais mettre maximum cost comme argument de betterThan n'a pas de sens
        if (child1.betterThan(parent1, maximumCost)) {
            parent1.content = new ArrayList<>(child1.content);
            parent1.cost = new ArrayList<>(child1.cost);
            parent1.value = child1.value;
        }
        if (child2.betterThan(parent2, maximumCost)) {
            parent2.content = new ArrayList<>(child2.content);
            parent2.cost = new ArrayList<>(child2.content);
            parent2.value = child2.value;
        }
    }

    // Normalement ça c'est l'algo du sujet mais jsuis pas sur sur de moi là

    //TODO Bizarre qu'on fasse la réparation cii étant donné qu'on l'applique spécifiquement à un bag + vérifier que ça fonctionne bien étant donné qu'on ne renvoie rien
    public void reparation(Bag bag) {
        List<Integer> decreasingUtility = new ArrayList<>();
        for (int i = 0; i < bag.content.size(); i++) {
            decreasingUtility.add(i);
        }
        decreasingUtility.sort((a, b) -> Integer.compare(
                (int) Bag.bagObjects.get(b).value,
                (int) Bag.bagObjects.get(a).value
        ));
        // sort works
        // c'est comme le répare qu'il y a dans mutation.
        for (int i = decreasingUtility.size()-1; i >= 0; i--) {
            if (bag.content.get(decreasingUtility.get(i)) == 1 && !bag.isValid(maximumCost)) {
                bag.removeBagObject(decreasingUtility.get(i));
            }
        }
        for (int i : decreasingUtility) {
            if (bag.content.get(i) == 0 && bag.isValidAddCostAndValue(i, maximumCost)) {
                bag.addBagObject(i);
            }
        }

    }


/*    public List<Bag> algorithmeGenetique(int sizePopulation, int numberOfGenerations, double mutationFactor) {
        population = new ArrayList<>();

        for (int generation = 0; generation < numberOfGenerations; generation++) {
            List<Bag> newPopulation = new ArrayList<>();
            while (newPopulation.size() < sizePopulation) {
                Bag parent1 = selection();
                Bag parent2 = selection();
                Bag[] enfants = crossover(parent1, parent2);
                enfants[0].mutation(this.maximumCost, mutationFactor);
                enfants[1].mutation(this.maximumCost, mutationFactor);
                reparation(enfants[0]);
                reparation(enfants[1]);
                replacement(parent1, parent2, enfants[0], enfants[1]);
                newPopulation.add(enfants[0]);
                newPopulation.add(enfants[1]);
            }
            population = newPopulation;
        }
        return population;
    }*/


    public Bag solve(double mutationRate, double elitistRate, int targetValue, double mutationFactor, int populationSize) {
        Population newPopulation = null;
        boolean containWinner = false;
        int cmp = 1;
        while (!containWinner){
            System.out.println("It n°: " + cmp++);
            newPopulation = crossover(selection());

            for(int i=0; i<newPopulation.size(); i++){
                if(Math.random()<mutationRate){
                    //TODO vérifier que j'utilise bien ta fonction + bizarre du coup on fait une mutation avec une proba de mutationRate + à chaque objet on le dchange avec un proba de mutationFactor
                    newPopulation.get(i).mutation(maximumCost, mutationFactor);
                }
            }

            for(int i=0; i<newPopulation.size(); i++){
                if(!newPopulation.get(i).isValid(maximumCost)){
                    reparation(newPopulation.get(i));
                }
            }

            Iterator<Bag> it = this.population.iterator();


            for(int j = 0; j<elitistRate*populationSize; j++){
                newPopulation.replace(it.next());
            }
            System.out.println("Best fitness : " + newPopulation.getBest().value);
            System.out.println("Best bag : " + newPopulation.getBest().content);
            containWinner = newPopulation.getBest().value >= targetValue;
            population = newPopulation;

        }

        return newPopulation.getBest();
    }

    // On choisit les parents pour la sélection
/*    private Bag selection() {
        // Le tournament en gros c'est pour la diversité c'est le compromis entre Exploration et Exploitation dont parlais le prof
        // si j'ai bien compris https://khayyam.developpez.com/articles/algo/genetic/
        int tournamentSize = 3;
        List<Bag> tournament = new ArrayList<>();
        for (int i = 0; i < tournamentSize; i++) {
            int randomIndex = (int) (Math.random() * population.size());
            tournament.add(population.get(randomIndex));
        }
        Bag best = tournament.get(0);
        for (Bag bag : tournament) {
            if (bag.betterThan(best, maximumCost)) {
                best = bag;
            }
        }
        return best;
    }*/

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
        return "GeneticAlgorithm{" +
                "values=" + values +
                ", costs=" + costs +
                ", BagObjects=" + BagObjects +
                ", population=" + population +
                ", populationSize=" + populationSize +
                '}';
    }
}