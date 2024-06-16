import java.util.*;

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

    // On fait le cross-over où le choix se base sur une proba de 1/2 (je suis pas sur sur de moi là je comprends pas les notes que j'ai
    // prisependant l'explication du prof ...)
    public Sac[] croisement(Sac pere, Sac mere) {
        List<Integer> listeEnfant1 = new ArrayList<>();
        List<Integer> listeEnfant2 = new ArrayList<>();

        for (int i = 0; i < pere.liste.size(); i++) {
            if (Math.random() < 0.5) {
                listeEnfant1.add(pere.liste.get(i));
                listeEnfant2.add(mere.liste.get(i));
            } else {
                listeEnfant1.add(mere.liste.get(i));
                listeEnfant2.add(pere.liste.get(i));
            }
        }

        Sac enfant1 = new Sac(pere.objets, listeEnfant1);
        Sac enfant2 = new Sac(mere.objets, listeEnfant2);
        return new Sac[]{enfant1, enfant2};
    }

    // là c'est pour remplacer le ac parents si y'a une meilleure fitness
    public void remplacement(Sac pere, Sac mere, Sac enfant1, Sac enfant2) {
        if (enfant1.estMeilleur(pere, poidsMaximum)) {
            pere.liste = new ArrayList<>(enfant1.liste);
            pere.poids = enfant1.poids;
            pere.valeur = enfant1.valeur;
        }
        if (enfant2.estMeilleur(mere, poidsMaximum)) {
            mere.liste = new ArrayList<>(enfant2.liste);
            mere.poids = enfant2.poids;
            mere.valeur = enfant2.valeur;
        }
    }

    // Normalement ça c'est l'algo du sujet mais jsuis pas sur sur de moi là
    public void reparation(Sac sac) {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < sac.liste.size(); i++) {
            indices.add(i);
        }
        indices.sort((a, b) -> Double.compare(
                (double) sac.objets.get(b).valeur / sac.objets.get(b).poids,
                (double) sac.objets.get(a).valeur / sac.objets.get(a).poids
        ));
        for (int i : indices) {
            if (sac.liste.get(i) == 1 && sac.poids > poidsMaximum) {
                sac.supprimeObjet(i);
            }
        }
        for (int i : indices) {
            if (sac.liste.get(i) == 0 && sac.poids + sac.objets.get(i).poids <= poidsMaximum) {
                sac.ajouteObjet(i);
            }
        }
    }

    public List<Sac> algorithmeGenetique(int taillePopulation, int nombreDeGenerations) {
        population = new ArrayList<>();
        for (int i = 0; i < taillePopulation; i++) {
            List<Integer> present = new ArrayList<>();
            for (int j = 0; j < objets.size(); j++) {
                present.add(Math.random() < 0.5 ? 1 : 0);
            }
            Sac sac = new Sac(objets, present);
            reparation(sac);
            population.add(sac);
        }
        for (int generation = 0; generation < nombreDeGenerations; generation++) {
            List<Sac> newPopulation = new ArrayList<>();
            while (newPopulation.size() < taillePopulation) {
                Sac parent1 = selection();
                Sac parent2 = selection();
                Sac[] enfants = croisement(parent1, parent2);
                enfants[0].mutation(this.poidsMaximum);
                enfants[1].mutation(this.poidsMaximum);
                reparation(enfants[0]);
                reparation(enfants[1]);
                remplacement(parent1, parent2, enfants[0], enfants[1]);
                newPopulation.add(enfants[0]);
                newPopulation.add(enfants[1]);
            }
            population = newPopulation;
        }
        return population;
    }

    // On choisit les parents pour la sélection
    private Sac selection() {
        // Le tournament en gros c'est pour la diversité c'est le compromis entre Exploration et Exploitation dont parlais le prof
        // si j'ai bien compris https://khayyam.developpez.com/articles/algo/genetic/
        int tournamentSize = 3;
        List<Sac> tournament = new ArrayList<>();
        for (int i = 0; i < tournamentSize; i++) {
            int randomIndex = (int) (Math.random() * population.size());
            tournament.add(population.get(randomIndex));
        }
        Sac best = tournament.get(0);
        for (Sac sac : tournament) {
            if (sac.estMeilleur(best, poidsMaximum)) {
                best = sac;
            }
        }
        return best;
    }
}