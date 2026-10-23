/**
 * Missions 1 and 2. Building the maze out of the walls.
 * A wall k joins the cells w.a()[k] and w.b()[k] and costs w.cost()[k]. Indexes start from 0,
 * the line number in walls.txt is the index + 1. The cells are connected by DisjointSet.
 */
import java.util.*;
public class Maze {

    /**
     * Mission 1. Take the walls in the order of the file. Break a wall when its two cells are in different sets.
     * Returns the indexes of the broken walls, in the order of breaking.
     */
    public static int[] breakWalls(Input.Walls w) {
        int cells = w.cells();
        DisjointSet ds = new DisjointSet(cells);
        int[] broken = new int[w.count()];
        int brokenCount = 0;
        for (int i = 0; i < w.count(); i++) {
            int a = w.a()[i];
            int b = w.b()[i];
            if(ds.union(a, b)) {
                broken[brokenCount] = i;
                brokenCount++;
            }
        }
        return java.util.Arrays.copyOf(broken, brokenCount);
    }

    /**
     * Mission 2. Kruskal: the same, but take the walls from the cheapest to the most expensive.
     * Returns the indexes of the broken walls.
     */
    public static int[] cheapestMaze(Input.Walls w) {
        DisjointSet ds = new DisjointSet(w.cells());
        Integer[] order = new Integer[w.count()];
        for (int i = 0; i < w.count(); i++) {
            order[i] = i;
        }
        java.util.Arrays.sort(order, (i, j) -> {
            int byCost = Integer.compare(w.cost()[i], w.cost()[j]);
            if (byCost != 0) {
                return byCost;
            }
            return Integer.compare(i, j);
        });
        int[] broken = new int[w.count()];
        int brokenCount = 0;
        for (int i : order) {
            int a = w.a()[i];
            int b = w.b()[i];
            if(ds.union(a, b)) {
                broken[brokenCount] = i;
                brokenCount++;
            }
        }
        return java.util.Arrays.copyOf(broken, brokenCount);
    }

    /**
     * Mission 2. Prim with a priority queue. Returns the total cost of the cheapest maze.
     * It must be equal to the total cost of cheapestMaze.
     */

    public static long primCost(Input.Walls w) {
        int cells = w.cells();
        List<Integer>[] graph = new ArrayList[cells];
        for (int i = 0; i < cells; i++) {
            graph[i] = new ArrayList<>();
        }
        for (int i = 0; i < w.count(); i++) {
            int a = w.a()[i];
            int b = w.b()[i];

            graph[a].add(i);
            graph[b].add(i);
        }
        boolean[] visited = new boolean[cells];
        PriorityQueue<Integer> pq = new PriorityQueue<>(
                (i, j) -> {
                    int byCost = Integer.compare(w.cost()[i], w.cost()[j]);
                    if (byCost != 0) {
                        return byCost;
                    }
                    return Integer.compare(i, j);
                }
        );
        visited[0] = true;
        int visitedCount = 1;
        long totalCost = 0;
        for (int wall : graph[0]) {
            pq.add(wall);
        }
        while (!pq.isEmpty() && visitedCount < cells) {
            int wall = pq.poll();
            int a = w.a()[wall];
            int b = w.b()[wall];
            if (visited[a] && visited[b]) {
                continue;
            }
            int next = visited[a] ? b : a;
            visited[next] = true;
            visitedCount++;
            totalCost += w.cost()[wall];
            for (int nextWall : graph[next]) {
                int x = w.a()[nextWall];
                int y = w.b()[nextWall];
                if (!visited[x] || !visited[y]) {
                    pq.add(nextWall);
                }
            }
        }
        if (visitedCount != cells) {
            throw new IllegalStateException(
                    "The wall graph is disconnected."
            );
        }
        return totalCost;
    }
}
