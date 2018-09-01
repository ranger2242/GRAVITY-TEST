package com.quadx.gravity.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.quadx.gravity.Game;
import com.quadx.gravity.control.*;
import com.quadx.gravity.shapes1_5_2.ShapeRendererExt;
import com.quadx.gravity.tools1_0_1.FPSModule;

import static com.badlogic.gdx.Input.Keys;
import static com.quadx.gravity.Game.scr;

/**
 * Created by Chris Cavazos on 9/29/2016.
 */
public class ControlGameState extends State {
    ShapeRendererExt sr = new ShapeRendererExt();
    FPSModule fpsModule = new FPSModule();
    public static Grid grid = new Grid();
    public static Vector2 view = new Vector2();
    Vector2 camC = new Vector2(scr);
    Player a = new Player();
    Player b = new Player();
    boolean debug = false;


    public ControlGameState(GameStateManager gsm) {
        super(gsm);
        Game.setScr(800,800);
        a.initUnits();
        grid = new Grid();
    }


    void updateCamPosition() {
        Vector3 position = cam.position;
        Vector2 p = new Vector2(position.x, position.y);
        Vector2 vp = new Vector2(cam.viewportWidth, cam.viewportHeight);
        float lerp = 0.2f;
        p = p.lerp(camC, lerp);
        cam.position.set(p.x, p.y, 0);
        view.set(new Vector2(p).sub(vp).scl(.5f));
        cam.update();
    }

    void camHandler() {
        float r = 20;
        float x = (pressed(Keys.A) ? r : 0) + (pressed(Keys.D) ? -r : 0);
        float y = (pressed(Keys.S) ? r : 0) + (pressed(Keys.W) ? -r : 0);
        camC.add(x, y);
    }

    Input in = Gdx.input;

    boolean pressed(int k) {
        return in.isKeyPressed(k);
    }

    @Override
    protected void handleInput() {
        camHandler();
        if (pressed(Keys.ESCAPE))
            gsm.pop();
        if (pressed(Keys.F1)) {
            debug = !debug;
        }
            if (pressed(Keys.Q)) {//spawn unit
                /*Vector2 p = new Vector2(scr.x * .2f, rn.nextInt((int) scr.y));
                a.unitList.add(new Unit(a, p));
                boolean b = rn.nextBoolean();
                Resource.Type r = b ? Food : Wood;
                a.unitList.get(a.unitList.size() - 1).gather(r);*/
            }
            if (pressed(Keys.E)) {
              /*  int index = rn.nextInt(a.unitList.size());
                if (index == 0)
                    index = 1;
                a.unitList.remove(index);*/
            }
    }

    @Override
    public void update(float dt) {
        updateCamPosition();
        handleInput();
        grid.clearSpentResources();
        a.update(dt);
        fpsModule.update(dt);
    }


    @Override
    public void render(SpriteBatch sb) {
        Gdx.gl20.glClearColor(0, 0, 0, 1);
        cam.setToOrtho(false);
        sr.setProjectionMatrix(cam.combined);
        sb.setProjectionMatrix(cam.combined);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        for (Unit u : a.unitList) {
            sr.setColor(u.getColor());
            sr.circle(view.x + u.getPosition().x, view.y + u.getPosition().y, 5);
            sr.setColor(Color.GREEN);
            sr.rect(u.getEnergyBar());
            sr.setColor(Color.RED);
            sr.rect(u.getLifeBar());
        }
        for (Resource r : grid.resources) {
            sr.setColor(r.getColor());
            sr.circle(view.x + r.getPosition().x, view.y + r.getPosition().y, 2);
        }
        for (Building bu : a.buildingList) {
            sr.setColor(bu.getColor());
            sr.circle(view.x + bu.getPosition().x, view.y + bu.getPosition().y, bu.radius);
        }
        sr.end();



        sb.begin();
        Game.getFont().draw(sb, "POP:" + a.getPop() + "/" + a.getPopMax(), 30, Game.HEIGHT - 60);
        Game.getFont().draw(sb, "WOOD:" + a.getWood(), 30, Game.HEIGHT - 30);
        Game.getFont().draw(sb, "FOOD:" + a.getFood(), 30, Game.HEIGHT - 90);

        if (debug) {
            for (Unit u : a.unitList) {
                Game.getFont().draw(sb, "" + u.getResourceIndex(), view.x + u.getPosition().x, view.y + u.getPosition().y + 10);
            }
        }
        sb.end();
        fpsModule.render(sb,sr,new Vector2(30,30));
    }

    @Override
    public void dispose() {

    }
}
