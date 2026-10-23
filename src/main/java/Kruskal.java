import java.util.*;

public class Kruskal {
    public static class Result {
        public long totalWeight = 0;
        public int componentsCount = 0;
        public long edgeScans = 0;
        public long comparisons = 0;
        public long dsuOps = 0;
    }

    public static Result run(Graph graph) {
        Result result = new Result();
        List<int[]> edges = new ArrayList<>();

        for (int u = 0; u < graph.totalVertices; u++) {
            for (Edge edge : graph.adjacencyList.get(u)) {
                if (!graph.isDirected && u > edge.destinationVertex) {
                    continue;
                }
                edges.add(new int[]{u, edge.destinationVertex, edge.edgeWeight});
            }
        }

        edges.sort((a, b) -> {
            result.comparisons++;
            return Integer.compare(a[2], b[2]);
        });

        DisjointSetUnion dsu = new DisjointSetUnion(graph.totalVertices);
        int addedEdges = 0;

        for (int[] edge : edges) {
            result.edgeScans++;
            int u = edge[0];
            int v = edge[1];
            int w = edge[2];

            if (dsu.find(u) != dsu.find(v)) {
                dsu.union(u, v);
                result.totalWeight += w;
                addedEdges++;
                if (addedEdges == graph.totalVertices - 1) {
                    break;
                }
            }
        }

        result.dsuOps = dsu.totalDsuOperations;
        result.componentsCount = dsu.components();
        return result;
    }
}