package representation;

import algorithm.GeneticAlgorithm;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Bag implements Comparable<Bag> {
    // TODO : refacto nos visibilité on peut pas avoir des attributs publiques partout c'est dég faut qu'on mette des accesseurs
    // Ca c'est les BagObjects qui peuvent être mis dans le sac
    public static List<BagObject> bagObjects;
    // Ca c'est la liste des BagObjects PRESENTS dans le sac c'est des 1 ou des 0 (j'ai mis integer car plus simple que boolean mais on peut changer
    // -> du même avis c'est plus simple integer
    public List<Integer> content;
    // Ca c'est le cost du sac
    public List<Integer> cost;
    // ça c'est ce que vaut le sac
    public int value;

    public Bag(List<BagObject> bagObjects, List<Integer> content) {
        Bag.bagObjects = bagObjects;
        this.content = content;
        this.cost = new ArrayList<>(bagObjects.get(0).costDimension());
        for (int i = 0; i < bagObjects.get(0).costDimension(); i++) {
            this.cost.add(0);
        }
        this.value = 0;

        for (int i = 0; i < content.size(); i++) {
            if (content.get(i) == 1) {
                addCostAndValue(i);
            }
        }
    }


    public Bag() {
        this.content = new ArrayList<>();
        this.cost = new ArrayList<>(bagObjects.get(0).costDimension());
        for (int i = 0; i < bagObjects.get(0).costDimension(); i++) {
            this.cost.add(0);
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


    public boolean isValidAdd(int n) {
        for (int j = 0; j < cost.size(); j++) {
            if (cost.get(j) + bagObjects.get(n).cost.get(j) > GeneticAlgorithm.maximumCost.get(j)) {
                return false;
            }
        }
        return true;
    }
    public void addBagObject(int n) {
        if (content.get(n) == 1) return;
        addCostAndValue(n);
        content.set(n, 1);
    }

    public void removeBagObject(int n) {
        if (content.get(n) == 0) return;
        removeCostandValue(n);
        content.set(n, 0);
    }

    // Notre méthode de mutation je pense qu'elle doit être là, j'ai quelque idées mais pas certaines de moi pour l'instant
    // En gros on switch de manière aléatoire 1 et 0 (présence de l'objet dans le sac) en checkant la contrainte de cost
    // Et on fait ça de manière complètement random mais je suis pas sûre de moi cette fonction doit être à changer
    // Je trouve ca bien comme méthode de mutation, j'ai mis la proba de mutation en parametre car c'est un truc qui peut
    // jouer beaucoup sur la perf de l'algo
    public void mutation(List<Integer> maximumCost, double facteurMutation) {
        Random rand = new Random();
        for (int i = 0; i < this.content.size(); i++) {
            if (rand.nextDouble() < facteurMutation) {
                if (this.content.get(i) == 1) {
                    this.removeBagObject(i);
                } else {
                    if (isValidAddCostAndValue(i, maximumCost)) {
                        this.addBagObject(i);
                    }
                }
            }
        }
    }


    // ça c'est pour savoir si une configuration d'un sac est meilleur qu'un autre (genre pour comparer deux solution de sac quoi
    // ça compare l'objet apelant avec celui en paramètre
    // Il faudrait pas plutot comparer les deuc fonctions de couts si la value est la meme?
    public boolean betterThan(Bag otherBag,  List<Integer> maximumCost){
        if (this.value > otherBag.value) return true;
        else if (this.value == otherBag.value) return isValid(maximumCost);
        return false;
    }



    // Bon ça c'est un ptit plus comme je passe jamais par des copy d'élément mais je sais qu'Alice oui mdr
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

    //TODO fonction à checker + changer le nom (ex : addObjet
    public void addCostAndValue(int n){
        for (int j = 0; j < costDimension(); j++) {
            this.cost.set(j, this.cost.get(j) + bagObjects.get(n).cost.get(j));
        }
        this.value += bagObjects.get(n).value;
    }

    //TODO très chelou cette fonction et nom absolument pas clair
    public boolean isValidAddCostAndValue(int n, List<Integer> maximumCost){
        Bag cloneBag = copy();
        cloneBag.addCostAndValue(n);
        return cloneBag.isValid(maximumCost);
    }


    //TODO fonction à checker + changer le nom (ex : removeBagObject)
    public void removeCostandValue(int n){
        for (int j = 0; j < costDimension(); j++) {
            this.cost.set(j, this.cost.get(j) - bagObjects.get(n).cost.get(j));
        }
        this.value -= bagObjects.get(n).value;
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