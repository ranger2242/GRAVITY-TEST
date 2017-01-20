package com.quadx.gravity.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.quadx.gravity.vectortest.Model;

import java.util.Random;

/**
 * Created by Chris Cavazos on 10/21/2016.
 */
public class VectorTestState extends State {
    ShapeRenderer shapeR = new ShapeRenderer();
    Model square= new Model();
    Random rn = new Random();
    float rot=0;
    float dtre=0;
    float dtcc=0;
    boolean flip=false;
    float dx=0;
    float dy=0;

    VectorTestState(GameStateManager gsm) {
        super(gsm);
        square.calcCenter();

    }

    @Override
    protected void handleInput() {
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
            gsm.pop();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.Q)){
                square.rotate(-2);
        }if(Gdx.input.isKeyPressed(Input.Keys.W)){
                square.rotate(2);
        }if(Gdx.input.isKeyPressed(Input.Keys.A)){
                square.scale(1.1);
        }if(Gdx.input.isKeyPressed(Input.Keys.S)){
                square.scale(.9);
        }if(Gdx.input.isKeyPressed(Input.Keys.I)){
            square.translate(new Vector2(0,2));
        }if(Gdx.input.isKeyPressed(Input.Keys.K)){
            square.translate(new Vector2(0,-2));
        }if(Gdx.input.isKeyPressed(Input.Keys.J)){
            square.translate(new Vector2(-2,0));
        }if(Gdx.input.isKeyPressed(Input.Keys.L)){
            square.translate(new Vector2(2,0));
        }
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            square.points.clear();
            square= new Model();
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        rot+=dt;
        dtre+=dt;
        dtcc+=dt;
        if(rot>.02){
            //square.rotate(2);
           // square.scale(1.3);
           // square.translate(new Vector2(dx,dy));
            rot=0;
        }
        /*
        if(dtre>1.5){
            square=new Model();
            dtre=0;
            dx=rn.nextInt(10)*rn.nextFloat();
            dy=rn.nextInt(10)*rn.nextFloat();

        }*/
        if(dtcc>.2){
            flip=!flip;
        }
        square.calcCenter();

    }

    @Override
    public void render(SpriteBatch sb) {
        Gdx.gl20.glClearColor(0,0,0,1);
        cam.setToOrtho(false);
        shapeR.setProjectionMatrix(cam.combined);
        sb.setProjectionMatrix(cam.combined);
        shapeR.begin(ShapeRenderer.ShapeType.Line);
        for(int i=0;i<square.points.size();i++) {
            if(flip)
            shapeR.setColor(rn.nextFloat(),rn.nextFloat(),rn.nextFloat(),1);
            if(i+1<square.points.size()){
                shapeR.line(square.points.get(i),square.points.get(i+1));
            }else{
                shapeR.line(square.points.get(i),square.points.get(0));
            }
        }
        shapeR.circle(square.center.x,square.center.y,2);
        shapeR.end();
    }

    @Override
    public void dispose() {

    }
}
