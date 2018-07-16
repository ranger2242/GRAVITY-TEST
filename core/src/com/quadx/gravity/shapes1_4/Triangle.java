package com.quadx.gravity.shapes1_4;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Created by Chris Cavazos on 1/23/2017.
 * V0.2
 */
@SuppressWarnings("WeakerAccess")
public class Triangle extends Shape {
    private float[] points = new float[6];

    public Triangle() {
        this(new float[]{0, 0, 0, 0, 0, 0});
    }

    public Triangle(float[] p) {
        points = p;
    }

    public Triangle(Vector2 a, Vector2 b, Vector2 c) {
        this(new float[]{a.x, a.y, b.x, b.y, c.x, c.y});
    }

    public Triangle(Triangle t) {
        int a = t.getPoints().length;
        float[] f = new float[a];
        for (int i = 0; i < a; i++) {
            f[i] = t.getPoints()[i];
        }
        points = f;

    }

    public Triangle(Vector2 pos, float angle, float r) {
        points = new float[]{pos.x, pos.y,
                (float) (pos.x + r * Math.cos(Math.toRadians(200 + angle))), (float) (pos.y + r * Math.sin(Math.toRadians(200 + angle))),
                (float) (pos.x + r * Math.cos(Math.toRadians(160 + angle))), (float) (pos.y + r * Math.sin(Math.toRadians(160 + angle)))};
    }

    public Vector2 a() {
        return new Vector2(points[0], points[1]);
    }

    public Vector2 b() {
        return new Vector2(points[2], points[3]);
    }

    public Vector2 c() {
        return new Vector2(points[4], points[5]);
    }

    public float[] getPoints() {
        return points;
    }

    public boolean overlaps(Rectangle r) {
        return overlaps(Line.asLines(r));

    }

    boolean overlaps(ArrayList<Line> lines) {
        ArrayList<Line> triLines = Line.asLines(this);
        boolean ov = false;
        for (int i = 0; i < triLines.size(); i++) {
            Line l1 = triLines.get(i);
            for (int j = 0; j < lines.size(); j++) {
                Line l2 = lines.get(j);
                ov = ov || l1.intersects(l2);
                System.out.println("");
            }
        }
        return ov;
    }

    public boolean overlaps(Triangle t) {
        return overlaps(Line.asLines(t));
    }

    public float vectorCross(Vector2 a, Vector2 b) {
        return (a.x * b.y) - (a.y * b.x);
    }

    private boolean sameSide(Vector2 p1, Vector2 p2, Vector2 a, Vector2 b) {
        float cp1 = vectorCross(new Vector2(b.x - a.x, b.y - a.y), new Vector2(p1.x - a.x, p1.y - a.y));
        float cp2 = vectorCross(new Vector2(b.x - a.x, b.y - a.y), new Vector2(p2.x - a.x, p2.y - a.y));
        float r = Vector2.dot(0, cp1, 0, cp2);
        return r >= 0;
    }

    public boolean containsPoint2(Vector2 p) {
        float[] f = points.clone();

        Vector2 a = new Vector2(f[0], f[1]);
        Vector2 b = new Vector2(f[2], f[3]);
        Vector2 c = new Vector2(f[4], f[5]);
        return sameSide(p, a, b, c) && sameSide(p, b, a, c) && sameSide(p, c, a, b);
    }

    public boolean containsPoint(Vector2 p) {
        float[] f = points.clone();
        Vector2 a = new Vector2(f[0], f[1]);
        Vector2 b = new Vector2(f[2], f[3]);
        Vector2 c = new Vector2(f[4], f[5]);

        Vector2 v0 = new Vector2(c.x - a.x, c.y - a.y);
        Vector2 v1 = new Vector2(b.x - a.x, b.y - a.y);
        Vector2 v2 = new Vector2(p.x - a.x, p.y - a.y);

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

    public static Triangle create(Vector2 pos, float hight, float width) {
        return new Triangle(new float[]{pos.x, (pos.y + (hight / 2)), (pos.x - (width / 2)), (pos.y - (hight / 2)), (pos.x + (width / 2)), (pos.y - (hight / 2))});
    }


    public static Triangle updatePoints(Vector2 pos, float angle, float r) {
        float[] points = new float[]{pos.x, pos.y,
                (float) (pos.x + r * Math.cos(Math.toRadians(200 + angle))), (float) (pos.y + r * Math.sin(Math.toRadians(200 + angle))),
                (float) (pos.x + r * Math.cos(Math.toRadians(160 + angle))), (float) (pos.y + r * Math.sin(Math.toRadians(160 + angle)))};
        return new Triangle(points);
    }

    public static ArrayList<Triangle> triangulate(Ngon o) {
        ArrayList<Triangle> output = new ArrayList<>();
        float[] f = o.getVerticies();
        float cx = o.getCenter().x;
        float cy = o.getCenter().y;
        Vector2 c = new Vector2(cx, cy);

        int count = 0;
        int n = o.getN();
        for (int i = 0; i < n; i++) {
            Vector2 a = new Vector2(f[count % (n * 2)], f[(count + 1) % (n * 2)]);
            Vector2 b = new Vector2(f[(count + 2) % (n * 2)], f[(count + 3) % (n * 2)]);
            output.add(new Triangle(a, b, c));
            count += 2;
        }
        return output;
    }

    public void setPos(Vector2 pos) {
        Vector2 delta = new Vector2();
        delta.x = pos.x - getCenter().x;
        delta.y = pos.y - getCenter().y;
        points[0] += delta.x;
        points[1] += delta.y;
        points[2] += delta.x;
        points[3] += delta.y;
        points[4] += delta.x;
        points[5] += delta.y;
    }

    public Vector2 getCenter() {
        float aveX = ((a().x + b().x + c().x) / 3);
        float aveY = ((a().y + b().y + c().y) / 3);
        return new Vector2(aveX, aveY);
    }

    public void scl(Vector2 srScl) {
        points[0] *= srScl.x;
        points[1] *= srScl.y;
        points[2] *= srScl.x;
        points[3] *= srScl.y;
        points[4] *= srScl.x;
        points[5] *= srScl.y;
    }

}
