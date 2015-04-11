package com.raymondtieu.minesweeper.models;

import com.raymondtieu.minesweeper.services.Game;

/**
 * Created by raymond on 2015-04-02.
 */
public class Field {

    static class Cell {
        private int numMines = 0;
        private boolean revealed = false;

        // -1 : no flag
        // 1 : flagged
        // 2 : flagged correctly
        // 3 : flagged incorrectly
        private int isFlagged = -1;

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

        public void setFlagged(int flag) {
            this.isFlagged = flag;
        }

        public boolean isFlagged() {
            return isFlagged != -1;
        }

        public int getFlagged() {
            return isFlagged;
        }
    }

    private int dimX;
    private int dimY;
    private int mines;
    private int cellsHidden;
    private int flagsPlaced;

    private Cell[][] field;
    private Game game;

    public Field(int dx, int dy, int mines) {
        setDimX(dx);
        setDimY(dy);
        setMines(mines);
        this.field = new Cell[dimX][dimY];

        // generate blank field with empty cells
        for (int i = 0; i < dimX; i++) {
            for (int j = 0; j < dimY; j++) {
                field[i][j] = new Cell();
            }
        }
    }

    public int getDimX() {
        return dimX;
    }

    public void setDimX(int x) {
        dimX = x;
    }

    public int getDimY() {
        return dimY;
    }

    public void setDimY(int y) {
        dimY = y;
    }

    public int getMines() {
        return mines;
    }

    public void setMines(int mines) {
        this.mines = mines;
    }

    public void generateField(int x, int y) {
        int n = this.mines;

        // place mines on blank field until there are no mines left to be placed
        // generate i and j value until position without mine is found
        while (n > 0) {
            int i = (int) (Math.random() * dimX);
            int j = (int) (Math.random() * dimY);

            // boolean values for when i and j are surrounding x and y
            boolean a_x = Math.abs(x - i) <= 1;
            boolean a_y = Math.abs(y - j) <= 1;

            // check for valid coordinates, not starting point or around it
            if (!(a_x && a_y)) {

                // no mine here
                if (field[i][j].getNumMines() < 9) {
                    field[i][j].setNumMines(9);

                    // update surrounding blocks
                    for (int a = -1; a <= 1; a++) {
                        for (int b = -1; b <= 1; b++) {
                            int s_x = i + a;
                            int s_y = j + b;

                            // check out of bounds
                            if (s_x >= 0 && s_x < dimX && s_y >= 0 && s_y < dimY)
                                field[s_x][s_y].addMine();
                        }
                    }

                    n -= 1;
                }
            }
        }

        cellsHidden = dimX * dimY - mines;
    }

    public void setRevealed(int x, int y) {
        field[x][y].setRevealed(true);
        cellsHidden--;

        game.notifyRevealed(x, y);
    }

    public int getNumMines(int x, int y) {
        return field[x][y].getNumMines();
    }

    public boolean isRevealed(int x, int y) {
        return field[x][y].isRevealed();
    }

    public int getCellsHidden() {
        return cellsHidden;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setFlag(int x, int y, int f) {
        game.notifyFlagged(x, y, field[x][y].isFlagged());
        field[x][y].setFlagged(f);

        if (f < 2)
            updateFlags(f);
    }

    public boolean isFlagged(int x, int y) {
        return field[x][y].isFlagged();
    }

    public int reveal(int x, int y) {
        revealAdjacent(x, y);

        return getNumMines(x, y);
    }

    private void revealAdjacent(int x, int y) {

        setRevealed(x, y);

        if (field[x][y].isFlagged()) {
            updateFlags(-1);
        }

        // if there are no adjacent mines, recursively reveal all cells
        // around this one
        if (getNumMines(x, y) == 0) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {

                    // position for cell around this one
                    int n = x + i;
                    int m = y + j;

                    // reveal if adjacent position is not out of bounds
                    if (n >= 0 && n < dimX && m >= 0 && m < dimY && !isRevealed(n, m)) {
                        revealAdjacent(n, m);
                    }
                }
            }
        }
    }

    public int getFlagType(int x, int y) {
        return field[x][y].getFlagged();
    }

    public int getFlagsPlaced() {
        return flagsPlaced;
    }

    public void updateFlags(int i) {
        flagsPlaced += i;
        game.notifyMinesLeft(mines - flagsPlaced);
    }

    public void setCellRevealed(int x, int y) {
        field[x][y].setRevealed(true);
    }

    public static interface MinesChangedListener {
        void onValueChanged(int newValue);
    }
}


