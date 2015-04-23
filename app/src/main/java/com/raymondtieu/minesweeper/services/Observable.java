package com.raymondtieu.minesweeper.services;

import com.raymondtieu.minesweeper.utils.Notification;

import java.util.ArrayList;
import java.util.Iterator;
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

    public void detach(Observer o) {
        observers.remove(o);
    }

    public void notifyRevealed(int position) {
        notifyObservers(Notification.REVEAL, position);
    }

    public void notifyFlagged(int position) {
        notifyObservers(Notification.FLAG, position);
    }

    public void notifyUnflag(int position) {
        notifyObservers(Notification.UNFLAG, position);
    }

    public void notifyInvalidRevealed(int position) {
        notifyObservers(Notification.INVALID_REVEAL, position);
    }


    public void notifyInvalidHidden(int position) {
        notifyObservers(Notification.INVALID_HIDDEN, position);
    }


    public void notifyMine(int position) {
        notifyObservers(Notification.MINE, position);
    }

    public void notifynMines(int value) {
        notifyObservers(Notification.NUM_MINES, value);
    }

    public void notifyToggleFlag(int value) {
        notifyObservers(Notification.TOGGLE_FLAG, value);
    }

    public void notifyStartTime() {
        notifyObservers(Notification.START_TIME, 0);
    }

    public void notifyStopTime() {
        notifyObservers(Notification.STOP_TIME, 0);
    }

    public void notifyWin() {
        notifyObservers(Notification.WIN, 0);
    }

    public void notifyLose() {
        notifyObservers(Notification.LOSE, 0);
    }

    private void notifyObservers(Notification type, int value) {

        // make copy of observers list in the case of adding/removing from
        // collection during iteration
        Iterator<Observer> it = (new ArrayList<>(observers)).iterator();

        while (it.hasNext()) {
            Observer o = it.next();
            o.update(type, value);
        }
    }
}
