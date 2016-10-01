package com.quadx.gravity.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

/**
 * Created by Chris Cavazos on 9/29/2016.
 */
public class Resource {
    Random rn= new Random();
    private Type type= null;
    private Vector2 pos=null;
    private Color c=Color.WHITE;
    protected float dt =5f;
    private boolean dead = false;
    int value=rn.nextInt(3)+2;
    int radius =1;

    Player takenBy = null;

    Resource(Type t,float x, float y){
        type=t;
        pos=new Vector2(x,y);
        setAttributes();
    }
    public enum Type{
        Food,Wood
    }
    public Hitbox getHitBox(){
        return new Hitbox(Hitbox.HitboxShape.Circle,pos,radius);
    }
    public Vector2 getPosition() {
        return pos;
    }
    public Color getColor() {
        return c;
    }
    void setAttributes(){
        switch (type){
            case Food: {
                c=new Color(0f,1f,0f,1);
                radius=2;
                dt=1;
                value=rn.nextInt(1)+8;
                break;
            }
            case Wood: {
                c=Color.BROWN;
                radius=4;
                dt=2;
                value=rn.nextInt(7)+2;
                break;
            }
        }

    }
    public void consume(Player p) {
        dt -= Gdx.graphics.getDeltaTime();
        if(dt<=0){
            dead=true;
            p.addResource(type,value);
        }
    }
    public float getDt() {
        return dt;
    }
    public Type getType() {
        return type;
    }
    public boolean isDead() {
        return dead;
    }
    public int getValue() {
        return value;
    }
}
