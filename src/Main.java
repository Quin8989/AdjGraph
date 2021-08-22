public class Main {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        AdjGraph g = new AdjGraph();
        boolean O = true;
        boolean X = false;
        boolean[][] maze = {{O,O,O,O,X,O,O,O},
                            {X,O,X,O,O,O,O,O},
                            {X,O,O,O,X,X,X,O},
                            {X,X,X,X,X,X,O,O}};


        g.createMaze(maze);
        AdjGraph.printPath(g.DijkstraPath(0, 30));

        long endTime = System.currentTimeMillis();
        System.out.println("That took " + (endTime - startTime) + " milliseconds");
    }
}
