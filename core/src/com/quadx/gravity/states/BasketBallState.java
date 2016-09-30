package com.quadx.gravity.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.quadx.gravity.Ball;
import com.quadx.gravity.Game;
import com.quadx.gravity.Paddle;

import static com.quadx.gravity.Game.HEIGHT;
import static com.quadx.gravity.Game.WIDTH;

/**
 * Created by Chris Cavazos on 9/10/2016.
 */
@SuppressWarnings("ALL")
public class BasketBallState extends State {
    private ShapeRenderer shapeR =new ShapeRenderer();
    private Paddle a = new Paddle(Game.WIDTH/4,40 , Color.RED);
    private Paddle b = new Paddle((Game.WIDTH/4)*3,40, Color.BLUE);
    private Ball ball = new Ball();

    public BasketBallState(GameStateManager gsm) {
        super(gsm);


    }

    @Override
    protected void handleInput() {
        float factor=.42f;
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
            gsm.pop();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            if(a.jump) {
                a.vely = 15;
                a.jump=false;
            }
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
        if(Gdx.input.isKeyPressed(Input.Keys.I)) {
            if (b.jump) {
                b.vely = 15;
                b.jump = false;
            }
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
        if( !(ball.x<0) && !(ball.x> Game.WIDTH) && !(ball.y<40) && !(ball.y> Game.HEIGHT)) {
            ball.x += ball.velx;
            ball.y += ball.vely;
        }
        if((ball.x<120)){
            if(ball.y>200 && ball.y<220 && ball.vely<0){
                b.points++;
                ball.reset();
            }else if(ball.x<ball.rad){
                ball.x = (0 + (ball.rad));
                ball.velx = -ball.velx;
            }
        }
        if((ball.x> Game.WIDTH-120)){
            if(ball.y>200 && ball.y<220 && ball.vely<0){
                a.points++;
                ball.reset();
            }else if(ball.x> Game.WIDTH-ball.rad) {
                ball.x = (Game.WIDTH - (ball.rad));
                ball.velx = -ball.velx;
            }

        }
            if((ball.y<40+(ball.rad))){
            ball.y=  (40+(ball.rad));
            ball.vely= -ball.vely;

        }
        if((ball.y> Game.HEIGHT-(ball.rad))){
            ball.y=  ( Game.HEIGHT-(ball.rad));
            ball.vely= -ball.vely;
        }
        float grav= -.5f;
        float fric=.99f;
        ball.vely+=grav;
        ball.vely*=fric;
        //ball.velx*=fric;
        if(a.y<=42){
            a.jump=true;
        }
        if(b.y<=42){
            b.jump=true;
        }
        a.vely+=grav;
        b.vely+=grav;
        a.setBounds(0,WIDTH, HEIGHT,40);
        b.setBounds(0,WIDTH, HEIGHT,40);
        a.move();
        b.move();
        a.distance(ball);
        b.distance(ball);
    }

    @Override
    public void render(SpriteBatch sb) {
        Gdx.gl20.glClearColor(0,0,0,1);
        shapeR.begin(ShapeRenderer.ShapeType.Line);
        shapeR.setColor(Color.WHITE);
        shapeR.circle(ball.x,ball.y,ball.rad);
        shapeR.line(0,40, WIDTH,40);
        shapeR.setColor (a.c);
        shapeR.rect(0,200,120,20);
        shapeR.arc(a.x,a.y,a.rad,0,180);
        shapeR.setColor(b.c);
        shapeR.rect( WIDTH-120, 200,120,20);
        shapeR.arc(b.x,b.y,b.rad,0,180);
        shapeR.end();
        sb.begin();
        Game.font.setColor(Color.WHITE);
        Game.font.draw(sb,""+a.points,Game.WIDTH/3,Game.HEIGHT-80);
        Game.font.draw(sb,""+b.points,(Game.WIDTH/3)*2,Game.HEIGHT-80);
        sb.end();
    }

    @Override
    public void dispose() {

    }
}
