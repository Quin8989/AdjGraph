public class Main {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        AdjGraph g = new AdjGraph();


/*  Shortest path algo demo:
        
        zero --3-- one --1-- four
          |        | |          |
          1        | |          7
          |        | |          |
        two ---7---   ---5--- three
         |                      |
         |----------2-----------|

*/

        int zero = g.addVertex();
        int one = g.addVertex();
        int two = g.addVertex();
        int three = g.addVertex();
        int four = g.addVertex();

        g.addUndirectedEdge(zero, one, 3);
        g.addUndirectedEdge(zero, two, 1);
        g.addUndirectedEdge(two, one, 7);
        g.addUndirectedEdge(two, three, 2);
        g.addUndirectedEdge(one, four, 1);
        g.addUndirectedEdge(one, three, 5);
        g.addUndirectedEdge(four, three, 7);
        AdjGraph.printPath(g.DijkstraPath(zero, three));

        /* maze-solver demo using the above algo (only uncomment each demo at a time)
        boolean O = true;
        boolean X = false;
        boolean[][] maze = {{O,O,O,O,X,O,O,O},
                            {X,O,X,O,O,O,O,O},
                            {X,O,O,O,X,X,X,O},
                            {X,X,X,X,X,X,O,O}};


        g.createMaze(maze);
        AdjGraph.printPath(g.DijkstraPath(0, 30)); */

        long endTime = System.currentTimeMillis();
        System.out.println("That took " + (endTime - startTime) + " milliseconds");
    }
}
