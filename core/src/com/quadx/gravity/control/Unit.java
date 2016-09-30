package com.quadx.gravity.control;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Chris Cavazos on 9/29/2016.
 */
public class Unit {
    Vector2 pos= null;
    Color c= new Color(1,0,0,1);
    public Unit(){
        pos=new Vector2(0,0);
    }
    public Unit(float x, float y){
        pos=new Vector2(x,y);
    }
    public Vector2 getPosition(){
        return pos;
    }
    public void move(float x,float y){
        pos.x=x;
        pos.y=y;
    }
    public Color getColor(){
        return c;
    }
}
