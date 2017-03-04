package com.quadx.gravity.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.quadx.gravity.Game;
import com.quadx.gravity.neuralnetwork.Food;
import com.quadx.gravity.neuralnetwork.Monster;
import com.quadx.gravity.neuralnetwork.Neuron;
import com.quadx.gravity.shapes.Circle;
import com.quadx.gravity.shapes.Line;
import com.quadx.gravity.shapes.ShapeRendererExt;

import java.util.ArrayList;
import java.util.Random;

import static com.quadx.gravity.Game.HEIGHT;
import static com.quadx.gravity.Game.WIDTH;

/**
 * Created by Chris Cavazos on 2/7/2017.
 */
public class NeuralState extends State{
    static Random rn = new Random();
    private ShapeRendererExt shapeR = new ShapeRendererExt();
    public static ArrayList<Monster> mons = new ArrayList<>();
    private static Vector2 mousePos= new Vector2(0,0);
    public static Circle mouseCircle = new Circle(mousePos,10);
    boolean leftPress=false;
    public static float soundActivation= 0;


    NeuralState(GameStateManager gsm) {
        super(gsm);
        Food.initFoodList(500);
        Monster m= new Monster(new Vector2(WIDTH / 3, Game.HEIGHT * 3 / 4),Color.BLUE);
        //Monster m1= new Monster(new Vector2(WIDTH*2 / 3, Game.HEIGHT * 3 / 4),Color.BLUE);
        float r=.7f;
        for(int i=0;i<20;i++){
            Monster m2 = new Monster(new Vector2(rn.nextInt((int) WIDTH),rn.nextInt((int) HEIGHT)),new Color(rn.nextFloat()*r,rn.nextFloat()*r,rn.nextFloat()*r,1));
            m2.setAngle(rn.nextInt(360));
            mons.add(m2);
        }
        m.setAngle(-30);
        //m1.setAngle(180);
        mons.add(m);
        //mons.add(m1);
    }

    @Override
    protected void handleInput() {
        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            leftPress=true;
            soundActivation=1;
        } else if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            leftPress=true;
            soundActivation=-1;
        }else{
            soundActivation=0;
            leftPress =false;
        }
    }

    @Override
    public void update(float dt) {
     handleInput();
        Food.spawn(dt);
        mousePos.x= Gdx.input.getX();
        mousePos.y= Game.HEIGHT- Gdx.input.getY();
        mouseCircle.center=mousePos;
        //sound
        if(leftPress){
            mouseCircle.radius+=200*dt;
        }
        else{
            mouseCircle.radius=10;
        }
        //
        for(Monster m : mons) {
            m.update(dt);
        }
        for(int i=mons.size()-1;i>=0;i--){
            Monster m=mons.get(i);
            if(m.getHunger()<=0){
                mons.remove(i);
            }
        }
        Food.updateFoodList(dt);
    }

    @Override
    public void render(SpriteBatch sb) {
        Gdx.gl20.glClearColor(0, 0f, 0, 1);
        cam.setToOrtho(false);

        shapeR.setProjectionMatrix(cam.combined);
        sb.setProjectionMatrix(cam.combined);
        shapeR.setColor(Color.WHITE);
//draw lines
        shapeR.begin(ShapeRenderer.ShapeType.Line);
        for(Monster m:mons){
            for(Neuron n: m.getInput()){
                Circle c=new Circle(n.getCircle().center,3);
                c.radius=3;
                shapeR.circle(c);
            }
        }
        shapeR.circle(mouseCircle);
        //draw sensors
        shapeR.setColor(Color.WHITE);
        for(Monster m:mons){
            ArrayList<Line> sensors=m.getSensors();
            for(Line l: sensors) {
                shapeR.line(l);
            }
            shapeR.circle(m.getProximCircle());
            shapeR.arc(m.getViewArc());
        }
        shapeR.end();
//draw filled
        shapeR.begin(ShapeRenderer.ShapeType.Filled);
        //draw Food
        shapeR.setColor(Color.GREEN);
        for(Food f:Food.foods){
            shapeR.circle(f.getCircle());
        }
        for(Monster m:mons){
            Color c=m.getColor();
            shapeR.setColor(c);
            for(Neuron n: m.getInput()) {
                if(n.isActive()) {
                    shapeR.circle(n.getCircle());
                }
            }
        }
        //drawPlayer
        for(Monster m:mons) {
            shapeR.setColor(m.getColor());
            shapeR.circle(m.getCircle());
        }
        shapeR.end();
        sb.begin();
        for (Monster m : mons) {
            ArrayList<String> list =m.getNeuronLables();
            Game.setFontSize(2);
            Vector2 v=m.getCircle().center;
            Game.getFont().draw(sb,m.getFoodIndex()+ "_"+m.getHunger(),v.x,v.y+20);
            for (int i = 0; i<list.size(); i++) {
                Vector2 p=m.getInput().get(0).getCircle().center;
                Game.getFont().draw(sb,list.get(i),(30+(10*mons.size()*2))+10,HEIGHT/2-(i*30)+10);
            }
        }
        sb.end();
    }

    @Override
    public void dispose() {

    }
}
