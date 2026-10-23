import java.util.*;

public class Prim {
    public static class Result {
        public long totalWeight = 0;
        public int componentsCount = 0;
        public long edgeScans = 0;
        public long comparisons = 0;
    }

    public static Result run(Graph graph) {
        Result result = new Result();
        boolean[] visited = new boolean[graph.totalVertices];
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> {
            result.comparisons++;
            return Integer.compare(a[1], b[1]);
        });

        int components = 0;

        for (int start = 0; start < graph.totalVertices; start++) {
            if (visited[start]) continue;

            components++;
            visited[start] = true;
            addEdges(graph, start, visited, pq, result);

            while (!pq.isEmpty()) {
                int[] current = pq.poll();
                int u = current[0];
                int weight = current[1];

                if (visited[u]) continue;

                visited[u] = true;
                result.totalWeight += weight;
                addEdges(graph, u, visited, pq, result);
            }
        }

        result.componentsCount = components;
        return result;
    }

    private static void addEdges(Graph graph, int u, boolean[] visited, PriorityQueue<int[]> pq, Result result) {
        for (Edge edge : graph.adjacencyList.get(u)) {
            result.edgeScans++;
            if (!visited[edge.destinationVertex]) {
                pq.add(new int[]{edge.destinationVertex, edge.edgeWeight});
            }
        }
    }
}