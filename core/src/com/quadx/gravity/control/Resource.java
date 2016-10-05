package com.quadx.gravity.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

/**
 * Created by Chris Cavazos on 9/29/2016.
 */
public class Resource {
    static Random rn= new Random();
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
    public static Type getRandomResource(){
        if(rn.nextBoolean()){
            return Type.Food;
        }else{
            return Type.Wood;
        }
    }
    public static Type getCalcResource(Player p){
        if(p.wood<p.food){
            return Type.Wood;
        }else{
            return Type.Food;
        }
    }
    boolean isColliding(Unit p){
        return Collision.circlular(getHitBox(), p.getHitBox());
    }

    void setAttributes(){
        switch (type){
            case Food: {
                c=new Color(0f,.4f,0f,1);
                radius=2;
                dt=.05f;
                value=10;
                break;
            }
            case Wood: {
                c=new Color(.2f,.2f,0,1);
                radius=4;
                dt=.05f;
                value=8;
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
