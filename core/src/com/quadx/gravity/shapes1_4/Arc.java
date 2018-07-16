package com.quadx.gravity.shapes1_4;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Created by Chris Cavazos on 2/7/2017.
 */
public class Arc extends Shape {
    private Vector2 pos;
    private float radius;
    private float start;
    private float angle;
    private int seg;
    private float w;

    public Arc(Vector2 pos,float radius, float start, float angle, int seg,float w){
        this.pos=pos;
        this.radius=radius;
        this.start=start;
        this.angle=angle;
        this.seg=seg;
        this.w=w;
    }
    public void setAngle(float ang){
        this.angle=ang;
    }
    public void setPos(Vector2 p){
        pos.set(p);
    }
    public void render(ShapeRendererExt sr){
        ArrayList<Vector2> points = new ArrayList<>();
        ArrayList<Vector2> points2 = new ArrayList<>();

        float a= angle/(seg-1);
        for(int i=0;i<seg;i++) {
            float x = (float) (pos.x + radius *Math.cos(Math.toRadians(start+(a*i))));
            float y = (float) (pos.y + radius *Math.sin(Math.toRadians(start+(a*i))));
            points.add(new Vector2(x,y));
            float x2 = (float) (pos.x + (radius + w) * Math.cos(Math.toRadians(start + (a * i))));
            float y2 = (float) (pos.y + (radius + w) * Math.sin(Math.toRadians(start + (a * i))));
            points2.add(new Vector2(x2,y2));

        }
        
        for(int i=0;i<points.size();i++){
            if(i<points.size()-1){
                sr.line(new Line(points.get(i),points.get(i+1)));
                sr.line(new Line(points.get(i),points2.get(i)));
                sr.line(new Line(points2.get(i),points2.get(i+1)));

            }
        }
    }

    @Override
    public Vector2 getCenter() {
        return pos;
    }

    public void setRadius(float r){
        this.radius=r;
    }
    public void setW(float w) {
        this.w = w;
    }

    public float getAngle() {
        return angle;
    }

    public float getRadius() {
        return radius;
    }

    public float getStart() {
        return start;
    }
}
