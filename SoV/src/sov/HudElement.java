package sov;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class HudElement {

	Vector2 position;
	Texture texture;
	Sprite sprite;
	
	public HudElement(Vector2 position, Texture texture) {
		this.position = position;
		
		this.texture = texture;
		sprite = new Sprite(texture);
		sprite.setPosition(position.x, position.y);
		
		
	}
	
	
	public void render(SpriteBatch spriteBatch, float x, float y) {
		
		sprite.setPosition(x, y);
		sprite.draw(spriteBatch);
	}
}
