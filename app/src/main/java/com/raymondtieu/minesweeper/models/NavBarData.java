package com.raymondtieu.minesweeper.models;

/**
 * Created by raymond on 2015-04-03.
 */
public class NavBarData {
    private String title;
    private int iconId;

    public NavBarData(String title, int iconId) {
        this.title = title;
        this.iconId = iconId;
    }

    public String getTitle() {
        return title;
    }

    public int getIconId() {
        return iconId;
    }
}
