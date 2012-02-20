package sov;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class GameHud {

	CoffeeGDX game;
	ArrayList<HudElement> elements;
	
	private MenuElement mainMenuElement;
	boolean mainMenuActive = false;
	private HudBarElement playerHealthBar;
	private HudBarElement playerStaminaBar;
	private HudBarElement playerManaBar;
	private Creature player;
	
	public GameHud(CoffeeGDX game) {
		this.game = game;
		elements = new ArrayList<HudElement>();
		
		// define the main menu element
		Texture texture = new Texture(new FileHandle("assets/menu/mainMenuBackground.png"));
		Vector2 position = new Vector2(0, 0);
		mainMenuElement = new MenuElement(position, texture);
		
		// add some selectables to it
		MenuItem item1 = new MenuItem(new Texture(new FileHandle("assets/menu/menuItem.png")), 
				new Texture(new FileHandle("assets/menu/menuItemSelected.png")), 
				new Vector2(300, 250));
		
		MenuItem item2 = new MenuItem(new Texture(new FileHandle("assets/menu/menuItem.png")), 
				new Texture(new FileHandle("assets/menu/menuItemSelected.png")), 
				new Vector2(300, 400));
		
		MenuItem item3 = new MenuItem(new Texture(new FileHandle("assets/menu/menuItem.png")), 
				new Texture(new FileHandle("assets/menu/menuItemSelected.png")), 
				new Vector2(300, 550));
		
		mainMenuElement.addItem(item1);
		mainMenuElement.addItem(item2);
		mainMenuElement.addItem(item3);
		
		mainMenuElement.selectPrevious();
		
		
		
	}
	
	/*
	 * Takes in the camera's position in the world, and translates
	 * it to screen (x,y) for the elements. Elements will then have 
	 * (0,0) on the top left corner of the screen 
	 */
	public void render(SpriteBatch spriteBatch, float camX, float camY) {
		
		float x = camX - Gdx.graphics.getWidth() / 4;
		float y = camY + Gdx.graphics.getHeight() / 4;
		
		//System.out.println(Gdx.graphics.getWidth());
		
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
		this.player = player;
		
		Texture texture = new Texture(new FileHandle("assets/creatures/mrEggEverything.png"));
		Vector2 position = new Vector2(0, 0);
		playerHealthBar = new HudBarElement(position, texture, new Vector2(0,0), new Vector2(64,8), player.body.getHitPoints());
		playerStaminaBar = new HudBarElement(new Vector2(0,12), texture, new Vector2(0,0), new Vector2(64,8), player.getStamina());
		playerManaBar = new HudBarElement(new Vector2(0,24), texture, new Vector2(0,0), new Vector2(64,8), player.getMana());
		
		player.body.addHealthBar(playerHealthBar.bar);
		
		
		elements.add(playerHealthBar);
		elements.add(playerStaminaBar);
		elements.add(playerManaBar);
	}

	public void update(float deltaTime) {
		playerHealthBar.bar.setCurrentValue(player.body.getHitPoints());
		playerStaminaBar.bar.setCurrentValue(player.getStamina());
		playerManaBar.bar.setCurrentValue(player.getMana());
	}
	public void removeElement(HudElement hudElement) {
		elements.remove(hudElement);
	}
	
	public void toggleMainMenu() {
		if (mainMenuActive == false) {
			//System.out.println("debug");
			this.addElement(mainMenuElement);
			game.paused = true;
			mainMenuActive = true;
		} else {
			this.removeElement(mainMenuElement);
			game.paused = false;
			mainMenuActive = false;
		}
		
	}

	public void handleMenuInput() {
		
		if (Gdx.input.isKeyPressed(GameConfiguration.moveLeft) || 
				Gdx.input.isKeyPressed(GameConfiguration.moveUp)) {
				game.keyPressed();
				mainMenuElement.selectPrevious();
		}
		
		if (Gdx.input.isKeyPressed(GameConfiguration.moveRight) || 
				Gdx.input.isKeyPressed(GameConfiguration.moveDown)) {
				game.keyPressed();
				mainMenuElement.selectNext();
		}
		
	}
	
}
