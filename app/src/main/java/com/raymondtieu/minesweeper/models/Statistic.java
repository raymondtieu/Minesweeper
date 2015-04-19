package com.raymondtieu.minesweeper.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by raymond on 2015-04-18.
 */
public class Statistic implements Parcelable {

    public static class Record implements Parcelable {
        private long date;
        private long time;

        public Record(long date, long time) {
            this.date = date;
            this.time = time;
        }

        @Override
        public String toString() {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

            Date d = new Date(date);

            return "" + (int) (time / 1000) + "s" + "   -   " + sdf.format(d);
        }

        protected Record(Parcel in) {
            date = in.readLong();
            time = in.readLong();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(date);
            dest.writeLong(time);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<Record> CREATOR = new Parcelable.Creator<Record>() {
            @Override
            public Record createFromParcel(Parcel in) {
                return new Record(in);
            }

            @Override
            public Record[] newArray(int size) {
                return new Record[size];
            }
        };
    }

    private Record[] bestRecords;
    private int gamesPlayed, gamesWon, winPercentage;
    private int winStreak, loseStreak;
    private String streak;

    public Statistic() {}

    public String getBestRecords() {
        String str = "";

        for (Record record : bestRecords) {
            if (record != null)
                str += record.toString();

            str +=  "\n";
        }

        return str;
    }

    public void setBestRecords(Record[] records) {
        this.bestRecords = records;
    }

    public String getGamesPlayed() {
        return "Games played: " + gamesPlayed;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public String getWinPercentage() {
        return "Win percentage: " +  winPercentage + "%";
    }

    public void calculateWinPercentage() {
        if (gamesPlayed == 0)
            winPercentage = 0;
        else
            winPercentage = (int) ((gamesWon * 100.0f) / gamesPlayed);
    }

    public String getGamesWon() {
        return "Games won: " + gamesWon;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    public String getWinStreak() {
        return "Longest winning streak: " + winStreak;
    }

    public void setWinStreak(int winStreak) {
        this.winStreak = winStreak;
    }

    public String getLoseStreak() {
        return "Longest losing streak: " + loseStreak;
    }

    public void setLoseStreak(int loseStreak) {
        this.loseStreak = loseStreak;
    }

    public String getStreak() {
        return "Current streak: " + streak;
    }

    public void setStreak(String streak) {
        this.streak = streak;
    }

    /* PARCELABLE METHODS */

    protected Statistic(Parcel in) {
        in.readTypedArray(bestRecords, Record.CREATOR);
        gamesPlayed = in.readInt();
        gamesWon = in.readInt();
        winPercentage = in.readInt();
        winStreak = in.readInt();
        loseStreak = in.readInt();
        streak = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedArray(bestRecords, flags);
        dest.writeInt(gamesPlayed);
        dest.writeInt(gamesWon);
        dest.writeInt(winPercentage);
        dest.writeInt(winStreak);
        dest.writeInt(loseStreak);
        dest.writeString(streak);
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
