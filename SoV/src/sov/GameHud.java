package sov;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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
	
}
