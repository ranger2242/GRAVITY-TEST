package com.quadx.gravity.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.quadx.gravity.physicsBody.GenBody;
import com.quadx.gravity.shapes1_4.ShapeRendererExt;

/**
 * Created by Chris Cavazos on 6/26/2018.
 */
public class GeneticState extends State {
    GenBody b1 = new GenBody(Color.WHITE, new Vector2(30,30));
    GenBody b2 = new GenBody(Color.RED, new Vector2(30,70));
    ShapeRendererExt sr = new ShapeRendererExt();


    public GeneticState(GameStateManager gsm) {
        super(gsm);

    }

    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch sb) {
        Gdx.gl20.glClearColor(0, 0, 0, 1);

        sr.begin(ShapeRenderer.ShapeType.Filled);
        b1.render(sr);
        b2.render(sr);
        sr.end();
    }

    @Override
    public void dispose() {

    }
}
