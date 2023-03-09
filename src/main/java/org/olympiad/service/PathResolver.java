package org.olympiad.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.olympiad.model.*;
import org.olympiad.model.Map;

import java.util.*;

public class PathResolver {
    private static final Logger logger = LogManager.getLogger(PathResolver.class);

        private Map map;


    public Answer findAnswer(Map map) {
        // находим базу и шахты с нужным количеством ресурсов
        this.map = map;
        Vertex base = null;
        List<Vertex> mines = new ArrayList<>();
        List<Integer> path = new ArrayList<>();
        int requiredResources = map.getGoal().getResources();
        int robotСapacity = map.getRobot().getSize();

        for (Vertex vertex : map.getVertex()) {
            if (vertex.getType().equals("base")) {
                base = vertex;
            } else if (vertex.getType().equals("mine")) {
                mines.add(vertex);
            }
        }
        if (base == null || mines.isEmpty()) {
            logger.info("No solution found");
            return new Answer(Collections.emptyList());
        }

        if (requiredResources>robotСapacity){
            Vertex start = base;
            Vertex stop = mines.get(0);
            int robotСap = robotСapacity;
            robotСap = robotСap - stop.getResources();
            path.addAll(dijkstraSearch(start, stop));
            for (int i = 1; i < mines.size(); i++) {
                start = stop;
                stop = mines.get(i);
                if (stop.getResources()>robotСap){
                    path.remove(path.size()-1);
                    path.addAll(dijkstraSearch(start, base));
                    start = base;
                    robotСap = robotСapacity;
                }
                robotСap = robotСap - stop.getResources();
                path.remove(path.size()-1);
                path.addAll(dijkstraSearch(start, stop));
            }
            path.remove(path.size()-1);
            path.addAll(dijkstraSearch(stop, base));
        }
        else if (requiredResources<=robotСapacity){
            Vertex start = base;
            Vertex stop = mines.get(0);
            path.addAll(dijkstraSearch(start,stop));
            if (mines.size()>0){
                for (int i = 1; i < mines.size(); i++) {
                    start = stop;
                    stop = mines.get(i);
                    path.remove(path.size()-1);
                    path.addAll(dijkstraSearch(start,stop));
                }
            }
            path.remove(path.size()-1);
            path.addAll(dijkstraSearch(stop,base));
        }

        // возвращаем ответ
        Answer answer = new Answer(path);
        logger.info("Response: {}", answer);
        return answer;
    }
    private List<Integer> dijkstraSearch(Vertex start, Vertex stop){
        int n = map.getVertex().size();
        int[] dist = new int[n];
        int[] prev = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(prev, -1);
        dist[start.getId()] = 0;

        PriorityQueue<VertexDistance> queue = new PriorityQueue<>(Comparator.comparingInt(VertexDistance::getDistance));
        queue.offer(new VertexDistance(start, 0));

        while (!queue.isEmpty()) {
            VertexDistance vd = queue.poll();
            Vertex current = vd.getVertex();
            int distance = vd.getDistance();
            if (current==stop){
                break;
            }
            if (distance > dist[current.getId()]) {
                continue; // уже найден кратчайший путь до этой вершины
            }
            for (Edge edge:map.getEdges()) {
                if (edge.getStart() == current.getId()) {
                    Vertex next = map.getVertex().get(edge.getStop());
                    int weight = edge.getSize();
                    if (dist[next.getId()] > dist[current.getId()] + weight) {
                        dist[next.getId()] = dist[current.getId()] + weight;
                        prev[next.getId()] = current.getId();
                        queue.offer(new VertexDistance(next, dist[next.getId()]));
                    }
                }
                else if (edge.getStop() == current.getId()) {
                    Vertex next = map.getVertex().get(edge.getStart());
                    int weight = edge.getSize();
                    if (dist[next.getId()] > dist[current.getId()] + weight) {
                        dist[next.getId()] = dist[current.getId()] + weight;
                        prev[next.getId()] = current.getId();
                        queue.offer(new VertexDistance(next, dist[next.getId()]));
                    }
                }
            }
        }

        // собираем маршрут в обратном порядке, начиная от конца
        List<Integer> path = new ArrayList<>();
        int current = stop.getId();
        while (current != -1) {
            path.add(current);
            current = prev[current];
        }
        Collections.reverse(path);
        return path;
    }

}
