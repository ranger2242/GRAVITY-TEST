package com.quadx.gravity.sim;

/**
 * Created by Chris Cavazos on 7/24/2016.
 */
public class Line {
    private float x1;
    private float x2;
    private float y1;
    private float y2;

    public Line(float q,float w,float e,float r){
        x1=q;
        y1=w;
        x2=e;
        y2=r;
    }
    public void update(float q,float w,float e,float r){
        x1=q;
        y1=w;
        x2=e;
        y2=r;
    }
    public void draw(){
     }
}
