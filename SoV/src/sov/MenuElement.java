package sov;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import sov.HudElement;


public class MenuElement extends HudElement {
	
	ArrayList<MenuItem> menuItems;
	MenuItem selected;

	public MenuElement(Vector2 position, Texture texture) {
		super(position, texture);
		
		menuItems = new ArrayList<MenuItem>();
	}
	
	public void addItem(MenuItem item) {
		menuItems.add(item);
	}
	
	public void selectNext() {
		selected = menuItems.get( menuItems.indexOf(selected) + 1);
	}
	
	public void selectPrevious() {
		selected = menuItems.get( menuItems.indexOf(selected) - 1);
	}
	
	public void render(SpriteBatch spriteBatch, float x, float y) {
		super.render(spriteBatch, x, y);
		
		for (MenuItem item : menuItems) {
			item.render(spriteBatch, x, y);
		}
	}
	

}
