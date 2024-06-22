package testsCases;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TestProblem {

    private List<Integer> values;
    private List<List<Integer>> costs;
    private List<Integer> maximumCost;

    public TestProblem(int problemIndex, String filename) throws IOException {
        this.values = new ArrayList<>();
        this.costs = new ArrayList<>();
        this.maximumCost = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            // Lecture du nombre total de problèmes
            int numberOfProblems = Integer.parseInt(reader.readLine().trim());

            // Aller au problème sélectionné
            for (int k = 1; k < problemIndex; k++) {
                skipProblem(reader);
            }

            // Lire les détails du problème (nombre d'objets et de contraintes)
            String[] firstLineParts = reader.readLine().trim().split("\\s+");
            int n = Integer.parseInt(firstLineParts[0]); // nombre d'objets
            int m = Integer.parseInt(firstLineParts[1]); // nombre de contraintes

            // Lire les valeurs (n premiers chiffres trouvés)
            readValues(reader, n);

            // Lire les coûts (m fois n chiffres au total)
            readCosts(reader, n, m);

            // Lire maximumCost (m valeurs)
            readMaximumCost(reader, m);

        }
    }

    private void readValues(BufferedReader reader, int n) throws IOException {
        values.clear();
        String line = reader.readLine();
        String[] parts = line.trim().split("\\s+");
        for (String part : parts) {
            values.add(Integer.parseInt(part));
        }
        if (values.size() != n) {
            throw new IOException("Incorrect number of values found.");
        }
    }

    private void readCosts(BufferedReader reader, int n, int m) throws IOException {
        costs.clear();
        for (int i = 0; i < m; i++) {
            List<Integer> costList = new ArrayList<>();
            String line = reader.readLine();
            String[] parts = line.trim().split("\\s+");
            for (String part : parts) {
                costList.add(Integer.parseInt(part));
            }
            if (costList.size() != n) {
                throw new IOException("Incorrect number of cost values for constraint " + (i + 1));
            }
            costs.add(costList);
        }
    }

    private void readMaximumCost(BufferedReader reader, int m) throws IOException {
        maximumCost.clear();
        String line = reader.readLine();
        String[] maxCostParts = line.trim().split("\\s+");
        for (String part : maxCostParts) {
            maximumCost.add(Integer.parseInt(part));
        }
        if (maximumCost.size() != m) {
            throw new IOException("Incorrect number of maximum cost values.");
        }
    }

    private void skipProblem(BufferedReader reader) throws IOException {
        // Lire la ligne du nombre d'objets et de contraintes
        reader.readLine();

        // Skip valeurs
        reader.readLine();

        // Skip coûts
        String line;
        while ((line = reader.readLine()) != null && !line.trim().isEmpty()) {
            // Lire chaque ligne de coûts jusqu'à la fin de section
        }

        // Skip maximumCost
        reader.readLine();
    }

    public List<Integer> getValues() {
        return values;
    }

    public List<List<Integer>> getCosts() {
        return costs;
    }

    public List<Integer> getMaximumCost() {
        return maximumCost;
    }

    public static void main(String[] args) {
        try {
            TestProblem problem = new TestProblem(1, "src/testsCases/mknapcb1.txt");
            System.out.println("Values: " + problem.getValues());
            System.out.println("Costs: " + problem.getCosts());
            System.out.println("Maximum Cost: " + problem.getMaximumCost());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
