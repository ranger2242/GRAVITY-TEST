package com.quadx.gravity.command;

import com.badlogic.gdx.Input;


/**
 * Created by Chris Cavazos on 8/8/2016.
 */
public class RightComm extends Command {
    public RightComm(){
        name="Right";
        keyboard= Input.Keys.D;

    }
    @Override
    public void execute() {
        if (pressed()) {

        }
    }
}
