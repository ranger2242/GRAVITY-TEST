package com.quadx.gravity.control;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Chris Cavazos on 9/29/2016.
 */
public class Resource {
    Type type= null;
    Vector2 pos=null;
    enum Type{
        Food,Wood
    }
    Resource(Type t,float x, float y){
        type=t;
        pos=new Vector2(x,y);
    }
}
