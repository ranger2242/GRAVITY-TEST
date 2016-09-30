package com.quadx.gravity.command;

import com.badlogic.gdx.Input;

/**
 * Created by Chris Cavazos on 8/12/2016.
 */
public class BackComm extends Command {
    public BackComm(){
        name="Back";
        keyboard= Input.Keys.TAB;
    }
    @Override
    public void execute() {
        if(pressed()) {

        }
    }
}
