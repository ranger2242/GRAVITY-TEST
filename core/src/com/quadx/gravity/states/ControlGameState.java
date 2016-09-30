package com.quadx.gravity.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Chris Cavazos on 9/29/2016.
 */
public class ControlGameState extends State{
    ControlGameState(GameStateManager gsm) {
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
        Gdx.gl20.glClearColor(.2f,.2f,.2f,1);
    }

    @Override
    public void dispose() {

    }
}
