package com.raymondtieu.minesweeper.models;

/**
 * Created by raymond on 2015-04-02.
 */
public class Board {

    public enum FlagType {
        NONE("None"), FLAG("Flag"), UNKNOWN("Unknown");

        private String type;

        private FlagType(String type) {
            this.type = type;
        }

        public String showType() {
            return this.type;
        }
    }

    static class Cell {
        private int numMines = 0;
        private boolean revealed = false;

        public int getNumMines() {
            return numMines;
        }
        public void setNumMines(int numMines) {
            this.numMines = numMines;
        }
        public boolean isRevealed() {
            return revealed;
        }
        public void setRevealed(boolean revealed) {
            this.revealed = revealed;
        }
        public void addMine() {
            this.numMines++;
        }
    }

    private int xDimension;
    private int yDimension;
    private int mines;
    private Cell[][] board;

    public Board(int dx, int dy, int m) {
        setxDimension(dx);
        setyDimension(dy);
        setMines(m);
        this.board = new Cell[xDimension][yDimension];

        // generate blank blocks
        for (int i = 0; i < xDimension; i++) {
            for (int j = 0; j < yDimension; j++) {
                board[i][j] = new Cell();
            }
        }
    }

    public int getxDimension() {
        return xDimension;
    }

    public void setxDimension(int xDimension) {
        this.xDimension = xDimension;
    }

    public int getyDimension() {
        return yDimension;
    }

    public void setyDimension(int yDimension) {
        this.yDimension = yDimension;
    }

    public int getMines() {
        return mines;
    }

    public void setMines(int mines) {
        this.mines = mines;
    }

    public void generateBoard(int x, int y) {
        int n = this.mines;

        // place mines on blank board until there are no mines left to be placed
        // generate i and j value until bomb can be placed
        while (n > 0) {
            int i = (int) (Math.random() * xDimension);
            int j = (int) (Math.random() * yDimension);

            // boolean values for when i and j are surrounding x and y
            boolean a_x = Math.abs(x - i) <= 1;
            boolean a_y = Math.abs(y - j) <= 1;

            // check for valid coordinates, i.e. not starting point or around it
            if (!(a_x && a_y)) {
                // not already bomb there
                if (!(board[i][j].getNumMines() >= 9)) {
                    board[i][j].setNumMines(9);

                    // update surrounding blocks
                    for (int a = -1; a <= 1; a++) {
                        for (int b = -1; b <= 1; b++) {
                            int s_x = i + a;
                            int s_y = j + b;

                            // check out of bounds
                            if (s_x >= 0 && s_x < xDimension && s_y >= 0 && s_y < yDimension)
                                board[s_x][s_y].addMine();
                        }
                    }

                    n -= 1;
                }
            }
        }
    }

    public int getNumMines(int x, int y) {
        return board[x][y].getNumMines();
    }

    public boolean isRevealed(int x, int y) {
        return board[x][y].isRevealed();
    }

    public int revealCell(int x, int y) {
        board[x][y].setRevealed(true);

        return getNumMines(x, y);
    }

    public void markCell(FlagType type) {
        // TODO
    }
}

