import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StackSafetyTest {
    @Test
    public void testStackOverflowSafetyOnPathGraph() {
        int n = 100000;
        Graph lineGraph = new Graph(n, true);
        for (int i = 0; i < n - 1; i++) {
            lineGraph.addEdge(i, i + 1, 1);
        }

        assertDoesNotThrow(() -> {
            Kosaraju.Result res = Kosaraju.run(lineGraph);
            assertEquals(n, res.sccCount);
            assertTrue(res.maxDepth > 0);
        });
    }
}