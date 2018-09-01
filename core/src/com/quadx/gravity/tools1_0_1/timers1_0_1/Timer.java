package com.quadx.gravity.tools1_0_1.timers1_0_1;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by Chris Cavazos on 7/17/2016.
 */
public class Timer {
    NumberFormat formatter = new DecimalFormat("#0.0000000");
    protected long start=0;
    protected long end=0;
    protected String name="";
    public Timer(String n){
        name=n;
    }

    public Timer() {

    }

    public void start(){
        start=System.nanoTime();
    }
    public void end(){
        end=System.nanoTime();
    }
    public String getElapsed(){
        return formatter.format(getElapsedD());
    }
    public double getElapsedD(){
        return (end-start)/( 1000000000.0);
    }

    public String runtime(){

        return name+": "+getElapsed()+" sec";
    }


    public void print() {
        System.out.println(runtime());
    }
    public void printS() {
        System.out.println(getElapsed());
    }
}
