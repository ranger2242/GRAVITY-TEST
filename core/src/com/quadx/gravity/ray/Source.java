package com.quadx.gravity.ray;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.quadx.gravity.shapes1_4.Line;
import com.quadx.gravity.shapes1_4.ShapeRendererExt;

import java.util.ArrayList;

import static com.quadx.gravity.Game.HEIGHT;
import static com.quadx.gravity.Game.WIDTH;

/**
 * Created by Chris Cavazos on 10/31/2017.
 */
public class Source {
    private ArrayList<Line> rays=new ArrayList<>();
    private Vector2 pos = new Vector2();
    private int n =1;
    private  float ang = 1;
    private Color color = new Color();
    public Source(Vector2 pos,int n, float ang){
        this.pos=pos;
        this.n=n;
        this.ang=ang;
        calculate();
    }

    public void render(ShapeRendererExt sr){
        sr.setColor(color);
        for(Line l : rays) {
            sr.line(l);
        }
    }

    void calculate(){
        rays.clear();
        for(int i=0;i<n;i++){
            double x=pos.x+2000*Math.cos(Math.toRadians((360/n)*i));
            double y=pos.y+2000*Math.sin(Math.toRadians((360/n)*i));

            Line r = new Line(pos,new Vector2((float)x,(float)y));
            rays.add(r);
        }
    }
    public void setPos(Vector2 pos) {
        this.pos = pos;
        wrap();
        calculate();
    }
    void wrap(){
        if(pos.x>WIDTH){
            pos.x=1;
        }
        if(pos.y>HEIGHT){
            pos.y=1;
        }
        if(pos.x<0){
            pos.x=WIDTH-1;
        }
        if(pos.y<0){
            pos.y=HEIGHT-1;
        }
    }

    public void collide(ArrayList<Rectangle> objs) {
        for(Rectangle r: objs){
            for(Line l1: rays) {
                for (Line l : Line.asLines(r)) {
                    if(l.intersects(l1)) {
                        l1.b.set(Line.intersectionPoint(l1, l));
                    }
                }
            }
        }
    }

    public Vector2 getPos() {
        return pos;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
