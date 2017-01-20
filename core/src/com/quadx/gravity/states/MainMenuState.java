package com.quadx.gravity.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.quadx.gravity.Game;

import java.util.ArrayList;

import static com.quadx.gravity.Game.HEIGHT;
import static com.quadx.gravity.Game.centerString;

/**
 * Created by Tom on 12/22/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class MainMenuState extends State{
    private static final ArrayList<String> options=new ArrayList<>();
    private static int selector=0;
    private int titlePosY=0;
    private int optionsPosY =0;
    private float dtCursor = 0;
    private static GameStateManager g;
    public MainMenuState(GameStateManager gsm) {
        super(gsm);
        g=gsm;
        addOptionsToList();
        titlePosY=(int)((HEIGHT/3)*2);
        optionsPosY =(int)((HEIGHT/3));
    }

    @Override
    protected void handleInput() {
        for(com.quadx.gravity.command.Command c:Game.commandList){
            c.execute();
        }
    }

    private void addOptionsToList(){
        options.add("Control");
        options.add("Asteroids");
        options.add("Basketball");
        options.add("Soccer");
        options.add("Gravity");
        options.add("RoachAI");
        options.add("Vector Test");
        options.add("Exit");
    }

    public static void incrementSelector(){
        selector--;
        if(selector<0)selector=options.size();
    }
    public static void decrementSelector() {
        selector++;
        if(selector>options.size())selector=0;
    }
    public static void selectOption(){
        switch (selector){
            case(0):{
                g.push(new HeightMapState(g));
                break;
            }
            case(1):{
                g.push(new AsteroidsState(g));
                break;
            }
            case(2):{
                g.push(new BasketBallState(g));
                break;
            }
            case(3):{
                g.push(new SoccerState(g));
                break;
            }case(4):{
                g.push(new GravityState(g));
                break;
            }case(5):{
                g.push(new GameState(g));
                break;
            }case(6):{
                g.push(new VectorTestState(g));
                break;
            }
            case(7):{
                System.exit(0);
                break;
            }
        }
    }
    @Override
    public void update(float dt) {
        dtCursor+=dt;
        if(dtCursor>.15) {
            dtCursor = 0;
            handleInput();
        }
    }
    @Override
    public void dispose() {
    }
    /////////////////////////////////////////////////////////////////////////////////////////
    //DRAWING FUNCTIONS
    @Override
    public void render(SpriteBatch sb) {
        Gdx.gl20.glClearColor(0,0,0,1);
        sb.begin();
        sb.end();
        drawTitle(sb);
        drawOptions(sb);
    }
    private void drawTitle(SpriteBatch sb){
        sb.begin();
        Game.setFontSize(5);
        Game.font.setColor(Color.WHITE);
        String title="-TEST AREA-";
        Game.getFont().draw(sb,title, centerString(title),titlePosY-100);
        sb.end();
    }
    private void drawOptions(SpriteBatch sb){

        sb.begin();
        Game.setFontSize(5);
        for(int i=0;i<options.size();i++){
            if(selector==i)
                Game.getFont().setColor(Color.BLUE);
            else
                Game.getFont().setColor(Color.WHITE);

            Game.getFont().draw(sb,options.get(i),centerString(options.get(i)),optionsPosY-(i*20));
        }
        sb.end();
    }

}
