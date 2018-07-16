package com.quadx.gravity.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.quadx.gravity.shapes1_4.ShapeRendererExt;
import com.quadx.gravity.tanks.Tank;
import com.quadx.gravity.tanks.TerrainGenerator;
import com.quadx.gravity.tanks.Wind;

/**
 * Created by Chris Cavazos on 10/7/2017.
 */
public class TankState extends State {
    public static Vector2 gravity = new Vector2(0,-.15f);
    static ShapeRendererExt sr = new ShapeRendererExt();
    TerrainGenerator tg= new TerrainGenerator();
    Tank p1= new Tank(new Vector2(100,100), Color.RED);
    public static Wind wind = new Wind(new Vector2(.2f,0));
    float dtf=0;

    public TankState(GameStateManager gsm) {
        super(gsm);
        sr.setAutoShapeType(true);
        tg.generateFlat();
    }

    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {
        tg.update();
        p1.update(dt);
        dtf+=dt;
        if(Gdx.input.isKeyPressed(Input.Keys.M)){
            if(dtf>.2f) {
                tg.generate();
                dtf=0;
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        Gdx.gl20.glClearColor(0,0,0,1);
        Gdx.gl20.glClear(GL20.GL_COLOR_CLEAR_VALUE);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        p1.render(sr);
        tg.render(sr);
        wind.render(sr);
        sr.end();
    }

    @Override
    public void dispose() {

    }
}
