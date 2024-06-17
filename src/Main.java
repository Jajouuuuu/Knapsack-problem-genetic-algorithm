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
        List<Integer> maximumCost = List.of(100, 10, 70, 40, 30, 80, 90, 70, 75, 50);
        List<List<Integer>> costs = List.of(
                List.of(95, 4, 60, 32, 23, 72, 80, 62, 65, 46),
                List.of(2, 4, 6, 3, 2, 7, 8, 6, 6, 4),
                List.of(950, 40, 600, 320, 230, 720, 800, 620, 650, 460)
        );
        List<Integer> values = List.of(55, 10, 47);
        ApplicationGenetique algo = new ApplicationGenetique(maximumCost, costs, values);
        int taillePopulation = 3;
        int nombreDeGenerations = 5;
        double mutationFactor = 0.1;
        List<Bag> resultats = algo.algorithmeGenetique(taillePopulation, nombreDeGenerations, mutationFactor);
        for (Bag bag : resultats) {
            System.out.println(bag);
        }
    }
}
