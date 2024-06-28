package testsCases;
import algorithm.GeneticAlgorithm;
import representation.Bag;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Classe permettant de générer un fichier CSV pour enregistrer les résultats obtenus lors de l'exécution
 * de divers algorithmes génétiques avec différentes configurations.
 */
public class ResultCSV {
    private FileWriter fileWriter;

    /**
     * Constructeur initialisant le fichier CSV avec les en-têtes.
     *
     * @param filePath Chemin vers le fichier CSV.
     * @throws IOException En cas d'erreur d'entrée/sortie lors de la création du fichier.
     */
    public ResultCSV(String filePath) throws IOException {
        fileWriter = new FileWriter(filePath);
        fileWriter.append("Iteration, Best Fitness,Elitisme Rate, Mutation Rate, Population Size, Crossover Type, Mutation Type, Duration (ms)\n");
    }

    /**
     * Ajoute un résultat dans le fichier CSV.
     *
     * @param iteration Numéro de l'itération.
     * @param bestFitness Meilleure fitness obtenue.
     * @param elitismeRate Taux d'élitisme utilisé.
     * @param mutationRate Taux de mutation utilisé.
     * @param populationSize Taille de la population.
     * @param crossoverType Type de crossover utilisé.
     * @param mutationType Type de mutation utilisé.
     * @param duration Durée d'exécution en millisecondes.
     * @throws IOException En cas d'erreur d'entrée/sortie lors de l'écriture dans le fichier.
     */
    public synchronized void addResult(int iteration, double bestFitness, double elitismeRate, double mutationRate, int populationSize,
                                       String crossoverType, String mutationType, long duration) throws IOException {
        fileWriter.append(String.format("%d, %.2f, %.2f, %.2f, %d, %s, %s, %d\n",
                iteration, bestFitness, elitismeRate, mutationRate, populationSize, crossoverType, mutationType, duration));
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
     * Méthode principale pour tester et enregistrer les résultats des algorithmes génétiques.
     *
     * @param args Arguments de la ligne de commande (non utilisés ici).
     * @throws IOException En cas d'erreur d'entrée/sortie lors de la lecture ou de l'écriture de fichiers.
     * @throws InterruptedException En cas d'interruption lors de l'attente de la terminaison des threads.
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        // Chemin pour l'enregistrement du CSV
        ResultCSV resultCSV = new ResultCSV("results_nouvelle_version_algo_test_pop_300.csv");

        // Création d'une instance du problème
        TestProblem testProblem = TestProblem.readFromFile("src/testsCases/mknap3.txt");
        Bag bag = new Bag(testProblem.getCosts(), testProblem.getValues(), testProblem.getMaximumCost());

        List<Double> elitismRates = Arrays.asList(0.1, 0.15, 0.2, 0.25);
        List<Double> mutationRates = Arrays.asList(0.1, 0.15, 0.2, 0.25);
        List<String> crossoverTypes = Arrays.asList("crossover", "singlePointCrossover", "twoPointCrossover");
        List<String> mutationTypes = Arrays.asList("scrambleMutation", "inversionMutation");

        int numThreads = Runtime.getRuntime().availableProcessors() * 2;
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        for (double elitismRate : elitismRates) {
            for (double mutationRate : mutationRates) {
                for (String crossoverType : crossoverTypes) {
                    for (String mutationType : mutationTypes) {
                        executorService.submit(() -> {
                            try {
                                long startTime = System.currentTimeMillis();
                                GeneticAlgorithm algo = new GeneticAlgorithm(bag, 150);
                                Bag solutionOptimale = algo.solve(elitismRate, mutationRate, mutationType, crossoverType, null);
                                long endTime = System.currentTimeMillis();
                                long duration = endTime - startTime;

                                synchronized (resultCSV) {
                                    resultCSV.addResult(algo.iterations, solutionOptimale.value, elitismRate, mutationRate, algo.populationSize, crossoverType, mutationType, duration);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }
            }
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.HOURS);

        resultCSV.close();
    }
}