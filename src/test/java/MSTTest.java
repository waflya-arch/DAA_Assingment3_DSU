import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MSTTest {
    @Test
    public void testKruskalAndPrimEquivalence() {
        for (int t = 0; t < 100; t++) {
            Graph graph = GraphGenerator.generateUndirected(20, 50);
            Kruskal.Result kRes = Kruskal.run(graph);
            Prim.Result pRes = Prim.run(graph);

            assertEquals(kRes.totalWeight, pRes.totalWeight);
            assertEquals(1, kRes.componentsCount);
            assertEquals(1, pRes.componentsCount);
        }
    }
}