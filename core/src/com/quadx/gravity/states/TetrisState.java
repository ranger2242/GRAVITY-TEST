package com.quadx.gravity.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.quadx.gravity.shapes1_4.ShapeRendererExt;
import com.quadx.gravity.tetris.Block;
import com.quadx.gravity.tetris.Board;

/**
 * Created by Chris Cavazos on 2/15/2018.
 */
public class TetrisState extends State {
    public static int x = 9, m = 16;
    public static Board board = new Board(x, m);
    ShapeRendererExt sr = new ShapeRendererExt();
    Block current = new Block();

    public TetrisState(GameStateManager gsm) {
        super(gsm);
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            current.move(Block.Direction.L);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            current.move(Block.Direction.R);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            current.move(Block.Direction.D);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
            current.rot(Block.Direction.L);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.X)) {
            current.rot(Block.Direction.R);

        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        current.update(dt,board);
        if(current.collided()){
            current=new Block();
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        Gdx.gl.glClearColor(.1f, .1f, .1f, 1);
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.WHITE);
        int sep = 30;
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < m; j++) {
                sr.setColor(board.colorAt(i, j));
                sr.rect(50 + (sep * (x - i)), 50 + (sep * (m - j)), sep - 2, sep - 2);
            }
        }
        int[][] s = current.getShape();
        Vector2 p = current.getPos();
        for (int i = 0; i < s.length; i++) {
            for (int j = 0; j < s[0].length; j++) {
                int x = (int) (p.x + i);
                int y = (int) (p.y + j);
                if(current.at(i,j)>0) {
                    sr.setColor(current.colorAt(i, j));

                    sr.rect(50 + (sep * (TetrisState.x - x)), 50 + (sep * (m - y)), sep - 2, sep - 2);
                }
            }
        }
        sr.end();
    }

    @Override
    public void dispose() {

    }
}
