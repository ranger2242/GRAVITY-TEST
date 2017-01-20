package com.quadx.gravity;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Chris Cavazos on 9/16/2016.
 */
public class EMath {
    public static float pathag(Vector2 a, Vector2 b){
        return (float) Math.sqrt(Math.pow(a.x-b.x,2)+Math.pow(a.y-b.y,2));
    }
    public static float pathag(double x1, double y1, double x2, double y2){
        return (float) Math.sqrt(Math.pow(x1-x2,2)+Math.pow(y1-y2,2));
    }
    public static float pathag(double a, double b){
        return (float) Math.sqrt(Math.pow(a,2)+Math.pow(b,2));
    }
    public static float angle(Vector2 a, Vector2 b){
        float angx= b.x-a.x;
        float angy= b.y-a.y;
        boolean cox,coy;
        float initdeg=(float) Math.toDegrees(Math.atan(angy/angx));
        if(dx(b,a)>=0)
            cox=true;
        else
            cox=false;
        if(dy(b,a)>=0)
            coy=true;
        else
            coy=false;

        if(!cox && coy){
            initdeg+=180;
        }
        if(!cox && !coy){
            initdeg+=180;
        }
        if(cox && !coy){
            initdeg+=360;
        }
        return initdeg;
    }
    public static float dx(Vector2 a, Vector2 b){
        return b.x-a.x;
    }
    public static float dy(Vector2 a, Vector2 b){
        return b.y-a.y;
    }
    public static float arcL(float theta ,float r,float dl){
        float arc= (float) (((2*Math.PI*r)/360)*dl);
        return 0;
    }
    public static float average(float[] f){
        float sum=0;
        for(int i=0;i<f.length;i++){
            sum+= f[i];
        }
        return sum/f.length;
    }
}
