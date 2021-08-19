public class Main {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        AdjGraph g = new AdjGraph();
        //g.generateGraph(5, 1, 8);
        //g.generateDisk(20, 6, 20);
        int A = g.addVertex();
        int B = g.addVertex();
        int C = g.addVertex();
        int D = g.addVertex();
        int E = g.addVertex();

        g.addUndirectedEdge(A, B, 3);
        g.addUndirectedEdge(A, C, 1);
        g.addUndirectedEdge(B, D, 5);
        g.addUndirectedEdge(B, C, 7);
        g.addUndirectedEdge(B, E, 1);
        g.addUndirectedEdge(E, D, 7);
        g.addUndirectedEdge(C, D, 2);


        //g.showGraphConnections();

        AdjGraph.printPath(g.DijkstraPath(C,E));

        long endTime = System.currentTimeMillis();
        System.out.println("That took " + (endTime - startTime) + " milliseconds");
    }
}
