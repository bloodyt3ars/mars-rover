package org.olympiad.model;

public class VertexDistance {
    private final Vertex vertex;
    private int distance;

    public VertexDistance(Vertex vertex, int distance) {
        this.vertex = vertex;
        this.distance = distance;
    }

    public Vertex getVertex() {
        return vertex;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
