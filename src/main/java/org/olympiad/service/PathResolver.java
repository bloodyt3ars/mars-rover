package org.olympiad.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.olympiad.model.*;
import org.olympiad.model.Map;

import java.util.*;

public class PathResolver {
    private static final Logger logger = LogManager.getLogger(PathResolver.class);


    // метод находит оптимальный путь для сбора необходимого количества ресурсов и доставки их на базу
/*    public Answer findAnswer(Map map) {
        //TODO - напишите здесь реализацию метода поиска одного из возможных оптимальных маршрутов на карте
        List<Integer> path = Collections.emptyList();
        Answer answer = new Answer(path);
        logger.info("Response: {}", answer);
        return answer;
    }*/

    public Answer findAnswer(Map map) {
        // находим базу и шахты с нужным количеством ресурсов
        Vertex base = null;
        List<Vertex> mines = new ArrayList<>();
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

        // инициализируем массив расстояний и предков
        int n = map.getVertex().size();
        int[] dist = new int[n];
        int[] prev = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(prev, -1);
        dist[base.getId()] = 0;


        // инициализируем очередь с приоритетом для хранения вершин в порядке возрастания расстояний
        PriorityQueue<VertexDistance> queue = new PriorityQueue<>(Comparator.comparingInt(VertexDistance::getDistance));
        queue.offer(new VertexDistance(base, 0));

        // выполняем алгоритм Дейкстры
        while (!queue.isEmpty()) {
            VertexDistance vd = queue.poll();
            Vertex current = vd.getVertex();
            int distance = vd.getDistance();
            if (distance > dist[current.getId()]) {
                continue; // уже найден кратчайший путь до этой вершины
            }
            for (Edge edge : map.getEdges()) {
                if (edge.getStart() == current.getId()) {
                    Vertex next = map.getVertex().get(edge.getStop());
                    int weight = edge.getSize();
                    if (current.getType().equals("mine") && current.getResources() >= requiredResources) {
                        weight = 0; // можем перенести все ресурсы до базы сразу из шахты, которая уже содержит достаточное количество ресурсов
                    }
                    if (dist[next.getId()] > dist[current.getId()] + weight) {
                        dist[next.getId()] = dist[current.getId()] + weight;
                        prev[next.getId()] = current.getId();
                        queue.offer(new VertexDistance(next, dist[next.getId()]));
                    }
                } else if (edge.getStop() == current.getId()) {
                    Vertex next = map.getVertex().get(edge.getStart());
                    int weight = edge.getSize();
                    if (current.getType().equals("mine") && current.getResources() >= requiredResources) {
                        weight = 0;
                    }
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
        int current = mines.get(0).getId();
        while (current != -1) {
            path.add(current);
            current = prev[current];
        }
        Collections.reverse(path);


        // возвращаем ответ
        Answer answer = new Answer(path);
        logger.info("Response: {}", answer);
        return answer;
    }

}
