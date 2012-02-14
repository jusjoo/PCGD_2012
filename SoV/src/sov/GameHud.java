package sov;

import java.util.ArrayList;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class GameHud {

	ArrayList<HudElement> elements;
	
	public GameHud() {
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
}
