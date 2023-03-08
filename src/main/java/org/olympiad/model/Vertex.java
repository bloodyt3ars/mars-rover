package org.olympiad.model;

public class Vertex {
    private int id;
    private String type;
    private int resources;
    public Vertex(int id, String type) {
        this.id = id;
        this.type = type;
    }
    public Vertex(int id, String type, int resources) {
        this.id = id;
        this.type = type;
        this.resources = resources;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public int getResources() {
        return resources;
    }
}
