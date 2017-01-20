package com.quadx.gravity.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.quadx.gravity.EMath;
import com.quadx.gravity.Game;
import com.quadx.gravity.heightmap.Cell;
import com.quadx.gravity.heightmap.Matrix;
import com.quadx.gravity.heightmap.Noise;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by Chris Cavazos on 1/16/2017.
 */
public class HeightMapState extends State {
    static float viewX;
    static float viewY;
    float dtf=0;
    int n = 10;
    int cellw=4;
    int res = 200;
    int[][] grid = new int[res][res];
    private ShapeRenderer shapeR = new ShapeRenderer();
    ArrayList<Color> colors = new ArrayList<>();
    Vector2 centerCamPos = new Vector2(Game.WIDTH / 2, Game.HEIGHT / 2);
    Cell[][] cells=new Cell[res][res];
    Random rn = new Random();
    Vector2 playerpos=new Vector2(100,100);


    HeightMapState(GameStateManager gsm) {
        super(gsm);
        initGrid();
    }
    float[][] addToMap(float[][] f,float[][]f2) {
        float[][] f3=new float[res][res];
        for (int i = 0; i < res; i++) {
            for (int j = 0; j < res; j++) {
                f3[i][j]=(f[i][j]+f2[i][j])/2;
            }
        }
        return f3;
    }
    float scanAverageHeight(float[][] f) {
        float a=0;
        for (int i = 0; i < res; i++) {
            for (int j = 0; j < res; j++) {
                a+=f[i][j];
            }
        }
        a/=(res*res);
        return a;
    }
    protected void handleInput() {
        float rate = 20;
        if(Gdx.input.isKeyPressed(Input.Keys.F1)){
            initGrid();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.P)){
            if (dtf > .15) {
                if (cellw > 4) {
                    cellw--;
                    calcCorners();
                dtf=0;
                   }
            }

        }
        if(Gdx.input.isKeyPressed(Input.Keys.O)) {
            if (dtf > .15) {
                if (cellw < 40) {
                    cellw++;
                    calcCorners();
                    dtf = 0;
                }
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            if(dtf>.15) {

                Matrix<Integer> rotator = new Matrix<>(Integer.class);
                grid = rotator.rotateMatrix(grid, res, true);
                playerpos.set(res- playerpos.y-1,playerpos.x);
                calcCorners();
                dtf=0;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            if(dtf>.15) {
                Matrix<Integer> rotator = new Matrix<>(Integer.class);
                grid = rotator.rotateMatrix(grid, res, false);
                playerpos.set(playerpos.y,res- playerpos.x-1);

                calcCorners();
                dtf = 0;
            }

        }

            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            centerCamPos.set(centerCamPos.x, centerCamPos.y - rate);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            centerCamPos.set(centerCamPos.x - rate, centerCamPos.y);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            centerCamPos.set(centerCamPos.x, centerCamPos.y + rate);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            centerCamPos.set(centerCamPos.x + rate, centerCamPos.y);
        }
        if(dtf>.1) {

            if (Gdx.input.isKeyPressed(Input.Keys.I)) {
                if (playerpos.y > 0)
                    playerpos.y -= 1;
                dtf=0;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.J)) {
                if (playerpos.x > 0)
                    playerpos.x -= 1;
                dtf=0;

            }
            if (Gdx.input.isKeyPressed(Input.Keys.K)) {
                if (playerpos.y < res - 1)
                    playerpos.y += 1;
                dtf=0;

            }
            if (Gdx.input.isKeyPressed(Input.Keys.L)) {
                if (playerpos.x < res - 1)
                    playerpos.x += 1;
                dtf=0;

            }
        }
    }
    public void update(float dt) {
        dtf+=dt;
            handleInput();

        updateCamPosition();

    }
    public void render(SpriteBatch sb) {
        Gdx.gl20.glClearColor(0, .4f, 0, 1);
        // cam.setToOrtho(false);

        shapeR.setProjectionMatrix(cam.combined);
        sb.setProjectionMatrix(cam.combined);
        Game.setFontSize(0);

        shapeR.begin(ShapeRenderer.ShapeType.Filled);

        for (int i = 0; i < res; i++) {
            for (int j = 0; j < res; j++) {
                float[] f=cells[i][j].getCorners().getVertices();
                shapeR.setColor(colors.get(Math.round( f[10])));
                shapeR.triangle(f[0],f[1],f[2],f[3],f[8],f[9]);
                shapeR.setColor(colors.get(Math.round( f[11])));
                shapeR.triangle(f[2],f[3],f[4],f[5],f[8],f[9]);
                shapeR.setColor(colors.get(Math.round( f[12])));
                shapeR.triangle(f[4],f[5],f[6],f[7],f[8],f[9]);
                shapeR.setColor(colors.get(Math.round( f[13])));
                shapeR.triangle(f[6],f[7],f[0],f[1],f[8],f[9]);
            }
        }
        shapeR.end();
        shapeR.begin(ShapeRenderer.ShapeType.Line);
        for (int i = 0; i < res; i++) {
            for (int j = 0; j < res; j++) {
                shapeR.setColor(Color.DARK_GRAY);
                shapeR.getColor().a=.5f;
                float[] fx=new float[8];
                for(int x=0;x<8;x++){
                    fx[x]=cells[i][j].getCorners().getVertices()[x];
                }
                shapeR.polygon(fx);
            }
        }
        shapeR.end();
        shapeR.begin(ShapeRenderer.ShapeType.Filled);
        float[] f= cells[(int) playerpos.x][(int) playerpos.y].getCorners().getVertices();
        shapeR.setColor(Color.BLUE);
        shapeR.rect(f[8],f[9],cellw,cellw);
        shapeR.end();

        //sb.end();
    }
    public void dispose() {

    }
    void averageMap() {
        for (int i = 0; i < res; i++) {
            for (int j = 0; j < res; j++) {
                int count = 0;
                float tot = 0;
                try {
                    tot += grid[i][j] + grid[i + 1][j + 1] + grid[i + 1][j] + grid[i + 1][j - 1] + grid[i - 1][j + 1] + grid[i - 1][j] + grid[i - 1][j - 1] + grid[i][j + 1] + grid[i][j - 1];
                    float f = tot / 9;
                    grid[i][j] = Math.round(f);
                } catch (ArrayIndexOutOfBoundsException e) {

                }
                //count++;


                // if(grid[i][j]<-5)grid[i][j]=5;
                // if(grid[i][j]>5)grid[i][j]=-5;
            }
        }
    }
    void initGrid() {
        float[][] f3=new float[res][res];
        float check =0;
        for(int i=0;check<.1f ;i++) {
            Noise noisemap = new Noise(null, .3f, 200, 200);
            noisemap.initialise();
            float[][] f = noisemap.getGrid();
            f3 = addToMap(f3,f);
            check=scanAverageHeight(f3);

        }
        for (int i = 0; i < res; i++) {
            for (int j = 0; j < res; j++) {
                grid[i][j] = Math.round(f3[i][j] * n);
                if (grid[i][j] < 0) grid[i][j] = 0;
                if (grid[i][j] > n - 1) grid[i][j] = n - 1;
            }
        }


        for (int i = 0; i < n + 1; i++) {
            float a = 1 * ((float) i / (float) n)*.4f;
            colors.add(new Color(a, a, a, 1));
        }
        Collections.reverse(colors);
        calcCorners();
        //dropFloors();
       // averageMap();
    }
    void calcCorners(){

        for (int i = 0; i < res; i++) {
            for (int j = 0; j < res; j++) {
                cells[i][j]=new Cell(new Vector2(i,j),grid[i][j]);
                Polygon polygon=new Polygon();

                int     q,w,e,
                        a,s,d,
                        z,x,c;
                s=grid[i][j];
                try{
                    q =grid[i-1][j+1];
                }catch (ArrayIndexOutOfBoundsException p){
                    q =grid[i][j];
                }
                try{
                    w =grid[i][j+1];
                }catch (ArrayIndexOutOfBoundsException p){
                    w =grid[i][j];
                }
                try{
                   e  =grid[i+1][j+1];
                }catch (ArrayIndexOutOfBoundsException p){
                   e  =grid[i][j];
                }
                try{
                    d =grid[i+1][j];
                }catch (ArrayIndexOutOfBoundsException p){
                   d  =grid[i][j];
                }
                try{
                  c   =grid[i+1][j-1];
                }catch (ArrayIndexOutOfBoundsException p){
                   c  =grid[i][j];
                }
                try{
                  x   =grid[i][j-1];
                }catch (ArrayIndexOutOfBoundsException p){
                   x  =grid[i][j];
                }
                try{
                   z  =grid[i-1][j-1];
                }catch (ArrayIndexOutOfBoundsException p){
                  z   =grid[i][j];
                }
                try{
                  a   =grid[i-1][j];
                }catch (ArrayIndexOutOfBoundsException p){
                  a   =grid[i][j];
                }
                float ul,ur,dl,dr;
               ul=  EMath.average(new float[]{q,w,a,s});
                ur=  EMath.average(new float[]{w,e,s,d});
                dl=  EMath.average(new float[]{a,s,z,x});
                dr=  EMath.average(new float[]{s,d,x,c});
                float heightmx=20;
                float t=(cellw*(2f/3f));
                float px0=(i*cellw);
                float px1=((i+1)*cellw);
                float py0=(heightmx*ul)+((j+1)*t);
                float py1=(heightmx*ur)+((j+1)*t);
                float py2=(j*t)+(heightmx*dl);
                float py3=(j*t)+(heightmx*dr);
                float xavg=(px0+px1)/2;
                float yavg=(py0+py1+py2+py3)/4;
                polygon.setVertices(new float[]{
                        px0,py0,
                        px1,py1,
                        px1,py3,
                        px0,py2,
                        xavg,yavg
                        ,ul,ur,
                        dl,dr
                });
                cells[i][j].setCorners(polygon);
            }
        }
    }
    void dropFloors() {

        for (int i = 0; i < res; i++) {
            for (int j = 0; j < res; j++) {
                if (grid[i][j] < (float) n / (float) 2) {
                    grid[i][j] /= 2;
                }
            }
        }
    }
    void riseCiel() {

        for (int i = 0; i < res; i++) {
            for (int j = 0; j < res; j++) {
                if (grid[i][j] > ((float) n * (float) 3) / (float) 4) {
                    float f = (float) grid[i][j] / (float) n;
                    float a = (1 - f) / 2;
                    a *= n;
                    grid[i][j] += a;
                    if (grid[i][j] > n) grid[i][j] = n - 1;

                }
            }
        }
    }
    void updateCamPosition() {
        Vector3 position = cam.position;
        float[] f= cells[(int) playerpos.x][(int) playerpos.y].getCorners().getVertices();
        Vector3 v =new Vector3(f[8],f[9],0);
        position.lerp(v,.2f);
        cam.position.set(position);
        cam.update();
        viewX = cam.position.x - cam.viewportWidth / 2;
        viewY = cam.position.y - cam.viewportHeight / 2;
    }

   }
