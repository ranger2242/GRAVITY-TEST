package com.quadx.gravity.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.quadx.gravity.ray.Source;
import com.quadx.gravity.shapes1_4.ShapeRendererExt;

import java.util.ArrayList;

import static com.quadx.gravity.EMath.rn;
import static com.quadx.gravity.Game.HEIGHT;
import static com.quadx.gravity.Game.WIDTH;
import static java.lang.Math.cos;

/**
 * Created by Chris Cavazos on 10/31/2017.
 */
public class RayState extends State {
    ShapeRendererExt sr = new ShapeRendererExt();
    Source a = new Source(new Vector2(WIDTH / 2, HEIGHT / 2), 120, 0);
    Source b = new Source(new Vector2(WIDTH / 2 - 200, HEIGHT / 2), 120, 0);
    Source b1 = new Source(new Vector2(WIDTH / 2 + 200, HEIGHT / 2), 120, 0);
    ArrayList<Source> sources = new ArrayList<>();

    ArrayList<Rectangle> objs = new ArrayList<>();
    Rectangle box = new Rectangle(WIDTH/2-50, HEIGHT/2-50, 100, 100);
    float angle = 0;

    public RayState(GameStateManager gsm) {
        super(gsm);
        //objs.add(box);
        a.setColor(Color.WHITE);
        caluclateSources();
        for (int i = 0; i < 10; i++) {
            //objs.add(new Rectangle(rn.nextInt((int) WIDTH), rn.nextInt((int) HEIGHT), 100, 100));

        }
    }

    float scale = 1;

    public void caluclateSources() {
        sources.clear();
        int n = 3;
        Color c = new Color(1, 1, 1, 1);
        for (int i = 0; i < n; i++) {
            // objs.add(new Rectangle(rn.nextInt((int) WIDTH),rn.nextInt((int) HEIGHT),100,100));
            float x = (float) ((WIDTH / 2) + scale * cos(Math.toRadians(angle + i * (360 / n))));
            float y = (float) ((HEIGHT / 2) + scale * Math.sin(Math.toRadians(angle + i * (360 / n))));
            Source s = new Source(new Vector2(x, y), 120, 0);
            c = new Color(rn.nextFloat(), rn.nextFloat(), rn.nextFloat(), 1);
            s.setColor(Color.WHITE);
            sources.add(s);
        }

    }

    @Override
    protected void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            scale += 2;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            scale -= 2;
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        angle += .1;
        caluclateSources();
        a.setPos(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
        a.collide(objs);
        for (Source s : sources) {
            s.collide(objs);
        }
  /*
        b.setPos(b.getPos().add(4,1));
        b.collide(objs);
        b1.collide(objs);*/
    }

    @Override
    public void render(SpriteBatch sb) {
        // cam.setToOrtho(false);

        sr.setProjectionMatrix(cam.combined);
        Gdx.gl20.glClearColor(0, 0, 0, 1);
        sr.begin(ShapeRenderer.ShapeType.Line);
        //a.render(sr);
        for (Source s : sources) {
            s.render(sr);
        }
        for (Rectangle r : objs) {
            sr.rect(r);
        }
        sr.end();
    }

    @Override
    public void dispose() {

    }
}
