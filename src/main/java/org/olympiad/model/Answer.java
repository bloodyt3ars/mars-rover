package org.olympiad.model;

import java.util.List;

public class Answer {
    private List<Integer> path;

    public Answer(List<Integer> path) {
        this.path = path;
    }

    public List<Integer> getPath() {
        return path;
    }
}
