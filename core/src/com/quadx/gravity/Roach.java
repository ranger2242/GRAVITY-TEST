package com.quadx.gravity;

import com.badlogic.gdx.Gdx;

import java.util.Random;

/**
 * Created by range_000 on 1/15/2016.
 */
@SuppressWarnings("ALL")
public class Roach {
    private float energy=200;
    private float angle = 0;
    private float targetSpeed=0;
    private float speed = 8f;
    private float targetAngle =0f;
    private double xSpd=0;
    private double ySpd=0;
    private double xVector=0;
    private double yVector=0;
    private double velocity=0;
    private float px = (Game.WIDTH / 2);
    private float py = (Game.HEIGHT / 2);
    private float dtDirectionChange =0;
    private float dtSpeedChange =0;
    private int width = 10;
    private int height = 10;
    private int mouseX=0;
    private int mouseY=0;
    private boolean mUp=false;
    private boolean mRight=false;
    boolean xDir =false;
    boolean yDir =false;
    private boolean moving =false;


    private Random rn = new Random();

    public Roach() {

    }

    public float getPX() {
        return px;
    }
    public float getPY() {
        return py;
    }
    public void calculateBehavior(){
        /////////////////////////////////////////
        //Sets booleans for mouse relative position
        mRight = mouseX > px + width / 2;
        mUp = mouseY > py + height / 2;
        ////////////////////////////////////////////
        //Checks if mouse is within sight of roach
        int sight = 100;
        if(mouseX>px- sight && mouseX<px+width+ sight && mouseY< py+height+ sight && mouseY >py- sight){
            dtDirectionChange+= Gdx.graphics.getDeltaTime();
            if(dtDirectionChange>.3f) {
                calculateEscapeAngle();
                dtDirectionChange=0;
            }
            moving=true;
            //update();
        }
        ////////////////////////////////////////////
        //Checks if roach is supposed to update
        if(moving){
            if(energy>0){
                move();
                changeAngle();
                float eDrainRate = .5f;
                energy-= eDrainRate;
            }
            else{
                moving=false;
            }
        }
        else{
            if(energy<200){
                energy+=4;
            }
        }
        //////////////////////////////////////////////
        //etc
        calculateSpeed();
    }
    private void move() {
        System.out.println(angle+" "+ Math.toRadians(angle));
        xVector=Math.cos(Math.toRadians(angle));
        yVector=Math.sin(Math.toRadians(angle));
        xSpd= (speed*xVector);
        ySpd=  (speed*yVector);
        velocity=(Math.sqrt(Math.pow(xSpd,2)+Math.pow(ySpd,2)));
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //Checks Window Boundaries
            if (px + width +xSpd < Game.WIDTH &&px-xSpd  >0 ) {
                px += xSpd;
            }
            else{
                System.out.println("%%%%%%");
                //px -=;
                targetAngle=rn.nextInt(360);
            }
            if (py + height+ySpd < Game.HEIGHT  &&py -ySpd >0) {
                py -=ySpd;
            }
            else{
                System.out.println("^^^^^");

                // py +=ySpd*2;
               targetAngle=rn.nextInt(360);
            }

        if(px==0|| px>=Game.WIDTH-width || py==0||py>=(Game.HEIGHT-height)){
            dtDirectionChange+=Gdx.graphics.getDeltaTime();
            if(dtDirectionChange>.3){
                angle=rn.nextInt(360);
                dtDirectionChange=0;
            }
        }
        if(px<0)px=width;
        if(px+width>Game.WIDTH)px=Game.WIDTH-width*2;
        if(py<0)py=height;
        if(py>Game.HEIGHT-height)py=Game.HEIGHT-height*2;

    }
    private void calculateSpeed(){
        dtSpeedChange+=Gdx.graphics.getDeltaTime();
        if(dtSpeedChange>rn.nextFloat()){
            float speedMax = 12f;
            targetSpeed=rn.nextInt((int) speedMax);
            dtSpeedChange=0;
        }

        int speedChangeRate=rn.nextInt(20);
        if(speed<targetSpeed){
            speed+=Gdx.graphics.getDeltaTime()*speedChangeRate;
        }
        if(speed>targetSpeed){
            speed-=Gdx.graphics.getDeltaTime()*speedChangeRate;

        }
    }
    private void calculateEscapeAngle(){
        float a=0;
        /////////////////////////////////////////
        //Checks mouse position relative to roach
        //and chooses random angle excluding
        //quadrant angles of mouse
        if(mUp){
            if(mRight){a=rn.nextInt(270);}
            else {
                int b=rn.nextInt(3);
                if(b==0){a=rn.nextInt(90)+270;}
                else {a=rn.nextInt(180);}}}
        else{
            if(mRight){a=rn.nextInt(270)+90;}
            else {
                int b=rn.nextInt(3);
                if(b==0){a=rn.nextInt(90);}
                else {a=rn.nextInt(180)+180;}}
        }
        ///////////////////////////////////////
        //Checks if roach is colliding with wall
        if(px==0.0f){
            System.out.println("##");
            int b=rn.nextInt(2);
            if(b==0) a=rn.nextInt(90);
            else a=rn.nextInt(90)+270;
            //moving=true;
        }
        ////////////////////////////////////////
        targetAngle =a;
    }
    private void changeAngle(){
        int b=rn.nextInt(9)+1;
        if(angle<targetAngle)
        {
            angle+=targetAngle*Gdx.graphics.getDeltaTime()*b;
        }
        if(angle>targetAngle)
        {
            angle-=targetAngle*Gdx.graphics.getDeltaTime()*b;
        }
    }
    public void setMousePos(int x, int y){
        mouseX=x;
        mouseY=y;
    }
    public int getWidth() {
        return width;
    }
    public double getyVector() {
        return yVector;
    }
    public double getxVector() {
        return xVector;
    }
    public float getAngle() {
        return angle;
    }
    public double getxSpd() {
        return xSpd;
    }
    public double getySpd() {
        return ySpd;
    }

    public double getVelocity() {
        return velocity;
    }

    public float getTargetAngle() {
        return targetAngle;
    }

    public float getEnergy() {
        return energy;
    }
}
