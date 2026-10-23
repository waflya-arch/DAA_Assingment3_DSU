import java.util.*;

public class Kahn {
    public static class Result {
        public List<Integer> order = new ArrayList<>();
        public boolean hasCycle = false;
        public long edgeScans = 0;
    }

    public static Result run(Graph graph) {
        Result result = new Result();
        int n = graph.totalVertices;
        int[] inDegree = new int[n];

        for (int u = 0; u < n; u++) {
            for (Edge edge : graph.adjacencyList.get(u)) {
                inDegree[edge.destinationVertex]++;
            }
        }

        Queue<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) {
                queue.add(i);
            }
        }

        while (!queue.isEmpty()) {
            int u = queue.poll();
            result.order.add(u);

            for (Edge edge : graph.adjacencyList.get(u)) {
                result.edgeScans++;
                inDegree[edge.destinationVertex]--;
                if (inDegree[edge.destinationVertex] == 0) {
                    queue.add(edge.destinationVertex);
                }
            }
        }

        if (result.order.size() < n) {
            result.hasCycle = true;
        }

        return result;
    }
}