package representation;

import java.util.*;

public class Bag implements Comparable<Bag> {
    public static List<BagObject> bagObjects;
    public static List<Integer> maximumCost; // Déplacé ici
    public List<Integer> content;
    public List<Integer> cost;
    public int value;

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

    public static void initializeObjects(List<BagObject> objList) {
        bagObjects = objList;
    }

    // Méthode pour créer un sac avec des objets ajoutés de manière aléatoire
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

    // Si le sac est valide alors ajout
    public boolean isValidAdd(int n) {
        for (int j = 0; j < cost.size(); j++) {
            if (this.cost.get(j) + bagObjects.get(n).getCost().get(j) > this.maximumCost.get(j)) {
                return false;
            }
        }
        return true;
    }

    // TODO : check si on ajoute bien un bagObect avec cette méthode et que ça update bien les cout et utilité pour toutes les dimensions
    // j'ai réintégré la méthode chelou addCostandValue directement ici je comprennais pas pourquoi c'était split si on s'en resservait pas ??
    public void addBagObject(int n) {
        if (content.get(n) == 1) return;
        for (int j = 0; j < bagObjects.get(n).costDimension(); j++) {
            this.cost.set(j, this.cost.get(j) + bagObjects.get(n).getCost().get(j));
        }
        this.value += bagObjects.get(n).value;
        content.set(n, 1);
    }

    // TODO : check si on remove bien un bagObect avec cette méthode et que ça update bien les cout et utilité pour toutes les dimensions
    // j'ai réintégré la méthode chelou removeCostandValue directement ici je comprennais pas pourquoi c'était split si on s'en resservait pas ??
    public void removeBagObject(int n) {
        if (content.get(n) == 0) return;
        for (int j = 0; j < costDimension(); j++) {
            this.cost.set(j, this.cost.get(j) - bagObjects.get(n).getCost().get(j));
        }
        this.value -= bagObjects.get(n).value;
        content.set(n, 0);
    }

    // Méthode de réparation qu'on applique directement sur un sac
    // maximumCost en paramètre car on check si on dépasse pas
    public void reparation(List<Integer> maximumCost) {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < this.content.size(); i++) {
            indices.add(i);
        }
        indices.sort((a, b) -> Integer.compare(
                bagObjects.get(b).value,
                bagObjects.get(a).value
        ));
        // sort works
        // c'est comme le répare qu'il y a dans mutation.
        for (int i = indices.size() - 1; i >= 0; i--) {
            if (this.content.get(indices.get(i)) == 1 && !this.isValid(maximumCost)) {
                this.removeBagObject(indices.get(i));
            }
        }
        for (int i : indices) {
            if (this.content.get(i) == 0 && this.isValidAdd(i)) {
                this.addBagObject(i);
            }
        }

    }

    public Bag copy() {
        Bag copy = new Bag();
        for (int i = 0; i < this.content.size(); i++) {
            if (this.content.get(i) == 1) {
                copy.addBagObject(i);
            }
        }
        return copy;
    }

    public int costDimension() {
        return cost.size();
    }

    public boolean isValid(List<Integer> maximumCost) {
        for (int i = 0; i < costDimension(); i++) {
            if (this.cost.get(i) > maximumCost.get(i)) {
                return false;
            }
        }
        return true;
    }

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