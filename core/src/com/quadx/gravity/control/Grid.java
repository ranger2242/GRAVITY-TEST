package com.quadx.gravity.control;

import com.badlogic.gdx.math.Vector2;
import com.quadx.gravity.Game;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Chris Cavazos on 9/30/2016.
 */
public class Grid {
    public ArrayList<Resource> resources = new ArrayList<>();
    Random rn =new Random();
    public Grid(){
        initGrid();
    }
    void initGrid(){
        resources.clear();
        plotResource(Resource.Type.Wood,rn.nextInt(30)+10,2000);
        plotResource(Resource.Type.Food,rn.nextInt(30)+10,1000);

    }
    void plotResource(Resource.Type type,int init, int n){
        ArrayList<Resource> trees1= new ArrayList<>();
        for(int i=0;i<init;i++){
            trees1.add(new Resource(type,rn.nextInt((int) Game.WIDTH),rn.nextInt((int) Game.HEIGHT)));
        }
        ArrayList<Resource> trees2= new ArrayList<>();
        for(int i=0;i<2000;i++){
            int index= rn.nextInt(trees1.size());
            Vector2 pos = trees1.get(index).getPosition();
            double x=pos.x+ rn.nextGaussian()*rn.nextInt(100);
            double y=pos.y+ rn.nextGaussian()*rn.nextInt(100);
            trees2.add(new Resource(type,(int)x,(int)y));
        }
        resources.addAll(trees1);
        resources.addAll(trees2);
        for(int i=resources.size()-1;i>=0;i--){
            Vector2 pos= resources.get(i).getPosition();
            if(pos.x>Game.WIDTH || pos.x <0 ||pos.y>Game.HEIGHT || pos.y <0){
                resources.remove(i);
            }
        }
    }
    public void clearDead(ArrayList<Unit> a){

        ArrayList<Resource> dead= new ArrayList<>();
        /*for(int i=resources.size()-1;i>=0;i--){
            if(resources.get(i).getDt()<=0){
                resources.remove(i);
            }
        }*/
        for(Unit u: a) {
            for (int i = resources.size() - 1; i >= 0; i--) {
                Resource r = resources.get(i);
                if(r.isDead() && Collision.circlular(u.getHitBox(),r.getHitBox())){
                //if (r.isDead() && r.getPosition().x ==u.getPosition().x &&  r.getPosition().y ==u.getPosition().y) {
                     r.getValue();
                    resources.remove(i);
                }
            }
        }
    }
}
