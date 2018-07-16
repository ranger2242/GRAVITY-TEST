package com.quadx.gravity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.quadx.gravity.sim.Body;

import java.util.ArrayList;

/**
 * Created by Chris Cavazos on 9/13/2016.
 */
@SuppressWarnings("ALL")
public class Ship {
    public ArrayList<Bullet> blist= new ArrayList<>();
    private ArrayList<Vector2> sides= new ArrayList<>();
    private float x=0;
    private float y=0;
    private float velx=0;
    private float vely=0;
    private Color c=new Color(Color.WHITE);
    private float length= 40;
    private float angle= 0;
    private float turn=0;
    private float[] points= new float[6];
    private boolean lb;
    private boolean rb;
    private boolean ub;
    private boolean db;
    private float lbi;
    private float rbi;
    private float ubi;
    private float dbi;
    private float fang=15;
    private float dtShot=0;
    public int score=0;
    private float initx;
    private float inity;
    boolean hit =false;

    public Ship(float x, float y, Color c) {
        initx=x;
        inity=y;
        this.x=x;
        this.y=y;
        this.c=c;

        updatePoints();
        updateSides();
    }
    public void rotate(float rate){
        turn+=rate;
    }
    public void update(float f){
        dtShot+=f;
        checkBullets();
        updatePoints();
        updateSides();
        velx*=.995;
        vely*=.995;
        move();
        setBounds(0, Game.WIDTH,Game.HEIGHT,0);
    }
    public void shoot(){
        if(this.dtShot>.2) {
            this.fire();
            this.dtShot=0;
        }
    }
    public void draw(ShapeRenderer shapeR){
        shapeR.setColor(c);
        shapeR.polygon(points);
        for(Bullet bu: blist){
            shapeR.circle(bu.x,bu.y,2);
        }
    }
    private void updateSides(){
        sides.clear();
        for(int i=0;i<points.length;i+=2) {
            sides.add(new Vector2(points[i], points[i+1]));
        }
    }
    private void updatePoints(){
        points= new float[]{this.x, this.y,
                (float) (this.x + length * Math.sin(Math.toRadians(angle-fang+turn))), (float) (this.y + length * Math.cos(Math.toRadians(angle-fang+turn))),
                (float) (this.x + length * Math.sin(Math.toRadians(fang+angle+turn))),(float) (this.y + length * Math.cos(Math.toRadians(fang+angle+turn)))};

    }
    private void checkBullets(){
        ArrayList<Bullet> found= new ArrayList<>();
        for(Bullet bu: blist) {
            bu.update();
            if(bu.death){
                found.add(bu);
            }
        }
        for(Bullet f:found) {
            blist.remove(f);
        }
    }
    public void forward(float fac){
        velx+=fac*Math.sin(Math.toRadians(angle+turn));
        vely+=fac*Math.cos(Math.toRadians( angle+turn));
    }
    private void setBounds(float a, float b, float c, float d){
        lbi=a;
        rbi=b;
        ubi=c;
        dbi=d;
        lb=x<a;
        rb=x>b;
        ub=y>c;
        db=y<d;
    }
    private void move(){
        if(lb || rb){
            if(lb){
                x= rbi;
            }
            else
                x=lbi;
        }
        if(ub || db){
            if(ub){
                y= dbi;
            }
            else
                y=ubi;
        }
        this.x+=this.velx;
        this. y+=this.vely;
    }
    private void checkHit(float a, float e, float l){
        if(Math.floor(a+e) ==Math.floor(l)){
                x=initx;
                y=inity;
            }
    }
    public boolean colA(ArrayList<Bullet> list){
        boolean hit=false;
        for(Bullet b :list){
            float a=mag(x,y,b.x,b.y);
            float e=mag(this.x+ length * Math.sin(Math.toRadians(angle-fang+turn)),this.y + length * Math.cos(Math.toRadians(angle-fang+turn)),b.x,b.y );
            checkHit(a,e,length);
            a=mag(x,y,b.x,b.y);
            e=mag(this.x+ length * Math.sin(Math.toRadians(angle+fang+turn)),this.y + length * Math.cos(Math.toRadians(angle+fang+turn)),b.x,b.y );
            checkHit(a,e,length);
            a=mag(this.x+ length * Math.sin(Math.toRadians(angle-fang+turn)),this.y + length * Math.cos(Math.toRadians(angle-fang+turn)),b.x,b.y );
            e=mag(this.x+ length * Math.sin(Math.toRadians(angle+fang+turn)),this.y + length * Math.cos(Math.toRadians(angle+fang+turn)),b.x,b.y );
            checkHit(a,e, (float) (2*length* Math.sin(Math.toRadians(fang))));
        }

        return hit;
    }
    public void colB(Body body){
        for(int i=0;i<points.length;i+=2){
            float l;
            float p1;
            float p2;

            for(int j=0;j<body.getPoints().length;j+=2) {
                if (i + 3 < points.length) {
                    l = EMath.pathag(points[i], points[i + 1], points[i + 2], points[i + 3]);
                    p1 = EMath.pathag(points[i], points[i + 1], body.getPoints()[i], body.getPoints()[i+1]);
                    p2 = EMath.pathag(points[i + 2], points[i + 3], body.getPoints()[i], body.getPoints()[i+1]);
                } else {
                    l = EMath.pathag(points[i], points[i + 1], points[0], points[1]);
                    p1 = EMath.pathag(points[i], points[i + 1],  body.getPoints()[i], body.getPoints()[i+1]);
                    p2 = EMath.pathag(points[0], points[1],  body.getPoints()[i], body.getPoints()[i+1]);
                }
                checkHit(p1,p2,l);
            }
        }
    }
    private float mag(double x1, double y1, double x2, double y2){
        return (float) Math.sqrt(Math.pow(x1-x2,2)+Math.pow(y1-y2,2));
    }
    private void fire() {
        blist.add(new Bullet(this.x,this.y,angle+turn+180,6));
    }


}
