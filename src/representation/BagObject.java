package representation;

import java.util.List;

public class BagObject {
    private int id; // TODO : id c'est l'indice du BagObject c'est bien ça ?
    private List<Integer> cost; // liste de coût en dimension n
    public int value; // utilité de l'objet, on doit pas aussi avoir une dimension TODO : refacto à utility le nom de la variable non ?


    public BagObject(int id, List<Integer> cost, int value) {
        this.id = id;
        this.cost = cost;
        this.value = value;
    }

    public int costDimension(){
        return cost.size();
    }

    public List<Integer> getCost() {
        return cost;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Element : " + id + ",cost :" + cost + ",value (utilité):" + value;
    }

    public int getIndex() {
        return id;
    }
}
