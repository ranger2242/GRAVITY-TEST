package com.quadx.gravity.treedude;

import com.badlogic.gdx.graphics.Color;
import com.quadx.gravity.Game;
import com.quadx.gravity.shapes1_4.ShapeRendererExt;

import java.util.ArrayList;

import static com.quadx.gravity.states.TreeDudeState.W;

/**
 * Created by Chris Cavazos on 7/30/2018.
 */
public class Tree {
    ArrayList<Integer> left= new ArrayList<>();
    ArrayList<Integer> right= new ArrayList<>();


    int lines=0;
    public Tree(){
        for(int i=0;i<6;i++){
            left.add(0);
            right.add(0);
        }
    }

    public void chop(){
        left.remove(0);
        right.remove(0);
        if(lines %2 ==1) {
            if (Game.rn.nextBoolean()) {
                left.add(1);
                right.add(0);
            }else{
                left.add(0);
                right.add(1);
            }
        }else{
            left.add(0);
            right.add(0);
        }
        lines++;
    }

    public void render(ShapeRendererExt sr) {
        sr.setColor(Color.WHITE);
        for(int i=0;i<left.size();i++){
            if(left.get(i)==1){
                sr.rect(0, (W * i),W,W);
            }
            if(right.get(i)==1){
                sr.rect(2*W, (W * i),W,W);
            }
        }
    }

    public int getLeft() {
        return left.get(0);
    }
    public int getRight() {
        return right.get(0);
    }
}
