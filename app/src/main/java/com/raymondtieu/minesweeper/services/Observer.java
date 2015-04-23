package com.raymondtieu.minesweeper.services;

import com.raymondtieu.minesweeper.utils.Notification;

/**
 * Created by raymond on 2015-04-22.
 */
public interface Observer {

    public void update(Notification type, int value);

}
