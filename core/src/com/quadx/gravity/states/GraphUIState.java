package com.quadx.gravity.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.quadx.gravity.graphui.Graph;
import com.quadx.gravity.shapes1_5_2.ShapeRendererExt;

import static com.quadx.gravity.Game.*;

/**
 * Created by Chris Cavazos on 9/1/2018.
 */
public class GraphUIState extends State {
    public static Graph graph = new Graph();
    ShapeRendererExt sr = new ShapeRendererExt();

    public GraphUIState(GameStateManager gsm) {
        super(gsm);
        sr.setAutoShapeType(true);
        graph.init();

    }

    @Override
    protected void handleInput() {
        mposPrev.set(mpos);
        mpos.set(Gdx.input.getX(), scr.y - Gdx.input.getY());
        mvel = new Vector2(mpos).sub(mposPrev);
    }

    @Override
    public void update(float dt) {
        handleInput();
        graph.update(dt);

    }

    @Override
    public void render(SpriteBatch sb) {
        Gdx.gl.glEnable(Gdx.gl20.GL_BLEND);
        Gdx.gl.glBlendFunc(Gdx.gl20.GL_SRC_ALPHA, Gdx.gl20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl20.glClearColor(0, 0, 0, 1);
        sr.begin();
        graph.render(sr);
        sr.end();
    }

    @Override
    public void dispose() {

    }
}
