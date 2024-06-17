import java.util.List;
import java.util.ArrayList;
import java.util.Random;

class Bag {
    // Ca c'est les objects qui peuvent être mis dans le sac
    List<Object> objects;
    // Ca c'est la liste des objects PRESENTS dans le sac c'est des 1 ou des 0 (j'ai mis integer car plus simple que boolean mais on peut changer
    // -> du même avis c'est plus simple integer
    List<Integer> content;
    // Ca c'est le cost du sac
    List<Integer> cost;
    // ça c'est ce que vaut le sac
    int value;

    public Bag(List<Object> objects, List<Integer> content) {
        this.objects = objects;
        this.content = content;
        this.cost = new ArrayList<>(objects.get(0).costDimension());
        for (int i = 0; i < objects.get(0).costDimension(); i++) {
            this.cost.add(0);
        }
        this.value = 0;

        for (int i = 0; i < content.size(); i++) {
            if (content.get(i) == 1) {
                addCostAndValue(i);
            }
        }
    }

    public void addObject(int n) {
        if (content.get(n) == 1) return;
        addCostAndValue(n);
        content.set(n, 1);
    }

    public void removeObject(int n) {
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
                    this.removeObject(i);
                } else {
                    if (isValidAddCostAndValue(i, maximumCost)) {
                        this.addObject(i);
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
        List<Integer> newList = new ArrayList<>(this.content);
        Bag bag = new Bag(this.objects, newList);
        bag.cost = this.cost;
        bag.value = this.value;
        return bag;
    }

    public int costDimension(){
        return cost.size();
    }

    public void addCostAndValue(int n){
        for (int j = 0; j < costDimension(); j++) {
            this.cost.set(j, this.cost.get(j) + objects.get(n).cost.get(j));
        }
        this.value += objects.get(n).value;
    }

    public boolean isValidAddCostAndValue(int n, List<Integer> maximumCost){
        Bag cloneBag = copy();
        cloneBag.addCostAndValue(n);
        return cloneBag.isValid(maximumCost);
    }

    public void removeCostandValue(int n){
        for (int j = 0; j < costDimension(); j++) {
            this.cost.set(j, this.cost.get(j) - objects.get(n).cost.get(j));
        }
        this.value -= objects.get(n).value;
    }

    public boolean isValid(List<Integer> maximumCost) {
        for (int i = 0; i < costDimension(); i++) {
            if (this.cost.get(i) > maximumCost.get(i)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "The bag's cost : " + cost + ", value : " + value + ", and elements in the bag : " + content;
    }
}