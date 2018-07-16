package com.quadx.gravity.command;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.quadx.gravity.states.PongState;


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
        if (cls.equals(PongState.class)) {
            if(Gdx.input.isKeyPressed(Input.Keys.D)){
                PongState.p1.setPos(new Vector2(1,0));
            }
            if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
                PongState.p2.setPos(new Vector2(1,0));

            }
        }
    }
}
