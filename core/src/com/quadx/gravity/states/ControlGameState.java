package com.quadx.gravity.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.quadx.gravity.control.Player;
import com.quadx.gravity.control.Unit;

/**
 * Created by Chris Cavazos on 9/29/2016.
 */
public class ControlGameState extends State{
    ShapeRenderer shapeR = new ShapeRenderer();
    ControlGameState(GameStateManager gsm) {
        super(gsm);
        a.initUnits();
    }
    Player a=new Player();
    Player b= new Player();

    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch sb) {
        Gdx.gl20.glClearColor(.2f,.2f,.2f,1);
        shapeR.begin(ShapeRenderer.ShapeType.Filled);
        for(Unit u : a.unitList){
            shapeR.setColor(u.getColor());
            shapeR.circle(u.getPosition().x,u.getPosition().y,5);
        }
        shapeR.end();
    }

    @Override
    public void dispose() {

    }
}
