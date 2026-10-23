import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DSUTest {

    @Test
    public void testInitialComponentsCount() {
        DisjointSetUnion dsu = new DisjointSetUnion(5);

        assertEquals(5, dsu.components());
    }

    @Test
    public void testUnionAndFindOperations() {
        DisjointSetUnion dsu = new DisjointSetUnion(5);

        assertTrue(dsu.union(0, 1));
        assertTrue(dsu.union(1, 2));

        assertEquals(3, dsu.components());

        assertEquals(dsu.find(0), dsu.find(2));

        assertFalse(dsu.union(0, 2));

        assertNotEquals(dsu.find(0), dsu.find(3));
    }

    @Test
    public void testOperationCounters() {
        DisjointSetUnion dsu = new DisjointSetUnion(10);

        dsu.union(0, 1);
        dsu.find(0);
        dsu.find(1);

        assertTrue(dsu.totalDsuOperations > 0, "Counter totalDsuOperations should be strictly positive");
    }
}