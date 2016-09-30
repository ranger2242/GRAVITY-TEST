package com.quadx.gravity.command;

import com.badlogic.gdx.Input;
import com.quadx.gravity.states.MainMenuState;

/**
 * Created by Chris Cavazos on 8/8/2016.
 */
public class UpComm extends Command {
    public UpComm(){
        name="Up";
        keyboard= Input.Keys.W;
    }
    @Override
    public void execute() {
        if(pressed()){
            if (cls.equals(MainMenuState.class)) {
                MainMenuState.incrementSelector();
            }
        }

    }
}
