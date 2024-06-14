public class Objet {
    int id;
    int poids;
    int valeur;


    public Objet(int id, int valeur, int profit) {
        this.id = id;
        this.poids = valeur;
        this.valeur = profit;
    }

    @Override
    public String toString() {
        return "Element : " + id + ",poids :" + poids + ",valeur (utilité):" + valeur;
    }
}
