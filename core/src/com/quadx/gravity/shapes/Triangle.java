package com.quadx.gravity.shapes;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Created by Chris Cavazos on 1/23/2017.
 * V0.2
 */
public class Triangle {
    float[] points= new float[6];
    public Triangle(){
        this(new float[]{0,0,0,0,0,0});
    }
    public Triangle(float[] p){
        points=p;
    }
    public float[] getPoints(){
        return points;
    }

    public boolean overlaps(Rectangle r){
        ArrayList<Line> triLines =Line.asLines(this);
        ArrayList<Line> rectLines=Line.asLines(r);
        boolean ov=false;
        for(Line l1: triLines){
            for(Line l2:rectLines){
                ov=ov|| Line.intersectsLine(l1,l2);
            }
        }
        return ov;
    }
    float vectorCross(Vector2 a, Vector2 b){
        return (a.x*b.y)-(a.y*b.x);
    }
    boolean sameSide(Vector2 p1, Vector2 p2, Vector2 a, Vector2 b){
        float cp1 = vectorCross(new Vector2(b.x-a.x,b.y-a.y),new Vector2(p1.x-a.x,p1.y-a.y));
        float cp2 = vectorCross(new Vector2(b.x-a.x,b.y-a.y),new Vector2(p2.x-a.x,p2.y-a.y));
        float r = Vector2.dot(0,cp1,0,cp2);
        if(r>=0)return true;
        else return false;
    }
    public boolean containsPoint2(Vector2 p) {
        float[] f=points.clone();

        Vector2 a = new Vector2(f[0], f[1]);
        Vector2 b = new Vector2(f[2], f[3]);
        Vector2 c = new Vector2(f[4], f[5]);
        if(sameSide(p,a,b,c) && sameSide(p,b,a,c) && sameSide(p,c,a,b))
            return true;
        else return  false;
    }
        public boolean containsPoint(Vector2 p) {
        float[] f=points.clone();
        Vector2 a = new Vector2(f[0], f[1]);
        Vector2 b = new Vector2(f[2], f[3]);
        Vector2 c = new Vector2(f[4], f[5]);

        Vector2 v0 =new Vector2(c.x-a.x,c.y-a.y);
        Vector2 v1 =new Vector2(b.x-a.x,b.y-a.y);
        Vector2 v2 =new Vector2(p.x-a.x,p.y-a.y);

        float[] dots = new float[5];
        dots[0] = Vector2.dot(v0.x, v0.y, v0.x, v0.y);
        dots[1] = Vector2.dot(v0.x, v0.y, v1.x, v1.y);
        dots[2] = Vector2.dot(v0.x, v0.y, v2.x, v2.y);
        dots[3] = Vector2.dot(v1.x, v1.y, v1.x, v1.y);
        dots[4] = Vector2.dot(v1.x, v1.y, v1.x, v2.y);
        float invd = 1 / (dots[0] * dots[3] - dots[1] * dots[1]);
        float u = (dots[3] * dots[2] - dots[1] * dots[4]) * invd;
        float v = (dots[0] * dots[4] - dots[1] * dots[2]) * invd;
        return (u >= 0) && (v >= 0) && (u + v < 1);

    }

}
