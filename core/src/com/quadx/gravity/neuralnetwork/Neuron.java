package com.quadx.gravity.neuralnetwork;

import com.badlogic.gdx.math.Vector2;
import com.quadx.gravity.shapes1_4.Circle;

import java.util.ArrayList;

/**
 * Created by Chris Cavazos on 2/7/2017.
 */
public class Neuron {
    private Circle circle = new Circle();
    private String name = "";
    private boolean active= false;
    private ArrayList<Neuron> connections= new ArrayList<>();
    private Vector2 sourceGlobalPos =new Vector2();
    private float activation = 0;
    public Neuron(){

    }
    public Neuron(String n, Circle c){
        circle=c;
        name=n;
    }
    public Neuron(Vector2 p,float thr){

    }
    public void attach(Neuron n){
        connections.add(n);
    }
    public void setCircle(Circle c){
        circle=c;
    }
    public Circle getCircle() {
        return circle;
    }
    public Vector2 getSourceGlobalPos(){return sourceGlobalPos;}
    public boolean isActive() {
        return active;
    }
    public float getActivation(){return activation;}
    public void setActive(boolean t){active=t;}
    public void setActivation(float f){activation=f;}
    public void setSourceGlobalPos(Vector2 v){
        sourceGlobalPos =v;}
}
