package com.quadx.gravity.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.quadx.gravity.Ball;
import com.quadx.gravity.Game;
import com.quadx.gravity.Paddle;

/**
 * Created by Chris Cavazos on 9/8/2016.
 */
@SuppressWarnings("ALL")
public class SoccerState extends State {
    private ShapeRenderer shapeR = new ShapeRenderer();
    private static Ball ball = new Ball();

    private Paddle a = new Paddle(Game.WIDTH/4, Game.HEIGHT/2 , Color.RED);
    private Paddle b = new Paddle((Game.WIDTH/4)*3, Game.HEIGHT/2 , Color.BLUE);

    public SoccerState(GameStateManager gsm) {
        super(gsm);

    }

    @Override
    protected void handleInput() {
        float factor=.42f;
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
            gsm.pop();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            a.vely+=factor;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            a.velx-=factor;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            a.vely-=factor;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            a.velx+=factor;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.I)){
            b.vely+=factor;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.J)){
            b.velx-=factor;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.K)){
            b.vely-=factor;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.L)){
            b.velx+=factor;
        }

    }

    @Override
    public void update(float dt) {
        handleInput();
        if( !(ball.x<0) && !(ball.x> Game.WIDTH) && !(ball.y<0) && !(ball.y> Game.HEIGHT)) {
            ball.x += ball.velx;
            ball.y += ball.vely;
        }
        if((ball.x<0+(ball.rad))){
            if(ball.y>Game.HEIGHT/3 && ball.y<(Game.HEIGHT/3)*2){
                b.points++;
                ball.reset();
            }else {
                ball.x = (0 + (ball.rad));
                ball.velx = -ball.velx;
            }
        }
        if((ball.x> Game.WIDTH-(ball.rad))){
            if(ball.y>Game.HEIGHT/3 && ball.y<(Game.HEIGHT/3)*2){
                a.points++;
                ball.reset();
            }else {
                ball.x = (Game.WIDTH - (ball.rad));
                ball.velx = -ball.velx;
            }

        }
        if((ball.y<0+(ball.rad))){
            ball.y=  (0+(ball.rad));
            ball.vely= -ball.vely;

        }
        if((ball.y> Game.HEIGHT-(ball.rad))){
            ball.y=  ( Game.HEIGHT-(ball.rad));
            ball.vely= -ball.vely;
        }
        float friction =.995f;
        ball.vely*=friction;
        ball.velx*=friction;
        a.move();
        b.move();
        a.distance(ball);
        b.distance(ball);
        a.distance2(b);
        b.distance2(a);
        a.setBounds(0,Game.WIDTH,Game.HEIGHT,0);
        b.setBounds(0,Game.WIDTH,Game.HEIGHT,0);
        //.setBounds(0+a.rad,(Game.WIDTH/2),Game.HEIGHT-a.rad,0+a.rad);
        //b.setBounds((Game.WIDTH/2),Game.WIDTH,Game.HEIGHT-b.rad,0+b.rad);
    }

    @Override
    public void render(SpriteBatch sb) {
        Gdx.gl20.glClearColor(0,0,0,1);
        shapeR.begin(ShapeRenderer.ShapeType.Filled);
        shapeR.setColor(Color.WHITE);
        shapeR.rect(Game.WIDTH/2-2,0,4,Game.HEIGHT);
        shapeR.circle(ball.x,ball.y,ball.rad);
        shapeR.end();

        shapeR.begin(ShapeRenderer.ShapeType.Line);
        shapeR.circle(Game.WIDTH/2,Game.HEIGHT/2,150);
        shapeR.setColor(a.c);
        shapeR.rect(0,Game.HEIGHT/3,50,(Game.HEIGHT/3));
        shapeR.circle(a.x,a.y,a.rad);
        shapeR.setColor(b.c);
        shapeR.rect(Game.WIDTH-50,Game.HEIGHT/3,Game.WIDTH,(Game.HEIGHT/3));

        shapeR.circle(b.x,b.y,b.rad);

        shapeR.end();
        sb.begin();
        Game.font.setColor(Color.WHITE);
        Game.font.draw(sb,""+a.points,Game.WIDTH/3,Game.HEIGHT-50);
        Game.font.draw(sb,""+b.points,(Game.WIDTH/3)*2,Game.HEIGHT-50);
        sb.end();
    }

    @Override
    public void dispose() {

    }
}
