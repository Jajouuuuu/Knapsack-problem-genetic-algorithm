import java.util.List;
import java.util.ArrayList;

public class ApplicationGenetique {

    int poidsMaximum;
    List<Integer> profits;
    List<Integer> poids;
    List<Objet> objets;
    List<Sac> population;
    List<Sac> populationInitiale;
    List<Sac> populationFinale;

    public ApplicationGenetique(int poidsMaximum, List<Integer> poids, List<Integer> profits) {
        this.poidsMaximum = poidsMaximum;
        this.profits = profits;
        this.poids = poids;
        this.objets = new ArrayList<>();
        for (int i = 0; i < profits.size(); i++) {
            objets.add(new Objet(i, poids.get(i), profits.get(i)));
        }
    }

    public Sac[] croisement(Sac pere, Sac mere) {
        return null;
    }

    public void remplacement(Sac pere, Sac mere, Sac enfant1, Sac enfant2) {

    }

    public List<Sac> algorithmeGenetique(int taillePopulation, int nombreDeGenerations) {
        return null;
    }
}
