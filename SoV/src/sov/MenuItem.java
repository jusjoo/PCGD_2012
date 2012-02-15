package sov;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class MenuItem extends HudElement {
	
	Sprite selected;

	public MenuItem(Texture texture, Texture selectedTexture, Vector2 position) {
		super(position, texture);
		selected = new Sprite(selectedTexture);
	}
	
	public void renderSelected(SpriteBatch spriteBatch, float x, float y) {
		
		selected.setPosition(	x + (position.x / 2) , 
							y - (position.y / 2) - sprite.getHeight() );
		selected.draw(spriteBatch);
	}
}
