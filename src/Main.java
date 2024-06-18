import java.util.List;

public class Main {
    public static void main(String[] args) {
        /*Objet obj1 = new Objet(1, 5, 8);
        Objet obj2 = new Objet(2, 5, 45);

        List<Objet> objs = new ArrayList<>();
        objs.add(obj1);
        objs.add(obj2);

        List<Integer> present = new ArrayList<>();
        for (int i = 0; i < objs.size(); i++) {
            present.add(0);
        }

        Sac sac = new Sac(objs, present);
        sac.ajouteObjet(1);
        System.out.println(sac);
        sac.supprimeObjet(1);
        System.out.println(sac);*/
        List<Integer> maximumCost = List.of(200, 160, 700, 400, 300, 850, 950, 700, 750, 550);
        List<List<Integer>> costs = List.of(
                List.of(95, 4, 60, 32, 23, 72, 80, 62, 65, 46),
                List.of(50, 10, 20, 32, 23, 72, 8, 2, 50, 46),
                List.of(30, 30, 10, 52, 43, 12, 50, 20, 20, 76),
                List.of(90, 20, 90, 72, 83, 42, 60, 40, 50, 60),
                List.of(50, 50, 50, 52, 53, 52, 50, 40, 50, 60),
                List.of(90, 90, 90, 92, 93, 92, 90, 90, 90, 90),
                List.of(40, 40, 40, 42, 43, 42, 40, 40, 40, 40),
                List.of(70, 70, 70, 72, 73, 72, 70, 70, 70, 70),
                List.of(2, 4, 6, 3, 2, 7, 8, 6, 6, 40),
                List.of(950, 40, 600, 320, 230, 720, 800, 620, 650, 460)
        );
        List<Integer> values = List.of(55, 10, 47, 30, 20, 60, 90, 10, 78, 10);
        GeneticAlgorithm algo = new GeneticAlgorithm(maximumCost, costs, values, 10);
        int taillePopulation = 10;
        int nombreDeGenerations = 100;
        double mutationFactor = 0.1;
        //List<Bag> resultats = algo.algorithmeGenetique(taillePopulation, nombreDeGenerations, mutationFactor);
        /*for (Bag bag : resultats) {
            System.out.println(bag);
        }*/

        //System.out.println(Bag.createRandomBag());
        Bag res = algo.solve(0.1, .04, 270, 0.4, taillePopulation);
        System.out.println(res);

    }
}
