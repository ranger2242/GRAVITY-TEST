package com.quadx.gravity.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.quadx.gravity.Game;
import com.quadx.gravity.control.*;

import static com.quadx.gravity.states.GravityState.rn;

/**
 * Created by Chris Cavazos on 9/29/2016.
 */
public class ControlGameState extends State{
    ShapeRenderer shapeR = new ShapeRenderer();
    public static Grid grid= new Grid();
    boolean debug=false;
    float dtSpawn=0;
    ControlGameState(GameStateManager gsm) {
        super(gsm);
        a.initUnits();
        grid=new Grid();
    }
    Player a=new Player();
    Player b= new Player();

    @Override
    protected void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
            gsm.pop();
        if (Gdx.input.isKeyPressed(Input.Keys.F1)){
            debug=!debug;
        }

            if(dtSpawn>.5f) {
            if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
                a.unitList.add(new Unit(a, Game.WIDTH / 5, rn.nextInt((int) Game.HEIGHT)));
                Resource.Type r= Resource.Type.Food;
                if(rn.nextBoolean())
                    r= Resource.Type.Wood;
                a.unitList.get(a.unitList.size()-1).gather(r);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                int index = rn.nextInt(a.unitList.size());
                if (index == 0)
                    index = 1;
                a.unitList.remove(index);
            }
            dtSpawn=0;
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        dtSpawn+=dt;
        grid.clearDead(a.unitList);
        a.update();

    }

    @Override
    public void render(SpriteBatch sb) {
        Gdx.gl20.glClearColor(0,0,0,1);
        shapeR.begin(ShapeRenderer.ShapeType.Filled);
        for(Unit u : a.unitList){
            shapeR.setColor(u.getColor());
            shapeR.circle(u.getPosition().x,u.getPosition().y,5);
        }
        for(Resource r : grid.resources){
            shapeR.setColor(r.getColor());
            shapeR.circle(r.getPosition().x,r.getPosition().y,2);
        }
        for(Building bu: a.buildingList){
            shapeR.setColor(bu.getColor());
            shapeR.circle(bu.getPosition().x,bu.getPosition().y,bu.radius);
        }
        shapeR.end();
        sb.begin();
        Game.getFont().draw(sb,"POP:"+a.getPop()+"/"+a.getPopMax(),30,Game.HEIGHT-60);
        Game.getFont().draw(sb,"WOOD:"+a.getWood(),30,Game.HEIGHT-30);
        Game.getFont().draw(sb,"FOOD:"+a.getFood(),30,Game.HEIGHT-90);

        if(debug) {
            for (Unit u : a.unitList) {
                Game.getFont().draw(sb, "" + u.getResourceIndex(), u.getPosition().x, u.getPosition().y + 10);
            }
        }
        sb.end();
    }

    @Override
    public void dispose() {

    }
}
