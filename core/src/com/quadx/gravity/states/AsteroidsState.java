package com.quadx.gravity.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.quadx.gravity.Game;
import com.quadx.gravity.asteroids.Ship;
import com.quadx.gravity.sim.Body;

import java.util.ArrayList;

import static com.quadx.gravity.states.GravityState.rn;


/**
 * Created by Chris Cavazos on 9/13/2016.
 */
@SuppressWarnings("ALL")
public class AsteroidsState extends State {
    private Ship a = new Ship(Game.WIDTH/4, Game.HEIGHT/2 , Color.RED);
    private Ship b = new Ship((Game.WIDTH/4)*3, Game.HEIGHT/2 , Color.BLUE);

    private ShapeRenderer shapeR = new ShapeRenderer();
    private ArrayList<Vector2> stars = new ArrayList<>();
    ArrayList<Body> rocks=new ArrayList<>();

    public AsteroidsState(GameStateManager gsm) {
        super(gsm);
        for(int i=0;i<200;i++) {
            stars.add(new Vector2(rn.nextInt((int) Game.WIDTH), rn.nextInt((int) Game.HEIGHT)));
        }
        for(int i=0;i<1;i++) {
            rocks.add(new Body(0,0,rn.nextInt((int) Game.WIDTH),rn.nextInt((int) Game.HEIGHT)));
        }

    }

    @Override
    protected void handleInput() {
        float factor = .1f;
        float tf = 4;
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
            gsm.pop();
        if (Gdx.input.isKeyPressed(Input.Keys.W))
            a.forward(-factor);
        if (Gdx.input.isKeyPressed(Input.Keys.A))
            a.rotate(-tf);
        if (Gdx.input.isKeyPressed(Input.Keys.S))
            a.forward(factor);
        if (Gdx.input.isKeyPressed(Input.Keys.C))
            a.shoot();
        if (Gdx.input.isKeyPressed(Input.Keys.D))
            a.rotate(tf);
        if (Gdx.input.isKeyPressed(Input.Keys.I))
            b.forward(-factor);
        if (Gdx.input.isKeyPressed(Input.Keys.J))
            b.rotate(-tf);
        if (Gdx.input.isKeyPressed(Input.Keys.K))
            b.forward(factor);
        if (Gdx.input.isKeyPressed(Input.Keys.L))
            b.rotate(tf);
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE))
            b.shoot();
    }
    @Override
    public void update(float dt) {
        handleInput();
        a.update(dt);
        b.update(dt);
        if(a.colA(b.blist))
            b.score++;
        if(b.colA(a.blist))
            a.score++;
        for(Body body : rocks){
            body.colA(a.blist);
            body.colA(b.blist);
            a.colB(body);
            b.colB(body);
            body.setBounds(0, Game.WIDTH,Game.HEIGHT,0);
            body.move();
            body.updatePoints();
        }
        for(int i=rocks.size()-1;i>-1;i--){
            if(rocks.get(i).hit)
                rocks.remove(i);
        }
        Body.dtSpawn+=dt;
        if(Body.dtSpawn>3){
            if(rocks.size()<10){
                rocks.add(new Body(0,0,rn.nextInt((int) Game.WIDTH),rn.nextInt((int) Game.HEIGHT)));
            }
            Body.dtSpawn=0;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        Gdx.gl20.glClearColor(0,0,0,1);
        shapeR.begin(ShapeRenderer.ShapeType.Line);
        a.draw(shapeR);
        b.draw(shapeR);
        for(Vector2 v: stars){
            float f=rn.nextFloat();
            shapeR.setColor(f,f,f,1);
            shapeR.circle(v.x,v.y,1);
        }
        shapeR.setColor(Color.GRAY);
        for(Body body: rocks){
            shapeR.polygon(body.getPoints());
        }
        shapeR.end();
        sb.begin();
        Game.font.setColor(Color.WHITE);
        Game.font.draw(sb,""+a.score,Game.WIDTH/3,Game.HEIGHT-50);
        Game.font.draw(sb,""+b.score,(Game.WIDTH/3)*2,Game.HEIGHT-50);
        sb.end();
    }
    @Override
    public void dispose() {

    }
}
