package com.quadx.gravity.tanks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.quadx.gravity.shapes1_4.ShapeRendererExt;

/**
 * Created by Chris Cavazos on 10/7/2017.
 */
public class Tank {
    TankBullet bullet = new TankBullet();
    Vector2 vel = new Vector2();
    Rectangle shape = new Rectangle();
    Polygon arm = new Polygon();
    Color color = new Color();
    double angle = 0;
    float w = 30, h = 20;


    public Tank(Vector2 pos, Color c) {
        shape = new Rectangle(pos.x, pos.y, w, h);
        arm = new Polygon(new float[]{pos.x, pos.y, pos.x, pos.y + 10, pos.x + 20, pos.y + 10, pos.x + 20, pos.y});
        arm.translate(w / 2, h / 2);
        color = c;
        arm.setOrigin(getPos().x, getPos().y);
    }

    public void move(Vector2 v) {
        vel.set(v);
    }

    public Vector2 getVel() {
        return vel;
    }

    public void setVel(Vector2 vel) {
        this.vel = vel;
    }

    public Rectangle getShape() {
        return shape;
    }

    public void setShape(Rectangle shape) {
        this.shape = shape;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Vector2 getPos() {
        return shape.getPosition(new Vector2());
    }

    public void update(float dt) {
        groundCollision();
        bullet.update();
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            angle += .6f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            angle -= .6f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            shape.setPosition(getPos().add(-1, 0));
            arm.translate(-1, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            shape.setPosition(getPos().add(1, 0));
            arm.translate(1, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            bullet = new TankBullet(getPos().x + w / 2, getPos().y + h / 2, (float) angle, 10);
            bullet.setGravity(true);
        }/*
        if(Gdx.input.isKeyPressed(Input.Keys.E)){
            angle-=.2;
        }*/
        arm.setRotation((float) angle);
    }

    public void groundCollision() {
        int f = TerrainGenerator.getMaxHeightBetween(getPos().x, getPos().x + w);
       // out(f + " " + getPos().x + " " + getPos().y);
        float dy = (f + 1) - getPos().y;
        shape.setPosition(getPos().x, f + 1);
        arm.translate(0, dy);

    }

    public void render(ShapeRendererExt sr) {
        sr.set(ShapeRenderer.ShapeType.Filled);
        sr.setColor(color);
        sr.rect(shape);
        sr.set(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.WHITE);
        sr.polygon(arm.getTransformedVertices());
        bullet.render(sr);
    }
}
