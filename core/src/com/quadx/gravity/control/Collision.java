package com.quadx.gravity.control;

import com.quadx.gravity.EMath;

/**
 * Created by Chris Cavazos on 10/1/2016.
 */
public class Collision {
    public static boolean circlular(Hitbox a, Hitbox b){
        float dx;
        float dy;
        dx= b.pos.x-a.pos.x;
        dy=b.pos.y-a.pos.y;
        double dist = EMath.pathag(dx, dy);
        dist = Math.floor(Math.abs(dist));
        if (dist < b.radius+a.radius) {
            return true;
        }else return false;
    }
}
