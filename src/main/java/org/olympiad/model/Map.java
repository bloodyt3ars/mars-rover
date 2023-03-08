package org.olympiad.model;

import java.util.List;

 public class Map {
        private List<Vertex> vertex;
        private List<Edge> edge;
        private Goal goal;

        private Robot robot;

        public Map(List<Vertex> vertices, List<Edge> edges, Goal goal, Robot robot) {
            this.vertex = vertices;
            this.edge = edges;
            this.goal = goal;
            this.robot = robot;
        }

        public List<Vertex> getVertex() {
            return vertex;
        }

        public List<Edge> getEdges() {
            return edge;
        }

        public Goal getGoal() {
            return goal;
        }

        public Robot getRobot() {
         return robot;
     }
    }

