public class Main {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        AdjGraph g = new AdjGraph();
        boolean[][] maze = { { true, false, true, false, true }, 
                             { true, false, true, true, true },
                             { true, false, true, false, true }, 
                             { true, true, true, false, true }};


        g.createMaze(maze);
        AdjGraph.printPath(g.DijkstraPath(0, 19));

        long endTime = System.currentTimeMillis();
        System.out.println("That took " + (endTime - startTime) + " milliseconds");
    }
}
