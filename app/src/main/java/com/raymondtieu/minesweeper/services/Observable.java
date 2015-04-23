package com.raymondtieu.minesweeper.services;

import com.raymondtieu.minesweeper.utils.Notification;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by raymond on 2015-04-22.
 */
public class Observable {

    private List<Observer> observers = new ArrayList<>();

    public void attach(Observer o) {
        if (!observers.contains(o)) {
            observers.add(o);
        }
    }

    public void notifyObservers(Notification type, int value) {
        for (Observer o : observers)
            o.update(type, value);
    }
}
