package com.quadx.gravity.pong;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import static com.quadx.gravity.Game.HEIGHT;
import static com.quadx.gravity.Game.WIDTH;
import static com.quadx.gravity.Game.ft;
import static com.quadx.gravity.states.PongState.mid;
import static com.quadx.gravity.states.PongState.scl;
import static com.quadx.gravity.states.PongState.sep;

/**
 * Created by Chris Cavazos on 9/20/2017.
 */
public class Player {
    Rectangle shape = new Rectangle();
    int depth = 0;
    float rate = 5;
    public int points = 0;
    Vector2 prevPos = new Vector2();
    boolean curve = false;
    float dtCurve = 0;

    public Player(int depth) {
        this.depth = depth;
        float w = 200;
        float h = 120;
        if (depth == 0) {
            shape = new Rectangle(mid.x, mid.y, w, h);
        }
        if (depth == 1) {
            shape = new Rectangle(mid.x, mid.y, w * scl.x, h * scl.y);

        }
    }

    public void update(float dt) {
        float x1 = 0;
        float x2 = WIDTH;
        float y1 = 0;
        float y2 = HEIGHT;
        float cx = shape.x;
        float cy = shape.y;
        if (depth == 1) {
            x1 += sep.x;
            x2 -= sep.x;
            y1 += sep.y;
            y2 -= sep.y;
        }
        if (cx < x1) {
            shape.x = x1 + 1;
        }
        if (cy < y1) {
            shape.y = y1 + 1;
        }

        if (cx + shape.width > x2) {
            shape.x = x2 - shape.width - 1;
        }
        if (cy + shape.height > y2) {
            shape.y = y2 - shape.height - 1;
        }
        if(curve)
        dtCurve+=dt;
    }

    public Color getColor() {
        if (depth == 0) return Color.BLUE;
        else return Color.RED;
    }

    public Rectangle getShape() {
        return shape;
    }

    public void setPos(Vector2 pos) {
        prevPos.set(shape.x, shape.y);
        shape.setPosition(shape.x + (rate * pos.x), shape.y + (rate * pos.y));
    }

    public Vector2 getVel() {
        float dx = shape.x - prevPos.x;
        float dy = shape.y - prevPos.y;

        return new Vector2(dx, dy);
    }

    public boolean canCurve() {
        if(dtCurve>5*ft){
            dtCurve=0;
            curve=false;
        }
        return curve;
    }

    public void setCurve(boolean curve) {
        this.curve = curve;
    }
}
