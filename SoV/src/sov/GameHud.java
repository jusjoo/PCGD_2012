package sov;

import java.util.ArrayList;

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
	
	public void render(SpriteBatch spriteBatch, float x, float y) {
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
		Vector2 position = new Vector2(-200, 100);
		HudBarElement playerHealthBar = new HudBarElement(position, texture, new Vector2(12,12), new Vector2(64,8), player.body.getHitPoints());
		
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
			Vector2 position = new Vector2(-(texture.getHeight() /2), -(texture.getWidth() /2) );
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
