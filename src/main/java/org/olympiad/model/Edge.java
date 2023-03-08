package org.olympiad.model;

public class Edge {
    private int start;
    private int stop;
    private int size;

    public Edge(int start, int stop, int size) {
        this.start = start;
        this.stop = stop;
        this.size = size;
    }

    public int getStart() {
        return start;
    }

    public int getStop() {
        return stop;
    }

    public int getSize() {
        return size;
    }
}

