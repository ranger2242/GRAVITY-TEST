package com.quadx.gravity.control;

import com.badlogic.gdx.math.Vector2;
import com.quadx.gravity.shapes1_5_2.EMath;

import java.util.ArrayList;
import java.util.Random;

import static com.quadx.gravity.Game.scr;

/**
 * Created by Chris Cavazos on 9/30/2016.
 */
public class Grid {
    public ArrayList<Resource> resources = new ArrayList<>();
    Random rn = new Random();

    public Grid() {
        initGrid();
    }

    void initGrid() {
        resources.clear();
        plotResource(Resource.Type.Wood, rn.nextInt(30) + 10, 2000);
        plotResource(Resource.Type.Food, rn.nextInt(30) + 10, 1000);

    }

    void plotResource(Resource.Type type, int init, int n) {
        ArrayList<Vector2> seeds = new ArrayList<>();
        ArrayList<Resource> trees2 = new ArrayList<>();
        for (int i = 0; i < init; i++) {
            seeds.add(EMath.randInt(scr));
        }
        for (int i = 0; i < 1000; i++) {
            Vector2 gauss = EMath.randGauss().scl(EMath.randInt(50));
            Vector2 pos = ((Vector2) EMath.getRand(seeds)).add(gauss);
            trees2.add(new Resource(type, pos));
        }
        resources.addAll(trees2);
    }

    public void clearSpentResources(float dt) {
        for (int i = resources.size() - 1; i >= 0; i--) {
            if (resources.get(i).isEmpty()) {
                resources.remove(i);
            }else
                resources.get(i).update(dt);
        }

    }

    public void update(float dt) {
        clearSpentResources(dt);

    }
}
