package com.quadx.gravity.tetris;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.quadx.gravity.Game;
import com.quadx.gravity.shapes1_4.Delta;
import com.quadx.gravity.states.TetrisState;

import static com.quadx.gravity.Game.ft;

/**
 * Created by Chris Cavazos on 2/15/2018.
 */
public class Block {
    Shape shape;
    Type type;
    Delta drop = new Delta(ft * 20);
    Delta dMove = new Delta(ft * 3);
    Delta dRot = new Delta(ft * 6);

    boolean collided = false;

    public Block() {
        type = Type.random();
        shape = new Shape(type);
    }

    public int[][] getShape() {
        return shape.getShape();
    }

    public Vector2 getPos() {
        return shape.pos;
    }

    public Color getColor() {
        switch (type) {

            case T:
                return Color.RED;
            case Z:
                return Color.BLUE;
            case S:
                return Color.GREEN;
            case O:
                return Color.BROWN;
            case L:
                return Color.YELLOW;
            case J:
                return Color.PURPLE;
            case I:
                return Color.CYAN;
        }
        return Color.WHITE;
    }

    public Color colorAt(int i, int j) {
        return getShape()[i][j] > 0 ? getColor() : Color.WHITE;
    }

    public void update(float dt, Board b) {
        dRot.update(dt);
        drop.update(dt);
        dMove.update(dt);
        if (drop.isDone()) {
            if (getPos().y<TetrisState.m && canMove(new Vector2(0, 1))) {
                shape.pos.y++;
                drop.reset();
            } else {
                collided = true;
                b.add(this);
            }

        }
    }


    private boolean canMove(Vector2 v) {
        int[][] s = getShape();
        boolean bool = true;
        for (int i = 0; i < s.length; i++) {
            for (int j = 0; j < s[0].length; j++) {
                int x = (int) (getPos().x + i);
                int y = (int) (getPos().y + j);
                int cx = (int) (x + v.x);
                int cy = (int) (y + v.y);
                try {
                    if ((s[i][j] > 0 && TetrisState.board.board[cx][cy] > 0))
                        bool = false;
                }catch (ArrayIndexOutOfBoundsException e){
                    bool=false;
                }
            }
        }
        return bool;
    }

    public boolean collided() {
        return collided;
    }

    public int at(int i, int j) {
        return shape.arr[i][j];

    }

    public void move(Direction d) {
        if (dMove.isDone()) {
            switch (d) {
                case L:
                    if (shape.pos.x + shape.arr.length < TetrisState.x && canMove(new Vector2(1, 0)))
                        shape.pos.x += 1;
                    break;
                case R:
                    if (shape.pos.x > 0 && canMove(new Vector2(-1, 0)))
                        shape.pos.x -= 1;
                    break;
                case D:
                    if (shape.pos.y + shape.arr[0].length < TetrisState.m && canMove(new Vector2(0, 1)))
                        shape.pos.y += 1;
                    break;
            }
            dMove.reset();
        }
    }

    public void rot(Direction l) {
        if (dRot.isDone()) {

            switch (l) {
                case L:
                    shape.rotL();
                    break;
                case R:
                    shape.rotR();
                    break;
            }
            dRot.reset();
        }
    }

    enum Type {
        T, Z, S, O, L, J, I;

        public static Type random() {
            return Type.values()[Game.rn.nextInt(Type.values().length)];
        }
    }

    public enum Direction {
        L, R, D
    }

    class Shape {
        int[][] arr;
        Vector2 pos = new Vector2();

        public Shape(Type t) {
            initShape(t);
        }

        void initShape(Type t) {
            switch (t) {
                case T:
                    arr = new int[][]{
                            new int[]{0, 1, 0},
                            new int[]{1, 1, 1}
                    };
                    break;
                case Z:
                    arr = new int[][]{
                            new int[]{2, 2, 0},
                            new int[]{0, 2, 2}
                    };
                    break;
                case S:
                    arr = new int[][]{
                            new int[]{0, 3, 3},
                            new int[]{3, 3, 0}
                    };
                    break;
                case O:
                    arr = new int[][]{
                            new int[]{4, 4},
                            new int[]{4, 4}
                    };
                    break;
                case L:
                    arr = new int[][]{
                            new int[]{0, 0, 5},
                            new int[]{5, 5, 5}
                    };
                    break;
                case J:
                    arr = new int[][]{
                            new int[]{6, 0, 0},
                            new int[]{6, 6, 6}
                    };
                    break;
                case I:
                    arr = new int[][]{
                            new int[]{7, 7, 7, 7}
                    };
                    break;
            }
        }

        public void rotR() {
            final int M = arr.length;
            final int N = arr[0].length;
            int[][] ret = new int[N][M];
            for (int r = 0; r < M; r++) {
                for (int c = 0; c < N; c++) {
                    ret[c][M-1-r] = arr[r][c];
                }
            }
            int[][] temp=arr;
            arr=ret;
            if(!Block.this.canMove(new Vector2(0,0))){
                arr=temp;
            }
        }

        public void rotL() {
            final int M = arr.length;
            final int N = arr[0].length;
            int[][] ret = new int[N][M];
            for (int r = 0; r < M; r++) {
                for (int c = 0; c < N; c++) {
                    ret[c][r] = arr[N-r-1][c];
                }
            }
            int[][] temp=arr;
            arr=ret;
            if(!Block.this.canMove(new Vector2(0,0))){
                arr=temp;
            }
        }

        public int[][] getShape() {
            return arr;
        }
    }


}
