package com.quadx.gravity.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.quadx.gravity.Game;
import com.quadx.gravity.physicsBody.Body;
import com.quadx.gravity.shapes1_5_2.EMath;
import com.quadx.gravity.shapes1_5_2.Rect;
import com.quadx.gravity.tools1_0_1.timers1_0_1.Delta;

import java.util.ArrayList;
import java.util.Random;

import static com.quadx.gravity.Game.HEIGHT;
import static com.quadx.gravity.Game.WIDTH;
import static com.quadx.gravity.control.Resource.Type.Food;
import static com.quadx.gravity.control.Resource.Type.Wood;
import static com.quadx.gravity.control.Unit.State.*;
import static com.quadx.gravity.shapes1_5_2.EMath.pathag;
import static com.quadx.gravity.states.ControlGameState.grid;
import static com.quadx.gravity.states.ControlGameState.view;
import static com.quadx.gravity.tools1_0_1.timers1_0_1.Time.MINUTE;
import static com.quadx.gravity.tools1_0_1.timers1_0_1.Time.SECOND;

/**
 * Created by Chris Cavazos on 9/29/2016.
 */
public class Unit {
    ArrayList<Vector2> farmPlot = null;

    private Vector2 init = new Vector2();
    private Vector2 pos = new Vector2();
    private Vector2 destination = new Vector2();
    Vector2 previousPos = new Vector2(Game.WIDTH / 2, Game.HEIGHT / 2);
    Vector2 opos;

    Random rn = new Random();
    private Color c = new Color(rn.nextFloat(), rn.nextFloat(), rn.nextFloat(), 1);
    Resource.Type targetRessource = null;
    State state = State.Stand;
    Player owner = null;
    Unit target = null;
    Building home = null;
    Resource rtarget = null;

    Delta dLife = new Delta(rn.nextFloat() * 2 * MINUTE);//what the actual fuck
    Delta dChangeDir = new Delta(10);
    Delta dHunger = new Delta(20);
    Delta dStarve = new Delta(5 * SECOND);
    Delta dAction = new Delta(1 * SECOND);

    private float moveSpeed = 100f;
    float rechargeTime = 0;
    float energy = 15f;
    float eMax = 15;

    boolean planter = false;
    boolean buildmode = false;
    boolean breed = false;
    boolean hasChild = false;
    private boolean dead = false;

    protected int objective = 25;
    private int resourceIndex = -1;
    static int cost = 200;
    int radius = 8;
    int ca = 0;
    int cc = 0;
    int plotCounter = 0;

    public Rectangle getEnergyBar() {
        return Rect.rect(pos().add(view).add(-radius, radius), radius * 2 * (energy / 15f), 2);
    }

    public Rectangle getLifeBar() {
        return Rect.rect(pos().add(view).add(-radius, radius + 2), 2 * radius * (1 - dLife.percent()), 2);
    }

    public State getState() {
        return state;
    }

    public enum State {
        Stand, Gather, Build, Plant, Rest, Breed
    }

    Unit(Player player, Vector2 p) {
        setPos(p);
        init.set(pos());
        owner = player;
        home = owner.spawner;
    }

    private void setPos(Vector2 p) {
        pos.set(p);
    }

    public Unit() {
        this(null, new Vector2());
    }

    public Unit(Player p, float x, float y) {
        this(p, new Vector2(x, y));
    }

    void spendEnergy(float dt) {

        if (getState() != Rest) {
            energy -= dt;
            if (energy <= 1) {
                setState(Rest);
            }
        }
    }

    boolean collisionCheck(Unit u) {
        return !u.equals(this) && getState() != Rest && Collision.circlular(getHitBox(), u.getHitBox());
    }

    boolean hasNextFarmPlot() {
        return getState() == State.Plant && (!(farmPlot.isEmpty() || pos().equals(destination)));
    }

    boolean canConsume() {
        return Collision.circlular(getHitBox(), rtarget.getHitBox());
    }

    public void update() {
        float dt = Gdx.graphics.getDeltaTime();
        dLife.update(dt);
        dChangeDir.update(dt);
        dAction.update(dt);
        checkHunger(dt);
        spendEnergy(dt);
        previousPos = pos();
        moveSpeed = 100 * dt;
        plotCounter %= 25;
        targetRessource = owner.wood < owner.food ? Wood : Food;
        if (EMath.isInRange(dLife.percent(),.2f,.4f) && !hasChild) {
            setState(Breed);
        }
        doAction();
        if (dAction.isDone())
            dAction.reset();
    }

    void breed() {
        float dt = Gdx.graphics.getDeltaTime();
        if (!hasChild) {
            if (target == null) {
                Unit u = (Unit) EMath.getRand(owner.unitList);
                if (u.getState() != Rest) {
                    target = u;
                    u.setState(Stand);
                }
            } else {
                destination.set(target.pos());

                if (pos().dst(target.pos()) < 3)
                    breed(target);
                else {
                    setPos(Body.getVector(-moveSpeed, pos(), target.pos()).add(pos()));
                    //setPos(pos().lerp(target.pos(), .01f));
                }
            }
        }
        if (dAction.isDone()) {

        }
    }

    void rest() {
        float dt = Gdx.graphics.getDeltaTime();
        goHome();
        if (pos().dst(destination) < 3) {
            energy += dt * 2;

        }
        if (dAction.isDone()) {
            if (energy >= eMax) {
                setState(State.Gather, targetRessource);
            }
        }
    }

    void plant() {

        if (hasNextFarmPlot())
            destination.set(farmPlot.get(plotCounter));

        makeResource(targetRessource);
        if (dAction.isDone()) {
            plotFarm(targetRessource);

        }
    }

    void stand() {
        if (dAction.isDone()) {
            destination = pos();
        }
    }

    void gather() {
        if (rtarget == null) {
            rtarget = findNearest(targetRessource);
        } else {
            destination.set(rtarget.pos());
            setPos(Body.getVector(-moveSpeed, pos(), destination).add(pos()));
            if (canConsume()) {
                consume();
            }
        }
        if (dAction.isDone()) {
            getNextResTarget(targetRessource);
        }
    }

    void doAction() {
        switch (getState()) {
            case Stand: {
                stand();
                break;
            }
            case Gather: {
                gather();
                break;
            }
            case Build: {
                break;
            }
            case Plant: {
                plant();
                break;
            }
            case Rest: {
                rest();
                break;
            }
            case Breed: {
                breed();
                break;
            }
        }
    }


    float inh(float a, float b) {
        int i = rn.nextInt(3);
        switch (i) {
            case 0: {
                return a;
            }
            case 1: {
                return b;
            }
            case 2: {
                return EMath.inRange(a, b);
            }
        }
        if(rn.nextFloat()<.05)
            a=rn.nextFloat();
        return a;
    }

    private void breed(Unit target) {
        Color c1 = target.getColor();
        Color c2 = getColor();
        Color c3 = new Color();
        int n = rn.nextInt(4);
        Vector2 p = owner.spawner.pos();
        for (int i = 0; i < n; i++) {
            Unit u = new Unit(owner, p);
            c3.r = inh(c1.r, c2.r);
            c3.g = inh(c1.g, c2.g);
            c3.b = inh(c1.b, c2.b);
            if(rn.nextFloat()<.01f){
                c3.set(rn.nextFloat(),rn.nextFloat(),rn.nextFloat(),1);
            }
            u.setColor(c3);
            u.home = ((Building) EMath.getRand(owner.buildingList));
            u.setState(Gather, targetRessource);
            owner.newUnits.add(u);
        }
        if(n>0){
            target.hasChild = true;
            hasChild = true;
            setState(Gather, targetRessource);
            target.setState(Gather, targetRessource);
        }


    }

    private void setColor(Color c3) {
        c.set(c3);
    }

    void checkHunger(float dt) {
        owner.sub(Food, .001);
        if (owner.food <= 0) {
            dStarve.update(dt);
        } else dStarve.reset();
        dead = dLife.isDone() || dStarve.isDone();
    }

    void setState(State state, Resource.Type target) {
        targetRessource = target;
        setState(state);
    }

    void setState(State state) {
        this.state = state;
    }


    private void goHome() {
        try {
            destination = home.pos();
        } catch (NullPointerException e) {
            destination = owner.spawner.pos();
        }
        float dt = Gdx.graphics.getDeltaTime();

        setPos(Body.getVector(-moveSpeed, pos(), destination).add(pos()));
    }

    void makeResource(Resource.Type type) {
        Resource r = new Resource(type, farmPlot.get(plotCounter).x, farmPlot.get(plotCounter).y);
        grid.resources.add(r);
        owner.addResource(Wood, -1);
        owner.addResource(Food, -1);
        // c = Color.WHITE;
        plotCounter++;
        objective--;
        if (objective <= 0) {
            objective = 25;
            setState(State.Gather, Resource.getCalcResource(owner));
        }
    }

    void plotFarm(Resource.Type type) {
        targetRessource = type;
        ArrayList<Vector2> dests = new ArrayList<>();
        Vector2 sh = EMath.randSign(EMath.randInt(10).add(1, 1)).add(20, 20);
        Vector2 d = EMath.randSign(EMath.randInt(10).add(4, 4));
        Vector2 vi = ((Building) EMath.getRand(owner.buildingList)).pos();
        Vector2 pos = new Body().getVector(200 + rn.nextInt(200), vi, pos()).add(vi);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Vector2 a = new Vector2(i * sh.x, sh.y * j);
                Vector2 b = new Vector2(j * d.x, d.y * i);
                dests.add(new Vector2(pos).add(a).add(b));
            }
        }
        farmPlot = dests;

    }

    void buildToggle() {
        buildmode = !buildmode;
    }

    void build(Building b) {
        buildToggle();
        if (buildmode) {
            boolean canbuild = true;
            if (b.getType() == Building.Type.House /*&& owner.population == owner.popmax*/) {
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
                    b.setPosition(pos());
                    b.owner.buildingList.add(b);
                    owner.addResource(b.costType, -b.cost);
                    buildToggle();
                } else {
                    if (dChangeDir.percent() > .5) {
                        if (dChangeDir.isDone()) {
                            buildToggle();
                            //dtChangeDir=0;
                        } else {
                            if (owner.buildingList.size() > 1) {
                                int index = rn.nextInt(owner.buildingList.size() - 1);
                                Vector2 p = owner.buildingList.get(index).pos();
                                float x = (float) (p.x + (rn.nextGaussian() * b.radius) - (owner.buildingList.get(index).radius + b.radius) * Math.sin(p.angleRad(pos())));
                                float y = (float) (p.y + (rn.nextGaussian() * b.radius) - (owner.buildingList.get(index).radius + b.radius) * Math.cos(p.angleRad(pos())));
                                if (x < 0) x = 0;
                                if (x > WIDTH) x = WIDTH;
                                if (y < 0) y = 0;
                                if (y > HEIGHT) y = HEIGHT;

                                Vector2 n = new Vector2(x, y);
                                destination = n;
                            } else {
                                destination.x = pos().x;
                                destination.y = Game.HEIGHT / 2;
                            }
                        }
                    } else {
                        destination = new Vector2(rn.nextInt((int) Game.WIDTH), rn.nextInt((int) Game.HEIGHT));
                        dChangeDir.reset();
                    }
                }
            }
        }
    }

    void iterateResources() {

    }

    void getNextResTarget(Resource.Type type) {
        targetRessource = type;
        rtarget = findNearest(type);
        if (rtarget != null) {
            rtarget.taken = true;
            destination = rtarget.pos();
        }
    }

    void gather2(Resource.Type type) {
        int found = findNthClosest(3, type);
        if (found != -1) {
            destination = grid.resources.get(found).pos();
            resourceIndex = found;
        }
    }

    void consume() {
        if (Collision.circlular(getHitBox(), rtarget.getHitBox())) {
            rtarget.consume(owner);
            if (rtarget.isEmpty()) {
                getNextResTarget(targetRessource);
            }
        }
    }


    public boolean collides(Resource r) {
        return Collision.circlular(getHitBox(), r.getHitBox());
    }

    public boolean isPlanter() {
        return planter;
    }

    public int getResourceIndex() {
        return resourceIndex;
    }

    public Vector2 pos() {
        return new Vector2(pos);
    }

    public Color getColor() {
        return c;
    }

    Hitbox getHitBox() {
        return new Hitbox(Hitbox.HitboxShape.Circle, pos(), radius);
    }

    Resource findNearest(Resource.Type type) {
        Resource found = null;
        float minDistance = 100000;
        for (Resource r : grid.resources) {
            if (r.getType() == type && !r.taken) {
                Vector2 rpos = r.pos();
                float dx = rpos.x - pos().x;
                float dy = rpos.y - pos().y;
                float dist = Math.abs(pathag(dx, dy));
                if (dist < minDistance) {
                    found = r;
                    minDistance = dist;
                }
            }
        }
        return found;
    }

    int findNthClosest(int n, Resource.Type type) {
        ArrayList<Vector2> list = new ArrayList<>();
        for (Resource r : grid.resources) {
            if (r.getType() == type) {
                Vector2 rpos = r.pos();
                float dx = rpos.x - pos().x;
                float dy = rpos.y - pos().y;
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

    boolean isDead() {
        return dead;
    }
}
