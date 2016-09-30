package com.quadx.gravity.command;

import com.badlogic.gdx.Input;


/**
 * Created by Chris Cavazos on 8/8/2016.
 */
public class LeftComm extends Command {
    public LeftComm(){
        name="Left";
        keyboard= Input.Keys.A;

    }
    @Override
    public void execute() {
        if(pressed()){

        }
    }
}
