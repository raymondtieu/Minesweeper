package com.raymondtieu.minesweeper.models;

import android.util.Log;

/**
 * Created by raymond on 2015-04-02.
 */
public class Field {

    private int dimX;
    private int dimY;
    private int mines;

    private Cell[][] field;

    public Field(int x, int y, int mines) {
        this.dimX = x;
        this.dimY = y;
        this.mines = mines;

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

    public int getDimY() {
        return dimY;
    }

    public int getMines() {
        return mines;
    }

    public int getNumCells() {
        return dimX * dimY;
    }

    public Cell getCell(int x, int y) {
        return field[x][y];
    }

    public void generateField(int x, int y) {
        int n = this.mines;

        // place mines on blank field until there are no mines left to be placed
        // generate i and j value until position without mine is found
        while (n > 0) {
            int a = (int) (Math.random() * dimX);
            int b = (int) (Math.random() * dimY);

            // true when i and j are surrounding x and y
            boolean adjacentX = Math.abs(x - a) <= 1;
            boolean adjacentY = Math.abs(y - b) <= 1;

            // check for valid coordinates (not starting point or around it)
            if (!(adjacentX && adjacentY)) {

                // if no mine at (a,b), set mine
                if (field[a][b].getAdjacentMines() < 9) {
                    field[a][b].setAdjacentMines(9);

                    // update surrounding blocks
                    for (int i = -1; i <= 1; i++) {
                        for (int j = -1; j <= 1; j++) {
                            int w = a + i;
                            int v = b + j;

                            // check out of bounds
                            if (w >= 0 && w < dimX && v >= 0 && v < dimY)
                                field[w][v].addAdjacentMine();
                        }
                    }

                    n--;
                }
            }
        }
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

    public boolean revealSurrounding(int x, int y) {
        // check for valid number of flags
        int n = field[x][y].getNumMines();

        int nFlags = 0;

        for (int a = -1; a <= 1; a++) {
            for (int b = -1; b <= 1; b++) {
                int s_x = x + a;
                int s_y = y + b;
                if (s_x >= 0 && s_x < dimX && s_y >= 0 && s_y < dimY) {
                    if (field[s_x][s_y].isFlagged())
                        nFlags++;
                }
            }
        }

        Log.i("Field", "flags = " + nFlags + ", " + "number of mines = " + n);
        // open surrounding cells
        if (n != nFlags)
            return -1;

        Log.i("Field", "revealing surrounding");
        for (int a = -1; a <= 1; a++) {
            for (int b = -1; b <= 1; b++) {
                int s_x = x + a;
                int s_y = y + b;
                if (s_x >= 0 && s_x < dimX && s_y >= 0 && s_y < dimY) {
                    if (!field[s_x][s_y].isRevealed() && !field[s_x][s_y].isFlagged())
                        reveal(s_x, s_y);
                }
            }
        }

        return true;
    }
}


