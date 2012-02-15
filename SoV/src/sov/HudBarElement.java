package sov;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class HudBarElement extends HudElement {

	BarElement bar;
	
	public HudBarElement(Vector2 position, Texture texture, Vector2 barPosition, Vector2 barSize, float maxValue) {
		super(position, texture);
		
		bar = new BarElement(barPosition.add(position), barSize, maxValue);
		
		
		// TODO Auto-generated constructor stub
	}
	
	public void render(SpriteBatch spriteBatch, float x, float y) {
		bar.render(spriteBatch, x, y);
		//super.render(spriteBatch, x, y);
		
	}

	
}
