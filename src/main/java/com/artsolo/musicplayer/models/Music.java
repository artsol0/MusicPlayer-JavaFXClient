package com.artsolo.musicplayer.models;

import java.io.Serializable;

public class Music implements Serializable {
    private final int id;
    private final String title;
    private final String performer;

    public Music(int id, String title, String performer) {
        this.id = id;
        this.title = title;
        this.performer = performer;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPerformer() {
        return performer;
    }
}
