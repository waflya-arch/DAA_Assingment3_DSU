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
}
