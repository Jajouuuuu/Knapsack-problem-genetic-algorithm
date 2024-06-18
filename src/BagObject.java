import java.util.List;

public class BagObject {
    int id;
    List<Integer> cost;
    int value;


    public BagObject(int id, List<Integer> cost, int value) {
        this.id = id;
        this.cost = cost;
        this.value = value;
    }

    public int costDimension(){
        return cost.size();
    }

    @Override
    public String toString() {
        return "Element : " + id + ",cost :" + cost + ",value (utilité):" + value;
    }
}
