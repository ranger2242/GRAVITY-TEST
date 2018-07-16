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

import java.util.ArrayList;

import static com.quadx.gravity.states.GravityState.rn;

/**
 * Created by Chris Cavazos on 9/29/2016.
 */
public class ControlGameState extends State{
    ShapeRenderer shapeR = new ShapeRenderer();
    public static Grid grid= new Grid();
    boolean debug=false;
    float dtSpawn=0;
    float dtFPS=0;
    Vector2 centerCamPos=new Vector2(Game.WIDTH/2,Game.HEIGHT/2);
    static float viewX;
    static float viewY;
    private float fps=0;
    static ArrayList<Integer> fpsList= new ArrayList<>();
    public ControlGameState(GameStateManager gsm) {
        super(gsm);
        a.initUnits();
        grid=new Grid();
    }
    Player a=new Player();
    Player b= new Player();
    void updateCamPosition() {
        Vector3 position = cam.position;
        float lerp = 0.2f;
        position.x += (centerCamPos.x- position.x) * lerp;
        position.y += (centerCamPos.y - position.y) * lerp;
        cam.position.set(position);
        cam.update();
        viewX = cam.position.x - cam.viewportWidth / 2;
        viewY = cam.position.y - cam.viewportHeight / 2;

    }
    @Override
    protected void handleInput() {
        float rate=20;
        if (Gdx.input.isKeyPressed(Input.Keys.W)){
            centerCamPos.set(centerCamPos.x,centerCamPos.y-rate);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)){
            centerCamPos.set(centerCamPos.x+rate,centerCamPos.y);
        }if (Gdx.input.isKeyPressed(Input.Keys.S)){
            centerCamPos.set(centerCamPos.x,centerCamPos.y+rate);
        }if (Gdx.input.isKeyPressed(Input.Keys.D)){
            centerCamPos.set(centerCamPos.x-rate,centerCamPos.y);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
            gsm.pop();
        if (Gdx.input.isKeyPressed(Input.Keys.F1)){
            debug=!debug;
        }

            if(dtSpawn>.2f) {
            if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
                a.unitList.add(new Unit(a, Game.WIDTH / 5, rn.nextInt((int) Game.HEIGHT)));
                Resource.Type r= Resource.Type.Food;
                if(rn.nextBoolean())
                    r= Resource.Type.Wood;
                a.unitList.get(a.unitList.size()-1).gather(r);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.E)) {
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
        if (dtFPS > .05) {
            fps = 1 / Gdx.graphics.getDeltaTime();
            fpsList.add((int) fps);
            if (fpsList.size() > 30) {
                fpsList.remove(0);
            }
            dtFPS = 0;
        } else {
            dtFPS += dt;
        }
        updateCamPosition();
        handleInput();
        dtSpawn+=dt;
        grid.clearDead(a.unitList);
        a.update();

    }



    @Override
    public void render(SpriteBatch sb) {
        Gdx.gl20.glClearColor(0,0,0,1);
        cam.setToOrtho(false);
        shapeR.setProjectionMatrix(cam.combined);
        sb.setProjectionMatrix(cam.combined);
        shapeR.begin(ShapeRenderer.ShapeType.Filled);
        for(Unit u : a.unitList){
            shapeR.setColor(u.getColor());
            shapeR.circle(viewX+u.getPosition().x,viewY+u.getPosition().y,5);
        }
        for(Resource r : grid.resources){
            shapeR.setColor(r.getColor());
            shapeR.circle(viewX+r.getPosition().x,viewY+r.getPosition().y,2);
        }
        for(Building bu: a.buildingList){
            shapeR.setColor(bu.getColor());
            shapeR.circle(viewX+bu.getPosition().x,viewY+bu.getPosition().y,bu.radius);
        }
        shapeR.end();

        int basex = (int) (30);
        int basey = (int) (30);
        //DRAW FPS COUNTER
        shapeR.begin(ShapeRenderer.ShapeType.Filled);
        shapeR.setColor(Color.BLACK);
        shapeR.rect(basex, basey, 100, 100);
        shapeR.end();
        shapeR.begin(ShapeRenderer.ShapeType.Line);
        shapeR.setColor(Color.WHITE);
        shapeR.rect(basex, basey, 100, 100);
        shapeR.line(basex, basey, basex + 100, basey);

        shapeR.setColor(Color.GREEN);
        int prev = 0;
        for (int i = 0; i < fpsList.size(); i++) {
            shapeR.line(basex + (i * 2), basey + prev, basex + ((i + 1) * 2), basey + fpsList.get(i));
            prev = fpsList.get(i);
        }
        double prev1=0;
        /*for (int i = 0; i < Tests.memUsageList.size(); i++) {
            shapeR.setColor(Color.PURPLE);
            shapeR.line(basex + (i * 2), (float) (basey +100* prev1), basex + ((i + 1) * 2), (float) (basey + 100*Tests.memUsageList.get(i)));
            prev1 = Tests.memUsageList.get(i);
        }*/
        shapeR.end();


        sb.begin();
        Game.getFont().draw(sb,(int)fps+" FPS",30,30);
        Game.getFont().draw(sb,"POP:"+a.getPop()+"/"+a.getPopMax(),30,Game.HEIGHT-60);
        Game.getFont().draw(sb,"WOOD:"+a.getWood(),30,Game.HEIGHT-30);
        Game.getFont().draw(sb,"FOOD:"+a.getFood(),30,Game.HEIGHT-90);

        if(debug) {
            for (Unit u : a.unitList) {
                Game.getFont().draw(sb, "" + u.getResourceIndex(), viewX+u.getPosition().x, viewY+u.getPosition().y + 10);
            }
        }
        sb.end();
    }

    @Override
    public void dispose() {

    }
}
