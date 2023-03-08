package org.olympiad.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.olympiad.model.Map;
import org.olympiad.model.*;

import java.util.*;

public class PathResolver1 {
    private static final Logger logger = LogManager.getLogger(PathResolver1.class);


    // метод находит оптимальный путь для сбора необходимого количества ресурсов и доставки их на базу
    public Answer findAnswer(Map map) {
        List<Vertex> vertices = map.getVertex();
        List<Edge> edges = map.getEdges();
        int robotСapacity = map.getRobot().getSize();
        int resourcesGoal = map.getGoal().getResources();

        int n = vertices.size();
        int[][] adj = new int[n][n];
        for (Edge e : edges) {
            adj[e.getStart()][e.getStop()] = e.getSize();
            adj[e.getStop()][e.getStart()] = e.getSize();
        }
        int[] parent = new int[n];
        Arrays.fill(parent, -1);
        boolean[] visited = new boolean[n];
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(0);
        visited[0] = true;
        while (!queue.isEmpty()) {

            int u = queue.poll();
            if (u == n - 1) break;
            for (int v = 0; v < n; v++) {
                if (adj[u][v] > 0 && !visited[v]) {
                    queue.offer(v);
                    visited[v] = true;
                    parent[v] = u;
                }
            }
        }
        List<Integer> path = new ArrayList<>();
        int u = n - 1;
        while (parent[u] != -1) {
            path.add(u);
            u = parent[u];
        }
        path.add(0);
        Collections.reverse(path);

        for (int i = path.size() - 2; i >= 0; i--) {
            path.add(path.get(i));
        }

        Answer answer = new Answer(path);
        logger.info("Response: {}", answer);
        return answer;
    }

}
