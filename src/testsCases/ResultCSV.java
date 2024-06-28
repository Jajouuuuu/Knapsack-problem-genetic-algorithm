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

public class ResultCSV {
    private FileWriter fileWriter;

    public ResultCSV(String filePath) throws IOException {
        fileWriter = new FileWriter(filePath);
        fileWriter.append("Iteration, Best Fitness,Elitisme Rate, Mutation Rate, Population Size, Crossover Type, Mutation Type, Duration (ms)\n");
    }

    public synchronized void addResult(int iteration, double bestFitness, double elitismeRate, double mutationRate, int populationSize,
                                       String crossoverType, String mutationType, long duration) throws IOException {
        fileWriter.append(String.format("%d, %.2f, %.2f, %.2f, %d, %s, %s, %d\n",
                iteration, bestFitness, elitismeRate, mutationRate, populationSize, crossoverType, mutationType, duration));
    }

    public void close() throws IOException {
        fileWriter.flush();
        fileWriter.close();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        TestProblem testProblem = TestProblem.readFromFile("src/testsCases/mknap3.txt");
        ResultCSV resultCSV = new ResultCSV("results_pop_1000_2.csv");

        int populationSize = 1000;
        List<Double> elitismRates = Arrays.asList(0.25, 0.3, 0.4, 0.5);
        List<Double> mutationRates = Arrays.asList(0.2, 0.4, 0.6, 0.8);
        List<String> crossoverTypes = Arrays.asList("crossover", "singlePointCrossover", "twoPointCrossover");
        List<String> mutationTypes = Arrays.asList("flipMutation", "swapMutation", "scrambleMutation", "inversionMutation");

        int numThreads = Runtime.getRuntime().availableProcessors() * 2;
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        for (double elitismRate : elitismRates) {
            for (double mutationRate : mutationRates) {
                for (String crossoverType : crossoverTypes) {
                    for (String mutationType : mutationTypes) {
                        executorService.submit(() -> {
                            try {
                                long startTime = System.currentTimeMillis();
                                int maxIt = (int) ((int) populationSize * 0.30);
                                GeneticAlgorithm algo = new GeneticAlgorithm(testProblem.getMaximumCost(), testProblem.getCosts(), testProblem.getValues(), populationSize);
                                Bag solutionOptimale = algo.solve(elitismRate, maxIt, mutationRate, populationSize, mutationType, crossoverType, null);
                                long endTime = System.currentTimeMillis();
                                long duration = endTime - startTime;

                                synchronized (resultCSV) {
                                    resultCSV.addResult(algo.iterations, solutionOptimale.value, elitismRate, mutationRate, populationSize, crossoverType, mutationType, duration);
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
