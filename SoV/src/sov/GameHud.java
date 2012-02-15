package sov;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class GameHud {

	CoffeeGDX game;
	ArrayList<HudElement> elements;
	
	
	private HudElement mainMenuElement;
	
	public GameHud(CoffeeGDX game) {
		this.game = game;
		elements = new ArrayList<HudElement>();
	}
	
	/*
	 * Takes in the camera's position in the world, and translates
	 * it to screen (x,y) for the elements. Elements will then have 
	 * (0,0) on the top left corner of the screen 
	 */
	public void render(SpriteBatch spriteBatch, float camX, float camY) {
		
		float x = camX - Gdx.graphics.getWidth() / 4;
		float y = camY + Gdx.graphics.getHeight() / 4;
		
		//System.out.println(Gdx.graphics.getWidth());
		
		spriteBatch.begin();
		for (HudElement element: elements) {
			element.render(spriteBatch, x, y);
		}
		spriteBatch.end();
	}

	public void addElement(HudElement hudElement) {
		elements.add(hudElement);
	}
	
	public void setPlayer(Creature player){
		
		Texture texture = new Texture(new FileHandle("assets/creatures/mrEggEverything.png"));
		Vector2 position = new Vector2(0, 0);
		HudBarElement playerHealthBar = new HudBarElement(position, texture, new Vector2(0,0), new Vector2(64,8), player.body.getHitPoints());
		
		player.body.addHealthBar(playerHealthBar.bar);
		
		elements.add(playerHealthBar);
	}

	public void removeElement(HudElement hudElement) {
		elements.remove(hudElement);
	}
	
	public void toggleMainMenu() {
		if (mainMenuElement == null) {
			System.out.println("debug");
			Texture texture = new Texture(new FileHandle("assets/creatures/mrEggEverything.png"));
			Vector2 position = new Vector2(500, 300);
			mainMenuElement = new HudElement(position, texture);
			this.addElement(mainMenuElement);
			game.paused = true;
		} else {
			this.removeElement(mainMenuElement);
			mainMenuElement = null;
			game.paused = false;
		}
		
	}
	
}
