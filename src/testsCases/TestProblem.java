package testsCases;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant une instance du problème du sac à dos multidimensionnel.
 * Contient les valeurs, les coûts et les contraintes définies à partir d'un fichier.
 */
public class TestProblem {

    private List<Integer> values;
    private List<List<Integer>> costs;
    private List<Integer> maximumCost;
    private Integer optimal;

    /**
     * Constructeur initialisant un problème de test avec ses paramètres.
     *
     * @param values Liste des valeurs des objets.
     * @param costs Matrice des coûts pour chaque objet et chaque contrainte.
     * @param maximumCost Liste des coûts maximaux permis pour chaque contrainte.
     * @param optimal Valeur optimale pour ce problème.
     */
    public TestProblem(List<Integer> values, List<List<Integer>> costs, List<Integer> maximumCost, Integer optimal) {
        this.values = values;
        this.costs = costs;
        this.maximumCost = maximumCost;
        this.optimal = optimal;
    }

    /**
     * Retourne les valeurs des objets.
     *
     * @return Liste des valeurs des objets.
     */
    public List<Integer> getValues() {
        return values;
    }

    /**
     * Retourne la matrice des coûts pour chaque objet et chaque contrainte.
     *
     * @return Matrice des coûts.
     */
    public List<List<Integer>> getCosts() {
        return costs;
    }

    /**
     * Retourne la liste des coûts maximaux permis pour chaque contrainte.
     *
     * @return Liste des coûts maximaux.
     */
    public List<Integer> getMaximumCost() {
        return maximumCost;
    }

    @Override
    public String toString() {
        return "TestProblem{" +
                "values=" + values +
                ", costs=" + costs +
                ", maximumCost=" + maximumCost +
                ", optimal=" + optimal +
                '}';
    }

    /**
     * Redéfinition de la méthode toString pour afficher les détails du problème.
     *
     * @return Chaîne représentant le problème avec ses valeurs, coûts et contraintes.
     */
    public static TestProblem readFromFile(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;

        // Première ligne
        line = reader.readLine();
        String[] firstLineParts = line.split(" ");
        int numberOfObjects = Integer.parseInt(firstLineParts[0]);
        int numberOfConstraints = Integer.parseInt(firstLineParts[1]);
        int optimal = Integer.parseInt(firstLineParts[firstLineParts.length - 1]);

        // Values
        line = reader.readLine();
        List<Integer> values = new ArrayList<>();
        String[] valuesParts = line.split(" ");
        for (String part : valuesParts) {
            values.add(Integer.parseInt(part));
        }

        // Costs
        List<List<Integer>> costs = new ArrayList<>();
        for (int i = 0; i < numberOfObjects; i++) {
            costs.add(new ArrayList<>());
        }
        for (int i = 0; i < numberOfConstraints; i++) {
            line = reader.readLine();
            String[] costsParts = line.split(" ");
            for (int j = 0; j < numberOfObjects; j++) {
                costs.get(j).add(Integer.parseInt(costsParts[j]));
            }
        }

        // Maximum costs
        line = reader.readLine();
        List<Integer> maximumCost = new ArrayList<>();
        String[] maxCostParts = line.split(" ");
        for (String part : maxCostParts) {
            maximumCost.add(Integer.parseInt(part));
        }

        reader.close();
        return new TestProblem(values, costs, maximumCost, optimal);
    }
}
