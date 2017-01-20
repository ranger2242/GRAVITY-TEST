package com.quadx.gravity.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.quadx.gravity.EMath;
import com.quadx.gravity.Game;

import java.util.ArrayList;
import java.util.Random;

import static com.quadx.gravity.EMath.pathag;
import static com.quadx.gravity.Game.HEIGHT;
import static com.quadx.gravity.Game.WIDTH;
import static com.quadx.gravity.control.Resource.Type.Wood;
import static com.quadx.gravity.states.ControlGameState.grid;

/**
 * Created by Chris Cavazos on 9/29/2016.
 */
public class Unit {
    Random rn = new Random();
    int radius = 5;
    static int cost = 200;
    Vector2 opos;
    private Vector2 init = null;
    private Vector2 pos = init;
    boolean buildmode = false;
    float dtChangeDir = 0;
    private Vector2 destination = pos;
    Vector2 previousPos = new Vector2(Game.WIDTH / 2, Game.HEIGHT / 2);
    private Color c = new Color(rn.nextFloat(), rn.nextFloat(), rn.nextFloat(), 1);
    private float moveSpeed = 1f;
    private int resourceIndex = -1;
    float standTime = 0;
    Resource.Type targetRessource = null;
    Player owner = null;
    float dtHunger = 0;
    private boolean dead = false;
    protected int objective = 25;
    State state = State.Stand;
    int ca = 0;
    int cc = 0;
    float energy = 15f;
    ArrayList<Vector2> farmPlot = null;
    int plotCounter = 0;
    float dtEndLife = rn.nextFloat() * (rn.nextInt(100) + 100);
    float dtLife = 0;

    enum State {
        Stand, Gather, Build, Plant, Rest
    }

    public Unit() {
        pos = new Vector2(0, 0);
    }

    public Unit(Player p, float x, float y) {

        pos = new Vector2(x, y);
        init = pos;
        owner = p;
    }

    public Vector2 getPosition() {
        return pos;
    }

    public Hitbox getHitBox() {
        return new Hitbox(Hitbox.HitboxShape.Circle, pos, radius);
    }

    void checkHunger() {
        if (dtHunger > 20) {
            if (owner.food > 10)
                owner.addResource(Resource.Type.Food, -10);
            else dead = true;
            dtHunger = 0;
        }
        if (dtLife > dtEndLife) {
            dead = true;
        }
    }

    public void move() {
        float dt = Gdx.graphics.getDeltaTime();
        dtLife = dt;
        dtChangeDir += dt;
        dtHunger += dt;
        checkHunger();
        previousPos = pos;
        if (state != State.Rest) {
            energy -= dt;
            if (energy <= 0) {
                changeState(State.Rest, targetRessource);
            }
        }

        if (state == State.Rest && EMath.pathag(pos, destination) < 2) {
            energy += dt;
            if (energy >= 15) {
                changeState(State.Gather, targetRessource);
            }
        }

        float dx;
        float dy;
        if (plotCounter > 24) plotCounter = 0;
        if (state == State.Plant && !farmPlot.isEmpty() && !pos.equals(destination)) {
            destination = farmPlot.get(plotCounter);
        }
        if (state != State.Stand) {

            boolean canMove = true;
            Vector2 moveAwayFrom = null;

            for (Unit u : owner.unitList) {
                if (!u.equals(this) && state != State.Rest) {
                    if (Collision.circlular(getHitBox(), u.getHitBox())) {
                        canMove = false;
                        moveAwayFrom = u.getPosition();
                    }
                }
                try {
                    dx = pos.x - destination.x;
                    dy = pos.y - destination.y;
                } catch (NullPointerException e) {
                    dx = pos.x;
                    dy = pos.y;
                }
                float dist = pathag(dx, dy);

                if (resourceIndex != -1 && resourceIndex < grid.resources.size() && Collision.circlular(getHitBox(), grid.resources.get(resourceIndex).getHitBox())) {
                    consume();
                } else {
                    if (canMove) {
                        float angle = (float) Math.atan2(dy, dx);
                        pos.x -= moveSpeed * Math.cos(angle);
                        pos.y -= moveSpeed * Math.sin(angle);
                    } else {

                        float angle = (float) Math.atan2(moveAwayFrom.y - pos.y, moveAwayFrom.x - pos.x);
                        pos.x -= moveSpeed * Math.cos(angle);
                        pos.y -= moveSpeed * Math.sin(angle);
                        gather2(targetRessource);
                    }
                }
            }
            if (previousPos.x - pos.x == 0 && previousPos.y - pos.y == 0) {
                standTime += Gdx.graphics.getDeltaTime();
            }
            if (state == State.Plant) {
                float f = Math.abs(EMath.pathag(pos.x, destination.x, pos.y, destination.y));
                makeResource(targetRessource);

            }
            if (state != State.Rest) {
                if (standTime > 5) {
                    standTime = 0;
                    checkState();
                }
            }
        }
    }

    public void changeState(State state, Resource.Type target) {
        targetRessource = target;
        this.state = state;
        checkState();
    }

    public void checkState() {
        switch (state) {

            case Stand: {
                destination = pos;
                break;
            }
            case Gather: {
                gather(targetRessource);
                break;
            }
            case Build: {
                break;
            }
            case Plant: {
                plotFarm(targetRessource);
                break;
            }
            case Rest: {
                int index = rn.nextInt(owner.buildingList.size());
                // int count =0;
                // while(!owner.buildingList.get(index).getType().equals(Building.Type.House) && count<5) {
                //    index=rn.nextInt(owner.buildingList.size());
                //    count++;
                // }
                destination = owner.buildingList.get(index).getPosition();
                break;
            }
        }
    }

    void makeResource(Resource.Type type) {
        Resource r = new Resource(type, farmPlot.get(plotCounter).x, farmPlot.get(plotCounter).y);
        grid.resources.add(r);
        owner.addResource(Wood, -1);
        owner.addResource(Resource.Type.Food, -1);
        // c = Color.WHITE;
        plotCounter++;
        objective--;
        if (objective <= 0) {
            objective = 25;
            changeState(State.Gather, Resource.getCalcResource(owner));
        }
    }

    public void plotFarm(Resource.Type type) {
        targetRessource = type;
        ArrayList<Vector2> dests = new ArrayList<>();
        Vector2 op = pos;
        int xshift = rn.nextInt(10) + 1;
        int yshift = rn.nextInt(10) + 1;
        if (rn.nextBoolean()) xshift *= -1;
        if (rn.nextBoolean()) yshift *= -1;
        int dx = rn.nextInt(10) + 4;
        int dy = rn.nextInt(10) + 4;
        if (rn.nextBoolean()) dx *= -1;
        if (rn.nextBoolean()) dy *= -1;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                int x = (int) (op.x + (i * (20 + xshift)) + ((j * dx)));
                int y = (int) (op.y + (j * (20 + yshift)) + ((i * dy)));
                dests.add(new Vector2(x, y));
            }
        }
        farmPlot = dests;

    }

    public Color getColor() {
        return c;
    }

    public void buildToggle() {
        buildmode = !buildmode;
    }

    public void build(Building b) {

        if (buildmode) {
            boolean canbuild = true;
            if (b.getType() == Building.Type.House && owner.population == owner.popmax) {
                for (Resource r : grid.resources) {
                    if (r.isColliding(this)) {
                        canbuild = false;
                    }
                }
                for (Building bu : owner.buildingList) {
                    if (Collision.circlular(getHitBox(), bu.getHitBox())) {
                        canbuild = false;
                    }
                }
                if (canbuild) {
                    b.setPosition(new Vector2(pos.x, pos.y));
                    b.owner.buildingList.add(b);
                    owner.addResource(b.costType, -b.cost);
                    buildToggle();
                } else {
                    if (dtChangeDir > 5) {
                        if (dtChangeDir > 10) {
                            buildToggle();
                            //dtChangeDir=0;
                        } else {
                            if (owner.buildingList.size() > 1) {
                                int index = rn.nextInt(owner.buildingList.size() - 1);
                                Vector2 p = owner.buildingList.get(index).getPosition();
                                float x = (float) (p.x + (rn.nextGaussian() * b.radius) - (owner.buildingList.get(index).radius + b.radius) * Math.sin(p.angleRad(pos)));
                                float y = (float) (p.y + (rn.nextGaussian() * b.radius) - (owner.buildingList.get(index).radius + b.radius) * Math.cos(p.angleRad(pos)));
                                if (x < 0) x = 0;
                                if (x > WIDTH) x = WIDTH;
                                if (y < 0) y = 0;
                                if (y > HEIGHT) y = HEIGHT;

                                Vector2 n = new Vector2(x, y);
                                destination = n;
                            } else {
                                destination.x = pos.x;
                                destination.y = Game.HEIGHT / 2;
                            }
                        }
                    } else {
                        destination = new Vector2(rn.nextInt((int) Game.WIDTH), rn.nextInt((int) Game.HEIGHT));
                        dtChangeDir = 0;
                    }
                }
            }
        }
    }

    void iterateResources() {

    }

    public int findNearest(Resource.Type type) {
        int found = -1;
        float minDistance = 100000;
        for (Resource r : grid.resources) {
            if (r.getType() == type) {
                Vector2 rpos = r.getPosition();
                float dx = rpos.x - pos.x;
                float dy = rpos.y - pos.y;
                float dist = Math.abs(pathag(dx, dy));
                if (dist < minDistance) {
                    found = grid.resources.indexOf(r);
                    minDistance = dist;
                }
            }
        }
        return found;
    }

    public int findNthClosest(int n, Resource.Type type) {
        ArrayList<Vector2> list = new ArrayList<>();
        for (Resource r : grid.resources) {
            if (r.getType() == type) {
                Vector2 rpos = r.getPosition();
                float dx = rpos.x - pos.x;
                float dy = rpos.y - pos.y;
                float dist = Math.abs(pathag(dx, dy));
                list.add(new Vector2(grid.resources.indexOf(r), dist));
            }
        }
        ArrayList<Vector2> sorted = new ArrayList<>();
        for (Vector2 a : list) {
            if (sorted.isEmpty()) {
                sorted.add(a);
            } else {
                int p = 0;
                for (Vector2 b : sorted) {
                    if (a.y > b.y) {
                        p++;
                    }
                }
                sorted.add(p, a);
            }
        }
        try {
            return (int) sorted.get(n).x;
        } catch (Exception e) {
            return -1;
        }
    }


    public void gather(Resource.Type type) {
        targetRessource = type;
        int found = findNearest(type);
        if (found != -1) {
            destination = grid.resources.get(found).getPosition();
            resourceIndex = found;
        }
    }

    void gather2(Resource.Type type) {
        int found = findNthClosest(3, type);
        if (found != -1) {
            destination = grid.resources.get(found).getPosition();
            resourceIndex = found;
        }
    }

    public void consume() {
        if (resourceIndex != -1 && resourceIndex < grid.resources.size() && Collision.circlular(getHitBox(), grid.resources.get(resourceIndex).getHitBox())) {
            grid.resources.get(resourceIndex).consume(owner);
            if (grid.resources.get(resourceIndex).getDt() <= 0) {
                gather(targetRessource);
            }
        }
    }

    public int getResourceIndex() {
        return resourceIndex;
    }

    public boolean isDead() {
        return dead;
    }
}
