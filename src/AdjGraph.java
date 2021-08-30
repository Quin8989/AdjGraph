import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;

/* 
 * NOTES:
 * 
 * - node and vertex are used interchangeably in comments.
 * 
 * - An adjacency matrix is used to document the vertices and it's connections. A Hashmap is used <K,V>.
 *  K = Vertex ID, V = a list containing information about all of it's edges(what node it connects to, difficulty, etc...)
 */
public class AdjGraph {

	static Integer vertexIDcount = 0; // vertices(ID's) in map
	HashMap<Integer, ArrayList<Integer[]>> AdjList = new HashMap<Integer, ArrayList<Integer[]>>(); // <vertex ID, {{ID
																									// of connection,
																									// difficulty},{},{},...}
	HashMap<Integer, Vertex> vertObjFind = new HashMap<Integer, Vertex>(); // <vertex ID, vertex>

	class Vertex {
		Integer vertexID;
		Integer x;
		Integer y;

		Vertex(Integer vertexID) {
			this.vertexID = vertexID;
		}

		Vertex(Integer vertexID, Integer x, Integer y) {
			this.vertexID = vertexID;
			this.x = x;
			this.y = y;
		}
	}

	class Edge {
		Integer difficulty;

		Edge(Integer difficulty) {
			this.difficulty = difficulty;
		}
	}

	/**
	 * Creates a vertex
	 * 
	 * @return vertex ID of newly created vertex
	 */
	public Integer addVertex() {
		Integer V_ID = createUniqueVertexID();
		Vertex vertex = new Vertex(V_ID);
		vertObjFind.put(V_ID, vertex);
		ArrayList<Integer[]> adjVertices = new ArrayList<Integer[]>(); // arraylist to store it's connections
		AdjList.put(V_ID, adjVertices);
		return vertex.vertexID;
	}

	/**
	 * Creates a vertex with x,y coordinates
	 * 
	 * @return vertex ID of newly created vertex
	 */
	public Integer addVertexWithPos(Integer x, Integer y) {
		Integer V_ID = createUniqueVertexID();
		Vertex vertex = new Vertex(V_ID, x, y);
		vertObjFind.put(V_ID, vertex);
		ArrayList<Integer[]> adjVertices = new ArrayList<Integer[]>();
		AdjList.put(V_ID, adjVertices);
		return vertex.vertexID;
	}

	private Integer createUniqueVertexID() { // assigns and increments int strating at 0
		Integer vertexID = vertexIDcount;
		vertexIDcount++;
		return vertexID;
	}

	/**
	 * Adds an edge starting from vertex1 to vertex2, vertices need to exist before
	 * calling this method
	 * 
	 * @param vertexID1, ID of an already existing vertex the edge starts from
	 * @param vertexID2, ID of an already existing vertex the edge connects to
	 * @param difficulty
	 * @return
	 */
	public boolean addDirectedEdge(Integer vertexID1, Integer vertexID2, Integer difficulty) {
		new Edge(difficulty);

		Integer[] vertex1WEdgeDiff = new Integer[2]; // Update map
		vertex1WEdgeDiff[0] = vertexID2;
		vertex1WEdgeDiff[1] = difficulty;
		AdjList.get(vertexID1).add(vertex1WEdgeDiff);
		return true;
	}

	public boolean addDirectedEdgeFromPos(Integer vertexID1, Integer vertexID2) {
		Integer difficulty = getdist(vertObjFind.get(vertexID1).x, vertObjFind.get(vertexID2).x,
				vertObjFind.get(vertexID1).y, vertObjFind.get(vertexID2).y);
		new Edge(difficulty);
		Integer[] vertex1WEdgeDiff = new Integer[2]; // Update map
		vertex1WEdgeDiff[0] = vertexID2;
		vertex1WEdgeDiff[1] = difficulty;
		AdjList.get(vertexID1).add(vertex1WEdgeDiff);
		return true;
	}

	private Integer getdist(double x1, double x2, double y1, double y2) {
		return (int) Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
	}

	public boolean addUndirectedEdgeFromPos(Integer vertexID1, Integer vertexID2) {
		addDirectedEdgeFromPos(vertexID1, vertexID2);
		addDirectedEdgeFromPos(vertexID2, vertexID1);
		return true;
	}

	public boolean addUndirectedEdge(Integer vertexID1, Integer vertexID2, Integer difficulty) {
		addDirectedEdge(vertexID1, vertexID2, difficulty);
		addDirectedEdge(vertexID2, vertexID1, difficulty);
		return true;
	}

	public boolean removeEdge(Integer vertexID1, Integer vertexID2) {
		for (Iterator<Integer[]> iterator = AdjList.get(vertexID1).iterator(); iterator.hasNext();) { // Update map
			Integer[] vertexWEdgeComp = iterator.next();
			if (vertexWEdgeComp[0].equals(vertexID2)) {
				iterator.remove();
				break;
			}
		}
		
		return true;
	}

	public boolean removeUndirectedEdge(Integer vertexID1, Integer vertexID2) {
		removeEdge(vertexID1, vertexID2);
		removeEdge(vertexID2, vertexID1);
		return true;
	}

	/**
	 * Creates a graph with random assignments of edges
	 * 
	 * @param vertices, Number of vertices desired
	 * @param density,  density of edges relative to vertices. 0 is no edges, 1 is a
	 *                  complete graph
	 * @param maxDiff,  maximum possible difficulty of edges
	 */
	public void generateGraph(Integer vertices, double density, Integer maxDiff) {
		Integer[] vertexCheck = new Integer[vertices];
		for (int i = 0; i < vertices; i++) { // Adds vertices to graph and a checklist
			vertexCheck[i] = addVertex();
		}

		LinkedList<Integer[]> checklist = findPossibleEdges(vertexCheck);
		Integer edges = (int) (0.5 * density * vertices * (vertices - 1)); // Amount of edges needed
		for (int edgeCount = 0; edgeCount < edges; edgeCount++) { // Add edges
			int randomDiff = ThreadLocalRandom.current().nextInt(0, maxDiff + 1);
			addUndirectedEdge(checklist.getFirst()[0], checklist.getFirst()[1], randomDiff);
			checklist.removeFirst();

			/*
			 * // for debugging System.out.println(" "); for (Integer[] f : checklist) {
			 * System.out.println(" "); for (Integer i : f) { System.out.print(i); } }
			 * System.out.println(" ");
			 */
		}

	}

	/**
	 * Creates a graph in the "shape" of a disk (difficulty is based on relative
	 * distance)
	 * 
	 * @param radius,   furthest from origin a node can appear
	 * @param vertices, Number of vertices desired
	 * @param maxDist,  maximum possible distance the edges may have
	 */
	public void generateDisk(Integer radius, Integer vertices, double maxDist) {
		Integer dist;
		Integer[] list = new Integer[vertices];
		for (int i = 0; i < vertices; i++) {
			double randomA = ThreadLocalRandom.current().nextDouble(0, 1);
			double randomB = ThreadLocalRandom.current().nextDouble(0, 1);
			list[i] = addVertexWithPos( // generate x,y
					(int) Math.round(randomB * radius * java.lang.Math.cos(2 * Math.PI * randomA / randomB)),
					(int) Math.round(randomB * radius * java.lang.Math.sin(2 * Math.PI * randomA / randomB)));
		}

		LinkedList<Integer[]> checklist = findPossibleEdges(list);

		for (int edgeCount = 0; edgeCount < (int) (0.5 * vertices * (vertices - 1)); edgeCount++) {
			dist = getdist(vertObjFind.get(checklist.getFirst()[0]).x, vertObjFind.get(checklist.getFirst()[1]).x,
					vertObjFind.get(checklist.getFirst()[0]).y, vertObjFind.get(checklist.getFirst()[1]).y);
			double randomC = ThreadLocalRandom.current().nextDouble(0, maxDist); // linear prob. func., lower diff edges
																					// more likely
			if (dist > randomC) {
				checklist.removeFirst();
				continue;
			}
			addUndirectedEdgeFromPos(checklist.getFirst()[0], checklist.getFirst()[1]);
			checklist.removeFirst();
		}

	}

	/**
	 * Helper method. Finds all possible undirected edge combinations given an array
	 * of Vertex IDs. Returns them in shuffled order
	 * 
	 * @param arr, An array of vertex IDs
	 * @return A shuffled linked list of all possible undirected edge combinations.
	 */
	public LinkedList<Integer[]> findPossibleEdges(Integer arr[]) {

		LinkedList<Integer[]> result = new LinkedList<Integer[]>();

		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr.length; j++) {
				Integer[] edge = { arr[i], arr[j] };
				if (arr[i] != arr[j]) {
					if (!hasInverted(arr[i], arr[j], result))
						result.add(edge);
				}
			}
		}

		Collections.shuffle(result);

		return result;
	}

	/**
	 * Helper method for findPossibleEdges. Determines if x,y or y,x are already in
	 * the linked list
	 * 
	 * @param x
	 * @param y
	 * @param c, Linked list containing integer arrays of size 2.
	 * @return true if x,y or y,x are already in the linked list.
	 */
	private boolean hasInverted(Integer x, Integer y, LinkedList<Integer[]> c) {

		for (int i = 0; i < c.size(); i++) {

			Integer cx = c.get(i)[0];
			Integer cy = c.get(i)[1];
			if ((x == cx && y == cy) || (x == cy && y == cx)) {

				return true;
			}
		}
		return false;
	}

	public void showGraphConnections() { // Used for debugging
		for (Entry<Integer, ArrayList<Integer[]>> vertex : AdjList.entrySet()) {
			ArrayList<Integer[]> values = vertex.getValue();
			System.out.println("\nvertexID " + vertex.getKey() + " is connected to: ");
			for (Integer[] value : values) {
				System.out.println("vertexID " + value[0] + " with a difficulty of " + value[1]);
			}
		}
	}

	/**
	 * Creates a map containing all nodes, their difficulty to be reached, and thie
	 * adjacent node needed to reach the respective node.
	 * 
	 * @param startID, ID of vertex all path difficulties are relative to
	 * @return a hashmap
	 */
	public HashMap<Integer, DijkstraNode> Dijkstra(Integer startID) {
		Comparator<DijkstraNode> comparator = new DikkiPQComparator(); // compares node difficulty used for the PQ
		HashMap<Integer, DijkstraNode> finished = new HashMap<Integer, DijkstraNode>(); // all vertices where it's
																						// difficulties are found are
																						// put here, <ID, diff>
		PriorityQueue<DijkstraNode> pq = new PriorityQueue<DijkstraNode>(comparator); // min PQ sorting the nodes based
																						// of difficulty

		DijkstraNode rootNode = new DijkstraNode(startID, startID, 0); // start node setup
		pq.add(rootNode);

		while (pq.size() != 0) {
			DijkstraNode currentVert = pq.peek();
			ArrayList<Integer[]> connectedVerts = AdjList.get(currentVert.V_ID);

			OUTER_LOOP: for (int i = 0; i < connectedVerts.size(); i++) {// go through each vertex directly connected to
																			// currentVert
				Integer[] visitedVert = connectedVerts.get(i);

				if (finished.containsKey(visitedVert[0])) {// next if vertex visited is already in finished map
					continue;
				}
				Integer relDiff = currentVert.diff + visitedVert[1]; // difficulty needed to reach visitedVert from
																		// starting vert
				for (DijkstraNode j : pq) { // check if there already exists a route faster than one discovered
					if (j.V_ID == visitedVert[0]) {
						if (relDiff >= j.diff) {
							continue OUTER_LOOP;
						}
						if (relDiff < j.diff) {
							pq.remove(j);
						}
					}
				}
				DijkstraNode newNode = new DijkstraNode(visitedVert[0], currentVert.V_ID, relDiff);
				pq.add(newNode);
			}
			finished.put(pq.peek().V_ID, pq.poll());
		}
		return finished;
	}

	/**
	 * returns a linked list of the nodes along the shortest path from start node to
	 * dest node
	 * 
	 * @param startID, ID of vertex to start path from
	 * @param desttID, ID of vertex to path to
	 * @return a linked list of nodes, {startNode, ... , destNode}
	 */
	public LinkedList<DijkstraNode> DijkstraPath(Integer startID, Integer destID) {
		HashMap<Integer, DijkstraNode> map = Dijkstra(startID);
		LinkedList<DijkstraNode> path = new LinkedList<DijkstraNode>();
		DijkstraNode currentNode;
		DijkstraNode nextNode;

		if (map.containsKey(destID)) { // check if there is a possible route
			currentNode = map.get(destID);
			nextNode = map.get(map.get(currentNode.V_ID).visitedVia);
			path.add(currentNode);

			while (currentNode != nextNode) {
				path.addFirst(nextNode);
				currentNode = nextNode;
				nextNode = map.get(map.get(currentNode.V_ID).visitedVia);
			}
		} else return null;
		return path;
	}

	public static void printPath(LinkedList<DijkstraNode> path) { // used to debug djikstra algo
		for (DijkstraNode i : path) {
			System.out.println(i.V_ID);
		}
	}

	class DikkiPQComparator implements Comparator<DijkstraNode> { // used to compare node difficulties in the PQ
		@Override
		public int compare(DijkstraNode x, DijkstraNode y) {

			if (x.diff < y.diff) {
				return -1;
			}
			if (x.diff > y.diff) {
				return 1;
			}
			return 0;
		}
	}

	class DijkstraNode { // Used in place of the vertex obj for djikstra algo
		Integer V_ID;
		Integer visitedVia;
		Integer diff;

		DijkstraNode(Integer V_ID, Integer visitedVia, Integer diff) {
			this.V_ID = V_ID;
			this.visitedVia = visitedVia;
			this.diff = diff;
		}

	}

	/**
	 * Creates a graph representing the shape of a maze in the form of bool[i][j] where:
	 * 
	 *  {{i = 0 , j = 0}, {i = 0 , j = 1}, {i = 0 , j = 2}... {i = 0 , j = n}
	 * 	 {i = 1 , j = 0}, ...
	 * 	 {i = 2 , j = 0}, ...
	 * 	 .
	 * 	 .
	 *   .
	 * 	 {i = k , j = 0}, ...								  {i = k , j = n}}
	 * 
	 * and the vertex ID's are:
	 * 
	 * {{0},    {1},    {2}, ... {n}
	 *  {n+1},  {n+2},       ...
	 *  {2n+1}, {2n+2},      ...
	 *  .
	 *  .
	 *  .
	 *  {kn-(n+1)}, 		 ... {kn-1}}
	 * 
	 * @param boolean[][], represents the maze, inner arrays must be same lengths, true = path, false = wall
	 */
	public void createMaze(boolean[][] maze){
		for(int i = 0 ; i < maze.length; i++){
			for(int j = 0 ; j < maze[0].length; j++){
			Integer newPixel = addVertex();
			if(j != 0 && maze[i][j] == true && maze[i][j-1] == true){ //add edge on horiz case
				addUndirectedEdge(newPixel, newPixel -1, 1);
			}
			if(i != 0 && maze[i][j] == true && maze[i-1][j] == true){ //add edge on vert case
				addUndirectedEdge(newPixel, newPixel -maze[0].length, 1);
			}
			}

		}
	}

}
