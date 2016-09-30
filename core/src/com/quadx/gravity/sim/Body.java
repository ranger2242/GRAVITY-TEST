package com.quadx.gravity.sim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.quadx.gravity.EMath;
import com.quadx.gravity.asteroids.Ship;
import com.quadx.gravity.states.GravityState;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Tom on 3/20/2016.
 */
@SuppressWarnings("ALL")
public class Body {
    private double mass=0;
    private double radius=0;
    private double x =0;
    private double density=0;
    private double y =0;
    private double velx=0;
    private double vely=0;
    private double accx=0;
    private double accy=0;
    private Color color;
    private float rotation=0;
    private int sides =0;
    float angle=0;
    float[] radMag=null;
    public static float dtSpawn=0;

    float[] points=null;
    //private float angle = 0;
    Random rn = new Random();

    private ArrayList<Double> velxComp=new ArrayList<>();
    private ArrayList<Double> velyComp=new ArrayList<>();

    private double acc=0;
    double speed=0;
    private double rate=Math.pow(10, GravityState.factor);
    private boolean lb;
    private boolean rb;
    private boolean ub;
    private boolean db;
    public boolean hit=false;
    private float lbi;
    private float rbi;
    private float ubi;
    private float dbi;

    public Body(double m, double r, double x, double y){
        mass=m;
        radius=r;
        this.x =x;
        this.y =y;
        sides = rn.nextInt(10)+5;
        rotation = rn.nextFloat()*4;
        if(rn.nextBoolean()){
            rotation*=-1;
        }
        radMag=new float[sides];
        velx+=rn.nextGaussian();
        vely+=rn.nextGaussian();
        for(int i=0;i<sides;i++){
            radMag[i]= (float) (rn.nextInt(50)+rn.nextGaussian());
            if(radMag[i]<0){
                radMag[i]*=-1;
            }
        }
        updatePoints();
    }
    private boolean checkBullets(ArrayList<Ship.Bullet> list){
        ArrayList<Ship.Bullet> found= new ArrayList<>();
        for(Ship.Bullet bu: list) {
            bu.update();
            if(bu.death){
                found.add(bu);
            }
        }
        if(found.size()>0){
            return true;
        }else {
            return false;
        }

    }
    public void setBounds(float a, float b, float c, float d){
        lbi=a;
        rbi=b;
        ubi=c;
        dbi=d;
        lb= x <a;
        rb= x >b;
        ub= y >c;
        db= y <d;
    }
    public void move(){
        double dt= Gdx.graphics.getDeltaTime();

        x = x +velx;//*dt*rate;
        y = y +vely;//*dt*rate;
        //velx=velx+accx*dt;
        //vely=vely+accy*dt;
        if(lb || rb){
            if(lb){
                x = rbi-1;
                //velx*=-1;
            }
            else {
                x = lbi+1;
                // velx *= -1;
            }
        }
        if(ub || db){
            if(ub){
                y = dbi+1;
                // vely*=-1;

            }
            else {
                y = ubi-1;
                // vely *= -1;
            }
        }

        //x+=velxi+velx*rate;
        //y+=velyi+vely*rate;
    }

    public void updatePoints(){
        angle+=rotation;
        points=new float[sides*2];
        for(int i = 0;i<sides*2;i+=2 ){
            //c=i;
            float thetax = ((360*i)/sides)+angle;
            float thetay =((360*(i+1))/sides)+angle;
            if(i==0){
            }
            points[i]= (float) ((x + (radMag[i/2]*Math.sin(Math.toRadians(thetax)))));
            points[i+1]= (float) (y +(radMag[i/2]*Math.cos(Math.toRadians(thetay))));

        }

    }
    private boolean checkHit(float a, float e, float l){
        if(Math.floor(a+e) ==Math.floor(l)){
            return true;
        }else
            return false;
    }
    public void colA(ArrayList<Ship.Bullet> list){
        for(Ship.Bullet b :list){
            for(int i=0;i<points.length;i+=2){
                float l;
                float p1;
                float p2;

                if(i+3<points.length) {
                    l=EMath.pathag(points[i], points[i + 1],points[i+2],points[i+3]);
                    p1 = EMath.pathag(points[i], points[i + 1], b.x, b.y);
                    p2 = EMath.pathag(points[i+2],points[i+3], b.x, b.y);
                }else{
                    l=EMath.pathag(points[i], points[i + 1],points[0],points[1]);
                    p1 = EMath.pathag(points[i], points[i + 1], b.x, b.y);
                    p2 = EMath.pathag(points[0],points[1], b.x, b.y);
                }
                if(checkHit(p1,p2,l)){
                    hit=true;
                }
            }
        }
    }

    public double getAcc() {
        return acc;
    }
    public double getDensity() {
        return density;
    }
    public double getMass() {
        return mass;
    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getRadius() {
        return radius;
    }
    public double getVel() {
        double vel = 0;
        return vel;
    }
    public double getVelx() {
        return velx;
    }
    public double getVely() {
        return vely;
    }
    public void setVelx(double velx) {
        this.velx = velx;
    }
    public void setVely(double vely) {
        this.vely = vely;
    }
    public void setDensity(double density) {
        this.density = density;
    }
    public void setAcc(double acc) {
        this.acc = acc;
    }
    public void setMass(double mass) {
        this.mass = mass;
    }
    public void setX(double x) {
        this.x = x;
    }
    public void setRadius(double radius) {
        this.radius = radius;
    }
    public void setY(double y) {
        this.y = y;
    }
    public void addAccx(double vel){
        velxComp.add(vel);
    }
    public void addAccy(double vel){
        velyComp.add(vel);
    }
    public void clearVel(){
        velxComp.clear();
        velyComp.clear();
    }
    public void sumVel(){
        accx=0;
        for(int i=0;i<velxComp.size();i++){
            accx+=velxComp.get(i);
        }
        accy=0;
        for(int i=0;i<velyComp.size();i++){
            accy+=velyComp.get(i);
        }
    }


    public void setAngle(int angle) {
        double angle1 = angle;
    }
    public void setAcc(double ax ,double ay){
        accx=ax;
        accy=ay;
    }
    public void setVeli(double i, double i1) {
        double velxi = i;
        double velyi = i1;
    }
    public void consume(Body b){
        mass+=b.mass;
        radius=Math.sqrt(mass/Math.PI);
    }

    public double getAccx() {
        return accx;
    }

    public double getAccy() {
        return accy;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public float[] getPoints() {
        return points;
    }
}
