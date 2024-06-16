import java.util.ArrayList;
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
        int poidsMaximum = 348;
        List<Integer> poids = List.of(95, 4, 60, 32, 23, 72, 80, 62, 65, 46);
        List<Integer> profits = List.of(55, 10, 47, 5, 4, 50, 8, 61, 85, 87);
        ApplicationGenetique algo = new ApplicationGenetique(poidsMaximum, poids, profits);
        int taillePopulation = 50;
        int nombreDeGenerations = 100;
        List<Sac> resultats = algo.algorithmeGenetique(taillePopulation, nombreDeGenerations);
        for (Sac sac : resultats) {
            System.out.println(sac);
        }
    }
}
