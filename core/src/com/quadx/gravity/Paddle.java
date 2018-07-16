package com.quadx.gravity;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by Chris Cavazos on 9/8/2016.
 */
@SuppressWarnings("ALL")
public class Paddle {
    public int points=0;
    public float x=0;
    public float y=0;
    public float velx=0;
    public float vely=0;
    public float rad=50;
    private boolean lb;
    private boolean rb;
    private boolean ub;
    private boolean db;
    private float lbi;
    private float rbi;
    private float ubi;
    private float dbi;
    public boolean jump=true;
    boolean move = true;


    public Color c=Color.WHITE;

    public Paddle(float v, float v1, Color red) {
        x=v;
        y=v1;
        c=red;
    }
    public void setBounds(float a, float b, float c, float d ){
        lbi=a;
        rbi=b;
        ubi=c;
        dbi=d;
        lb=x<a;
        rb=x>b;
        ub=y>c;
        db=y<d;
    }
    public void distance(Ball b){


        float dx= this.x-b.x;
        float dy= this.y-b.y;
        float m= (float) Math.sqrt(Math.pow(dx,2)+Math.pow(dy,2));
        if(this.rad+b.rad>=m){
            double deg=Math.toDegrees(Math.atan2(dy,dx));
            float vm= (float) Math.sqrt(Math.pow(velx,2)+Math.pow(vely,2));
            b.velx= (float) -(vm* Math.cos(Math.toRadians(deg)));
            b.vely= (float) -(vm* Math.sin(Math.toRadians(deg)));
            System.out.println(velx+" "+vely);
            System.out.println(" @"+deg);
        }

    }
    public void distance2(Paddle p){
        float dx= this.x-p.x;
        float dy= this.y-p.y;
        float m= (float) Math.sqrt(Math.pow(dx,2)+Math.pow(dy,2));
        if(this.rad+p.rad>=m){
            double deg=Math.toDegrees(Math.atan2(dy,dx));
            float vm= (float) Math.sqrt(Math.pow(velx,2)+Math.pow(vely,2));
            p.velx= (float) -(vm* Math.cos(Math.toRadians(deg)));
            p.vely= (float) -(vm* Math.sin(Math.toRadians(deg)));
            System.out.println(velx+" "+vely);
            System.out.println(" @"+deg);
        }
    }
    public void move(){
        if(lb || rb){
            if(lb){
               x= lbi;
            }
            else
                x=rbi;
            velx=0;
        }
        if(ub || db){
            if(ub){
                y= ubi;
            }
            else
                y=dbi;
            vely=0;
        }
        this.x+=this.velx;
        this. y+=this.vely;
        float f=.95f;
        this.velx*=f;
        this.vely*=f;
    }
}
