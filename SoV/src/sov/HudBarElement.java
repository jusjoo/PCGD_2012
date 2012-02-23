package sov;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Color;

public class HudBarElement extends HudElement {

	BarElement bar;
	
	public HudBarElement(Vector2 position, Texture texture, Vector2 barPosition, Vector2 barSize, float maxValue, Color color, boolean vertical) {
		super(position, texture);
		
		bar = new BarElement(barPosition.add(position), barSize, maxValue, color, vertical);

	}
	
	public void render(SpriteBatch spriteBatch, float x, float y) {
		bar.render(spriteBatch, x, y);
		//super.render(spriteBatch, x, y);
		
	}

	
}
