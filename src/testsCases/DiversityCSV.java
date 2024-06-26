package testsCases;

import algorithm.GeneticAlgorithm;
import representation.Bag;
import representation.Population;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class DiversityCSV {
    private FileWriter fileWriter;

    public DiversityCSV(String filePath) throws IOException {
        fileWriter = new FileWriter(filePath);
        fileWriter.append("Iteration, Diversity, Elitism Rate, Mutation Rate, Population Size, Crossover Type, Mutation Type\n");
    }

    public synchronized void addResult(int iteration, int diversity, double elitismRate, double mutationRate,
                                       int populationSize, String crossoverType, String mutationType) throws IOException {
        fileWriter.append(String.format("%d, %d, %.2f, %.2f, %d, %s, %s\n",
                iteration, diversity, elitismRate, mutationRate, populationSize, crossoverType, mutationType));
    }

    public void close() throws IOException {
        fileWriter.flush();
        fileWriter.close();
    }

    public static void main(String[] args) throws IOException {
        TestProblem testProblem = TestProblem.readFromFile("src/testsCases/mknap2.txt");
        DiversityCSV diversityCSV = new DiversityCSV("diversity_results.csv");

        // J'ai fait comme l'a demandé Alice pour étuder la diversité on se focus sur le problème 2 avec une population de 300 et on fait varier tout le reste
        int populationSize = 300;

        List<Double> elitismRates = Arrays.asList(0.05, 0.10, 0.15, 0.20, 0.25, 0.3, 0.4, 0.5);
        List<Double> mutationRates = Arrays.asList(0.2, 0.4, 0.6, 0.8);
        List<String> crossoverTypes = Arrays.asList("crossover", "singlePointCrossover", "twoPointCrossover");
        List<String> mutationTypes = Arrays.asList("flipMutation", "swapMutation", "scrambleMutation", "inversionMutation");

        for (double elitismRate : elitismRates) {
            for (double mutationRate : mutationRates) {
                for (String crossoverType : crossoverTypes) {
                    for (String mutationType : mutationTypes) {
                        GeneticAlgorithm algo = new GeneticAlgorithm(testProblem.getMaximumCost(), testProblem.getCosts(), testProblem.getValues(), populationSize);
                        // /!\ ça c'est pour sauvegader la diversité toutes les 10 itérations de l'algo -> j'ajoute un BiConsumer dans la méthode solve pour ça
                        // on va l'utiliser qu'ici ce paramètre donc quand on s'en sert pas on peut juste laisser le paramètre à null et ca changera pas l'algo
                        algo.solve(elitismRate, 100, mutationRate, populationSize, mutationType, crossoverType,
                                (iteration, population) -> {
                                    try {
                                        int diversity = population.getOrderedPopulationsize(); // Assuming this method gives diversity count
                                        diversityCSV.addResult(iteration, diversity, elitismRate, mutationRate, populationSize, crossoverType, mutationType);
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
