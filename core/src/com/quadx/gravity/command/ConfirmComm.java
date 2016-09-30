package com.quadx.gravity.command;

import com.badlogic.gdx.Input;
import com.quadx.gravity.states.MainMenuState;

/**
 * Created by Chris Cavazos on 8/8/2016.
 */
public class ConfirmComm extends Command {
    public ConfirmComm(){
        name="Confirm";
        keyboard= Input.Keys.ENTER;
    }
    @Override
    public void execute() {
        if(pressed()){
            if (cls.equals(MainMenuState.class)) {
                MainMenuState.selectOption();
            }
        }
    }
}
