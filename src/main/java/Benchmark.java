import java.io.*;
import java.util.*;

public class Benchmark {
    public static void main(String[] args) throws IOException {
        int[] sizes = {1000, 3162, 10000, 31623, 100000};

        File resultsDir = new File("results");
        if (!resultsDir.exists()) {
            resultsDir.mkdirs();
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter("results/results.csv"))) {
            writer.println("algorithm,graph,n,m,time_ms,dsu_ops,edge_scans,comparisons,max_depth,result");

            for (int n : sizes) {
                int[] densities = {4 * n, 20 * n};
                for (int m : densities) {
                    runMSTBenchmarks(writer, n, m);
                    runKosarajuBenchmark(writer, n, m);
                    runKahnBenchmark(writer, n, m);
                    runDAGBenchmark(writer, n, m);
                }
            }
        }
    }

    private static void runMSTBenchmarks(PrintWriter writer, int n, int m) {
        Graph graph = GraphGenerator.generateUndirected(n, m);

        double[] kruskalTimes = new double[5];
        Kruskal.Result kRes = null;
        for (int i = 0; i < 5; i++) {
            long start = System.nanoTime();
            kRes = Kruskal.run(graph);
            kruskalTimes[i] = (System.nanoTime() - start) / 1e6;
        }
        Arrays.sort(kruskalTimes);

        double[] primTimes = new double[5];
        Prim.Result pRes = null;
        for (int i = 0; i < 5; i++) {
            long start = System.nanoTime();
            pRes = Prim.run(graph);
            primTimes[i] = (System.nanoTime() - start) / 1e6;
        }
        Arrays.sort(primTimes);

        if (kRes.totalWeight != pRes.totalWeight) {
            throw new IllegalStateException("Kruskal and Prim weights do not match!");
        }

        writer.printf(Locale.US, "Kruskal,undirected,%d,%d,%.2f,%d,%d,%d,0,%d\n",
                n, m, kruskalTimes[2], kRes.dsuOps, kRes.edgeScans, kRes.comparisons, kRes.totalWeight);

        writer.printf(Locale.US, "Prim,undirected,%d,%d,%.2f,0,%d,%d,0,%d\n",
                n, m, primTimes[2], pRes.edgeScans, pRes.comparisons, pRes.totalWeight);
    }

    private static void runKosarajuBenchmark(PrintWriter writer, int n, int m) {
        Graph graph = GraphGenerator.generateDirected(n, m);

        double[] times = new double[5];
        Kosaraju.Result res = null;
        for (int i = 0; i < 5; i++) {
            long start = System.nanoTime();
            res = Kosaraju.run(graph);
            times[i] = (System.nanoTime() - start) / 1e6;
        }
        Arrays.sort(times);

        writer.printf(Locale.US, "Kosaraju,directed,%d,%d,%.2f,0,%d,0,%d,%d\n",
                n, m, times[2], res.edgeScans, res.maxDepth, res.sccCount);
    }

    private static void runKahnBenchmark(PrintWriter writer, int n, int m) {
        Graph graph = GraphGenerator.generateDAG(n, m);

        double[] times = new double[5];
        Kahn.Result res = null;
        for (int i = 0; i < 5; i++) {
            long start = System.nanoTime();
            res = Kahn.run(graph);
            times[i] = (System.nanoTime() - start) / 1e6;
        }
        Arrays.sort(times);

        writer.printf(Locale.US, "Kahn,dag,%d,%d,%.2f,0,%d,0,0,%d\n",
                n, m, times[2], res.edgeScans, res.order.size());
    }

    private static void runDAGBenchmark(PrintWriter writer, int n, int m) {
        Graph graph = GraphGenerator.generateDAG(n, m);

        double[] times = new double[5];
        DagShortestPath.Result res = null;
        for (int i = 0; i < 5; i++) {
            long start = System.nanoTime();
            res = DagShortestPath.run(graph);
            times[i] = (System.nanoTime() - start) / 1e6;
        }
        Arrays.sort(times);

        writer.printf(Locale.US, "DagShortestPath,dag,%d,%d,%.2f,0,%d,0,0,%d\n",
                n, m, times[2], res.edgeScans, res.sumDistances);
    }
}