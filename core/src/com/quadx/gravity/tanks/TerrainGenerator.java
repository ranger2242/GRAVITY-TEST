package com.quadx.gravity.tanks;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.quadx.gravity.EMath;
import com.quadx.gravity.shapes1_4.Circle;
import com.quadx.gravity.shapes1_4.Line;
import com.quadx.gravity.shapes1_4.ShapeRendererExt;

import java.util.ArrayList;

import static com.quadx.gravity.EMath.rn;
import static com.quadx.gravity.Game.HEIGHT;
import static com.quadx.gravity.Game.WIDTH;


/**
 * Created by Chris Cavazos on 10/7/2017.
 */
public class TerrainGenerator {
    public static ArrayList<Line> lines = new ArrayList<>();
    static float n = 300;
    static float sx = (int) (WIDTH / n);

    public TerrainGenerator() {

    }

    public void generate() {
        n=300;
        sx= (int) (WIDTH/(n/1.5));
        lines.clear();
        int init = rn.nextInt((int) (HEIGHT - 100));
        for (int i = 0; i < n; i++) {
            int y;
            if (i == 0) {
                y = (int) (init + 30 * rn.nextGaussian());
            } else
                y = (int) (lines.get(i - 1).b.y + 30 * rn.nextGaussian());
            int y2 = (int) (y + 30 * rn.nextGaussian());
            if (y > HEIGHT - 100) y = (int) (HEIGHT - 100);
            if (y2 > HEIGHT - 100) y2 = (int) (HEIGHT - 100);
            if (y < 0) y =   0;
            if (y2 < 0) y2 = 0;
            lines.add(new Line(new Vector2(i * sx, y), new Vector2((i + 1) * sx, y2)));
        }
        for (int j = 0; j < 500; j++)
            for (int i = 0; i < n - 1; i++) {
                Line l1 = lines.get(i);
                Line l2 = lines.get(i + 1);
                lines.get(i).b.y = EMath.average(new float[]{l1.a.y, l1.b.y, l2.a.y});
                lines.get(i + 1).a.y = EMath.average(new float[]{l1.b.y, l2.a.y, l2.b.y});
            }
        for (int i = 0; i < lines.size() - 1; i += 2) {
            Line l = new Line(lines.get(i).b, lines.get(i + 1).a);
            lines.add(i, l);
        }
        n=lines.size();
        sx= (int) (WIDTH/(n/1.5));
    }

    public void generateFlat(){
        n=300;
        sx= (int) (WIDTH/(n/1.5));
        lines.clear();
        int y=rn.nextInt((int) (HEIGHT-100))+1;
        for (int i = 0; i < n; i++) {
            lines.add(new Line(new Vector2(i * sx, y), new Vector2((i + 1) * sx, y)));
        }

        for (int i = 0; i < lines.size() - 1; i += 2) {
            Line l = new Line(lines.get(i).b, lines.get(i + 1).a);
            lines.add(i, l);
        }
        n=lines.size();
        sx= (int) (WIDTH/(n/1.5));
    }

    public void render(ShapeRendererExt sr) {
        sr.set(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.WHITE);
        for (int i = 0; i < lines.size(); i++) {
            sr.line(lines.get(i));
        }
    }
    public static void explodeAt(Circle c){
        for(Line l : lines) {
           if(c.intersects(l.a) && l.intersects(c)){

               float y1 = c.center.y - l.a.y;
               if(c.center.y>l.a.y && y1<c.radius) {
                   float x = l.a.x - c.center.x + c.radius;
                   float y = (.02f * x * x - 2 * x+c.radius/2)/2;

                   l.a.set(l.a.x, l.a.y + (y - y1));
               }
               float y1b = c.center.y - l.b.y;
               if(c.center.y>l.b.y && y1<c.radius) {
                   float x = l.b.x - c.center.x + c.radius;
                   float y = (.02f * x * x - 2 * x+c.radius/2)/2;

                   l.b.set(l.b.x, l.b.y + (y - y1));
               }
            }
        }
    }
    public static int getMaxHeightBetween(float x1, float x2) {
        if (x1 < x2 && sx !=0) {
            try {
                int i1 = (int) (x1 / sx);
                int i2 = (int) (x2 / sx);
                float max = 0;
                for (int i = i1; i < i2; i++) {
                    Line l = lines.get(i);
                    if (l.a.y > max)
                        max = l.a.y;
                    if (l.b.y > max)
                        max = l.b.y;
                }
                return (int) max;
            }catch (ArrayIndexOutOfBoundsException e){return 0;}
        } else
            return 0;
    }
    public void update(){
        for(int i=0;i<lines.size();i++){
            Line l = lines.get(i);
            if(l.length()<50){
                lines.remove(i);
                lines.addAll(l.subdivide( Math.round(l.length()/50) +1));
            }
        }
    }
}
