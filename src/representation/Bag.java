package representation;

import java.util.*;
/**
 * Classe représentant un sac à dos.
 */
public class Bag implements Comparable<Bag> {
    public static List<BagObject> bagObjects;
    public static List<Integer> maximumCost;
    public List<Integer> content;
    public List<Integer> cost;
    public int value;

    /**
     * Constructeur par défaut de la classe Bag.
     */
    public Bag() {
        this.content = new ArrayList<>();
        this.cost = new ArrayList<>(bagObjects.get(0).costDimension());
        for (int i = 0; i < bagObjects.get(0).costDimension(); i++) {
            this.cost.add(0);
        }
        for (int i = 0; i < bagObjects.size(); i++) {
            this.content.add(0);
        }
        this.value = 0;
    }

    /**
     * Constructeur avec paramètres de la classe Bag.
     *
     * @param costs Liste des coûts des objets
     * @param values Liste des valeurs des objets
     * @param maxCost Liste des coûts maximums autorisés
     */
    public Bag(List<List<Integer>> costs, List<Integer> values, List<Integer> maxCost) {
        bagObjects = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            bagObjects.add(new BagObject(i, costs.get(i), values.get(i)));
        }
        initializeObjects(bagObjects);
        this.content = new ArrayList<>();
        this.cost = new ArrayList<>(bagObjects.get(0).costDimension());
        for (int i = 0; i < bagObjects.get(0).costDimension(); i++) {
            this.cost.add(0);
        }
        for (int i = 0; i < bagObjects.size(); i++) {
            this.content.add(0);
        }
        this.value = 0;
        this.maximumCost = maxCost;
    }

    /**
     * Initialise les objets du sac à dos.
     *
     * @param objList Liste des objets à ajouter au sac à dos
     */
    public static void initializeObjects(List<BagObject> objList) {
        bagObjects = objList;
    }

    /**
     * Crée un sac avec des objets ajoutés de manière aléatoire.
     *
     * @return un nouveau sac avec des objets aléatoires
     */
    public static Bag createRandomBag() {
        Bag newBag = new Bag();
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < bagObjects.size(); i++) {
            indices.add(i);
        }
        Collections.shuffle(indices);
        for (int index : indices) {
            if (newBag.isValidAdd(index)) {
                newBag.addBagObject(index);
            }
        }
        return newBag;
    }

    /**
     * Vérifie si l'ajout d'un objet au sac est valide.
     *
     * @param n Indice de l'objet à ajouter
     * @return vrai si l'ajout est valide, faux sinon
     */
    public boolean isValidAdd(int n) {
        for (int j = 0; j < cost.size(); j++) {
            if (this.cost.get(j) + bagObjects.get(n).getCost().get(j) > this.maximumCost.get(j)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Ajoute un objet au sac en mettant à jour les coûts et la valeur.
     *
     * @param n Indice de l'objet à ajouter
     */
    public void addBagObject(int n) {
        if (content.get(n) == 1) return;
        for (int j = 0; j < bagObjects.get(n).costDimension(); j++) {
            this.cost.set(j, this.cost.get(j) + bagObjects.get(n).getCost().get(j));
        }
        this.value += bagObjects.get(n).value;
        content.set(n, 1);
    }

    /**
     * Retire un objet du sac en mettant à jour les coûts et la valeur.
     *
     * @param n Indice de l'objet à retirer
     */
    public void removeBagObject(int n) {
        if (content.get(n) == 0) return;
        for (int j = 0; j < costDimension(); j++) {
            this.cost.set(j, this.cost.get(j) - bagObjects.get(n).getCost().get(j));
        }
        this.value -= bagObjects.get(n).value;
        content.set(n, 0);
    }

    /**
     * Répare un sac en retirant les objets non valides et en ajoutant des objets valides.

     */
    public void reparation() {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < this.content.size(); i++) {
            indices.add(i);
        }
        indices.sort((a, b) -> Integer.compare(
                bagObjects.get(b).value,
                bagObjects.get(a).value
        ));
        for (int i = indices.size() - 1; i >= 0; i--) {
            if (this.content.get(indices.get(i)) == 1 && !this.isValid()) {
                this.removeBagObject(indices.get(i));
            }
        }
        for (int i : indices) {
            if (this.content.get(i) == 0 && this.isValidAdd(i)) {
                this.addBagObject(i);
            }
        }

    }

    /**
     * Crée une copie du sac.
     *
     * @return une copie du sac
     */
    public Bag copy() {
        Bag copy = new Bag();
        for (int i = 0; i < this.content.size(); i++) {
            if (this.content.get(i) == 1) {
                copy.addBagObject(i);
            }
        }
        return copy;
    }

    /**
     * Retourne le nombre de dimensions de coût.
     *
     * @return le nombre de dimensions de coût
     */
    public int costDimension() {
        return cost.size();
    }

    /**
     * Vérifie si le sac est valide en comparant les coûts aux coûts maximums autorisés.
     *
     * @return vrai si le sac est valide, faux sinon
     */
    public boolean isValid() {
        for (int i = 0; i < costDimension(); i++) {
            if (this.cost.get(i) > maximumCost.get(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Vérifie si le contenu de ce sac est identique à celui d'un autre sac.
     *
     * @param bag le sac à comparer
     * @return vrai si les contenus sont identiques, faux sinon
     */
    public boolean hasSameContent(Bag bag) {
        for (int i = 0; i < this.content.size(); i++) {
            if (!bag.content.get(i).equals(this.content.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "The bag's cost : " + this.cost + ", value : " + this.value + ", and elements in the bag : " + this.content;
    }

    @Override
    public int compareTo(Bag o) {
        if (this == o || this.hasSameContent(o))
            return 0;
        int fitness1 = this.value, fitness2 = o.value;
        if (fitness1 > fitness2)
            return 1;
        else {
            if (fitness1 < fitness2)
                return -1;
            else {
                int totalCost1 = this.cost.stream().mapToInt(Integer::intValue).sum();
                int totalCost2 = o.cost.stream().mapToInt(Integer::intValue).sum();
                return Integer.compare(totalCost2, totalCost1);
            }
        }
    }
}