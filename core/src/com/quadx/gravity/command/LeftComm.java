package com.quadx.gravity.command;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.quadx.gravity.states.PongState;


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
        if (cls.equals(PongState.class)) {
            if(Gdx.input.isKeyPressed(Input.Keys.A)){
                PongState.p1.setPos(new Vector2(-1,0));
            }
            if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
                PongState.p2.setPos(new Vector2(-1,0));

            }
        }
    }
}
