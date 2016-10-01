package com.quadx.gravity.control;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.quadx.gravity.Game;

import java.util.Random;

/**
 * Created by Chris Cavazos on 9/30/2016.
 */
public class Building {
    Vector2 pos;
    public int radius = 15;
    int cost = 500;
    Color c = Color.WHITE;
    Resource.Type costType = Resource.Type.Wood;
    Player owner = null;
    Type t = null;
    float dtSpawn=0;
    Random rn = new Random();

    public Type getType() {
        return t;
    }

    public enum Type {
        House, Spawn
    }
    public Building(Player o,Type t,Vector2 p){
        owner = o;
        setAttributes(t);
        pos=p;
    }

    public Building(Player o, Type t) {
        owner = o;
        setAttributes(t);
    }
    public Hitbox getHitBox(){
        return new Hitbox(Hitbox.HitboxShape.Circle,pos,radius);
    }
    public boolean checkCost(int resource){
        return resource>=cost;
    }
    void setAttributes(Type t) {
        this.t=t;
        switch (t) {
            case House: {
                radius = 15;
                cost = 500;
                c = Color.WHITE;
                costType = Resource.Type.Wood;
                break;
            }
            case Spawn: {
                radius = 10;
                cost = 1000;
                c = Color.RED;
                costType = Resource.Type.Wood;
                break;
            }
        }
    }

    public void setPosition(Vector2 position) {
        this.pos = position;
    }

    public Color getColor() {
        return c;
    }
    public void update(float dt){
        if(t==Type.Spawn){
            dtSpawn+=dt;
            if(dtSpawn>2){
                if(owner.food>=3*Unit.cost) {
                    if (owner.getPop() < owner.getPopMax()) {
                        Unit u = new Unit(owner, Game.WIDTH/2, Game.HEIGHT/2);
                        Resource.Type r= Resource.Type.Food;
                        if(rn.nextBoolean())
                            r= Resource.Type.Wood;
                        u.gather(r);
                        owner.unitList.add(u);
                        dtSpawn = 0;
                    }
                    owner.addResource(Resource.Type.Food,-Unit.cost);
                }
            }
        }
    }

    public Vector2 getPosition() {
        return pos;
    }
}
