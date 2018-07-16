package com.quadx.gravity.command;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.quadx.gravity.states.MainMenuState;
import com.quadx.gravity.states.PongState;

/**
 * Created by Chris Cavazos on 8/8/2016.
 */
public class DownComm extends Command {
    public DownComm(){
        name="Down";
        keyboard= Input.Keys.S;
    }
    @Override
    public void execute() {
        if(pressed()){
            if (cls.equals(MainMenuState.class)) {
                MainMenuState.decrementSelector();
            }
        }
        if (cls.equals(PongState.class)) {
            if(Gdx.input.isKeyPressed(Input.Keys.S)){
                PongState.p1.setPos(new Vector2(0,-1));
            }
            if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
                PongState.p2.setPos(new Vector2(0,-1));

            }
        }
    }
}
