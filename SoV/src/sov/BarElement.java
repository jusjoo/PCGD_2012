package sov;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class BarElement {

	private float currentValue;
	private float maxValue;
	private Color fillColor = Color.GREEN;
	private Color backgroundColor = Color.BLACK;	
	

	private HashMap<Integer, Sprite> savedStates;
	
	private int barPositionX;
	private int barPositionY;
	private int barSizeX;
	private int barSizeY;
	private boolean verticalRender = false;
	
	

	public BarElement(Vector2 barPosition, Vector2 barSize, float maxValue) {
		
		
		this.barPositionX = (int)barPosition.x;
		this.barPositionY = (int)barPosition.y;
		
		this.barSizeX =	(int)barSize.x;
		this.barSizeY = (int)barSize.y;
		
		this.maxValue = maxValue;
		this.currentValue = maxValue;		

		savedStates = new HashMap<Integer, Sprite>();

	}
	public BarElement(Vector2 barPosition, Vector2 barSize, float maxValue, Color color) {
		this.barPositionX = (int)barPosition.x;
		this.barPositionY = (int)barPosition.y;
		
		this.barSizeX =	(int)barSize.x;
		this.barSizeY = (int)barSize.y;
		
		this.maxValue = maxValue;
		this.currentValue = maxValue;		

		savedStates = new HashMap<Integer, Sprite>();
		
		this.fillColor = color;		
	}
	public BarElement(Vector2 barPosition, Vector2 barSize, float maxValue, Color color, boolean verticalRender) {
		this.barPositionX = (int)barPosition.x;
		this.barPositionY = (int)barPosition.y;
		
		this.barSizeX =	(int)barSize.x;
		this.barSizeY = (int)barSize.y;
		
		this.maxValue = maxValue;
		this.currentValue = maxValue;		

		savedStates = new HashMap<Integer, Sprite>();
		
		this.fillColor = color;
		this.verticalRender = verticalRender;
	}

	public void setCurrentValue(float value) {
		currentValue = value;
	}
	
	public void render(SpriteBatch spriteBatch, float x, float y) {
		
			Sprite bar;
			
			if (verticalRender) {
				bar = getBarVertical();
			}
			else {
				bar = getBar();
			}
			
			bar.setPosition(x + barPositionX,y - barPositionY - barSizeY);
			bar.draw(spriteBatch);		
	}
	
	private Sprite getBarVertical() {
		int barHeight = (int) Math.ceil((barSizeY * getPercentage()));
		// Check that the wanted sprite doesn't exist already
		Sprite sprite = savedStates.get(barHeight);
		if (sprite != null) {
			return sprite;
		} else {
			Pixmap pixmap = new Pixmap(barSizeX, barSizeY, Pixmap.Format.RGB565);
			
			
			pixmap.setColor( backgroundColor );
			pixmap.fillRectangle(0, 0, barSizeX, barSizeY);
			pixmap.setColor(fillColor);
			
			// get the rounded bar width, so we don't have to save every float value in savedStates
			
			if(currentValue > 0) {
				
				pixmap.fillRectangle(0, (barSizeY-barHeight), barSizeX, barHeight);
			}
			
			Texture texture = new Texture(pixmap);
			sprite = new Sprite(texture);
			
			savedStates.put(barHeight, sprite);
			
			return sprite;
		}
	}
	
	private Sprite getBar() {
		int barWidth = (int) Math.ceil((barSizeX * getPercentage()));
		// Check that the wanted sprite doesn't exist already
		Sprite sprite = savedStates.get(barWidth);
		if (sprite != null) {
			return sprite;
		} else {
			Pixmap pixmap = new Pixmap(barSizeX, barSizeY, Pixmap.Format.RGB565);
			pixmap.setColor(Color.BLACK);
			pixmap.fillRectangle(0, 0, barSizeX, barSizeY);
			pixmap.setColor(Color.GREEN);
			
			// get the rounded bar width, so we don't have to save every float value in savedStates
			
			if(currentValue > 0) {
				
				pixmap.fillRectangle(0, 0, barWidth, barSizeY);
			}
			
			Texture texture = new Texture(pixmap);
			sprite = new Sprite(texture);
			
			savedStates.put(barWidth, sprite);
			
			return sprite;
		}
	}
	
	private float getPercentage() {
		return currentValue/maxValue;
	}
}
