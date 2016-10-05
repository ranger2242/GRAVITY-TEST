package com.quadx.gravity;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Chris Cavazos on 9/16/2016.
 */
public class EMath {
    public static float pathag(Vector2 a, Vector2 b){
        return (float) Math.sqrt(Math.pow(a.x-b.x,2)+Math.pow(a.y-b.y,2));
    }
    public static float pathag(double x1, double y1, double x2, double y2){
        return (float) Math.sqrt(Math.pow(x1-x2,2)+Math.pow(y1-y2,2));
    }
    public static float pathag(double a, double b){
        return (float) Math.sqrt(Math.pow(a,2)+Math.pow(b,2));
    }
}
