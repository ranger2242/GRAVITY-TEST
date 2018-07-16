package com.quadx.gravity.command;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.quadx.gravity.states.MainMenuState;
import com.quadx.gravity.states.PongState;

import static com.quadx.gravity.states.PongState.p1;
import static com.quadx.gravity.states.PongState.p2;

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
        if (cls.equals(PongState.class)) {
            if(Gdx.input.isKeyPressed(Input.Keys.C)){
                PongState.ball.curve(p1.canCurve());
            }
            if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)){
                PongState.ball.curve(p2.canCurve());
            }
        }
    }
}
