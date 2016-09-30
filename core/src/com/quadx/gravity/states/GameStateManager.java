package com.quadx.gravity.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.quadx.gravity.command.Command;

import java.util.Stack;

/**
 * Created by Brent on 6/26/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class GameStateManager {

    private final Stack<State> states;

    public GameStateManager(){
        states = new Stack<>();
    }

    public void push(State state){
        Command.cls=state.getClass();
        states.push(state);
    }
    public void clear(){
        states.clear();
    }
    public void pop(){
        states.pop().dispose();
        Command.cls=states.peek().getClass();
    }

    public void set(State state){
        states.pop().dispose();
        Command.cls=state.getClass();
        states.push(state);
    }

    public void update(float dt){
        states.peek().update(dt);
    }

    public void render(SpriteBatch sb){
        states.peek().render(sb);
    }
}
