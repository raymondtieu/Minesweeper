package com.raymondtieu.minesweeper.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by raymond on 2015-04-18.
 */
public class Statistic implements Parcelable{

    private Long bestTime;
    private int gamesPlayed, gamesWon, winPercentage;
    private int winStreak, loseStreak, streak;

    public Statistic() {

    }

    public Long getBestTime() {
        return bestTime;
    }

    public void setBestTime(Long bestTime) {
        this.bestTime = bestTime;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public int getWinPercentage() {
        return winPercentage;
    }

    public void setWinPercentage(int winPercentage) {
        this.winPercentage = winPercentage;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    public int getWinStreak() {
        return winStreak;
    }

    public void setWinStreak(int winStreak) {
        this.winStreak = winStreak;
    }

    public int getLoseStreak() {
        return loseStreak;
    }

    public void setLoseStreak(int loseStreak) {
        this.loseStreak = loseStreak;
    }

    public int getStreak() {
        return streak;
    }

    public void setStreak(int streak) {
        this.streak = streak;
    }

    /* PARCELABLE METHODS */

    protected Statistic(Parcel in) {
        bestTime = in.readByte() == 0x00 ? null : in.readLong();
        gamesPlayed = in.readInt();
        gamesWon = in.readInt();
        winPercentage = in.readInt();
        winStreak = in.readInt();
        loseStreak = in.readInt();
        streak = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (bestTime == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(bestTime);
        }
        dest.writeInt(gamesPlayed);
        dest.writeInt(gamesWon);
        dest.writeInt(winPercentage);
        dest.writeInt(winStreak);
        dest.writeInt(loseStreak);
        dest.writeInt(streak);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Statistic> CREATOR = new Parcelable.Creator<Statistic>() {
        @Override
        public Statistic createFromParcel(Parcel in) {
            return new Statistic(in);
        }

        @Override
        public Statistic[] newArray(int size) {
            return new Statistic[size];
        }
    };
}
