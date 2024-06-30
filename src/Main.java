import algorithm.GeneticAlgorithm;
import representation.Bag;
import testsCases.TestProblem;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        String filePath = chooseProblemFile(scanner);
        TestProblem testProblem = TestProblem.readFromFile(filePath);
        Bag bag = new Bag(testProblem.getCosts(), testProblem.getValues(), testProblem.getMaximumCost());

        String mutationMethod = chooseMutationMethod(scanner);
        String crossoverType = chooseCrossoverType(scanner);

        GeneticAlgorithm algo = new GeneticAlgorithm(bag, 50);
        algo.filePath =filePath;
        Bag solutionOptimale = algo.solve(0.7, 0.5, mutationMethod, crossoverType, null);

        System.out.println("\nSolution optimale trouvée :");
        System.out.println(solutionOptimale);
        System.out.println("Optimal find at iteration: " + algo.optiFindAt);

        scanner.close();
    }

    private static String chooseProblemFile(Scanner scanner) {
        System.out.println("Choix du fichier de problème :");
        System.out.println("1. mknap1.txt - 15 objets & 10 contraintes");
        System.out.println("2. mknap2.txt - 20 objets & 10 contraintes");
        System.out.println("3. mknap3.txt - 28 objets & 10 contraintes");
        System.out.println("4. mknap4.txt - 39 objets & 5 contraintes");
        System.out.println("5. mknap5.txt - 50 objets & 5 contraintes");

        System.out.print("Choisissez le fichier de problème (1-5) : ");
        int problemChoice = scanner.nextInt();
        String filePath;
        switch (problemChoice) {
            case 1:
                filePath = "src/testsCases/mknap1.txt";
                break;
            case 2:
                filePath = "src/testsCases/mknap2.txt";
                break;
            case 3:
                filePath = "src/testsCases/mknap3.txt";
                break;
            case 4:
                filePath = "src/testsCases/mknap4.txt";
                break;
            case 5:
                filePath = "src/testsCases/mknap5.txt";
                break;
            default:
                System.out.println("Choix invalide. Utilisation de mknap5.txt par défaut.");
                filePath = "src/testsCases/mknap5.txt";
                break;
        }
        return filePath;
    }

    private static String chooseMutationMethod(Scanner scanner) {
        System.out.println("\nChoix de la méthode de mutation :");
        System.out.println("1. flipMutation");
        System.out.println("2. swapMutation");
        System.out.println("3. scrambleMutation");
        System.out.println("4. inversionMutation");

        System.out.print("Choisissez la méthode de mutation (1-4) : ");
        int mutationChoice = scanner.nextInt();
        String mutationMethod;
        switch (mutationChoice) {
            case 1:
                mutationMethod = "flipMutation";
                break;
            case 2:
                mutationMethod = "swapMutation";
                break;
            case 3:
                mutationMethod = "scrambleMutation";
                break;
            case 4:
                mutationMethod = "inversionMutation";
                break;
            default:
                System.out.println("Choix invalide. Utilisation de scrambleMutation par défaut.");
                mutationMethod = "scrambleMutation";
                break;
        }
        return mutationMethod;
    }

    private static String chooseCrossoverType(Scanner scanner) {
        System.out.println("\nChoix du type de croisement :");
        System.out.println("1. singlePointCrossover");
        System.out.println("2. twoPointCrossover");
        System.out.println("3. uniformCrossover");

        System.out.print("Choisissez le type de croisement (1-3) : ");
        int crossoverChoice = scanner.nextInt();
        String crossoverType;
        switch (crossoverChoice) {
            case 1:
                crossoverType = "singlePointCrossover";
                break;
            case 2:
                crossoverType = "twoPointCrossover";
                break;
            case 3:
                crossoverType = "uniformCrossover";
                break;
            default:
                System.out.println("Choix invalide. Utilisation de twoPointCrossover par défaut.");
                crossoverType = "twoPointCrossover";
                break;
        }
        return crossoverType;
    }
}
