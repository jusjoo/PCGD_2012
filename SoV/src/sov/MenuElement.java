package sov;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import sov.HudElement;


public class MenuElement extends HudElement {
	
	ArrayList<MenuItem> menuItems;
	ArrayList<HudElement> hudElements;
	MenuItem selected;

	public MenuElement(Vector2 position, Texture texture) {
		super(position, texture);
		
		menuItems = new ArrayList<MenuItem>();
		hudElements = new ArrayList<HudElement>();
	}
	
	public void addItem(MenuItem item) {
		if (menuItems.isEmpty()) {
			selected = item;
		}
		menuItems.add(item);		
	}	
	
	public void AddHudElement(HudElement element) {
		hudElements.add(element);
	}
	
	public void selectNext() {
		
		int nextIndex = menuItems.indexOf(selected) + 1;
		
		if (menuItems.size() > nextIndex) {
			selected = menuItems.get(nextIndex);
		}
		
	}
	
	public void selectPrevious() {
		int nextIndex = menuItems.indexOf(selected) - 1;
		
		if (nextIndex >= 0) {
			selected = menuItems.get(nextIndex);
		}
	}
	
	public void render(SpriteBatch spriteBatch, float x, float y) {
		super.render(spriteBatch, x, y);
		
		for (MenuItem item : menuItems) {
			if (item == selected) {
				item.renderSelected(spriteBatch, x, y);
			} else {
				item.render(spriteBatch, x, y);
			}
		
		}
		for (HudElement el : hudElements) {
			el.render(spriteBatch, x, y);
		}
	}
	

}
