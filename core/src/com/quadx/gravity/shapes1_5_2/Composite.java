package com.quadx.gravity.shapes1_5_2;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Created by Chris Cavazos on 12/14/2017.
 */
public class Composite extends Shape {
    ArrayList<Shape> body = new ArrayList<>();

    public void add(Shape s) {
        body.add(s);
    }

    public boolean intersects(Ngon ngon) {
        ArrayList<Line> lines = null;
        for (Triangle t : Triangle.triangulate(ngon)) {
            lines.addAll(Line.asLines(t));
        }

        for (Shape s : body) {
            for (Line ls : lines) {
                if (s instanceof Line) {
                    Line l = (Line) s;
                    return l.intersects(ls);
                }
            }
        }
        return false;
    }

    @Override
    public Vector2 getCenter() {
        return null;
    }
}
