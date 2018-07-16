package com.quadx.gravity.physicsBody;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.quadx.gravity.shapes1_4.Circle;
import com.quadx.gravity.shapes1_4.ShapeRendererExt;

import java.util.Arrays;

import static com.quadx.gravity.Game.rn;

/**
 * Created by Chris Cavazos on 6/26/2018.
 */
public class GenBody extends Body {
    Circle[] shapes = new Circle[4];
    Color[] colors = new Color[4];


    public GenBody(Color ic, Vector2 pos){
        Arrays.fill(colors,ic);
        for(int i=0;i<shapes.length;i++){
            Circle c= new Circle( new Vector2(pos).add(30* i ,0), (rn.nextInt(4)+1)*5);
            shapes[i]=c;
        }
    }
    public GenBody(float m, float r, float x, float y) {
        super(m, r, x, y);
    }

    public GenBody breed(GenBody b){
        return new GenBody(Color.WHITE,new Vector2());
    }

    public void render(ShapeRendererExt sr){
        for(int i=0;i<4;i++){
            sr.setColor(colors[i]);
            sr.circle(shapes[i]);
        }

    }
}
