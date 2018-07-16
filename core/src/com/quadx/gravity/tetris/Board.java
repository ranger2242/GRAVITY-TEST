package com.quadx.gravity.tetris;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Chris Cavazos on 2/15/2018.
 */
public class Board {
    int[][] board;

    public Board(int n, int m) {
        board = new int[n][m];
    }

    public void add(Block b) {
        int[][] s = b.getShape();
        Vector2 p = b.getPos();
        for (int i = 0; i < s.length; i++) {
            for (int j = 0; j < s[0].length; j++) {
                int x = (int) (p.x + i);
                int y = (int) (p.y + j);
                board[x][y] = s[i][j] >0 ? s[i][j] : board[x][y];
            }
        }
    }

    public void clear() {
        for (int i = 0; i < board.length; i++) {
            boolean b = true;
            for (int j = 0; j < board[0].length; j++) {
                b = board[i][j] > 0 && b;
            }
        }
    }

    public Color colorAt(int i, int j) {
        switch (board[i][j]){
            case 1:
                return Color.RED;
            case 2:
                return Color.BLUE;
            case 3:
                return Color.GREEN;
            case 4:
                return Color.BROWN;
            case 5:
                return Color.YELLOW;
            case 6:
                return Color.PURPLE;
            case 7:
                return Color.CYAN;
        }
        return Color.WHITE;
    }
}
