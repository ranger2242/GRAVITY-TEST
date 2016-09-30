package com.quadx.gravity.command;

import com.badlogic.gdx.Gdx;

/**
 * Created by Chris Cavazos on 8/8/2016.
 */
public abstract class Command {
    String name;
    int keyboard;

    Command(){
    }
    public static Class cls;
    public abstract void execute();
    public void changeKey(int k){
        keyboard=k;
    }
    public int getButtonK(){return keyboard;}

    boolean pressed(){
        boolean special=false;
        if(keyboard==59||keyboard==60){//check shift
            if(Gdx.input.isKeyPressed(59) || Gdx.input.isKeyPressed(60)){
                special=true;
            }
        }
        return (Gdx.input.isKeyPressed(keyboard) || special);
    }

    public String print(){
        String s=name;
        while (s.length()<40)
            s+=" ";
        s+=(char)(keyboard+36);
        while(s.length()<52)
            s+=" ";
        String contButtonName = "";
        s+= contButtonName;

        return s;
    }
}
