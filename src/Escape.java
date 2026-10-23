/**
 * Mission 3. Escape: DFS from cell 0 to the last cell (rows * cols - 1) in the maze of Mission 1.
 * Two neighbouring cells are connected when the wall between them is broken.
 * The neighbours are tried in this order: up, right, down, left (cell - cols, cell + 1, cell + cols, cell - 1).
 * The ready method Graphs.open(w, broken) gives you the open neighbours of every cell, in no particular order:
 * to visit them in the required order, go through the four cells above one by one and take those that are in the list.
 * The program must not overflow the stack on a maze of 1000 by 1000 cells.
 */

public class Escape {

    /**
     * path: the cells of the way out, from 0 to the last cell.
     * visited: how many cells are marked as visited at the moment the exit is marked (the start and the exit are counted).
     * deadEnds: how many cells of the maze have exactly one open neighbour.
     */
    public record Result(int[] path, int visited, int deadEnds) {}

    public static Result escape(Input.Walls w, int[] broken) {
        int cells = w.cells();
        int cols = w.cols();
        int[][] open = Graphs.open(w, broken);
        boolean[] visited = new boolean[cells];
        int[] stack = new int[cells];
        int top = 0;
        stack[top++] = 0;
        int[] nextDirection = new int[cells];
        int[] parent = new int[cells];
        java.util.Arrays.fill(parent, -1);
        visited[0] = true;
        int visitedCount = 1;
        int exit = cells - 1;
        while (top > 0) {
            int current = stack[top - 1];
            if (current == exit) {
                break;
            }
            if (nextDirection[current] == 4) {
                top--;
                continue;
            }
            int next = -1;
            if (nextDirection[current] == 0) {
                next = current - cols;
            } else if (nextDirection[current] == 1) {
                next = current + 1;
            } else if (nextDirection[current] == 2) {
                next = current + cols;
            } else {
                next = current - 1;
            }
            nextDirection[current]++;
            if (next < 0 || next >= cells) {
                continue;
            }
            boolean isOpen = false;
            for (int neighbor : open[current]) {
                if (neighbor == next) {
                    isOpen = true;
                    break;
                }
            }
            if (!isOpen || visited[next]) {
                continue;
            }
            visited[next] = true;
            visitedCount++;
            parent[next] = current;
            stack[top++] = next;
        }
        int pathLength = 0;
        int cell = exit;
        while (cell != -1) {
            pathLength++;
            cell = parent[cell];
        }
        int[] path = new int[pathLength];
        cell = exit;
        for (int i = pathLength - 1; i >= 0; i--) {
            path[i] = cell;
            cell = parent[cell];
        }
        int deadEnds = 0;
        for (int i = 0; i < cells; i++) {
            if (open[i].length == 1) {
                deadEnds++;
            }
        }
        return new Result(path, visitedCount, deadEnds);
    }
}
