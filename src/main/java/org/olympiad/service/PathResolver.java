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
            if (vertex.getType().equalsIgnoreCase("base")) {
                base = vertex;
            } else if (vertex.getType().equalsIgnoreCase("mine")) {
                mines.add(vertex);
            }
        }

        if (requiredResources>robotСapacity){
            if (mines.size()==1){
                Vertex start = base;
                Vertex stop = mines.get(0);
                int robotСap = robotСapacity;
                int mineResources = stop.getResources();
                while ((mineResources>robotСap)||(mineResources>0)){
                    if (path.size()>0){
                        path.remove(path.size()-1);
                    }
                    mineResources = mineResources - robotСapacity;
                    path.addAll(dijkstraSearch(start, stop));
                    path.remove(path.size()-1);
                    path.addAll(dijkstraSearch(stop, start));
                }
            }
            else {
                Vertex start = base;
                Vertex stop = null;
                int robotСap = robotСapacity;
                int mineResources;
                for (Vertex mine:mines) {
                    stop = mine;
                    mineResources = stop.getResources();
                    while ((mineResources>robotСap)||(mineResources>0)){
                        if (path.size()>0) {
                            path.remove(path.size() - 1);
                        }
                        mineResources = mineResources - robotСapacity;
                        path.addAll(dijkstraSearch(start, stop));
                        path.remove(path.size()-1);
                        start = base;
                        path.addAll(dijkstraSearch(stop, start));
                    }
                }
            }




            /*Vertex start = base;
            Vertex stop = mines.get(0);
            int robotСap = robotСapacity;
            int mineResources = stop.getResources();
            if (robotСap>=mineResources){
                robotСap = robotСap - mineResources;
                path.addAll(dijkstraSearch(start, stop));
            }
            else if (robotСap<mineResources){
                while (robotСap<mineResources){
                    mineResources = mineResources - robotСap;
                    if (path.size()>0){
                        path.remove(path.size()-1);
                    }
                    path.addAll(dijkstraSearch(start, stop));
                    path.remove(path.size()-1);
                    path.addAll(dijkstraSearch(stop, start));
                    robotСap = robotСapacity;
                }
                if ((mines.size()>1)&&(mineResources>0)){
                    path.remove(path.size()-1);
                    path.addAll(dijkstraSearch(start, stop));
                    path.remove(path.size()-1);
                    path.addAll(dijkstraSearch(stop, start));
                    stop = start;
                    robotСap = robotСapacity;
                }
            }
            if (mines.size()>1){
                for (int i = 1; i < mines.size(); i++) {
                    start = stop;
                    stop = mines.get(i);
                    mineResources = stop.getResources();
                    if (mineResources>robotСap){
                        while (mineResources>robotСap){
                            path.remove(path.size()-1);
                            path.addAll(dijkstraSearch(start, stop));
                            mineResources = mineResources - robotСap;
                            start = base;
                            path.remove(path.size()-1);
                            path.addAll(dijkstraSearch(stop, start));
                            robotСap = robotСapacity;
                        }
                        if (mineResources>0){
                            path.remove(path.size()-1);
                            path.addAll(dijkstraSearch(start, stop));
                            path.remove(path.size()-1);
                            path.addAll(dijkstraSearch(stop, start));
                            stop = start;
                            robotСap = robotСapacity;
                        }
                    }
                    else {
                        robotСap = robotСap - stop.getResources();
                        path.remove(path.size()-1);
                        path.addAll(dijkstraSearch(start, stop));
                    }
                }
                path.remove(path.size()-1);
                path.addAll(dijkstraSearch(stop, base));
            } else if ((mineResources>0)&&(mines.size()==1)){
                path.remove(path.size()-1);
                path.addAll(dijkstraSearch(start, stop));
                path.remove(path.size()-1);
                path.addAll(dijkstraSearch(stop, start));
            }*/
        }
        else if (requiredResources<=robotСapacity){
            if (mines.size()==1){
                Vertex start = base;
                Vertex stop = mines.get(0);
                path.addAll(dijkstraSearch(start,stop));
                path.remove(path.size()-1);
                path.addAll(dijkstraSearch(stop,base));
            }
            else {
                Vertex start = base;
                Vertex stop = null;
                for (Vertex mine: mines) {
                    stop = mine;
                    if (path.size()>0){
                        path.remove(path.size()-1);
                    }
                    path.addAll(dijkstraSearch(start,stop));
                    start = stop;
                }
                path.remove(path.size()-1);
                path.addAll(dijkstraSearch(stop,base));
            }
        }

        // возвращаем ответ
        Answer answer = new Answer(path);
        logger.info("Response: {}", answer);
        return answer;
    }
    private List<Integer> dijkstraSearch(Vertex start, Vertex stop){
        int n = map.getVertex().size();
        int[] dist = new int[2*n];
        int[] prev = new int[2*n];
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
