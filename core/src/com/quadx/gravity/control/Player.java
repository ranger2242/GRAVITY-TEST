package com.quadx.gravity.control;

import com.quadx.gravity.Game;

import java.util.ArrayList;

/**
 * Created by Chris Cavazos on 9/29/2016.
 */
public class Player {
    public ArrayList<Unit> unitList = new ArrayList<>();

    public void initUnits(){
        unitList.add(new Unit(Game.WIDTH/5,Game.HEIGHT/3));
        unitList.add(new Unit((Game.WIDTH/5)*2,Game.HEIGHT/3));
        unitList.add(new Unit(Game.WIDTH/5,(Game.HEIGHT/3)*2));
        unitList.add(new Unit((Game.WIDTH/5)*2,(Game.HEIGHT/3)*2));
    }
}
