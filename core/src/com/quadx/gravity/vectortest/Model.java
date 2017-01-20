package com.quadx.gravity.vectortest;

import com.badlogic.gdx.math.Vector2;
import com.quadx.gravity.EMath;
import com.quadx.gravity.Game;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Chris Cavazos on 10/21/2016.
 */
public class Model {
    public ArrayList<Vector2> points= new ArrayList<>();
    public Vector2 center;
    Random rn = new Random();
    public Model(){
        int vert=rn.nextInt(35)+2;
        float x= Game.WIDTH/2;
        float y=Game.HEIGHT/2;
        points.add(new Vector2(x-10,y));
        points.add(new Vector2(x,y+20));
        points.add(new Vector2(x+10,y));
       // points.add(new Vector2(x,y));
       // points.add(new Vector2(x,y));
       // points.add(new Vector2(x,y));
      //  points.add(new Vector2(x,y));

        for( int i=0; i <vert; i++){

        }
    }
    public void calcCenter(){
        float sx=0;
        float sy=0;
        for(Vector2 v : points){
            sx+=v.x;
            sy+=v.y;
        }
        if(points.size() !=0) {
            sx = sx / points.size();
            sy = sy /points.size();
            center=new Vector2(sx,sy);
        }
    }
    public void translate(Vector2 disp){
        for(Vector2 v : points){
            v.x+=disp.x;
            v.y+=disp.y;
        }
    }
    public void rotate(double degAngle){
        for(Vector2 v : points){
            float tor= EMath.pathag(v,center);
            boolean cox,coy;
            float initdeg=EMath.angle(v,center);
            float deg= (float) (degAngle+initdeg);
            v.x= (float) (center.x+ tor*(Math.cos(Math.toRadians(deg))));
            v.y= (float) (center.y+ tor*(Math.sin(Math.toRadians(deg))));
        }
    }
    public void scale(double factor){
        for(Vector2 v :points){
            boolean cox,coy;
            float mag= EMath.pathag(v,center);
            float deg= EMath.angle(v,center);
            v.x= (float) (mag*factor* Math.cos(Math.toRadians(deg))+center.x);
            v.y =(float) (mag*factor* Math.sin(Math.toRadians(deg))+center.y);

        }
    }
}
