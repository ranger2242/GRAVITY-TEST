package com.quadx.gravity.tools1_0_1.timers1_0_1;

/**
 * Created by Chris Cavazos on 5/23/2018.
 */
public class Oscillator extends Delta {
    boolean val=false;

    public Oscillator(float lim) {
        super(lim);
    }
    public void update(float dt){
        super.update(dt);
        if(isDone()){
            val=!val;
            reset();
        }
    }

    public boolean getVal(){
        return val;
    }
}
