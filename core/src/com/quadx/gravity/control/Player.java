package com.quadx.gravity.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.quadx.gravity.EMath;
import com.quadx.gravity.Game;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Chris Cavazos on 9/29/2016.
 */
public class Player {
    public ArrayList<Unit> unitList = new ArrayList<>();
    public ArrayList<Building> buildingList = new ArrayList<>();
    int wood=0;
    int population=0;
    int popmax=25;
    int houses=0;
    int food=0;
    Random rn = new Random();
    public void initUnits(){
        int n=25;
        for(int i=0;i<n;i++)
        unitList.add(new Unit(this, Game.WIDTH/5,(Game.HEIGHT-30)-(i*10)));

        for(Unit u:unitList){
            if(rn.nextBoolean())
            u.gather(Resource.Type.Wood);
            else
                u.gather(Resource.Type.Food);
        }
        buildingList.add(new Building(this, Building.Type.Spawn,new Vector2(Game.WIDTH/4,Game.HEIGHT/2)));
    }
    void updateUnits(){
        for(int i=unitList.size()-1;i>=0;i--){
            if(unitList.get(i).isDead()){
                unitList.remove(i);
            }
        }
        for(Unit u:unitList){
            u.move();
        }
        for(int i=0;i<unitList.size();i++){
            if(i+1<unitList.size()){
                if(EMath.pathag(unitList.get(i+1).getPosition().x-unitList.get(i).getPosition().x,unitList.get(i+1).getPosition().y-unitList.get(i).getPosition().y)>15){
                    unitList.get(i).move();
                    unitList.get(i).consume();
                }
            }
            else{
                if(EMath.pathag(unitList.get(i).getPosition().x-unitList.get(0).getPosition().x,unitList.get(i).getPosition().y-unitList.get(0).getPosition().y)>15){
                    unitList.get(i).move();
                    unitList.get(i).consume();
                }
            }
        }
        popmax=25+(houses*5);
        population=unitList.size();
    }
    public void buildHouse(){
        Building house= new Building(this, Building.Type.House);
        if(house.checkCost(wood)){
            unitList.get(0).buildToggle();
            unitList.get(0).build(house);
        }
    }
    public void update(){
        updateUnits();
        buildHouse();
        houses=0;
        for(Building b:buildingList){
            b.update(Gdx.graphics.getDeltaTime());

            if(b.getType()==Building.Type.House){
               houses++;
            }
        }
    }

    public int getWood() {
        return wood;
    }
    public int getFood(){
        return food;
    }
    public void addResource(Resource.Type t, int value){
        switch (t){

            case Food: {
                food+=value;
                break;
            }
            case Wood: {
                wood+=value;
                break;
            }
        }
    }

    public int getPop() {
        return population;
    }
    public int getPopMax() {
        return popmax;
    }

}
