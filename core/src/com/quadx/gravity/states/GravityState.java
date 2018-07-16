package com.quadx.gravity.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.quadx.gravity.ColorConverter;
import com.quadx.gravity.Game;
import com.quadx.gravity.physicsBody.Body;
import com.quadx.gravity.shapes1_4.Delta;
import com.quadx.gravity.shapes1_4.ShapeRendererExt;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Random;

import static com.badlogic.gdx.graphics.Color.BLACK;
import static com.quadx.gravity.Game.*;

/**
 * Created by Tom on 3/20/2016.
 */
@SuppressWarnings("ALL")
public class GravityState extends State {
    private static ShapeRendererExt sr = new ShapeRendererExt();
    public static ArrayList<Body> bodyList = new ArrayList<>();
    private static ArrayList<Body> tempBody = new ArrayList<>();
    private static ArrayList<Integer> indexToRemove = new ArrayList<>();
    public static float G = (float) (6.67 * Math.pow(10, -11));


    private int[] backx = new int[1000];
    private int[] backy = new int[1000];
    private int[] backr = new int[1000];
    private float twinkle = 0;
    private boolean trip = false;
    private double tempAng = 0;
    public static int factor = 2;
    public static Random rn = new Random();
    int viReduce = 4;

    public GravityState(GameStateManager gsm) {
        super(gsm);
        for (int i = 0; i < 1000; i++) {
            backr[i] = rn.nextInt(2);
            backx[i] = rn.nextInt((int) WIDTH);
            backy[i] = rn.nextInt((int) HEIGHT);
        }
        int bodyCount = 100;
        for (int i = 0; i < bodyCount; i++) {
            float mass = 1000;
            float ra = (float) ( Math.sqrt(mass / Math.PI) / 4);

            Body b = new Body(mass, ra);
            bodyList.add(b);
        }
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            gsm.pop();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            cam.zoom *= .5;
            //cam.viewportHeight*=.5;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            cam.zoom *= 1.5;
            //cam.viewportHeight*=1.5;
        }
        cam.update();
    }

    private void out(String s) {
        //System.out.println(s);
    }

    @Override
    public void update(float dt) {
        twinkle += dt;
        handleInput();
        out("------------------------------------");
        try {
            for (Body b : bodyList) {
                b.update();
                //System.out.print("#");
            }
           // System.out.println("@");

        } catch (ConcurrentModificationException e) {
        }

        processMetrics();
        updateFPSCounter(dt);
    }


    public static Vector2[] getForceComp(Body self) {
        int n = bodyList.size();
        Vector2[] arr = new Vector2[n];

        for(int i=0;i<n;i++) {
            Body b= bodyList.get(i);
            double r =self.dst(b);
            float r2 = b.getRadius() + self.getRadius();
            if(self.equals(b))
                arr[i]=new Vector2();
            else if(r<r2){
                arr[i]=new Vector2();
                self.setVel(self.getVel().scl(.9995f));
            }
            else
                arr[i] = self.getFVec(b);
        }
        //System.out.println(Arrays.toString(arr));
        return arr;
    }


    @Override
    public void render(SpriteBatch sb) {
        Gdx.gl20.glClearColor(0, 0, 0, 1);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        try {
            for (int i = 0; i < 1000; i++) {
                ColorConverter color = (new ColorConverter(rn.nextInt(50) + 70, rn.nextInt(50) + 70, rn.nextInt(50) + 70, 1));
                sr.setColor(color.getLIBGDXColor());
                if (rn.nextInt(100) < 98 && twinkle < 20) {

                    sr.rect(backx[i], backy[i], backr[i], backr[i]);
                    twinkle = 0;
                }
            }
            for (Body b : bodyList) {
                b.render(sr);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        } catch (NullPointerException er) {
        }

        sr.end();
        drawFPSModule(sb, sr, new Vector2(50, 50));
    }

    @Override
    public void dispose() {

    }


    ////========================================================================================
    public static ArrayList<Double> memUsageList = new ArrayList<>();
    static ArrayList<Integer> fpsList = new ArrayList<>();
    static float fps = 0;
    static boolean displayFPS = true;
    private static long currentMemUsage;
    private static long meterListMax = 60;
    static Delta dFPS = new Delta(3 * ft);

    public static void processMetrics() {
        Runtime runtime = Runtime.getRuntime();
        currentMemUsage = runtime.totalMemory() / (1024 * 1024);
        memUsageList.add((double) (currentMemUsage / runtime.maxMemory() / (1024 * 1024)));
        if (memUsageList.size() > meterListMax)
            memUsageList.remove(0);
    }

    public static void updateFPSList(float fps) {
        fpsList.add((int) fps);
        if (fpsList.size() > meterListMax) {
            fpsList.remove(0);
        }
    }

    static void updateFPSCounter(float dt) {
        dFPS.update(dt);
        if (dFPS.isDone()) {
            fps = 1 / dt;
            updateFPSList(fps);
            dFPS.reset();
        }
    }

    public static void drawFPSModule(SpriteBatch sb, ShapeRendererExt sr, Vector2 pos) {
        //fps meter module
        if (displayFPS) { //TODO optomize this to draw faster
            //DRAW FPS meter
            sr.begin(ShapeRenderer.ShapeType.Filled);
            sr.setColor(BLACK);
            sr.rect(pos.x, pos.y, 100, 100);
            sr.end();
            sr.begin(ShapeRenderer.ShapeType.Line);
            sr.setColor(Color.WHITE);
            sr.rect(pos.x, pos.y, 100, 100);
            sr.line(pos.x, pos.y, pos.x + 100, pos.y);

            sr.setColor(Color.GREEN);
            int prev = 0;
            for (int i = 0; i < fpsList.size(); i++) {
                sr.line(pos.x + (i * 2), pos.y + prev, pos.x + ((i + 1) * 2), pos.y + fpsList.get(i));
                prev = fpsList.get(i);
            }
            double prev1 = 0;
            for (int i = 0; i < memUsageList.size(); i++) {
                sr.setColor(Color.PURPLE);
                sr.line(pos.x + (i * 2), (float) (pos.y + 100 * prev1), pos.x + ((i + 1) * 2), (float) (pos.y + 100 * memUsageList.get(i)));
                prev1 = memUsageList.get(i);
            }
            sr.end();
            //draw fps counter
            sb.begin();
            if (displayFPS) {
                Game.setFontSize(1);
                Game.getFont().setColor(Color.WHITE);
                Game.getFont().draw(sb, (int) fps + " FPS", pos.x + 2, pos.y + 80);
                double x = 0;
                try {
                    x = memUsageList.get(memUsageList.size() - 1);
                } catch (Exception ignored) {
                }
                Game.getFont().draw(sb, (int) currentMemUsage + "MB " + Math.floor(x * 100) + "%", pos.x + 2, pos.y + 95);
            }
            sb.end();
        }
    }
}
