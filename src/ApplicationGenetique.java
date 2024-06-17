import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ApplicationGenetique {

    List<Integer> maximumCost;
    List<Integer> values;

    // je ne crois pas qu'il faille d'attribut cost ici. On évalue cout des sacs individuellement
    List<List<Integer>> costs;
    List<Object> objects;
    List<Bag> population;
    List<Bag> initialPopulation;
    List<Bag> finalPopulation;

    public ApplicationGenetique(List<Integer> maximumCost, List<List<Integer>> costs, List<Integer> values) {
        this.maximumCost = maximumCost;
        this.values = values;
        this.costs = costs;
        this.objects = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            objects.add(new Object(i, costs.get(i), values.get(i)));
        }
    }

    // On fait le cross-over où le choix se base sur une proba de 1/2 (je suis pas sur sur de moi là je comprends pas les notes que j'ai
    // prise pendant l'explication du prof ...) -> c'est bien de faire 1/2, on pourrait faire autre proportion en mettant ce parametre
    // en argument de la fonction, mais c'est pas le plus important des parametre
    public Bag[] crossover(Bag parent1, Bag parent2) {
        List<Integer> contentChild1 = new ArrayList<>();
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

        Bag child1 = new Bag(parent1.objects, contentChild1);
        Bag child2 = new Bag(parent2.objects, contentChild2);
        return new Bag[]{child1, child2};
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
    public void reparation(Bag bag) {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < bag.content.size(); i++) {
            indices.add(i);
        }
        indices.sort((a, b) -> Integer.compare(
                (int) bag.objects.get(b).value,
                (int) bag.objects.get(a).value
        ));
        // sort works
        // c'est comme le répare qu'il y a dans mutation.
        for (int i = indices.size()-1; i >= 0; i--) {
            if (bag.content.get(indices.get(i)) == 1 && !bag.isValid(maximumCost)) {
                bag.removeObject(indices.get(i));
            }
        }
        for (int i : indices) {
            if (bag.content.get(i) == 0 && bag.isValidAddCostAndValue(i, maximumCost)) {
                bag.addObject(i);
            }
        }

    }


    public List<Bag> algorithmeGenetique(int sizePopulation, int numberOfGenerations, double mutationFactor) {
        population = new ArrayList<>();
        for (int i = 0; i < sizePopulation; i++) {
            List<Integer> present = new ArrayList<>();
            for (int j = 0; j < objects.size(); j++) {
                present.add(Math.random() < 0.5 ? 1 : 0);
            }
            Bag bag = new Bag(objects, present);
            reparation(bag);
            population.add(bag);
        }
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
    }

    // On choisit les parents pour la sélection
    private Bag selection() {
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
    }
}