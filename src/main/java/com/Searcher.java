package com;

public class Searcher {
    private static Searcher instance;

    private Searcher() {

    }

    public static Searcher getInstance() {
        if (instance == null) {
            instance = new Searcher();
        }
        return instance;
    }


}
