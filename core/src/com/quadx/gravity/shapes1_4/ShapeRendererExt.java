package com.quadx.gravity.shapes1_4;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Chris Cavazos on 1/21/2017.
 * v0.2
 */
public class ShapeRendererExt extends ShapeRenderer {

    public void rect(Rectangle r) {
        rect(r.x,r.y,r.width,r.height);
    }
    public void triangle(Triangle t){
        float[] f=t.getPoints();
        triangle(f[0],f[1],f[2],f[3],f[4],f[5]);
    }
    public void circle(Circle c){
        circle(c.center.x,c.center.y,c.radius);
    }
    public void ngon(Ngon o){
        polygon(o.getVerticies());
    }

    public void ngonFill(Ngon o){
        for(Triangle t: Triangle.triangulate(o)){
            triangle(t.a().x,t.a().y,t.b().x,t.b().y,t.c().x,t.c().y);

        }
    }
    public void line(Line l){
        line(l.a,l.b);
    }
    public void arc(Arc a){
        Vector2 v= a.getCenter();
        arc(v.x,v.y,a.getRadius(),a.getStart(),a.getAngle());
    }
}