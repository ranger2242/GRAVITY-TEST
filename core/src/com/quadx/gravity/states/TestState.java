package com.quadx.gravity.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.quadx.gravity.shapes1_4.Circle;


/**
 * Created by Chris Cavazos on 9/29/2017.
 */
public class TestState extends State {
    ShapeRenderer sr = new ShapeRenderer();
    float width = 0;
    float height = 0;

    public TestState(GameStateManager gsm) {
        super(gsm);
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

    }

    @Override
    protected void handleInput() {

    }

    Circle circle = new Circle(new Vector2(400, 300), 70);

    @Override
    public void update(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            circle.center.add(1, 0);
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        Gdx.gl20.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.RED);
        sr.circle(circle.center.x, circle.center.y, circle.radius);
        sr.rect(50,50,100,670,Color.RED,Color.BLUE,Color.GRAY,Color.GREEN);
        sr.end();
    }

    @Override
    public void dispose() {

    }
}
