package com.quadx.gravity;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.quadx.gravity.command.Command;
import com.quadx.gravity.states.GameStateManager;
import com.quadx.gravity.states.MainMenuState;

import java.util.ArrayList;

//import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
@SuppressWarnings("ALL")
public class Game extends ApplicationAdapter {
	private SpriteBatch batch;
	Texture img;
	public static BitmapFont font;
	private static final BitmapFont[] fonts = new BitmapFont[6];
	private GameStateManager gameStateManager;
	public static ArrayList<Command> commandList=new ArrayList<>();
	public Game(){

	}
	public static void out(String s){
		System.out.println(s);
	}
	public static float HEIGHT=700;
	public static float WIDTH=1350;
	private static float strWidth(String s){
		GlyphLayout gl=new GlyphLayout();
		gl.setText(Game.getFont(), s);
		return gl.width;
	}
	public static float centerString(String s){
		return (WIDTH/2)- (strWidth(s)/2);
	}
	public static void setFontSize(int x){
		font=fonts[x];
	}
	public static BitmapFont getFont(){

		return font;
	}
	private static BitmapFont createFont(int x){
		BitmapFont temp=new BitmapFont();

		try {
			FreeTypeFontGenerator generator= new FreeTypeFontGenerator(Gdx.files.internal("fonts\\prstart.ttf"));
			FreeTypeFontGenerator.FreeTypeFontParameter parameter= new FreeTypeFontGenerator.FreeTypeFontParameter();
			parameter.size = x;
			temp = generator.generateFont(parameter);
			//console("Font Generated");
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return temp;
	}
	@Override
	public void create () {
		font = new BitmapFont();
		font.setColor(Color.BLACK);
		commandList.add(new com.quadx.gravity.command.UpComm());
		commandList.add(new com.quadx.gravity.command.DownComm());
		commandList.add(new com.quadx.gravity.command.LeftComm());
		commandList.add(new com.quadx.gravity.command.RightComm());
		commandList.add(new com.quadx.gravity.command.ConfirmComm());
		//commandList.add(new com.quadx.gravity.command.BackComm());
		fonts[0]=createFont(6);
		fonts[1]=createFont(10);
		fonts[2]=createFont(12);
		fonts[3]=createFont(14);
		fonts[4]=createFont(16);
		fonts[5]=createFont(20);
		Gdx.graphics.setWindowedMode(1350,700);
		//HEIGHT = Gdx.graphics.getHeight();
		//WIDTH = Gdx.graphics.getWidth();
		batch = new SpriteBatch();
		gameStateManager = new GameStateManager();
		gameStateManager.push(new MainMenuState(gameStateManager));
		//gameStateManager.push(new PMotionState(gameStateManager));

	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(1, 1, 1, 1);
		gameStateManager.update(Gdx.graphics.getDeltaTime());
		gameStateManager.render(batch);
	}
}
