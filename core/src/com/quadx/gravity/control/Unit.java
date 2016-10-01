package com.quadx.gravity.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.quadx.gravity.EMath;
import com.quadx.gravity.Game;

import java.util.ArrayList;
import java.util.Random;

import static com.quadx.gravity.Game.HEIGHT;
import static com.quadx.gravity.Game.WIDTH;
import static com.quadx.gravity.states.ControlGameState.grid;

/**
 * Created by Chris Cavazos on 9/29/2016.
 */
public class Unit {
    Random rn = new Random();
    int radius = 5;
    static int cost=200;
    private Vector2 init = null;
    private Vector2 pos = init;
    boolean buildmode = false;
    float dtChangeDir = 0;
    private Vector2 destination = pos;
    Vector2 previousPos=new Vector2(Game.WIDTH/2,Game.HEIGHT/2);
    private Color c = new Color(rn.nextFloat(), rn.nextFloat(), rn.nextFloat(), 1);
    private float moveSpeed = 1f;
    private int resourceIndex = -1;
    float standTime = 0;
    Resource.Type targetRessource=null;
    Player owner = null;
    float dtHunger = 0;
    private boolean dead =false;
    public Unit() {
        pos = new Vector2(0, 0);
    }

    public Unit(Player p, float x, float y) {

        pos = new Vector2(x, y);
        init = pos;
        owner = p;
    }

    public Vector2 getPosition() {
        return pos;
    }

    public Hitbox getHitBox() {
        return new Hitbox(Hitbox.HitboxShape.Circle, pos, radius);
    }

    void checkHunger(){
        if(dtHunger>20 ){
            if(owner.food>1)
            owner.addResource(Resource.Type.Food,-2);
            else dead=true;
            dtHunger=0;
        }
    }
    public void move() {
        float dt= Gdx.graphics.getDeltaTime();
        dtChangeDir +=dt;
        dtHunger+=dt;
        checkHunger();
        previousPos=pos;
        float dx;
        float dy;
        boolean canMove = true;
        Vector2 moveAwayFrom=null;
        for (Unit u : owner.unitList) {
            if (!u.equals(this))
                if (Collision.circlular(getHitBox(), u.getHitBox())) {
                    canMove = false;
                    moveAwayFrom=u.getPosition();
                }
        }
        try {
            dx = pos.x - destination.x;
            dy = pos.y - destination.y;
        } catch (NullPointerException e) {
            dx = pos.x;
            dy = pos.y;
        }
        float dist = EMath.pathag(dx, dy);
        if (resourceIndex !=-1&& resourceIndex<grid.resources.size() && Collision.circlular(getHitBox(),grid.resources.get(resourceIndex).getHitBox())) {
            consume();
        } else {
            if(canMove) {
                float angle = (float) Math.atan2(dy, dx);
                pos.x -= moveSpeed * Math.cos(angle);
                pos.y -= moveSpeed * Math.sin(angle);
            }else{

                float angle = (float) Math.atan2(moveAwayFrom.y-pos.y,moveAwayFrom.x-pos.x);
                pos.x -= moveSpeed * Math.cos(angle);
                pos.y -= moveSpeed * Math.sin(angle);
                gather2(targetRessource);
            }
        }
        if (previousPos.x-pos.x==0 && previousPos.y-pos.y==0) {
            standTime += Gdx.graphics.getDeltaTime();
        }
        if (standTime > 5) {
            standTime = 0;
            gather(targetRessource);
        }


    }

    public Color getColor() {
        return c;
    }

    public void buildToggle() {
        buildmode = !buildmode;
    }

    public void build(Building b) {

        if (buildmode) {
            boolean canbuild = true;
            if (b.getType() == Building.Type.House && owner.population == owner.popmax) {
                for (Resource r : grid.resources) {
                    if(Collision.circlular(getHitBox(),r.getHitBox())){
                        canbuild=false;
                    }
                }
                for (Building bu : owner.buildingList) {
                    if(Collision.circlular(getHitBox(),bu.getHitBox())){
                        canbuild = false;
                    }
                }
                if (canbuild) {
                    b.setPosition(new Vector2(pos.x, pos.y));
                    b.owner.buildingList.add(b);
                    owner.addResource(b.costType, -b.cost);
                    buildToggle();
                } else {
                    if (dtChangeDir > 5) {
                        if (dtChangeDir > 10) {
                            buildToggle();
                            //dtChangeDir=0;
                        } else {
                            if (owner.buildingList.size() > 1) {
                                int index = rn.nextInt(owner.buildingList.size() - 1);
                                Vector2 p = owner.buildingList.get(index).getPosition();
                                float x = (float) (p.x + (rn.nextGaussian() * b.radius) - (owner.buildingList.get(index).radius + b.radius) * Math.sin(p.angleRad(pos)));
                                float y = (float) (p.y + (rn.nextGaussian() * b.radius) - (owner.buildingList.get(index).radius + b.radius) * Math.cos(p.angleRad(pos)));
                                if (x < 0) x = 0;
                                if (x > WIDTH) x = WIDTH;
                                if (y < 0) y = 0;
                                if (y > HEIGHT) y = HEIGHT;

                                Vector2 n = new Vector2(x, y);
                                destination = n;
                            } else {
                                destination.x = pos.x;
                                destination.y = Game.HEIGHT / 2;
                            }
                        }
                    } else {
                        destination = new Vector2(rn.nextInt((int) Game.WIDTH), rn.nextInt((int) Game.HEIGHT));
                        dtChangeDir = 0;
                    }
                }
            }
        }
    }

    public int findNearest(Resource.Type type) {
        int found = -1;
        float minDistance = 100000;
        for (Resource r : grid.resources) {
            if (r.getType() == type) {
                Vector2 rpos = r.getPosition();
                float dx = rpos.x - pos.x;
                float dy = rpos.y - pos.y;
                float dist = Math.abs(EMath.pathag(dx, dy));
                if (dist < minDistance) {
                    found = grid.resources.indexOf(r);
                    minDistance = dist;
                }
            }
        }
        return found;
    }
    public int findNthClosest(int n, Resource.Type type) {
        ArrayList<Vector2> list = new ArrayList<>();
        for (Resource r : grid.resources) {
            if (r.getType() == type) {
                Vector2 rpos = r.getPosition();
                float dx = rpos.x - pos.x;
                float dy = rpos.y - pos.y;
                float dist = Math.abs(EMath.pathag(dx, dy));
                list.add(new Vector2(grid.resources.indexOf(r), dist));
            }
        }
        ArrayList<Vector2> sorted = new ArrayList<>();
        for (Vector2 a : list) {
            if (sorted.isEmpty()) {
                sorted.add(a);
            } else {
                int p=0;
                for (Vector2 b : sorted) {
                        if(a.y>b.y){
                            p++;
                        }
                }
                sorted.add(p,a);
            }
        }
        try {
            return (int) sorted.get(n).x;
        }catch (Exception e){
            return -1;
        }
    }



    public void gather(Resource.Type type) {
        targetRessource=type;
        int found = findNearest(type);
        if (found != -1) {
            destination = grid.resources.get(found).getPosition();
            resourceIndex = found;
        }
    }
    void gather2(Resource.Type type){
        int found = findNthClosest(3,type);
        if (found != -1) {
            destination = grid.resources.get(found).getPosition();
            resourceIndex = found;
        }
    }

    public void consume() {
        if (resourceIndex != -1 && resourceIndex < grid.resources.size()&&Collision.circlular(getHitBox(),grid.resources.get(resourceIndex).getHitBox()) ) {
            grid.resources.get(resourceIndex).consume(owner);
            if (grid.resources.get(resourceIndex).getDt() <= 0) {
                gather(targetRessource);
            }
        }
    }

    public int getResourceIndex() {
        return resourceIndex;
    }

    public boolean isDead() {
        return dead;
    }
}
