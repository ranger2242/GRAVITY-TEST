package com.quadx.gravity.physicsBody;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.quadx.gravity.EMath;
import com.quadx.gravity.Game;
import com.quadx.gravity.shapes1_4.Circle;
import com.quadx.gravity.shapes1_4.Line;
import com.quadx.gravity.shapes1_4.ShapeRendererExt;
import com.quadx.gravity.shapes1_4.Tools;
import com.quadx.gravity.states.GravityState;

import static com.quadx.gravity.Game.HEIGHT;
import static com.quadx.gravity.Game.WIDTH;
import static com.quadx.gravity.states.GravityState.G;
import static com.quadx.gravity.states.GravityState.rn;

/**
 * Created by Tom on 3/20/2016.
 */
public class Body {

    public final Vector2 pos = new Vector2();
    private final Vector2 vel = new Vector2();
    private final Vector2 acc = new Vector2();
    private final Vector2 unit = new Vector2();
    private Color color = new Color(Color.WHITE);
    private final Circle shape = new Circle();
    private double radius;
    private float mass;
    public int id=0;

    public Body(float m, float r, float x, float y) {
        mass = m;
        radius = r;
        pos.set(x, y);
        //vel.set(randVel(10),randVel(10));
        shape.radius=r;
        shape.center.set(pos);
        setColor();
    }

    public Body(float mass, float ra) {
        this(mass,ra,rn.nextInt((int) WIDTH), rn.nextInt((int) HEIGHT));
    }

    public Body() {
    }

    float randVel(float d){
        return rn.nextFloat() *(rn.nextBoolean()?1:-1)*d;
    }

    public void setVel(Vector2 vel) {
        this.vel.set(vel);
    }
    public Vector2 getForceV(){
        return new Vector2(acc).scl(mass);
    }

    public void render(ShapeRendererExt sr) {
        sr.setColor(color);
        sr.circle(shape);
        //renderDebug(sr);
    }

    public void update() {
        applyForce();
        acc.scl(-1);

        vel.add(acc.x, acc.y);
        int f = 10;
        if (vel.x > f)
            vel.x = f;
        if (vel.y > f)
            vel.y = f;
        if (vel.x < -f)
            vel.x = -f;
        if (vel.y < -f)
            vel.y = -f;
        //System.out.println(acc.toString());

        //System.out.println(vel.toString());
        pos.add(vel);
        shape.center.set(pos);
        pos.set(Tools.wrap(this));
    }

    public Vector2 getPos() {
        return shape.center;
    }

    public Vector2 getVel() {
        return new Vector2(vel);
    }

    public Vector2 getFVec(Body b) {
        float r = b.pos.dst(pos);
        float m = (b.mass * mass);
        float r2 = (r * r);
        float force = G * (m / r2) * 100000000f;
        unit.set(getVector(10, b.pos, pos));
        return getVector(-force, b.pos, pos);
    }

    public Vector2 getVector(double vel, Vector2 start, Vector2 end) {
        float dx = EMath.dx(end, start);
        float dy = EMath.dy(end, start);
        double theta = Math.atan2(dy, dx);
        float vx = (float) (vel * Math.cos(theta));
        float vy = (float) (vel * Math.sin(theta));
        return new Vector2(vx, vy);
    }

    public double dst(Body b){
        return shape.center.dst(b.getShape().center);
    }
    private void applyForce() {
        Vector2[] arr = GravityState.getForceComp(this);
        Vector2 sum = new Vector2();
        for (Vector2 anArr : arr) {
            if(anArr !=null)
            sum.add(anArr);
        }
        sum.scl((1 / mass));
        acc.set(sum.x, sum.y);
    }

    private void setColor() {
        float r = Game.rn.nextFloat() + .2f;
        float g = Game.rn.nextFloat() + .2f;
        float b = Game.rn.nextFloat() + .2f;
        color = new Color(r > 1 ? 1 : r, g > 1 ? 1 : g, b > 1 ? 1 : b, 1);

    }

    private void renderDebug(ShapeRendererExt sr) {
        Vector2 scl = new Vector2(acc).scl(1000);
        Vector2 scl2 = new Vector2(vel).scl(100);
        Vector2 scl3 = new Vector2(unit).scl(10);
        Vector2 end = new Vector2(pos).add(scl);
        Vector2 end2 = new Vector2(pos).add(scl2);
        Vector2 end3 = new Vector2(pos).add(scl3);
        Line l = new Line(end, pos);
        Line l2 = new Line(end2, pos);
        Line l3 = new Line(end3, pos);

        sr.setColor(Color.RED);
        sr.line(l);
        sr.setColor(Color.GREEN);
        sr.line(l2);
        sr.setColor(Color.YELLOW);
        sr.line(l3);
    }

    private float calcRadius(){
        return (float) (4 * Math.sqrt(mass / Math.PI) / 1);
    }
    public float getRadius() {
        return (float)radius;
    }


    public void consume(Body b) {
        mass+=b.mass;
        radius=calcRadius();
    }

    public float getMass() {
        return mass;
    }

    public Circle getShape() {
        return shape;
    }

    public Vector2 getAcc() {
        return acc;
    }

    public void addForce(Vector2 forceV) {
        vel.add(forceV);
        //System.out.println(forceV.toString());
    }

    public void flipVel() {
        vel.scl(-1);
    }

    public void setPos(Vector2 pos) {
        this.pos.set(pos);
        shape.center.set(pos);
    }
}
