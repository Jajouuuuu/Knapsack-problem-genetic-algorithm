import algorithm.GeneticAlgorithm;
import representation.Bag;
import testsCases.TestProblem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static testsCases.TestProblem.readFromFile;

public class Main {
    public static void main(String[] args) throws IOException {
        // On initialise des valeurs en plusieurs dimensions pour le problème
        //Ici on a 6 objets et 10 contraintes
         /*List<Integer> values = List.of(
                560, 1125, 300, 620, 2100, 431, 68, 328, 47, 122,
                322, 196, 41, 25, 425, 4260, 416, 115, 82, 22,
                631, 132, 420, 86, 42, 103, 215, 81, 91, 26,
                49, 420, 316, 72, 71, 49, 108, 116, 90, 738,
                1811, 430, 3060, 215, 58, 296, 620, 418, 47, 81
        );
        List<List<Integer>> costs = List.of(
                List.of(40, 16, 38, 8, 38),
                List.of(91, 92, 39, 71, 52),
                List.of(10, 41, 32, 30, 30),
                List.of(30, 16, 71, 60, 42),
                List.of(160, 150, 80, 200, 170),
                List.of(20, 23, 26, 18, 9),
                List.of(3, 4, 5, 6, 7),
                List.of(12, 18, 40, 30, 20),
                List.of(3, 6, 8, 4, 0),
                List.of(18, 0, 12, 8, 3),
                List.of(9, 12, 30, 31, 21),
                List.of(25, 8, 15, 6, 4),
                List.of(1, 2, 0, 3, 1),
                List.of(1, 1, 1, 0, 2),
                List.of(10, 0, 23, 18, 14),
                List.of(280, 200, 100, 60, 310),
                List.of(10, 20, 0, 21, 8),
                List.of(8, 6, 20, 4, 4),
                List.of(1, 2, 3, 0, 6),
                List.of(1, 1, 0, 2, 1),
                List.of(49, 70, 40, 32, 18),
                List.of(8, 9, 6, 15, 15),
                List.of(21, 22, 8, 31, 38),
                List.of(6, 4, 0, 2, 10),
                List.of(1, 1, 6, 2, 4),
                List.of(5, 5, 4, 7, 8),
                List.of(10, 10, 22, 8, 6),
                List.of(8, 6, 4, 2, 0),
                List.of(2, 4, 6, 8, 0),
                List.of(1, 0, 1, 0, 3),
                List.of(0, 4, 5, 2, 0),
                List.of(10, 12, 14, 8, 10),
                List.of(42, 8, 8, 6, 6),
                List.of(6, 4, 2, 7, 1),
                List.of(4, 3, 8, 1, 3),
                List.of(8, 0, 0, 0, 0),
                List.of(0, 10, 20, 0, 3),
                List.of(10, 0, 0, 20, 5),
                List.of(1, 6, 6, 8, 4),
                List.of(40, 28, 12, 14, 0),
                List.of(86, 93, 6, 20, 30),
                List.of(11, 9, 6, 2, 12),
                List.of(120, 30, 80, 40, 16),
                List.of(8, 22, 13, 6, 18),
                List.of(3, 0, 6, 1, 3),
                List.of(32, 36, 22, 14, 16),
                List.of(28, 45, 14, 20, 22),
                List.of(13, 13, 0, 12, 30),
                List.of(2, 2, 1, 0, 4),
                List.of(4, 2, 2, 1, 0)
        );


               List.of(8, 12, 13, 64, 22, 41),
                List.of(8, 12, 13, 75, 22, 41),
                List.of(3, 6, 4, 18, 6, 4),
                List.of(5, 10, 8, 32, 6, 12),
                List.of(5, 13, 8, 42, 6, 20),
                List.of(5, 13, 8, 48, 6, 20),
                List.of(0, 0, 0, 0, 8, 0),
                List.of(3, 0, 4, 0, 8, 0),
                List.of(3, 2, 4, 0, 8, 4),
                List.of(3, 2, 4, 8, 8, 4)


       List<Integer> maximumCost = List.of(800, 650, 550, 550, 650);*/
        TestProblem testProblem = readFromFile("src/testsCases/mknap4.txt");
        System.out.println(testProblem);
        GeneticAlgorithm algo = new GeneticAlgorithm(testProblem.getMaximumCost(), testProblem.getCosts(), testProblem.getValues(), 500);
        int maxIt = 300;
        int taillePopulation = 1000;
        String mutationMethod = "flipMutation";     // either : flipMutation, swapMutation, scrambleMutation or inversionMutation
        String crossOver = "crossover";  // either : crossover, singlePointCrossover, twoPointCrossover, uniformCrossover
        Bag solution_optimale = algo.solve(0.1, maxIt, 0.4, taillePopulation, mutationMethod, crossOver);
        System.out.println(solution_optimale);
/*

        Bag newBag = new Bag();
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < Bag.bagObjects.size(); i++) {
            indices.add(i);
        }
        Collections.shuffle(indices);
        indices = List.of(1,2,5,0,3,4);
        System.out.println(indices);
        int i = 0;
        for (int index : indices) {
            System.out.println("indexe" + index);
            System.out.println("Objects indiexe : " + Bag.bagObjects.get(index));
            if (newBag.isValidAdd(index)) {
                newBag.addBagObject(index);
            }
            System.out.println(newBag);
        }
        System.out.println(newBag);
*/




      /*  Bag bag = Bag.createRandomBag();
        System.out.println(bag);
        bag.addBagObject(0);
        System.out.println(bag);*/

     /*   Bag newBag = new Bag();
        System.out.println(newBag);
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < Bag.bagObjects.size(); i++) {
            indices.add(i);
        }
        System.out.println(indices);
        Collections.shuffle(indices);
        System.out.println(indices);

        System.out.println("\n\n\n Début de la boucle \n\n\n");
        for (int index : indices) {
            System.out.println("index "  + index);
            System.out.println("On tente d'ajouter le : " + index + " : " + newBag.isValidAdd(index));
            if (newBag.isValidAdd(index)) {
                System.out.println("avant ajout : " +newBag);
                newBag.addBagObject(index);
                System.out.println("après ajout : " +newBag);
            }
        }
        System.out.println(newBag);*/

    /*    List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < Bag.bagObjects.size(); i++) {
            indices.add(i);
        }
        System.out.println(indices);
        Collections.shuffle(indices);
        System.out.println(indices);
        System.out.println(indices);
        Collections.shuffle(indices);
        System.out.println(indices);
        System.out.println(indices);
        Collections.shuffle(indices);
        System.out.println(indices);
        System.out.println(indices);
        Collections.shuffle(indices);
        System.out.println(indices);*/

    }
}