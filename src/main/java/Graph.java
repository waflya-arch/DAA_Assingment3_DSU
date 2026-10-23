import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Graph {
    public int totalVertices;
    public int totalEdges;
    public boolean isDirected;
    public List<List<Edge>> adjacencyList;

    public Graph(int totalVertices,boolean isDirected){
        this.totalVertices = totalVertices;
        this.isDirected = isDirected;
        this.adjacencyList = new ArrayList<>();
        for(int vertexId = 0;vertexId < totalVertices;vertexId++){
            this.adjacencyList.add(new ArrayList<>());
        }
    }

    public void addEdge(int sourceVertex,int destinationVertex,int edgeWeight){
        this.adjacencyList.get(sourceVertex).add(new Edge(sourceVertex,edgeWeight));

        if(!isDirected){
            this.adjacencyList.get(destinationVertex).add(new Edge(sourceVertex,edgeWeight));
        }
    }

    public static Graph readFromFile(String filePath) throws IOException {
        try(BufferedReader fileReader = new BufferedReader(new FileReader(filePath))){
            String[] firstLineParts = fileReader.readLine().trim().split("\\s+");

            int totalVertices = Integer.parseInt(firstLineParts[0]);
            int totalEdges = Integer.parseInt(firstLineParts[1]);
            boolean isDirected = Integer.parseInt(firstLineParts[2]) == 1;

            Graph graph = new Graph(totalVertices,isDirected);
            graph.totalEdges = totalEdges;

            for(int edgeIndex = 0;edgeIndex < totalEdges;edgeIndex++){
                String line = fileReader.readLine();
                if(line == null) break;

                String[] edgeParts = line.trim().split("\\s+");
                int sourceVertex = Integer.parseInt(edgeParts[0]);
                int destinationVertex = Integer.parseInt(edgeParts[1]);
                int edgeWeight = Integer.parseInt(edgeParts[2]);

                graph.addEdge(sourceVertex,destinationVertex,edgeWeight);
            }

            return graph;
        }
    }
}
