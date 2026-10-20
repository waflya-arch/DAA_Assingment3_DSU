public class DisjointSetUnion {
    private final int[] parentOrLeader;
    private final int[] treeRank;
    private int componentCount;
    private long totalDsuOperations = 0;

    public DisjointSetUnion(int totalVertices){
        parentOrLeader = new int[totalVertices];
        treeRank = new int[totalVertices];
        componentCount = totalVertices;

        for(int vertexId = 0;vertexId < totalVertices;vertexId++){
            parentOrLeader[vertexId] = vertexId;
            treeRank[vertexId] = 0;
        }
    }
}
