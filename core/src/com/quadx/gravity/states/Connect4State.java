package com.quadx.gravity.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.quadx.gravity.Game;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static com.quadx.gravity.Game.ft;

/**
 * Created by Chris Cavazos on 9/22/2017.
 */
public class Connect4State extends State {
    public Environment environment;
    public PerspectiveCamera cam;
    public ModelBatch modelBatch;
    public Model model;
    public ModelInstance instance;
    public CameraInputController camController;
    ModelBuilder modelBuilder = new ModelBuilder();
    ArrayList<ModelInstance> grid = new ArrayList<>();
    ArrayList<ModelInstance> pieces = new ArrayList<>();
    ModelInstance planeInstance;
    ModelInstance lastPlacedMarker;
    boolean flipColor=false;
    static float n=6;
    static int[][][] gridArr= new int[(int)n][(int)n][(int)n];
    static int[] lastPlaced= {0,0,0};
    float dtPlace=0;
    ModelInstance winView;


    public Connect4State(GameStateManager gameStateManager) {
        super(new GameStateManager());
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                for(int k=0;k<n;k++){
                    gridArr[i][j][k]=0;
                }
            }
        }
        modelBatch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));


        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(10f, 10f, 10f);
        cam.lookAt(0, 0, 0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();
        cam.update();
        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);

        model = modelBuilder.createLineGrid((int)n, (int)n, 1, 1, new Material(ColorAttribute.createDiffuse(Color.GRAY)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        for (int i = 0; i < n+1; i++) {
            instance = new ModelInstance(model);
            instance.transform.translate(n/2, 1f * i, n/2);
            instance.calculateTransforms();
            grid.add(instance);

            for (int j = 0; j < n+1; j++) {
                modelBuilder.begin();
                MeshPartBuilder builder = modelBuilder.part("line", 1, 3, new Material());
                builder.setColor(Color.GRAY);
                builder.line(i, 0.0f, j, i, n, j);
                Model model = modelBuilder.end();
                ModelInstance instance = new ModelInstance(model);
               grid.add(instance);
            }
        }
        model = modelBuilder.createXYZCoordinates(3, new Material(ColorAttribute.createDiffuse(Color.WHITE)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        instance = new ModelInstance(model);
        grid.add(instance);
    }
    public float getCameraRotation() {
        float camAngle = -(float) Math.atan2(cam.up.x, cam.up.y) * MathUtils.radiansToDegrees + 180;
        return camAngle;
    }
    @Override
    protected void handleInput() {
        float mx=Gdx.input.getX();
        float my=Gdx.input.getY();
        Ray ray = cam.getPickRay(mx, my);
        float[] f=currentPlane.getVertices();

        if (dtPlace>40*ft && Gdx.input.isButtonPressed(1) && Intersector.intersectRayBoundsFast(ray,new BoundingBox(new Vector3(f[0],f[1],f[2]),new Vector3(f[6],f[7],f[8])))) {
            Vector3 pos=new Vector3();
            if(getVeiwPlane().contains("XY"))
                pos=getClickCordsXY((int)mx,(int)my);
            if(getVeiwPlane().contains("XZ"))
                pos=getClickCordsXZ((int)mx,(int)my);
            if(getVeiwPlane().contains("YZ"))
                pos=getClickCordsYZ((int)mx,(int)my);
            tpos=pos;
            pos.set((int)pos.x,(int)pos.y,(int)pos.z);
            if((int) pos.x <n && (int) pos.y < n && (int) pos.z <n
                    && (int) pos.x >=0 && (int) pos.y >=0 && (int) pos.z >=0) {

                if (gridArr[(int) pos.x][(int) pos.y][(int) pos.z] == 0) {
                    if(flipColor)
                        gridArr[(int) pos.x][(int) pos.y][(int) pos.z] = 1;
                    else
                        gridArr[(int) pos.x][(int) pos.y][(int) pos.z] = 2;

                } else {
                    if (getVeiwPlane().contains("XY")) {

                        int count = -1;
                        for (int i = (int) pos.z; i < n && gridArr[(int) pos.x][(int) pos.y][i] != 0; i++) {
                            count = i;
                        }
                        count++;
                        pos.set((int) pos.x, (int) pos.y, count);
                    }
                    if (getVeiwPlane().contains("XZ")) {
                        int count = -1;
                        for (int i = (int) pos.y; i < n && gridArr[(int) pos.x][i][(int) pos.z] != 0; i++) {
                            count = i;
                        }
                        count++;

                        pos.set((int) pos.x, count, (int) pos.z);
                    }
                    if (getVeiwPlane().contains("YZ")) {
                        int count = -1;
                        for (int i = (int) pos.x; i < 5 && gridArr[i][(int) pos.y][(int) pos.z] != 0; i++) {
                            count = i;
                        }
                        count++;

                        pos.set(count, (int) pos.y, (int) pos.z);
                    }
                    if((int) pos.x <n && (int) pos.y < n && (int) pos.z <n) {

                        if (flipColor)
                            gridArr[(int) pos.x][(int) pos.y][(int) pos.z] = 1;
                        else
                            gridArr[(int) pos.x][(int) pos.y][(int) pos.z] = 2;
                    }
                }
                lastPlaced[0]=(int)pos.x;
                lastPlaced[1]=(int)pos.y;
                lastPlaced[2]=(int)pos.z;
                model = modelBuilder.createBox(.2f, .2f, .2f, new Material(ColorAttribute.createDiffuse(new Color(0,0,1,.1f))), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
                lastPlacedMarker = new ModelInstance(model);
                lastPlacedMarker.transform.translate(new Vector3(pos).add(.25f,.25f,.25f));
                lastPlacedMarker.calculateTransforms();
                pos.add(.5f, .5f, .5f);
                Color c;
                if(flipColor)
                    c=Color.RED;
                else
                    c=Color.WHITE;
                model = modelBuilder.createBox(.5f, .5f, .5f, new Material(ColorAttribute.createDiffuse(c)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
                instance = new ModelInstance(model);
                instance.transform.translate(pos);
                instance.calculateTransforms();
                pieces.add(instance);
            }
            flipColor=!flipColor;
            dtPlace=0;
        }
    }
    Vector3 tpos=new Vector3();
    Polygon currentPlane = new Polygon( new float[]{0, 0, 0, n, 0, 0, n, n, 0, 0, n, 0});

    public void calculatePlane() {
        String s = getVeiwPlane();
        float[] f;
        if (s.equals("XY")) {
            f = new float[]{0, 0, 0, n, 0, 0, n, n, 0, 0, n, 0};
            currentPlane.setVertices(f);
            model = modelBuilder.createRect(f[0], f[1], f[2], f[3], f[4], f[5], f[6], f[7], f[8], f[9], f[10], f[11], 0, 0, 1, new Material(ColorAttribute.createDiffuse(Color.RED)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
            planeInstance = new ModelInstance(model);
        }
        if (s.equals("-XY")) {
            f = new float[]{0, 0, n, n, 0, n, n, n, n, 0, n, n};
            currentPlane.setVertices(f);
            model = modelBuilder.createRect(f[0], f[1], f[2], f[3], f[4], f[5], f[6], f[7], f[8], f[9], f[10], f[11], 0, 0, -1, new Material(ColorAttribute.createDiffuse(Color.RED)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
            planeInstance = new ModelInstance(model);
        }
        if (s.equals("YZ")) {
            f = new float[]{0, 0, 0, 0, n, 0, 0, n, n, 0, 0, n};
            currentPlane.setVertices(f);
            model = modelBuilder.createRect(f[0], f[1], f[2], f[3], f[4], f[5], f[6], f[7], f[8], f[9], f[10], f[11], 1, 0, 0, new Material(ColorAttribute.createDiffuse(Color.RED)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
            planeInstance = new ModelInstance(model);
        }
        if (s.equals("-YZ")) {
            f = new float[]{n, 0, 0, n, n, 0, n, n, n, n, 0, n};
            currentPlane.setVertices(f);
            model = modelBuilder.createRect(f[0], f[1], f[2], f[3], f[4], f[5], f[6], f[7], f[8], f[9], f[10], f[11], -1, 0, 0, new Material(ColorAttribute.createDiffuse(Color.RED)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
            planeInstance = new ModelInstance(model);
        }
        if (s.equals("XZ")) {
            f = new float[]{0, n, 0, n, n, 0, n, n, n, 0, n, n};
            currentPlane.setVertices(f);
            model = modelBuilder.createRect(f[0], f[1], f[2], f[3], f[4], f[5], f[6], f[7], f[8], f[9], f[10], f[11], 0, 1, 0, new Material(ColorAttribute.createDiffuse(Color.RED)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
            planeInstance = new ModelInstance(model);
        }
        if (s.equals("-XZ")) {
            f = new float[]{0, 0, 0, n, 0, 0, n, 0, n, 0, 0, n};
            currentPlane.setVertices(f);
            model = modelBuilder.createRect(f[0], f[1], f[2], f[3], f[4], f[5], f[6], f[7], f[8], f[9], f[10], f[11], 0, -1, 0, new Material(ColorAttribute.createDiffuse(Color.RED)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
            planeInstance = new ModelInstance(model);
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        camController.update();
        calculatePlane();
        dtPlace+=dt;
        checkWins(1);
        checkWins(2);
    }

    boolean win =false;
    public void checkWins(int p) {
        for (int i = 0; i < n - 4; i++) {
            for (int j = 0; j < n - 4; j++) {
                for (int k = 0; k < n - 4; k++) {
                    Vector3 a=new Vector3();
                    Vector3 b=new Vector3();
                    boolean win=false;
                    if (gridArr[i][j][k] == p && gridArr[i + 1][j][k] == p && gridArr[i + 2][j][k] == p && gridArr[i + 3][j][k] == p) {
                        a=new Vector3(i,j,k);
                        b=new Vector3(i+3,j,k);
                        win=true;
                    }
                    if (gridArr[i][j][k] == 1 && gridArr[i][j + 1][k] == p && gridArr[i][j + 2][k] == p && gridArr[i][j + 3][k] == p) {
                        a=new Vector3(i,j,k);
                        b=new Vector3(i,j+3,k);
                        win=true;

                    }
                    if (gridArr[i][j][k] == 1 && gridArr[i][j][k + 1] == p && gridArr[i][j][k + 2] == p && gridArr[i][j][k + 3] == p) {
                        a=new Vector3(i,j,k);
                        b=new Vector3(i,j,k+3);
                        win=true;

                    }
                    if(win) {
                        this.win=win;
                        modelBuilder.begin();
                        MeshPartBuilder builder = modelBuilder.part("line", 1, 3, new Material());
                        builder.setColor(Color.GREEN);
                        builder.line(a.x+.5f, a.y+.5f, a.z+.5f, b.x+.5f, b.y+.5f, b.z+.5f);
                        Model model = modelBuilder.end();
                        winView = new ModelInstance(model);
                    }
                }
            }
        }
    }

    public Vector3 getClickCordsXZ(int screenX, int screenY) {
        Vector3 tmpVector = new Vector3();
        Ray ray = cam.getPickRay(screenX, screenY);
        final float distance = -ray.origin.y / ray.direction.y;
        tmpVector.set(ray.direction).scl(distance).add(ray.origin);
        return format(tmpVector);
    }
    public Vector3 getClickCordsXY(int screenX, int screenY) {
        Vector3 tmpVector = new Vector3();
        Ray ray = cam.getPickRay(screenX, screenY);
        final float distance = -ray.origin.z / ray.direction.z;
        tmpVector.set(ray.direction).scl(distance).add(ray.origin);
        return format(tmpVector);
    }
    public Vector3 getClickCordsYZ(int screenX, int screenY) {
        Vector3 tmpVector = new Vector3();
        Ray ray = cam.getPickRay(screenX, screenY);
        final float distance = -ray.origin.x / ray.direction.x;
        tmpVector.set(ray.direction).scl(distance).add(ray.origin);
        return format(tmpVector);
    }



    @Override
    public void render(SpriteBatch sb) {
        Gdx.gl.glClearColor(.1f, .1f, .1f, 1);
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        modelBatch.begin(cam);
        for (ModelInstance instance : grid) {
            modelBatch.render(instance, environment);
        }
        if(lastPlacedMarker !=null){
            modelBatch.render(lastPlacedMarker,environment);
        }if(winView !=null){
            modelBatch.render(winView,environment);
        }
        for (ModelInstance instance : pieces) {
            modelBatch.render(instance, environment);
        }

        modelBatch.end();
        sb.begin();
        Game.getFont().draw(sb, getVeiwPlane(), 30, 70);

        Game.getFont().draw(sb, win + "", 30, 130);

        Game.getFont().draw(sb, format(tpos) + "", 30, 110);

        Game.getFont().draw(sb, format(cam.direction) + "", 30, 90);
        if(getVeiwPlane().contains("XY"))
            Game.getFont().draw(sb, getClickCordsXY(Gdx.input.getX(), Gdx.input.getY()) + "", 30, 50);
        if(getVeiwPlane().contains("XZ"))
            Game.getFont().draw(sb, getClickCordsXZ(Gdx.input.getX(), Gdx.input.getY()) + "", 30, 50);
        if(getVeiwPlane().contains("YZ"))
            Game.getFont().draw(sb, getClickCordsYZ(Gdx.input.getX(), Gdx.input.getY()) + "", 30, 50);
        Game.getFont().draw(sb, getCameraRotation() + "", 30, 30);
        sb.end();
    }

    String getVeiwPlane() {
        Vector3 d = cam.direction;
        if (d.x>.85f)
            return "YZ";
        if (d.x<-.85f )
            return "-YZ";
        if (d.y>.85f)
            return "XZ";
        if (d.y<-.85f )
            return "-XZ";
        if (d.z>.85f )
            return "XY";
        if (d.z<-.85f )
            return "-XY";

        return "-";
    }

    static DecimalFormat df = new DecimalFormat("0.00");

    public static Vector3 format(Vector3 in) {
        return new Vector3(Float.parseFloat(df.format(in.x)), Float.parseFloat(df.format(in.y)), Float.parseFloat(df.format(in.z)));
    }
    @Override
    public void dispose() {
        modelBatch.dispose();
        model.dispose();
    }
}
