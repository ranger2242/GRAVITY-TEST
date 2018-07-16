package com.quadx.gravity.shapes1_4;

import com.badlogic.gdx.math.Vector2;

@SuppressWarnings("WeakerAccess")
public class Ngon extends Shape {
    private final Vector2 center = new Vector2();
    private float r = 0;
    private float[] points = new float[16];
    private int n = 1;
    private float angle = 0;
    private Vector2 scl=new Vector2(1,1);

    public Ngon(Vector2 pos, float r, int n) {
        this(pos, r, n, 0);
    }

    public Ngon(Vector2 pos, float r, int n, float angle) {
        this.n = n;
        this.r = r;
        this.angle=angle;
        points = new float[n * 2];
        center.set(pos);
       recalcPoints();

    }
    void recalcPoints(){
        int count = 0;
        for (int i = 0; i < n; i++) {
            float angd=((360 / n) * i) + angle;
            float ang= (float) Math.toRadians(angd);
            points[count] = (float) (center.x + ((r * scl.x) * Math.cos(ang)));
            points[count + 1] = (float) (center.y + ((r * scl.y) * Math.sin(ang)));
            count += 2;
        }
    }

    @SuppressWarnings("unused")
    public Ngon(Ngon ngon) {
        this(ngon.getCenter(), ngon.getR(), ngon.getN(), ngon.getAngle());
    }

    public Ngon() {
        this(new Vector2(), 0, 1, 0);
    }


    public void setPos(Vector2 v) {
        center.set(v);
        recalcPoints();
    }

    public float[] getVerticies() {
        return points;
    }

    public Vector2 getCenter() {
        return center;
    }

    public int getN() {
        return n;
    }

    public float getR() {
        return r;
    }

    public float getAngle() {
        return angle;
    }

    public void scl(Vector2 srScl) {
        this.scl.set(srScl);
        recalcPoints();
    }

    public void setAngle(float angle) {
        this.angle = angle;
        recalcPoints();
    }
}