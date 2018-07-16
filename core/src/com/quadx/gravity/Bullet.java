package com.quadx.gravity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.quadx.gravity.shapes1_4.Circle;
import com.quadx.gravity.shapes1_4.ShapeRendererExt;
import com.quadx.gravity.states.TankState;

import static com.quadx.gravity.states.TankState.wind;

public class Bullet{
    public float x=0;
    public float y=0;
    public float velx=0;
    public float vely=0;
    public float dt=0;
    Circle shape= new Circle();
    public boolean death=false;
    public boolean gravity=false;



    public Bullet(float x, float y, float ang, float mag){
        this.x=x;
        this.y=y;
        shape.center.set(new Vector2(x,y));
        shape.radius=3;
        velx= (float) (mag*Math.cos(Math.toRadians(ang)));
        vely= (float) (mag*Math.sin(Math.toRadians(ang)));
    }

    public Bullet() {

    }
    public void render(ShapeRendererExt sr){
        sr.set(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.WHITE);
        sr.circle(shape);
        if(death){
            sr.circle(shape.center.x,shape.center.y,50);
        }
    }


    public void setGravity(boolean gravity) {
        this.gravity = gravity;
    }

    public void setDeath(boolean death) {
        this.death = death;
    }

    public void update(){
        dt+= Gdx.graphics.getDeltaTime();
        x+=velx+wind.getVel().x;
        y+=vely+wind.getVel().y;
        if(gravity){
            vely+= TankState.gravity.y;
        }
        shape.center.set(new Vector2(x,y));

        if(!death) {
          death=true;

        }
    }
}