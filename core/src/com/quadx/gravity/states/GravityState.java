package com.quadx.gravity.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.quadx.gravity.ColorConverter;
import com.quadx.gravity.sim.Body;

import java.util.ArrayList;
import java.util.Random;

import static com.quadx.gravity.Game.HEIGHT;
import static com.quadx.gravity.Game.WIDTH;

/**
 * Created by Tom on 3/20/2016.
 */
@SuppressWarnings("ALL")
public class GravityState extends State {
    private static ShapeRenderer shapeR = new ShapeRenderer();
    private static ArrayList<Body> bodyList = new ArrayList<>();
    private static ArrayList<Body> tempBody = new ArrayList<>();
    private static ArrayList<Integer> indexToRemove = new ArrayList<>();

    private int[] backx=new int[1000];
    private int[] backy=new int[1000];
    private int[] backr=new int[1000];
    private float twinkle=0;
    private boolean trip=false;
    private double tempAng=0;
    public static int factor=6;
    static Random rn = new Random();
    int viReduce=4;
    public GravityState(GameStateManager gsm) {
        super(gsm);
        for(int i=0;i<1000;i++){
            backr[i]=rn.nextInt(2);
            backx[i]=rn.nextInt((int) WIDTH);
            backy[i]=rn.nextInt((int) HEIGHT);
        }
        double m=1000;
        //double r=Math.sqrt(m/Math.PI);
        bodyList.add(new Body(50000,50,(int)(WIDTH/2),(int)(HEIGHT/2)));
        int bodyCount = 500;
        for(int i = 0; i< bodyCount; i++){
            Body b;
            double mass=rn.nextInt(500);
            if(mass<5)mass=5;
            double ra=Math.sqrt(mass/Math.PI)/2;
            b=new Body(mass,ra,rn.nextInt((int) WIDTH),rn.nextInt((int) HEIGHT));
            b=generateVi(b);
            bodyList.add(b);
        }
        for(Body body: bodyList){
            int r=rn.nextInt(255);
            int g=rn.nextInt(255);
            int b=rn.nextInt(255);
            while(r+b+g<40){
                r=rn.nextInt(255);
                g=rn.nextInt(255);
                b=rn.nextInt(255);
            }
            ColorConverter color = new ColorConverter(r,g,b,1);
            body.setColor(color.getLIBGDXColor());
        }
    }

    @Override
    protected void handleInput() {
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
            gsm.pop();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            cam.zoom*=.5;
            //cam.viewportHeight*=.5;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            cam.zoom*=1.5;
            //cam.viewportHeight*=1.5;
        }
        cam.update();
    }
    private void out(String s){
       //System.out.println(s);
    }

    @Override
    public void update(float dt) {
        twinkle+=dt;
        handleInput();
        out("------------------------------------");


        for(Body b: bodyList) {
            b.clearVel();
            int index = bodyList.indexOf(b);
            for (int i = 0; i < bodyList.size(); i++) {
                if (i != index && !trip) {
                    double force=calculateForce(bodyList.get(index),bodyList.get(i));
                    setVelocityVectors(index, i, force);
                }
            }
            if(!trip)
            b.sumVel();
            trip=false;
        }
        for(Body b:bodyList){
            if(b.getX()> WIDTH|| b.getX()<0 || b.getY()> HEIGHT || b.getY()<0) {

                if (b.getX() > WIDTH) {
                    b.setVelx(-b.getVelx());

                     b.setX(WIDTH);
                } else if (b.getX() < 0) {
                    b.setVelx(-b.getVelx());

                    b.setX(0);
                }
                if (b.getY() > HEIGHT) {
                    b.setVely(-b.getVely());
                    b.setY(HEIGHT);

                } else if (b.getY() < 0) {
                    b.setVely(-b.getVely());

                    b.setY(0);
                }
                b.setAcc(-b.getAccx(), -b.getAccy());
            }

        }

            for (int i = 0; i < indexToRemove.size(); i++) {
                bodyList.remove(indexToRemove.get(i));
                out("removed");
            }
            for (int i = 0; i < tempBody.size(); i++) {
                bodyList.add(tempBody.get(i));
                out("added");
            }
        indexToRemove.clear();
        tempBody.clear();
        trip=false;
    }
    void generateViFromList(int index){
        boolean a1=rn.nextBoolean();
        boolean a2=rn.nextBoolean();

        double vxi=rn.nextDouble();
        double vyi=rn.nextDouble();

        if(a1)vxi*=-1;
        if(a2)vyi*=-1;
    }
    private Body generateVi(Body b){
        boolean a1=rn.nextBoolean();
        boolean a2=rn.nextBoolean();

        double vxi=rn.nextInt(2)+1/2;
        double vyi=rn.nextInt(2)+1/2;

        if(a1)vxi*=-1;
        if(a2)vyi*=-1;
        b.setVeli(vxi,vyi);
        return b;
    }
    private void setVelocityVectors(int self, int comp, double force){
        Body a=bodyList.get(self);
        Body b=bodyList.get(comp);

        double xDif = a.getX() - b.getX();
        double yDif = a.getY() - b.getY();
        double distanceSquared = xDif * xDif + yDif * yDif;
        boolean collision = distanceSquared < (a.getRadius() + b.getRadius()) * (a.getRadius() + b.getRadius());
        if(collision)trip=true;
        if(!collision){
        if(a.getY()>b.getY()) {
            bodyList.get(self).addAccy((force*-Math.sin(Math.toRadians(tempAng)))/a.getMass());
        }
        else if(a.getY()<b.getY()){
            bodyList.get(self).addAccy((force*Math.sin(Math.toRadians(tempAng)))/a.getMass());
        }
        if(a.getX()<b.getX()) {
            bodyList.get(self).addAccx((force*Math.cos(Math.toRadians(tempAng)))/a.getMass());
        }
        else if(a.getX()>b.getX()){
            bodyList.get(self).addAccx((force*-Math.cos(Math.toRadians(tempAng)))/a.getMass());
        }
            bodyList.get(self).move();
        }

        if(collision){
            bodyList.get(self).addAccx(((a.getAccx()+b.getAccx())/2));
            bodyList.get(self).addAccy(((a.getAccy()+b.getAccy())/2));
            double vx=(a.getMass()*a.getVelx()+b.getMass()*b.getVelx())/(a.getMass()+b.getMass());
            double vy=(a.getMass()*a.getVely()+b.getMass()*b.getVely())/(a.getMass()+b.getMass());
            bodyList.get(self).setVelx(vx);
            bodyList.get(self).setVely(vy);
            bodyList.get(comp).setVelx(vx);
            bodyList.get(comp).setVely(vy);
        }
    }
   private double calculateForce(Body b1, Body b2){
        double dx=Math.abs(b1.getX()-b2.getX());
        double dy=Math.abs(b1.getY()-b2.getY());
        double distance= Math.sqrt((Math.pow(dx,2)+Math.pow(dy,2)));
       tempAng= Math.toDegrees(Math.atan2(dy,dx));
        double g =6.67*Math.pow(10,-11);
       return (g*b1.getMass()*b2.getMass())/Math.pow(distance,2);
    }

    @Override
    public void render(SpriteBatch sb) {

        Gdx.gl20.glClearColor(0,0,0,1);
        shapeR.begin(ShapeRenderer.ShapeType.Filled);
        try {

            for(int i=0;i<1000;i++) {
                ColorConverter color = (new ColorConverter(rn.nextInt(50)+70, rn.nextInt(50)+70, rn.nextInt(50)+70,1));
                shapeR.setColor(color.getLIBGDXColor());
                    if(rn.nextInt(100)<98 && twinkle<20) {

                        shapeR.rect(backx[i], backy[i], backr[i], backr[i]);
                        twinkle = 0;
                    }
            }
            for (int i = 0; i < bodyList.size(); i++) {
                Body b = bodyList.get(i);
                ColorConverter color = new ColorConverter(rn.nextInt(255),rn.nextInt(255),rn.nextInt(255),1);

                shapeR.setColor(b.getColor());
                shapeR.circle((float) b.getX(), (float) b.getY(), (float) b.getRadius());
            }
        }catch(ArrayIndexOutOfBoundsException e){}
        catch ( NullPointerException er){}
        shapeR.end();
    }

    @Override
    public void dispose() {

    }
}
