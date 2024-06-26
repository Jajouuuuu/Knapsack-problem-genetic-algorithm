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
        TestProblem testProblem = TestProblem.readFromFile("src/testsCases/mknap1.txt");
        ResultCSV resultCSV = new ResultCSV("results.csv");

        List<Double> elitismRates = Arrays.asList( 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0);
        List<Integer> populationSizes = Arrays.asList(100, 300);
        String crossoverType = "crossover";
        String mutationType = "flipMutation";
        List<Double> mutationRates = Arrays.asList(0.1, 0.2, 0.3, 0.4, 0.5);

        int numThreads = Runtime.getRuntime().availableProcessors() * 2;
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        for (double mutationRate : mutationRates) {
            for (int populationSize : populationSizes) {
                for (double elitismRate : elitismRates) {
                    executorService.submit(() -> {
                        try {
                            long startTime = System.currentTimeMillis();
                            int maxIt = (int) ((int) populationSize * 0.10);
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

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.HOURS);

        resultCSV.close();
    }
}
