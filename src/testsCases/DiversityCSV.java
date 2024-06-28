package testsCases;

import algorithm.GeneticAlgorithm;
import representation.Bag;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Classe permettant de générer un fichier CSV pour enregistrer les résultats de diversité
 * obtenus lors de l'exécution d'algorithmes génétiques avec différentes configurations.
 */
public class DiversityCSV {
    private FileWriter fileWriter;

    /**
     * Constructeur initialisant le fichier CSV avec les en-têtes.
     *
     * @param filePath Chemin vers le fichier CSV.
     * @throws IOException En cas d'erreur d'entrée/sortie lors de la création du fichier.
     */
    public DiversityCSV(String filePath) throws IOException {
        fileWriter = new FileWriter(filePath);
        fileWriter.append("Iteration, Diversity, Elitism Rate, Mutation Rate, Population Size, Crossover Type, Mutation Type\n");
    }

    /**
     * Ajoute un résultat de diversité dans le fichier CSV.
     *
     * @param iteration Numéro de l'itération.
     * @param diversity Diversité mesurée.
     * @param elitismRate Taux d'élitisme utilisé.
     * @param mutationRate Taux de mutation utilisé.
     * @param populationSize Taille de la population.
     * @param crossoverType Type de crossover utilisé.
     * @param mutationType Type de mutation utilisé.
     * @throws IOException En cas d'erreur d'entrée/sortie lors de l'écriture dans le fichier.
     */
    public synchronized void addResult(int iteration, int diversity, double elitismRate, double mutationRate,
                                       int populationSize, String crossoverType, String mutationType) throws IOException {
        fileWriter.append(String.format("%d, %d, %.2f, %.2f, %d, %s, %s\n",
                iteration, diversity, elitismRate, mutationRate, populationSize, crossoverType, mutationType));
    }

    /**
     * Ferme le FileWriter et termine l'écriture dans le fichier CSV.
     *
     * @throws IOException En cas d'erreur d'entrée/sortie lors de la fermeture du fichier.
     */
    public void close() throws IOException {
        fileWriter.flush();
        fileWriter.close();
    }

    /**
     * Méthode principale pour tester et enregistrer les résultats de diversité.
     *
     * @param args Arguments de la ligne de commande (non utilisés ici).
     * @throws IOException En cas d'erreur d'entrée/sortie lors de la lecture ou de l'écriture de fichiers.
     */
    public static void main(String[] args) throws IOException {
        // Chemin pour l'enregistrement du CSV
        DiversityCSV diversityCSV = new DiversityCSV("new_diversity_results.csv");

        // Création d'une instance du problème
        TestProblem testProblem = TestProblem.readFromFile("src/testsCases/mknap3.txt");
        Bag bag = new Bag(testProblem.getCosts(), testProblem.getValues(), testProblem.getMaximumCost());

        // TODO : je suis pas sur d'avoir tester avec les bons paramètre ici
        // Valeur des paramètres à tester
        List<Double> elitismRates = Arrays.asList(0.05, 0.10, 0.15, 0.20, 0.25, 0.3, 0.4, 0.5);
        List<Double> mutationRates = Arrays.asList(0.2, 0.4, 0.6, 0.8);
        // TODO : si on vire le crossover il faut update ici
        List<String> crossoverTypes = Arrays.asList("crossover", "singlePointCrossover", "twoPointCrossover");
        List<String> mutationTypes = Arrays.asList("flipMutation", "swapMutation", "scrambleMutation", "inversionMutation");

        for (double elitismRate : elitismRates) {
            for (double mutationRate : mutationRates) {
                for (String crossoverType : crossoverTypes) {
                    for (String mutationType : mutationTypes) {
                        GeneticAlgorithm algo = new GeneticAlgorithm(bag, 150);
                        // /!\ ça c'est pour sauvegader la diversité toutes les 10 itérations de l'algo -> j'ajoute un BiConsumer dans la méthode solve pour ça
                        // on va l'utiliser qu'ici ce paramètre donc quand on s'en sert pas on peut juste laisser le paramètre à null et ca changera pas l'algo
                        algo.solve(elitismRate, mutationRate, mutationType, crossoverType,
                                (iteration, population) -> {
                                    try {
                                        int diversity = population.getOrderedPopulationsize();
                                        diversityCSV.addResult(iteration, diversity, elitismRate, mutationRate, algo.populationSize, crossoverType, mutationType);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                });
                    }
                }
            }
        }
        diversityCSV.close();
    }
}
