import algorithm.GeneticAlgorithm;
import representation.Bag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // On initialise des valeurs en plusieurs dimensions pour le problème

        //Ici on a 6 objets et 10 contraintes
        List<Integer> maximumCost = List.of(80, 96, 20, 36, 44, 48, 10, 18, 22, 24);
        List<List<Integer>> costs = List.of(
                List.of(8,8,3,5,5,5,0,3,3,3),
                List.of(12,12,6,10,13,13,0,0,2,2),
                List.of(13,13,4,8,8,8,0,4,4,4),
                List.of(64,75,18,32,42,48,0,0,0,8),
                List.of(22,22,6, 6, 6,6,8,8,8,8),
                List.of(41,41,4,12,20,20,0,0,4,4)


           /*     List.of(8, 12, 13, 64, 22, 41),
                List.of(8, 12, 13, 75, 22, 41),
                List.of(3, 6, 4, 18, 6, 4),
                List.of(5, 10, 8, 32, 6, 12),
                List.of(5, 13, 8, 42, 6, 20),
                List.of(5, 13, 8, 48, 6, 20),
                List.of(0, 0, 0, 0, 8, 0),
                List.of(3, 0, 4, 0, 8, 0),
                List.of(3, 2, 4, 0, 8, 4),
                List.of(3, 2, 4, 8, 8, 4)
                */

        );
       List<Integer> values = List.of(100, 600, 1200, 2400,500, 2000);
        GeneticAlgorithm algo = new GeneticAlgorithm(maximumCost, costs, values, 500);
        int maxIt = 100;
        int taillePopulation = 1000;
        Bag solution_optimale = algo.solve(0.1, maxIt, 0.4, taillePopulation);
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