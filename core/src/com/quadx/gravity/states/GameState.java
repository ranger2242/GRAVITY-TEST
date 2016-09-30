package com.quadx.gravity.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.quadx.gravity.Game;
import com.quadx.gravity.Roach;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by range_000 on 1/15/2016.
 */
@SuppressWarnings("ALL")
public class GameState extends State {
    private DecimalFormat df = new DecimalFormat("0.00");
    private static ShapeRenderer shapeR = new ShapeRenderer();
    private static ArrayList<String> output = new ArrayList<String>();
    private Roach roach = new Roach();
    private int mouseX=0;
    private int mouseY=0;

    public GameState(GameStateManager gsm){

        super(gsm);
    }

    @Override
    protected void handleInput() {
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
            gsm.pop();
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        setOutput();
        mouseX= Gdx.input.getX();
        mouseY= (int) (Game.HEIGHT- Gdx.input.getY());
        cam.position.set(0,0,0);
        roach.setMousePos(mouseX,mouseY);
        roach.calculateBehavior();
    }

    @Override
    public void render(SpriteBatch sb) {
        drawTextOutput(sb);

        shapeR.begin(ShapeRenderer.ShapeType.Filled);
        shapeR.setColor(Color.BLACK);
        shapeR.rect(roach.getPX(),roach.getPY(),10,10);
        shapeR.end();
    }

    private void setOutput(){
        output.clear();
        output.add("Angle: "+ df.format(roach.getAngle()));
        output.add("Target Angle: "+df.format(roach.getTargetAngle()));
        output.add("Direction x:"+df.format(roach.getxVector()) +" y:"+df.format(roach.getyVector()) );
        char degree = 0x00B0;
        output.add("Velocity "+df.format(roach.getVelocity())+" @"+ df.format(roach.getAngle())+ degree +"  Vx:"+df.format(roach.getxSpd()) +" Vy:"+df.format(roach.getySpd()) );
        output.add("Mouse x:"+mouseX+" y:"+mouseY);
        output.add("bot right"+(roach.getPX()+roach.getWidth())+" "+roach.getPY());
        output.add("bot left"+roach.getPX()+" "+roach.getPY());
        output.add("Energy:"+roach.getEnergy());
    }
    private void drawTextOutput(SpriteBatch sb){
        sb.begin();
        for(int i =0;i<output.size();i++){
            Game.font.draw(sb,output.get(i),100,Game.HEIGHT-30-(i*20));
        }
        sb.end();
    }

    @Override
    public void dispose() {

    }
}
