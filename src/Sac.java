import java.util.List;
import java.util.ArrayList;

class Sac {
    // Ca c'est les objets qui peuvent être mit dans le sac
    List<Objet> objets;
    // Ca c'est la liste des objets PRESENTS dans le sac c'est des 1 ou des 0 (j'ai mit integer car p^lus simple que boolean mais on peut changer
    List<Integer> liste;
    // Ca c'est le poids du sac
    int poids;
    // ça c'est ce que vaut le sac
    int valeur;

    public Sac(List<Objet> objets, List<Integer> liste) {
        this.objets = objets;
        this.liste = liste;
        this.poids = 0;
        this.valeur = 0;

        for (int i = 0; i < liste.size(); i++) {
            if (liste.get(i) == 1) {
                this.poids += objets.get(i).poids;
                this.valeur += objets.get(i).valeur;
            }
        }
    }

    public void ajouteObjet(int n) {
        if (liste.get(n) == 1) return;
        this.poids += objets.get(n).poids;
        this.valeur += objets.get(n).valeur;
        liste.set(n, 1);
    }

    public void supprimeObjet(int n) {
        if (liste.get(n) == 0) return;
        this.poids -= objets.get(n).poids;
        this.valeur -= objets.get(n).valeur;
        liste.set(n, 0);
    }

    @Override
    public String toString() {
        return "Le sac - poids : " + poids + ", valeur : " + valeur + ", et éléments dans le sac" + liste;
    }

    // Notre méthode de mutation je pense qu'elle doit être là, j'ai quelque idées mais pas certaines de moi pour l'instant
    // donc je vais push des méthodes vide concernant l'algo génétique pour le moment
    public void mutation(int poidsMaximum){

    }

    // ça c'est pour savoir si une configuration d'un sac est meilleur qu'un autre (genre pour comparer deux solution de sac quoi
    // ça compare l'objet apelant avec celui en paramètre
    public boolean estMeilleur(Sac autre, int poidsMaximum){
        if (this.valeur > autre.valeur) return true;
        if (this.valeur < autre.valeur) return false;
        return this.poids <= poidsMaximum && this.poids < autre.poids;
    }

    // Bon ça c'est un ptit plus comme je passe jamais par des copy d'élément mais je sais qu'Alice oui mdr
    public Sac copy() {
        List<Integer> newList = new ArrayList<>(this.liste);
        Sac sac = new Sac(this.objets, newList);
        sac.poids = this.poids;
        sac.valeur = this.valeur;
        return sac;
    }
}