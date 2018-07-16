package com.quadx.gravity.neuralnetwork;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.quadx.gravity.EMath;
import com.quadx.gravity.Game;
import com.quadx.gravity.shapes1_4.Arc;
import com.quadx.gravity.shapes1_4.Circle;
import com.quadx.gravity.shapes1_4.Line;
import com.quadx.gravity.shapes1_4.Triangle;
import com.quadx.gravity.states.NeuralState;

import java.util.ArrayList;
import java.util.Random;

import static com.quadx.gravity.Game.HEIGHT;
import static com.quadx.gravity.Game.WIDTH;
import static com.quadx.gravity.neuralnetwork.Food.foods;
import static com.quadx.gravity.states.NeuralState.mons;
import static com.quadx.gravity.states.NeuralState.mouseCircle;

/**
 * Created by Chris Cavazos on 2/7/2017.
 */
public class Monster {
    static Random rn = new Random();

    private ArrayList<Neuron> input = new ArrayList<>();

    private ArrayList<Line> sensors = new ArrayList<>();
    private Vector2 pos = new Vector2();
    private Vector2 vel = new Vector2(0, 0);
    private float angle = 12;

    private float viewAngle = 60;
    private float viewDist = 250;

    private float turnSpeed = 3;
    private float turn = 180;
    private boolean turning = false;

    private float dtHunger = 0;
    private float hunger = 100;
    private float trHunger = .4f;

    private int foodindex=-1;
    private boolean eating=false;
    private float dtEating = 0;
    private float trEating = .05f;

    private Circle proxmCircle = new Circle(pos, 15);
    private Circle circle = new Circle(pos, 10);
    private Color color;

    public Monster(Vector2 p, Color c) {
        color = c;
        pos = p;
        initInputNeurons();
        updateSensorPos();
    }

    void initInputNeurons() {
        int rad = 6;
        int n =8;
        //0 left sight bound
        //1 right sight bound
        //2 object in vision
        //3 standing on food
        //4 is hungry
        //5 object in left field of view
        //6 object in right field of view
        //7 proximity sensor
        for (int i = 0; i < n; i++) {
            input.add(new Neuron("", new Circle(new Vector2(30+(20* mons.size()), Game.HEIGHT / 2 - (i * 30)), rad)));
        }
    }
    void clearActiveNeurons() {
        for (Neuron n : input) {
            n.setActive(false);
        }
    }
    void updateSensorPos() {
        for(int i=0;i<input.size();i++){
            int ind=mons.indexOf(this);
            input.get(i).getCircle().center.set(new Vector2(30+(20* ind), Game.HEIGHT / 2 - (i * 30)));
        }
        proxmCircle.center.set(pos);
        sensors.clear();
        float dx = (float) (pos.x + viewDist * Math.cos(Math.toRadians((viewAngle / 2) + angle)));
        float dy = (float) (pos.y + viewDist * Math.sin(Math.toRadians((viewAngle / 2) + angle)));
        sensors.add(new Line(pos, new Vector2(dx, dy)));

        dx = (float) (pos.x + viewDist * Math.cos(Math.toRadians(-(viewAngle / 2) + angle)));
        dy = (float) (pos.y + viewDist * Math.sin(Math.toRadians(-(viewAngle / 2) + angle)));
        sensors.add(new Line(pos, new Vector2(dx, dy)));

        dx = (float) (pos.x + viewDist * Math.cos(Math.toRadians(angle)));
        dy = (float) (pos.y + viewDist * Math.sin(Math.toRadians(angle)));
         sensors.add(new Line(pos, new Vector2(dx, dy)));

    }
    void fixAngles(){
        viewAngle = viewAngle % 360;
        if(angle<0) angle+=360;
        angle=angle%360;
    }

    public void update(float dt) {
       // clearActiveNeurons();
        pos.add(vel);
        sense(dt);
        react(dt);
        fixPos();
        updateSensorPos();
        fixAngles();

    }

    void fixPos() {
        if (pos.x > WIDTH)
            pos.x = 0;
        if (pos.x < 0)
            pos.x = WIDTH;
        if (pos.y > HEIGHT)
            pos.y = 0;
        if (pos.y < 0)
            pos.y = HEIGHT;
        circle.center = pos;
    }



    void react(float dt) {

        if(input.get(2).isActive()) {//sound
            if (input.get(2).getActivation() < 0)
                move(input.get(2).getSourceGlobalPos(), true);

            if(input.get(2).getActivation()>0)
                move(input.get(2).getSourceGlobalPos(),false);
        }
        if(input.get(5).isActive() )//left sights
            turn((true),turnSpeed);
        if(input.get(6).isActive() )//right sights
            turn((false),turnSpeed);

        if(input.get(3).isActive() && input.get(4).isActive()) {//hunger and on food
            eat(dt);
        }
        if(input.get(7).isActive()){//proximity
            Vector2 v=input.get(7).getSourceGlobalPos();
            if(v != null)
                move(v,true);
        }
        forward(2);


    }
    public void forward(float fac) {
        if(!eating) {
            vel.x = (float) (fac * Math.cos(Math.toRadians(angle)));
            vel.y = (float) (fac * Math.sin(Math.toRadians(angle)));
        }else{
            vel.x=0;
            vel.y=0;
        }
    }
    void turn(boolean left, float rate) {
        if (left)
            angle += (rate + turn * Gdx.graphics.getDeltaTime());
        else angle -= (rate + turn * Gdx.graphics.getDeltaTime());
    }
    void eat(float dt) {
        dtEating+=dt;

        if(dtEating> trEating && foodindex<foods.size() && foodindex>=0){
            setEating(true);
            Food.consume(foodindex);
            hunger+=100*dt;
            dtEating=0;
        }
        if(foodindex == -1)
            setEating(false);
    }
    void move(Vector2 v1,boolean away){
        float ang= EMath.angle(v1, proxmCircle.center);
        if(away)
        ang+=180;
        ang= ang % 360;
       angle=ang;
        /*
        float f=3;
        boolean r=angle > ang-5 && angle< ang+5;
        if(!r&& EMath.isInHalfRange(ang,0,360,false))//left
            angle-=f*ang*Gdx.graphics.getDeltaTime();

        if(!r&&EMath.isInHalfRange(ang,0,360,true))
            angle+=f*ang*Gdx.graphics.getDeltaTime();
    */
    }


    void sense(float dt) {
        senseWall(sensors.get(0).b, input.get(0));
        senseWall(sensors.get(1).b, input.get(1));
        sight();
        sound();
        senseHunger(dt);
        senseProxim();
    }
    void senseWall(Vector2 v, Neuron n) {
        boolean b = false;
        if (v.x > WIDTH || v.x < 0 || v.y > HEIGHT || v.y < 0)
            b = true;
        n.setActive(b);
    }
    void senseProxim(){
        ArrayList<Vector2> list = new ArrayList<>();
        for(Monster m: mons){
            if(!m.equals(this)){
                if(m.getProximCircle().overlaps(proxmCircle)){
                    Circle c1=m.getProximCircle();
                    list.add(c1.center);
                }
            }
        }
        Vector2 v= EMath.vectorAverage(list);
        if(!list.isEmpty())
        trigger(7,true,v);
        else
            trigger(7,false,null);
    }
    void senseHunger(float dt) {
        if (!isEating()) {
            dtHunger += dt;
            if (hunger > 0 && dtHunger > trHunger) {
                hunger -= 2;
                dtHunger = 0;
            }
        }
        if (hunger > 100) hunger = 100;
        if (hunger < 60) {
            trigger(4, true);
        }
        if(hunger>=100){
            trigger(4, false);
            eating=false;
        }

    }
    private void sight() {
        boolean a,b;
        ArrayList<Vector2> dest = new ArrayList<>();
        for (Food f : foods) {
            a = inSight(f.getPos(), pos, sensors.get(0).b, sensors.get(1).b);
            if (a)
                dest.add(f.getPos());
        }
        for (Monster m : mons) {
            if (!m.equals(this)) {
                b=inSight(m.getPos(),pos,sensors.get(0).b,sensors.get(1).b);
                if(b){
                  //  dest.add(m.getPos());
                }
            }
        }
        Vector2 nearest= findNearest(dest);
        if(nearest !=null) {
            a = inSight(nearest, pos, sensors.get(0).b, sensors.get(2).b);
            trigger(5, a);

            b = inSight(nearest, pos, sensors.get(2).b, sensors.get(1).b);
            trigger(6, b);
        }else{
            trigger(5,false);
            trigger(6,false);

        }

    }
    private void sound(){
        if(NeuralState.mouseCircle.overlaps(circle)){
            trigger(2,true,mouseCircle.center,NeuralState.soundActivation);
        }else
            trigger(2,false,mouseCircle.center,NeuralState.soundActivation);

    }


    void trigger(int i, boolean b,Vector2 pos) {
        input.get(i).setActive(b);
        input.get(i).setSourceGlobalPos(pos);
    }
    void trigger(int i, boolean b,Vector2 pos,float act) {
        input.get(i).setActive(b);
        input.get(i).setSourceGlobalPos(pos);
        input.get(i).setActivation(act);
    }

    void trigger(int i, boolean b) {
        input.get(i).setActive(b);
    }

    public Vector2 findNearest(ArrayList<Vector2> test) {
        Vector2 found = null;
        float minDistance = 100000;
        for (Vector2 v : test) {
            float dist =v.dst(pos);
            if (dist < minDistance) {
                found = v;
                minDistance = dist;
            }
        }
        return found;
    }

    public boolean inSight(Vector2 test,Vector2 center, Vector2 va, Vector2 vb){
        float[] f= new float[]{center.x,center.y,va.x,va.y,vb.x,vb.y};
        Triangle t=new Triangle(f);
        return t.containsPoint2(test);
    }
    public void setVel(Vector2 v) {
        vel = v;
    }
    public void setAngle(int angle) {
        this.angle = angle;
    }
    public float getViewAngle() {
        return viewAngle;
    }
    public float getAngle() {
        return angle;
    }
    public void setEating(boolean b){eating=b;}
    public boolean isEating(){return eating;}
    public Arc getViewArc() {
        return new Arc(pos, sensors.get(0).length(), getAngle() - (getViewAngle() / 2), getViewAngle(),5,2);
    }
    public Color getColor() {
        return color;
    }
    public Circle getCircle() {
        return circle;
    }
    public Circle getProximCircle() {
        return proxmCircle;
    }
    public Vector2 getPos() {
        return pos;
    }
    public ArrayList<Line> getSensors() {
        return sensors;
    }
    public ArrayList<Neuron> getInput() {
        return input;
    }
    public ArrayList<String> getNeuronLables(){
        ArrayList<String> list= new ArrayList<>();
        list.add("sight L-bound");
        list.add("sight R-bound");
        list.add("sound");
        list.add("touching food");
        list.add("hungry");
        list.add("object in L-view");
        list.add("object in R-view");
        list.add("proxim sensor");

        //0
        //1 right sight bound
        //2 object in vision
        //3 standing on food
        //4 is hungry
        //5 object in left field of view
        //6 object in right field of view
        return list;
    }

    public void setFoodIndex(int f) {
        foodindex=f;
    }

    public int getFoodIndex() {
        return foodindex;
    }

    public float getHunger() {
        return hunger;
    }
}
