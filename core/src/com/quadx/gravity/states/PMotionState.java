package com.quadx.gravity.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.quadx.gravity.Game;
import com.quadx.gravity.sim.Line;

import java.util.ArrayList;

import static com.quadx.gravity.states.GravityState.rn;

/**
 * Created by Chris Cavazos on 7/24/2016.
 */
@SuppressWarnings("ALL")
public class PMotionState extends State {
    public static SpriteBatch sb = new SpriteBatch();
    private static ShapeRenderer sr=new ShapeRenderer();
    private static float dtMove=0;
    private static ArrayList<Line> leftCirc= new ArrayList<>();
    private static ArrayList<Line> rightCirc= new ArrayList<>();
    private int modxr=0;
    private boolean flip=false;

    public PMotionState(GameStateManager gsm) {
        super(gsm);
        for(int i=0;i<360;i+=15) {
            float fx = (float) ((Game.WIDTH / 6) * Math.cos(Math.toRadians(i+dtMove)) + Game.WIDTH / 3);
            float fy = (float) ((Game.WIDTH / 6) * Math.sin(Math.toRadians(i+dtMove)) + Game.HEIGHT / 2);
            float dx = (float) (((Game.WIDTH / 6) / 2) * Math.cos(Math.toRadians(i+dtMove)-Math.toRadians(5)) + Game.WIDTH / 3);
            float dy = (float) (((Game.WIDTH / 6) / 2) * Math.sin(Math.toRadians(i+dtMove)-Math.toRadians(5)) + Game.HEIGHT / 2);
            leftCirc.add(new Line(dx, dy, fx, fy));

        }
        for(int i=0;i<360;i+=15) {
            float fx = (float) ((Game.WIDTH / 6) * Math.cos(Math.toRadians(i-dtMove)) + (Game.WIDTH / 3)*2);
            float fy = (float) ((Game.WIDTH / 6) * Math.sin(Math.toRadians(i-dtMove)) + Game.HEIGHT / 2);
            float dx = (float) ((Game.WIDTH / 6) / 2 * Math.cos(Math.toRadians(i-dtMove)+Math.toRadians(5)) +  (Game.WIDTH / 3)*2);
            float dy = (float) ((Game.WIDTH / 6) / 2 * Math.sin(Math.toRadians(i-dtMove)+Math.toRadians(5)) + Game.HEIGHT / 2);
            rightCirc.add(new Line( dx, dy, fx, fy));
        }
    }

    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {
        int rate=2;
        if(modxr>Game.WIDTH/2)
            flip=!flip;
        if(modxr<-Game.WIDTH/2)
            flip=!flip;
        if(flip){
            modxr+=rate;
        }else
            modxr-=rate;
        dtMove+=1;
    }

    @Override
    public void render(SpriteBatch sb) {
        Gdx.gl20.glClearColor(0,0,0,1);
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.WHITE);
        float leftCirX=(Game.WIDTH / 3);
        float rightCirX=((Game.WIDTH)/3)*2;

        for(int i=0;i<360;i+=15) {
                float fx = (float) ((Game.WIDTH/6) * Math.cos(Math.toRadians(i+dtMove))*rn.nextFloat() + Game.WIDTH / 3+Game.WIDTH/12);
                float fy = (float) ((Game.WIDTH/6) * Math.sin(Math.toRadians(i+dtMove)) + Game.HEIGHT / 2);
                float dx = (float) (((Game.WIDTH/6) / 2)-modxr * Math.cos(Math.toRadians(i+dtMove)*rn.nextFloat()*rn.nextFloat()-Math.toRadians(5)) + Game.WIDTH / 3+Game.WIDTH/12);
                float dy = (float) ( (Game.WIDTH/6) / 2 *rn.nextFloat() * Math.sin(Math.toRadians(i+dtMove)-Math.toRadians(5)) + Game.HEIGHT / 2);
                sr.line(dx, dy, fx, fy);
        }
        for(int i=0;i<360;i+=15) {
            float fx = (float) ((Game.WIDTH/6) * Math.cos(Math.toRadians(i-dtMove))*rn.nextFloat() + (Game.WIDTH / 3)*2-Game.WIDTH/12);
            float fy = (float) ((Game.WIDTH/6) * Math.sin(Math.toRadians(i-dtMove)) + Game.HEIGHT / 2);
            float dx = (float) (((Game.WIDTH/6)/ 2)* Math.cos(Math.toRadians(i-dtMove)+Math.toRadians(5)) +  (Game.WIDTH / 3)*2-Game.WIDTH/12);
            float dy = (float) (((Game.WIDTH/6) / 2 * Math.sin(Math.toRadians(i-dtMove)+Math.toRadians(5)) + Game.HEIGHT / 2));
            sr.line(dx, dy, fx, fy);
        }
        float leftCirPX=leftCirX+Game.WIDTH/12;
        float rightCirPX=rightCirX-Game.WIDTH/12;
        sr.circle(leftCirPX, Game.HEIGHT/2,(Game.WIDTH/6));
        sr.circle(rightCirPX, Game.HEIGHT/2,(Game.WIDTH/6));
        sr.circle(leftCirPX, Game.HEIGHT/2,(Game.WIDTH/6)/2);
        sr.circle(rightCirPX, Game.HEIGHT/2,(Game.WIDTH/6)/2);
        sr.end();
    }

    @Override
    public void dispose() {

    }
}
