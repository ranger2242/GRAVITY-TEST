package com.quadx.gravity.heightmap;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Chris Cavazos on 1/19/2017.
 */
public class Cell {
    Vector2 pos=new Vector2();
    int height=0;
    Polygon corners=new Polygon();

    public Cell(Vector2 p, int h){
        pos=p;
        height=h;
    }
    public void setCorners(Polygon c){
        corners=c;
    }

    public Polygon getCorners() {
        return corners;
    }
}
