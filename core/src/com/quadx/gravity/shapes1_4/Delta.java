package com.quadx.gravity.shapes1_4;

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
        return dtPassed>=limit;
    }
    public void reset(){
        dtPassed=0;
    }


    public void finish() {
        dtPassed=Float.MAX_VALUE;
    }

    public float getLimit() {
        return limit;
    }
}