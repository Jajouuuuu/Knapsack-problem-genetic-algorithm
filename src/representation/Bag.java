package representation;

import algorithm.GeneticAlgorithm;

import java.util.*;

public class Bag implements Comparable<Bag> {
    // TODO : refacto nos visibilité on peut pas avoir des attributs publiques partout c'est dég faut qu'on mette des accesseurs
    public static List<BagObject> bagObjects; // BagObjects pouvant être mit dans le sac
    public List<Integer> content; // Liste de 1 ou 0 indiquant si l'objet est présent ou non dans le sac
    public List<Integer> cost; // Liste de dimension n indiquant les coût du sac TODO : refacto à budget pour coller avec l'énoncé de TP sinon on confond avec cost des objets je trouve
    public int value; // Valeur du sac (utilité ?)

    public Bag() {
        this.content = new ArrayList<>();
        this.cost = new ArrayList<>(bagObjects.get(0).costDimension());
        for (int i = 0; i < bagObjects.get(0).costDimension(); i++) {
            this.cost.add(0);
        }
        for(int i = 0; i < bagObjects.size(); i++){
            this.content.add(0);
        }
        this.value = 0;
    }
    // Méthode statique pour initialiser les objets
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
            if (this.cost.get(j) + bagObjects.get(n).getCost().get(j) > GeneticAlgorithm.maximumCost.get(j)) {
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
        // On trie les object par valeur (utilité normalement non ? mais je trouve pas notre représentation de l'utilité ?)
        List<BagObject> sortedObjects = new ArrayList<>(bagObjects);
        sortedObjects.sort(Comparator.comparingInt(BagObject::getValue).reversed());
        List<Integer> cumulativeCosts = new ArrayList<>(cost);
        // On check si on a une violation de contrainte
        for (int l = sortedObjects.size() - 1; l >= 0; l--) {
            BagObject obj = sortedObjects.get(l);
            int index = obj.getIndex();
            boolean canAdd = true;
            for (int j = 0; j < cost.size(); j++) {
                if (cost.get(j) + obj.getCost().get(j) > maximumCost.get(j)) {
                    canAdd = false;
                    break;
                }
            }
            // Si on viole une contrainte en ajoutant un objet on le tej
            // itéré sur cost.get[0] size
            if (!canAdd) {
                for (int j = 0; j < cost.size(); j++) {
                    cumulativeCosts.set(j, cumulativeCosts.get(j) - obj.getCost().get(j));
                }
                cost = cumulativeCosts;
                value -= obj.getValue();
            }
            // Et on itère pour voir si ceux qu'on a virer avant peuvent être ajouté
            if (!canAdd) {
                for (int j = 0; j < cost.size(); j++) {
                    if (cost.get(j) + obj.getCost().get(j) <= maximumCost.get(j)) {
                        cumulativeCosts.set(j, cumulativeCosts.get(j) + obj.getCost().get(j));
                    }
                }
                cost = cumulativeCosts;
                value += obj.getValue();
            }
        }
    }

    public Bag copy() {
        Bag copy = new Bag();
        for(int i = 0; i<this.content.size(); i++){
            if(this.content.get(i) == 1){
                copy.addBagObject(i);
            }
        }
        return copy;
    }

    public int costDimension(){
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

    public boolean hasSameContent(Bag bag){
        for(int i = 0; i<this.content.size(); i++){
            if(!bag.content.get(i).equals(this.content.get(i))){
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "The bag's cost : " + cost + ", value : " + value + ", and elements in the bag : " + content;
    }

    @Override
    public int compareTo(Bag o) {
        if(this == o || this.hasSameContent(o))
            return 0;
        int fitness1 = this.value, fitness2 = o.value;
        if(fitness1 > fitness2)
            return 1;
        else {
            if(fitness1 < fitness2)
                return -1;
            else {
                int totalCost1 = this.cost.stream().mapToInt(Integer::intValue).sum();
                int totalCost2 = o.cost.stream().mapToInt(Integer::intValue).sum();
                return Integer.compare(totalCost2, totalCost1);
            }
        }
    }
}