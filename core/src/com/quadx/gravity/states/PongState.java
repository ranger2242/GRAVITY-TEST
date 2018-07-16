package com.quadx.gravity.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.quadx.gravity.Game;
import com.quadx.gravity.pong.Ball;
import com.quadx.gravity.pong.Player;
import com.quadx.gravity.shapes1_4.Circle;
import com.quadx.gravity.shapes1_4.ShapeRendererExt;

import static com.quadx.gravity.Game.HEIGHT;
import static com.quadx.gravity.Game.WIDTH;

/**
 * Created by Chris Cavazos on 9/20/2017.
 */
public class PongState extends State {
    ShapeRendererExt sr = new ShapeRendererExt();
    public static Vector2 scl = new Vector2(.75f,5f/7f);
    public static Vector2 mid = new Vector2(WIDTH / 2, HEIGHT / 2);
    public static Vector2 sep = new Vector2(170,100);

    public static Player p1=new Player(0);
    public static Player p2 = new Player(1);
    public static Ball ball = new Ball();

    public PongState(GameStateManager gsm) {
        super(gsm);
        sr.setAutoShapeType(true);
    }

    @Override
    protected void handleInput() {
        for (com.quadx.gravity.command.Command c : Game.commandList) {
            c.execute();
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        ball.update(dt);
        p1.update(dt);
        p2.update(dt);
    }

    @Override
    public void render(SpriteBatch sb) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        sr.begin(ShapeRenderer.ShapeType.Line);
        srDrawBackground();
        srDrawPlayer(p2);
        srDrawBall();
        srDrawPlayer(p1);

        sr.end();
        sb.begin();
        Game.font.setColor(Color.BLUE);
        Game.font.draw(sb,""+p1.points,Game.WIDTH/3,Game.HEIGHT-80);
        Game.font.setColor(Color.RED);

        Game.font.draw(sb,""+p2.points,(Game.WIDTH/3)*2,Game.HEIGHT-80);
        sb.end();

    }
    void srDrawPlayer(Player p){
        sr.setColor(p.getColor());
        sr.rect(p.getShape());
        sr.set(ShapeRenderer.ShapeType.Filled);
        Color c= sr.getColor();
        c.a=.2f;
        sr.setColor(c);
        sr.rect(p.getShape());
        sr.set(ShapeRenderer.ShapeType.Line);

    }


    public void srDrawBackground() {
        int xs = 170;
        int ys = 100;
        sr.set(ShapeRenderer.ShapeType.Filled);
        sr.setColor(.2f,.2f,.2f,.2f);
        sr.rect(0,0,xs,HEIGHT);
        sr.rect(xs,0,WIDTH-(xs*2),ys);
        sr.rect(xs,HEIGHT-ys,WIDTH-(xs*2),ys);
        sr.rect(WIDTH-xs,0,xs,HEIGHT);

        sr.set(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.WHITE);
        sr.line(0, 0, xs, ys);
        sr.line(0, HEIGHT, xs, HEIGHT - ys);
        sr.line(WIDTH - xs, ys, WIDTH, 0);
        sr.line(WIDTH - xs, HEIGHT - ys, WIDTH, HEIGHT);
        sr.rect(xs, ys, WIDTH - (2 * xs), HEIGHT - (2 * ys));
    }

    float c = 0;
    boolean flip = true;

    public void srDrawBall() {
        Circle cir = ball.getShape();
        sr.setColor(new Color(.3f,.3f,.3f,.5f));
        sr.line(ball.getVLine());
        sr.line(ball.getHLine());

        sr.setColor(ball.getColor());
        sr.circle(cir);
        float wr= cir.radius;
        if (c > 1 || c < 0) {
            flip = !flip;

        }
        float shift = (c * cir.radius);
        if (flip) {
            c += .005;
            wr += shift;
        } else {
            c -= .005;
            wr -= shift;
        }
        sr.ellipse(cir.center.x - (wr / 2), cir.center.y - cir.radius, wr, cir.radius * 2);
        sr.ellipse(cir.center.x -cir.radius , cir.center.y  - (wr / 2), cir.radius*2, wr);
        sr.set(ShapeRenderer.ShapeType.Filled);
        Color c= sr.getColor();
        c.a=.2f;
        sr.setColor(c);
        sr.circle(cir);
        sr.ellipse(cir.center.x - (wr / 2), cir.center.y - cir.radius, wr, cir.radius * 2);
        sr.ellipse(cir.center.x -cir.radius , cir.center.y  - (wr / 2), cir.radius*2, wr);
        sr.set(ShapeRenderer.ShapeType.Line);
    }

    @Override
    public void dispose() {

    }
}
