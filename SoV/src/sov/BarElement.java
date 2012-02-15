package sov;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class BarElement {

	private float currentValue;
	private float maxValue;
	
	
	private int barPositionX;
	private int barPositionY;
	private int barSizeX;
	private int barSizeY;

	public BarElement(Vector2 barPosition, Vector2 barSize, float maxValue) {
		
		
		this.barPositionX = (int)barPosition.x;
		this.barPositionY = (int)barPosition.y;
		
		this.barSizeX =	(int)barSize.x;
		this.barSizeY = (int)barSize.y;
		
		this.maxValue = maxValue;
		this.currentValue = maxValue;

	}

	public void setCurrentValue(float value) {
		currentValue = value;
	}
	
	public void render(SpriteBatch spriteBatch, float x, float y) {
						
		Sprite bar = getBar();
		bar.setPosition(x + barPositionX, 
						y - barPositionY - barSizeY);
		bar.draw(spriteBatch);
		
	}
	
	private Sprite getBar() {
		
		
		Pixmap pixmap = new Pixmap(barSizeX, barSizeY, Pixmap.Format.RGB565);
		pixmap.setColor(Color.BLACK);
		pixmap.fillRectangle(0, 0, barSizeX, barSizeY);
		pixmap.setColor(Color.GREEN);
		if(currentValue > 0) {
			pixmap.fillRectangle(0, 0, (int) ((int) barSizeX*getPercentage()), barSizeY);
		}
		
		Texture texture = new Texture(pixmap);
		Sprite sprite = new Sprite(texture);
		
		return sprite;
		
	}
	
	private float getPercentage() {
		return currentValue/maxValue;
	}
}
