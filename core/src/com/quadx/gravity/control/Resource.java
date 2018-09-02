package com.quadx.gravity.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.quadx.gravity.tools1_0_1.timers1_0_1.Delta;

import java.util.Random;

import static com.quadx.gravity.tools1_0_1.timers1_0_1.Time.SECOND;

/**
 * Created by Chris Cavazos on 9/29/2016.
 */
public class Resource {
    static Random rn= new Random();
    private Type type= null;
    private Vector2 pos=null;
    private Color c=Color.WHITE;
    Delta dReset = new Delta(2*SECOND);
    Delta dRes = new Delta(4*SECOND);

    public boolean taken=false;
    private boolean dead = false;
    int value=rn.nextInt(10)+5;
    int radius =1;

    Player takenBy = null;

    Resource(Type t, Vector2 v){
        this(t,v.x,v.y);
    }

    Resource(Type t,float x, float y){
        type=t;
        pos=new Vector2(x,y);
        setAttributes();
    }

    public void update(float dt) {
        if(taken) {
            dReset.update(dt);
            if (dReset.isDone()) {
                taken = false;
                dReset.reset();
            }
        }
    }

    public void consume(Player p) {
        dRes.update(Gdx.graphics.getDeltaTime());
        if(dRes.isDone()){
            dead=true;
            p.addResource(type,value);
        }
    }


    public enum Type{
        Food,Wood
    }
    public Hitbox getHitBox(){
        return new Hitbox(Hitbox.HitboxShape.Circle,pos,radius);
    }
    public Vector2 pos() {
        return pos;
    }
    public Color getColor() {
 /*       if(taken)
            return Color.MAGENTA;
        else*/
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
                c=new Color(0f,.6f,0f,1);
                radius=2;
                value=10;
                break;
            }
            case Wood: {
                c=new Color(.4f,.4f,2,1);
                radius=4;
                value=8;
                break;
            }
        }

    }

    public Type getType() {
        return type;
    }
    public boolean isEmpty() {
        return dRes.isDone();
    }
    public int getValue() {
        return value;
    }
}
