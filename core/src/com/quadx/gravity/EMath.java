package com.quadx.gravity;

/**
 * Created by Chris Cavazos on 9/16/2016.
 */
public class EMath {
    public static float pathag(double x1, double y1, double x2, double y2){
        return (float) Math.sqrt(Math.pow(x1-x2,2)+Math.pow(y1-y2,2));
    }
}
