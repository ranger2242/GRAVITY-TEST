package com.quadx.gravity.graphui;

import com.badlogic.gdx.math.Vector2;
import com.quadx.gravity.shapes1_5_2.Circle;
import com.quadx.gravity.shapes1_5_2.ShapeRendererExt;

import java.util.ArrayList;

import static com.quadx.gravity.Game.rn;
import static com.quadx.gravity.Game.scr;

/**
 * Created by Chris Cavazos on 9/1/2018.
 */
public class Graph {
    ArrayList<Node> nodes = new ArrayList<>();
    boolean nodeClicked = false;
    Node active = null;

    public void init() {
        for(int i=0;i<10;i++){
            nodes.add(new Node(new Circle(new Vector2(scr).scl(rn.nextFloat(),rn.nextFloat()), 15)));

        }
        for(int i =0;i<15;i++) {
            int n=nodes.size();
            int a=rn.nextInt(n);
            int b=rn.nextInt(n);
            if(a==b){
                a=(a+1)%n;
            }
            nodes.get(a).connect(nodes.get(b));
        }

    }

    public void update(float dt) {
        boolean cl = false;
        for (Node n : nodes) {
            n.update(dt);
            if (n.clicked) {
                cl = true;
                active=n;
            }
            for(Node n1 : nodes){
                if(n1.shape.overlaps(n.shape) && n1 != n){
                    n.body.setPos(n.body.getPos().add(n1.body.getVector(100*dt,n.shape.center,n1.shape.center)));

                }
            }
        }
        nodeClicked = cl;
    }

    public void render(ShapeRendererExt sr) {
        for (Node n : nodes) {
            n.render(sr);
        }
    }
}
