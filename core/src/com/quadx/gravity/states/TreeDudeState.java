package com.quadx.gravity.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.quadx.gravity.Game;
import com.quadx.gravity.shapes1_4.Delta;
import com.quadx.gravity.shapes1_4.ShapeRendererExt;
import com.quadx.gravity.treedude.Dude;
import com.quadx.gravity.treedude.Tree;

import static com.quadx.gravity.Game.ft;

/**
 * Created by Chris Cavazos on 7/30/2018.
 */
public class TreeDudeState extends State {
    public static final int W = 100;
    ShapeRendererExt sr = new ShapeRendererExt();
    int time=100;
    Delta dTime= new Delta(10*ft);
    Dude d = new Dude();
    Tree t = new Tree();
    boolean death = false;
    String high = "0";

    public TreeDudeState(GameStateManager gsm) {
        super(gsm);
        sr.setAutoShapeType(true);

    }

    @Override
    protected void handleInput() {

    }
    public void death(){
        death=true;
        if(Integer.parseInt(d.getChops()) > Integer.parseInt(high)){
            high=d.getChops();
        }
    }

    @Override
    public void update(float dt) {
        dTime.update(dt);
        if(d.getPos()==0 && t.getLeft()==1){
            death();
        }
        if(d.getPos()==1 && t.getRight()==1){
            death();
        }
        if(dTime.isDone()){
            time--;
            dTime.reset();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            t=new Tree();
            d=new Dude();
            death=false;
        }
        if(!death) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
                d.setPos(0);
                t.chop();
                time++;
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
                d.setPos(1);
                t.chop();
                time++;

            }
        }

    }

    @Override
    public void render(SpriteBatch sb) {
        Gdx.gl20.glClearColor(0, 0, 0, 1);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        t.render(sr);
        d.render(sr);
        drawTimer(sr);
        sr.end();
        sb.begin();
        sb.setColor(Color.WHITE);

        Game.setFontSize(5);
        BitmapFont f = Game.getFont();
        f.draw(sb,"SCORE: "+d.getChops(),400,300);
        f.draw(sb,"HIGH: "+high,400,280);

        if(death) {
            f.draw(sb, "DEAD", 400, 260);

        }

        sb.end();
    }

    private void drawTimer(ShapeRendererExt sr) {
        sr.setColor(Color.WHITE);
        sr.rect(400,320,200*(time/100f),20);
        sr.set(ShapeRenderer.ShapeType.Line);
        sr.rect(400,320,200,20);
    }

    @Override
    public void dispose() {

    }
}
