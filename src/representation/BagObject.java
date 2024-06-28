package representation;

import java.util.List;

/**
 * Classe représentant un objet de sac à dos.
 */
public class BagObject {
    // TODO : maybe refacto les noms d'attributs ou bien les commentés (je confond un peu les attributs donc je veux pas trop faire de la D)
    protected int id;
    protected List<Integer> cost;
    public int value;

    /**
     * Constructeur de la classe BagObject.
     *
     * @param id Identifiant de l'objet
     * @param cost Liste des coûts de l'objet en dimensions multiples
     * @param value Valeur (ou utilité) de l'objet
     */
    public BagObject(int id, List<Integer> cost, int value) {
        this.id = id;
        this.cost = cost;
        this.value = value;
    }

    /**
     * Retourne le nombre de dimensions de coût.
     *
     * @return le nombre de dimensions de coût
     */
    public int costDimension(){
        return cost.size();
    }

    /**
     * Retourne la liste des coûts de l'objet.
     *
     * @return la liste des coûts
     */
    public List<Integer> getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return "Element : " + id + ",cost :" + cost + ",value (utilité):" + value;
    }

}
