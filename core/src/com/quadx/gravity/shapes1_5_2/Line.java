package com.quadx.gravity.shapes1_5_2;


import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Created by Chris Cavazos on 2/4/2017.
 */
public class Line extends Shape {
    public Vector2 a;
    public Vector2 b;

    public Line(Vector2 a, Vector2 b) {
        this.a = a;
        this.b = b;
    }

    public Line(Vector2 center, double x1,double y1,double x2,double y2 ){
       this(new Vector2(center).add((float)x1,(float)y1),
               new Vector2(center).add((float)x2,(float)y2));
    }

    public Line() {
        this(new Vector2(), new Vector2());
    }

    public boolean intersects(Line l1){
        float s1x, s1y, s2x, s2y;
        s1x = this.b.x - this.a.x;
        s1y = this.b.y - this.a.y;

        s2x = l1.b.x - l1.a.x;
        s2y = l1.b.y - l1.a.y;
        float s = (-s1y * (this.a.x - l1.a.x) + s1x * (this.a.y - l1.a.y)) / (-s2x * s1y + s1x * s2y);
        float t = (s2x * (this.a.y - l1.a.y) - s2y * (this.a.x - l1.a.x)) / (-s2x * s1y + s1x * s2y);
        if (s >= 0 && s <= 1 && t >= 0 && t <= 1) {
            return true;
        }
        return false;
    }


    public static ArrayList<Line> asLines(Triangle t) {
        float[] points = t.getPoints();
        ArrayList<Line> triLines = new ArrayList<>();
        Vector2 a = new Vector2(points[0], points[1]),
                b = new Vector2(points[2], points[3]),
                c = new Vector2(points[4], points[5]);
       /* a.set(a.x,fixHeight(a));
        b.set(b.x,fixHeight(b));
        c.set(c.x,fixHeight(c));
*/
        triLines.add(new Line(a, b));
        triLines.add(new Line(b, c));
        triLines.add(new Line(c, a));
        return triLines;
    }

    public static ArrayList<Line> asLines(Rectangle r) {
        ArrayList<Line> rect = new ArrayList<>();
        Vector2 a = new Vector2(r.x, r.y),
                b = new Vector2(r.x, r.y + r.height),
                c = new Vector2(r.x + r.width, r.y + r.height),
                d = new Vector2(r.x + r.width, r.y);
  /*      a.set(a.x,fixHeight(a));
        b.set(b.x,fixHeight(b));
        c.set(c.x,fixHeight(c));
        d.set(d.x,fixHeight(d));
*/
        rect.add(new Line(a, b));
        rect.add(new Line(b, c));
        rect.add(new Line(c, d));
        rect.add(new Line(d, a));
        return rect;
    }

    public float length() {
        return a.dst(b);
    }

    @Override
    public Vector2 getCenter() {
        float x = a.x + ((b.x - a.x) / 2);
        float y = a.y + ((b.y - a.y) / 2);
        return new Vector2(x, y);

    }

    public ArrayList<Line> subdivide(int n) {
        float dx = EMath.dx(a, b) / n;
        float dy = EMath.dy(a, b) / n;
        ArrayList<Line> lines = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Line an = new Line(new Vector2(a.x + (i * dx), a.y + (i * dy)), new Vector2(a.x + ((i + 1) * dx), a.y + ((i + 1) * dy)));
            lines.add(an);
        }
        return lines;
    }

    public boolean intersects(Circle cir) {
        Vector2 c = cir.center;
        float r = cir.radius;

        return ((c.x - a.x) * (b.y - a.y) - (c.y - a.y) * (b.x - a.x)) <= r;
       /* float x0=c.center.x,y0=c.center.y;
        return c.radius >= Math.abs(
                ((b.x-a.x)*x0)+
                        ((a.y-b.y)*y0)+
                        ((a.x-b.x)*a.y)+
                        ((b.y-a.y)*a.x))
                / Math.sqrt((Math.pow(b.x-a.x,2)+Math.pow(a.y-b.y,2)));*/
    }

    public static Vector2 intersectionPoint(Line l, Line l1) {
        float s1x, s1y, s2x, s2y;
        s1x = l.b.x - l.a.x;
        s1y = l.b.y - l.a.y;

        s2x = l1.b.x - l1.a.x;
        s2y = l1.b.y - l1.a.y;
        float s = (-s1y * (l.a.x - l1.a.x) + s1x * (l.a.y - l1.a.y)) / (-s2x * s1y + s1x * s2y);
        float t = (s2x * (l.a.y - l1.a.y) - s2y * (l.a.x - l1.a.x)) / (-s2x * s1y + s1x * s2y);
        Vector2 out = new Vector2();
        if (s >= 0 && s <= 1 && t >= 0 && t <= 1) {

            out = new Vector2(l.a.x + (t * s1x), l.a.y + (t * s1y));
        }
        return out;
    }

    public boolean intersects(Ngon n) {
        for (Triangle t : Triangle.triangulate(n)) {
            for (Line a : Line.asLines(t)) {
                if (intersects(a))
                    return true;
            }
        }
        return false;
    }
}
