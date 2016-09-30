package com.quadx.gravity;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by Tom on 1/5/2016.
 */
@SuppressWarnings("ALL")
public class ColorConverter {
    private int redI=0;
    private int greenI=0;
    private int blueI=0;
    private float redF=0;
    private float greenF=0;
    private float blueF=0;
    java.awt.Color awt;
    private Color lbgdx;

    public ColorConverter(int r, int g, int b,int a){
        redI=r;
        greenI=g;
        blueI=b;
        convert(redI,blueI,greenI);
        lbgdx=new Color(redF,greenF,blueF,a);
        printValues();
    }
    public ColorConverter(float r, float g, float b, int a){
        redF=r;
        greenF=g;
        blueF=b;
        convert(redF,blueF,greenF);
        lbgdx=new Color(greenF,greenF,blueF,a);
        printValues();
    }
    private void printValues(){
        //System.out.println(redF+" "+greenF+" "+blueF);
    }
    public  Color getLIBGDXColor(){
        return lbgdx;
    }
    private void convert(int r, int g, int b){
        redF=(float)r/255;
        greenF=(float)g/255;
        blueF=(float)b/255;
    }
    private void convert(float r, float g, float b){
        redI=(int)r*255;
        greenF=(int)g*255;
        blueF=(int)b*255;

    }


}
