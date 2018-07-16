package com.quadx.gravity.pong;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.quadx.gravity.shapes1_4.Circle;
import com.quadx.gravity.shapes1_4.Line;

import static com.quadx.gravity.EMath.rn;
import static com.quadx.gravity.Game.*;
import static com.quadx.gravity.states.PongState.*;

/**
 * Created by Chris Cavazos on 9/20/2017.
 */
public class Ball {
    Circle shape = new Circle();
    Vector2 vel = new Vector2(0,0);
    boolean direction = true;
    float smin = 10;
    float smax = 40;
    float range = smax - smin;
    float bounded;
    float dtDeath=0;
    float dtCurve=0;
    boolean deathPause=true;
    public boolean curve=false;
    Vector2 curveVel=new Vector2(0,0);

    public Ball() {
        shape = new Circle(mid, range/2);
    }

    public void update(float dt) {
        shape.center.add(vel);
        if(deathPause){
            dtDeath+=dt;
            if(dtDeath>120*ft){
                vel = new Vector2((float) rn.nextGaussian() * 2, (float) rn.nextGaussian() * 2);
                deathPause=false;
                dtDeath=0;
            }
        }else {
            if (direction) {
                shape.radius += .15;
            } else
                shape.radius -= .15;
        }
        if (shape.radius > smax) {
            shape.radius = smax;
            direction = !direction;

        }
        if (shape.radius < smin) {
            shape.radius = smin;
            direction = !direction;
        }
        bounded = ((shape.radius - (smax / 2) / range) - smin) / range;
        if (bounded > 1) bounded = 1;
        if (bounded < 0) bounded = 0;
        if(curve){
            dtCurve+=dt;
            if(dtCurve<4*ft){
                curveVel.add(curveVel.x*.7f,curveVel.y*.7f);
                vel.add(curveVel);
            }else {
                dtCurve = 0;
                curve=false;
            }

        }
        collision();
        fixPos();
    }

    void collision() {
        if (bounded < .05) {
            if (shape.overlaps(p2.getShape())) {
                vel.add(p2.getVel());
                direction = !direction;
                p2.setCurve(true);
            } else {
                reset();
                p1.points++;
            }

        }
        if (bounded > .94) {
            if (shape.overlaps(p1.getShape())) {
                vel.add(p1.getVel());
                direction = !direction;
                p1.setCurve(true);

            } else {
                reset();
                p2.points++;

            }
        }
    }

    void reset() {
        shape.center = new Vector2(WIDTH / 2, HEIGHT / 2);
        shape.radius=smin+(range/2);
        deathPause=true;
        vel=new Vector2(0,0);
    }

    void fixPos() {
        float x1 = getHLine().a.x;
        float x2 = getHLine().b.x;
        float y1 = getVLine().a.y;
        float y2 = getVLine().b.y;
        float cx = shape.center.x;
        float cy = shape.center.y;
        float r = shape.radius;
        if (cx - r < x1) {
            shape.center.x = x1 + r + 1;
            impact(-1, 1);
        }
        if (cy - r < y1) {
            shape.center.y = y1 + r + 1;
            impact(1, -1);

        }

        if (cx + r > x2) {
            shape.center.x = x2 - r - 1;
            impact(-1, 1);

        }
        if (cy + r > y2) {
            shape.center.y = y2 - r - 1;
            impact(1, -1);
        }
    }

    void impact(int x, int y) {
        vel.set(vel.x * (x*.1f), vel.y * (y*.5f));
    }

    public Circle getShape() {
        return shape;
    }

    public void setShape(Circle shape) {
        this.shape = shape;
    }

    public boolean isDirection() {
        return direction;
    }

    public void setDirection(boolean direction) {
        this.direction = direction;
    }

    public Color getColor() {

        float r = (float) Math.acos(bounded);
        float b = (float) Math.asin(bounded);
        return new Color(r, 0, b, 1);
    }

    public Line getHLine() {
        float y1 = ((WIDTH - (WIDTH * scl.x)) / 2) * (1 - bounded);
        return new Line(new Vector2(y1, shape.center.y), new Vector2(WIDTH - y1, shape.center.y));
    }

    public Line getVLine() {
        float y1 = ((HEIGHT - (HEIGHT * scl.y)) / 2) * (1 - bounded);
        return new Line(new Vector2(shape.center.x, y1), new Vector2(shape.center.x, HEIGHT - y1));
    }

    public void curve(boolean can) {
        if(!curve && can) {
            curve = true;
            int x=rn.nextInt(2) + 1;
            int y=rn.nextInt(2) + 1;
            if(rn.nextBoolean())
                x*=-1;
            if(rn.nextBoolean())
                y*=-1;

            curveVel.set(x, y);
        }
    }
}
