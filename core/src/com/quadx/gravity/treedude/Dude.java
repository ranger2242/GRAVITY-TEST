package com.quadx.gravity.treedude;

import com.badlogic.gdx.graphics.Color;
import com.quadx.gravity.shapes1_4.ShapeRendererExt;

import static com.quadx.gravity.states.TreeDudeState.W;

/**
 * Created by Chris Cavazos on 7/30/2018.
 */
public class Dude {
    int pos = 0;
    int chops=0;
    public void render(ShapeRendererExt sr){
        sr.setColor(Color.RED);
        sr.rect(pos == 0? 0: 2*W,0,W,W);
    }

    public void setPos(int i) {
        pos=i;
        chops++;
    }

    public int getPos() {
        return pos;
    }

    public String getChops() {
        return chops+"";
    }
}
