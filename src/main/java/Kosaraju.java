import java.util.*;

public class Kosaraju {
    public static class Result {
        public int sccCount = 0;
        public long edgeScans = 0;
        public int maxDepth = 0;
    }

    public static Result run(Graph graph) {
        Result result = new Result();
        int n = graph.totalVertices;
        boolean[] visited = new boolean[n];
        Deque<Integer> finishOrder = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                dfsPass1(graph, i, visited, finishOrder, result);
            }
        }

        Graph reversed = new Graph(n, true);
        for (int u = 0; u < n; u++) {
            for (Edge edge : graph.adjacencyList.get(u)) {
                reversed.addEdge(edge.destinationVertex, u, edge.edgeWeight);
            }
        }

        Arrays.fill(visited, false);
        while (!finishOrder.isEmpty()) {
            int u = finishOrder.pop();
            if (!visited[u]) {
                result.sccCount++;
                dfsPass2(reversed, u, visited, result);
            }
        }

        return result;
    }

    private static void dfsPass1(Graph graph, int start, boolean[] visited, Deque<Integer> finishOrder, Result result) {
        Deque<int[]> stack = new ArrayDeque<>();
        stack.push(new int[]{start, 0});
        visited[start] = true;

        while (!stack.isEmpty()) {
            result.maxDepth = Math.max(result.maxDepth, stack.size());
            int[] frame = stack.peek();
            int u = frame[0];
            int edgeIndex = frame[1];

            if (edgeIndex < graph.adjacencyList.get(u).size()) {
                frame[1]++;
                Edge edge = graph.adjacencyList.get(u).get(edgeIndex);
                result.edgeScans++;

                if (!visited[edge.destinationVertex]) {
                    visited[edge.destinationVertex] = true;
                    stack.push(new int[]{edge.destinationVertex, 0});
                }
            } else {
                stack.pop();
                finishOrder.push(u);
            }
        }
    }

    private static void dfsPass2(Graph graph, int start, boolean[] visited, Result result) {
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(start);
        visited[start] = true;

        while (!stack.isEmpty()) {
            result.maxDepth = Math.max(result.maxDepth, stack.size());
            int u = stack.pop();

            for (Edge edge : graph.adjacencyList.get(u)) {
                result.edgeScans++;
                if (!visited[edge.destinationVertex]) {
                    visited[edge.destinationVertex] = true;
                    stack.push(edge.destinationVertex);
                }
            }
        }
    }
}