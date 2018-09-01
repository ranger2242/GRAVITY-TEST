package com.quadx.gravity.control;

import com.badlogic.gdx.math.Vector2;
import com.quadx.gravity.tools1_0_1.timers1_0_1.Delta;

import java.util.ArrayList;
import java.util.Random;

import static com.quadx.gravity.Game.scr;
import static com.quadx.gravity.control.Building.Type.Spawn;
import static com.quadx.gravity.control.Resource.Type.Food;
import static com.quadx.gravity.control.Resource.Type.Wood;
import static com.quadx.gravity.control.Unit.State.Gather;
import static com.quadx.gravity.control.Unit.State.Plant;

/**
 * Created by Chris Cavazos on 9/29/2016.
 */
public class Player {
    public ArrayList<Unit> unitList = new ArrayList<>();
    public ArrayList<Building> buildingList = new ArrayList<>();
    double wood = 0;
    int population = 0;
    int popmax = 25;
    int houses = 0;
    double food = 1000;
    int planter = -1;
    float dtPlant = 0;
    Random rn = new Random();
    Delta dPlant = new Delta(5f);

    public void initUnits() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Vector2 p = new Vector2(scr).scl(.5f)
                        .add((i * 20) - (50), (j * 20) - (50));
                unitList.add(new Unit(this, p));
            }
        }
        for (Unit u : unitList) {
            boolean b = rn.nextBoolean();
            u.changeState(Gather, b ? Wood : Food);
        }
        buildingList.add(new Building(this, Spawn, new Vector2(scr).scl(.5f)));
        setPopulation();
    }

    void updateUnits() {
        for (int i = unitList.size() - 1; i >= 0; i--) {
            if (unitList.get(i).isDead()) {
                unitList.remove(i);
                setPopulation();
            }
        }
        for (Unit u : unitList) {
            u.move();
        }
    }
    void setPopmax(){
        popmax = 25 + (houses * 5);
    }
    void setPopulation(){
        population = unitList.size();
    }

    public void buildHouse() {
        Building house = new Building(this, Building.Type.House);
        if (house.checkCost((int) wood)) {
            unitList.get(0).build(house);
            setPopmax();
            houses++;

        }
    }

    Resource.Type priorityResource() {
        return (wood / 2) < food ? Wood : Food;
    }

    void clearPlanters(){
        for (Unit n : unitList) {
            if (n.state==Plant)
                n.changeState(Gather, Resource.getCalcResource(this));
        }
    }
    void setPlanters(){
        double per = Math.floor(1 + (population / 20));
        System.out.println(per);
        for (int i = 0; i < per; i++) {
            int index = rn.nextInt(unitList.size());
            if (rn.nextFloat()<.25f) {
                unitList.get(index).changeState(Unit.State.Plant, priorityResource());
            }
        }
    }

    void agricultureManager(float dt){
        dPlant.update(dt);
        if (dPlant.isDone()) {
            clearPlanters();
            setPlanters();
            dPlant.reset();
        }
    }

    public void update(float dt) {
        agricultureManager(dt);
        updateUnits();
        buildHouse();
    }

    public int getWood() {
        return (int) wood;
    }

    public int getFood() {
        return (int) food;
    }

    public void addResource(Resource.Type t, double value) {
        switch (t) {
            case Food: {
                food += value;
                break;
            }
            case Wood: {
                wood += value;
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

    public void sub(Resource.Type t, double i) {
        addResource(t,-i);
    }
}
