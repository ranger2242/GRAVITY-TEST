package com.quadx.gravity.tools1_0_1.timers1_0_1;

/**
 * Created by Chris Cavazos on 9/29/2017.
 */
public class Delta {
    private float dtPassed = 0;
    private float limit;
    public Delta(float lim) {
        limit=lim;
    }
    public void update(float dt) {
        if(dtPassed<=limit) {
            dtPassed += dt;
        }
    }
    public boolean isDone(){
        return dtPassed>limit;
    }
    public void reset(){
        dtPassed=0;
    }

    public float percent(){
        if(limit!=0)
            return dtPassed/limit;
        else return 0;
    }

    public void finish() {
        dtPassed=Float.MAX_VALUE;
    }

    public float getLimit() {
        return limit;
    }

    public boolean triggerUpdate(float dt, boolean trigger) {
        boolean b=trigger;
        if(b){
            update(dt);
            if(isDone()){
                b=false;
                reset();
            }
        }
        return b;
    }
}

