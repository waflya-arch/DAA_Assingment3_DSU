import java.util.*;

public class DagShortestPath {
    public static class Result {
        public long[] distances;
        public int[] parent;
        public long sumDistances = 0;
        public long edgeScans = 0;
    }

    public static Result run(Graph dag) {
        Result result = new Result();
        int n = dag.totalVertices;

        Kahn.Result kahnResult = Kahn.run(dag);
        if (kahnResult.hasCycle) {
            throw new IllegalArgumentException("Graph contains cycles");
        }

        result.distances = new long[n];
        result.parent = new int[n];
        Arrays.fill(result.distances, Long.MAX_VALUE / 2);
        Arrays.fill(result.parent, -1);

        int[] inDegree = new int[n];
        for (int u = 0; u < n; u++) {
            for (Edge edge : dag.adjacencyList.get(u)) {
                inDegree[edge.destinationVertex]++;
            }
        }

        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) {
                result.distances[i] = 0;
            }
        }

        for (int u : kahnResult.order) {
            if (result.distances[u] != Long.MAX_VALUE / 2) {
                for (Edge edge : dag.adjacencyList.get(u)) {
                    result.edgeScans++;
                    if (result.distances[u] + edge.edgeWeight < result.distances[edge.destinationVertex]) {
                        result.distances[edge.destinationVertex] = result.distances[u] + edge.edgeWeight;
                        result.parent[edge.destinationVertex] = u;
                    }
                }
            }
        }

        for (int i = 0; i < n; i++) {
            result.sumDistances += result.distances[i];
        }

        return result;
    }
}