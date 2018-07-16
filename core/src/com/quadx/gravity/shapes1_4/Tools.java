package com.quadx.gravity.shapes1_4;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.quadx.gravity.physicsBody.Body;

import static com.quadx.gravity.Game.HEIGHT;
import static com.quadx.gravity.Game.WIDTH;

/**
 * Created by Chris Cavazos on 2/1/2018.
 */
public class Tools {
    public static Vector2 wrap(Body body) {
        Vector2 pos = body.getPos();
        Vector3 npos=wrap(pos);
        if(npos.z==1)
        body.setVel(body.getVel().scl(.2f));
        return new Vector2(npos.x , npos.y);
    }
    public static Vector3 wrap(Vector2 pos) {
        boolean b=false;
        b=pos.x>WIDTH+1 || pos.x<0 || pos.y>HEIGHT+1 || pos.y<0;

        float x=pos.x %= WIDTH + 1;
        float y=pos.y %= HEIGHT+1;
        return new Vector3((x < 0) ? x + WIDTH : x,(y < 0) ? y + HEIGHT : y , b?1:0);
    }
}
