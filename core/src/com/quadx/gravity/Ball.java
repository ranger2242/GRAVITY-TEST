package com.quadx.gravity;

/**
 * Created by Chris Cavazos on 9/8/2016.
 */
@SuppressWarnings("ALL")
public class Ball {
    public float x=Game.WIDTH/2;
    public float y=Game.HEIGHT/2;
    public float velx=0;
    public float vely=0;
    public float rad=20;
    public void reset(){
        x=Game.WIDTH/2;
        y=Game.HEIGHT/2;
        velx=0;
        vely=0;
    }
}
