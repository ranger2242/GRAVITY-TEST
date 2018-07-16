package com.quadx.gravity.neuralnetwork;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.quadx.gravity.Game;
import com.quadx.gravity.shapes1_4.Circle;

import java.util.ArrayList;
import java.util.Random;

import static com.quadx.gravity.Game.HEIGHT;
import static com.quadx.gravity.Game.WIDTH;
import static com.quadx.gravity.states.NeuralState.mons;

/**
 * Created by Chris Cavazos on 2/7/2017.
 */
public class Food {
    static Random rn=new Random();
    public static ArrayList<Food> foods = new ArrayList<>();
    private Circle circle;
    private Vector2 pos=new Vector2();
    private int value=10;

    public static float dtSpawn =0;
    private static float trConsume=1f;

    public Food(Vector2 pos){
         circle= new Circle(pos, 2);
        this.pos=pos;
    }

    public void addValue(float v){
        value+=v;
    }
    public Vector2 getPos(){return pos;}
    public int getValue(){return value;}
    public Circle getCircle(){return circle;}
    void fixPos() {
        if (pos.x > WIDTH || pos.x < 0 || pos.y > HEIGHT ||pos.y < 0)
        {
            pos.x=rn.nextInt((int) Game.WIDTH);
            pos.y=rn.nextInt((int) Game.HEIGHT);
        }
        circle.center = pos;
    }
    public static void spawn(float dt){
        dtSpawn +=dt;
        if(dtSpawn>7f){
            int a= rn.nextInt(50)+10;
            for(int i=0;i<a;i++){
                Vector2 v= foods.get(rn.nextInt(foods.size())).getPos();
                Vector2 v2 = new Vector2(v.x,v.y);
                v2.x+=20* rn.nextGaussian();
                v2.y+=20* rn.nextGaussian();
                Food f=new Food(v2);
                f.fixPos();
                foods.add(f);
            }
            dtSpawn=0;
        }
    }
    public static void consume(int index){
        foods.get(index).addValue( (-2* Gdx.graphics.getDeltaTime()));
        if(foods.get(index).getValue()<=0)
            foods.remove(index);
    }
    public static void initFoodList(int n){
        foods.clear();
        for(int i=0;i<n;i++){
            foods.add(new Food(new Vector2(rn.nextInt((int) Game.WIDTH),rn.nextInt((int) Game.HEIGHT))));
        }
    }
    public static void updateFoodList(float dt) {
        checkIfMonsterOnFood(dt);
        for (int i = foods.size() - 1; i >= 0; i--) {
            Food f = foods.get(i);
            if (f.getValue() <= 0)
                foods.remove(i);
        }
    }
    static void checkIfMonsterOnFood(float dt) {
        for (Monster m : mons) {
            boolean b=false;
            int index=-1;
            for (Food f : foods) {
                if (m.getCircle().overlaps(f.getCircle())) {
                    b=true;
                    index=foods.indexOf(f);
                }
            }
            if(b)
               m.setFoodIndex(index);
            else {
                m.setFoodIndex(-1);
                m.setEating(false);
            }

            m.trigger(3, b);//trigger food sense
        }
    }
}
