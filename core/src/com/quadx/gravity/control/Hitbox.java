package com.quadx.gravity.control;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Chris Cavazos on 10/1/2016.
 */
public class Hitbox {
    Vector2 pos=null;
    HitboxShape shape= null;
    double radius=0;
    enum HitboxShape{
        Square,Circle
    }
    public Hitbox(HitboxShape sh, Vector2 p, double rad){
        shape=sh;
        pos=p;
        radius=rad;
    }
}
