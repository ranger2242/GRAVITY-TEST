package com.quadx.gravity.tanks;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.quadx.gravity.shapes1_4.ShapeRendererExt;

import static com.quadx.gravity.EMath.rn;
import static com.quadx.gravity.Game.HEIGHT;
import static com.quadx.gravity.Game.WIDTH;

/**
 * Created by Chris Cavazos on 10/8/2017.
 */
public class Wind {
    Vector2 vel = new Vector2();
    public Wind(Vector2 v){
        vel.set(v);
    }

    public Vector2 getVel() {
        return vel;
    }

    public void setVel() {
        this.vel = new Vector2((float)(4*rn.nextGaussian()),(float)(rn.nextGaussian()) );
        if(rn.nextBoolean())
            vel.x=-vel.x;
        if(rn.nextBoolean())
            vel.y=-vel.y;
    }

    public void render(ShapeRendererExt sr){
        sr.set(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.WHITE);
        Vector2 start= new Vector2(WIDTH/2,HEIGHT-100);
        Vector2 end = new Vector2(vel).scl(20).add(start);
        sr.line(end,start);
        sr.circle(end.x,end.y,3);

    }
}
