package com.quadx.gravity.tools1_0_1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.quadx.gravity.shapes1_4.Delta;
import com.quadx.gravity.shapes1_5_2.ShapeRendererExt;

import java.util.ArrayList;

import static com.badlogic.gdx.graphics.Color.BLACK;

/**
 * Created by Chris Cavazos on 9/30/2017.
 * updated dependency issues 12-7-17
 */
public class FPSModule {
    private static final ArrayList<Integer> fpsList = new ArrayList<>();
    private final Delta dFPS = new Delta(.05f);
    private static float fps = 0;
    private boolean enabled = true;
    private static final ArrayList<Double> memUsageList = new ArrayList<>();
    private static long currentMemUsage = 0;

    public void toggle() {
        enabled = !enabled;
    }

    public void setEnabled(boolean b) {
        enabled = b;
    }

    public void update(float dt) {
        dFPS.update(dt);
        processMetrics();
        if (dFPS.isDone()) {
            fps = 1 / Gdx.graphics.getDeltaTime();
            fpsList.add((int) fps);
            if (fpsList.size() > 50) {
                fpsList.remove(0);
            }
        }
    }

    public void render(SpriteBatch sb, ShapeRendererExt shapeR, Vector2 pos) {
        if (enabled) { //TODO optomize this to render faster
            //DRAW FPS meter
            shapeR.begin(ShapeRenderer.ShapeType.Filled);
            shapeR.setColor(BLACK);
            shapeR.rect(pos.x, pos.y, 100, 100);
            shapeR.end();
            shapeR.begin(ShapeRenderer.ShapeType.Line);
            shapeR.setColor(Color.WHITE);
            shapeR.rect(pos.x, pos.y, 100, 100);
            shapeR.line(pos.x, pos.y, pos.x + 100, pos.y);

            shapeR.setColor(Color.GREEN);
            int prev = 0;
            for (int i = 0; i < fpsList.size(); i++) {
                shapeR.line(pos.x + (i * 2), pos.y + prev, pos.x + ((i + 1) * 2), pos.y + fpsList.get(i));
                prev = fpsList.get(i);
            }
            double prev1 = 0;
            for (int i = 0; i < memUsageList.size(); i++) {
                shapeR.setColor(Color.PURPLE);
                shapeR.line(pos.x + (i * 2), (float) (pos.y + 100 * prev1), pos.x + ((i + 1) * 2), (float) (pos.y + 100 * memUsageList.get(i)));
                prev1 = memUsageList.get(i);
            }
            shapeR.end();
            //render fps counter
            sb.begin();
            if (enabled) {
                Fonts.setFontSize(1);
                Fonts.getFont().setColor(Color.WHITE);
                Fonts.getFont().draw(sb, (int) fps + " FPS", pos.x + 2, pos.y + 80);
                double x = 0;
                if(!memUsageList.isEmpty())
                x = memUsageList.get(memUsageList.size() - 1);
                Fonts.getFont().draw(sb, (int) currentMemUsage + "MB " + Math.floor(x * 100) + "%", pos.x + 2, pos.y + 95);
            }
            sb.end();
        }
    }

    private static void processMetrics() {
        Runtime runtime = Runtime.getRuntime();
        currentMemUsage = runtime.totalMemory() / (1024 * 1024);
        memUsageList.add((double) (currentMemUsage / runtime.maxMemory() / (1024 * 1024)));
        if (memUsageList.size() > 50)
            memUsageList.remove(0);
    }
}
