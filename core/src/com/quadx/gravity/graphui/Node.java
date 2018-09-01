package com.quadx.gravity.graphui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.quadx.gravity.physicsBody.Body;
import com.quadx.gravity.shapes1_5_2.Circle;
import com.quadx.gravity.shapes1_5_2.ShapeRendererExt;

import java.util.ArrayList;

import static com.quadx.gravity.Game.mpos;
import static com.quadx.gravity.Game.mvel;
import static com.quadx.gravity.states.GraphUIState.graph;

/**
 * Created by Chris Cavazos on 9/1/2018.
 */
public class Node {
    public Circle shape;
    Color color = new Color(1,1,1,.5f);
    Body body  = new Body();
    boolean clicked = false;

    ArrayList<Node> links = new ArrayList<>();

    public Node(Circle circle){
        shape = circle;
        System.out.println("@"+shape.center.toString());
        body.setPos(shape.center);
    }

    public void connect(Node n){
        links.add(n);
        n.links.add(this);
    }
    public void render(ShapeRendererExt sr) {
        sr.set(ShapeRenderer.ShapeType.Filled);
        sr.setColor(color);
        sr.circle(shape);
        sr.set(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.WHITE);
        sr.circle(shape);
        for(Node n: links){
            sr.line(n.shape.center,this.shape.center);
        }
    }

    public void update(float dt) {
        //body.update();
        boolean b=shape.intersects(mpos);
        boolean a=Gdx.input.isButtonPressed(0);
        if(a&& b && (!graph.nodeClicked  || graph.active==this)){
            body.setPos(shape.center.lerp(mpos,.5f).add(new Vector2(mvel.scl(1.1f))));
            clicked=true;
        }
        for(Node n : links){
            Vector2 va = body.getPos();
            Vector2 vb= n.body.getPos();
            double d= va.dst(vb);
            if(d>300) {
                n.body.setPos(n.body.getPos().add(body.getVector(-100*dt,vb,va)));
            }
        }

        if(clicked && !(a && b)){
            body.setVel(new Vector2(mvel).scl(.2f));
            clicked=false;
        }
        body.setVel(body.getVel().scl(.965f));
        body.setPos(body.getPos().add(body.getVel()));
        shape.center.set(body.getPos());

    }
}
