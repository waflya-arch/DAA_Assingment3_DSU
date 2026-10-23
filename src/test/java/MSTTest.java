import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MSTTest {
    @Test
    public void testKruskalAndPrimEquivalence() {
        for (int seed = 0; seed < 100; seed++) {
            Graph graph = generateRandomConnectedGraph(20, 50, seed);
            Kruskal.Result kRes = Kruskal.run(graph);
            Prim.Result pRes = Prim.run(graph);

            assertEquals(kRes.totalWeight, pRes.totalWeight, "Mismatch at seed: " + seed);
            assertEquals(kRes.componentsCount, pRes.componentsCount);
        }
    }

    private Graph generateRandomConnectedGraph(int n, int m, long seed) {
        java.util.Random rnd = new java.util.Random(seed);
        Graph graph = new Graph(n, false);
        for (int i = 1; i < n; i++) {
            graph.addEdge(i, rnd.nextInt(i), 1 + rnd.nextInt(1000));
        }
        for (int i = n - 1; i < m; i++) {
            int a = rnd.nextInt(n);
            int b = rnd.nextInt(n);
            while (b == a) b = rnd.nextInt(n);
            graph.addEdge(a, b, 1 + rnd.nextInt(1000));
        }
        return graph;
    }
}