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
		
	private MenuElement activeMenuElement;
	private MenuElement mainMenuElement;
	private MenuElement chargenMenuElement;
	boolean mainMenuActive = false;
	private HudBarElement playerHealthBar;
	private HudBarElement playerStaminaBar;
	private HudBarElement playerManaBar;
	private Creature player;
	
	private MenuItem play;
	private MenuItem hiscore;
	private MenuItem quit;
	private MenuItem back;
	private MenuItem barbarian;
	private MenuItem ninja;
	private MenuItem sorceress;
	
	public GameHud(CoffeeGDX game) {
		this.game = game;
		elements = new ArrayList<HudElement>();		
		
		// define the main menu element
		Texture texture = new Texture(new FileHandle("assets/menu/logo_v3.png"));
		Vector2 position = new Vector2(0, 0);
		mainMenuElement = new MenuElement(position, texture);		
		
		// add some selectables to it
		play = new MenuItem(new Texture(new FileHandle("assets/menu/menuPlayNormal.png")), 
				new Texture(new FileHandle("assets/menu/menuPlaySelected.png")), 
				new Vector2(300, 250));
		
		hiscore = new MenuItem(new Texture(new FileHandle("assets/menu/menuHiScoreNormal.png")), 
				new Texture(new FileHandle("assets/menu/menuHiScoreSelected.png")), 
				new Vector2(300, 400));
		
		quit = new MenuItem(new Texture(new FileHandle("assets/menu/menuQuitNormal.png")), 
				new Texture(new FileHandle("assets/menu/menuQuitSelected.png")), 
				new Vector2(300, 550));
					
		mainMenuElement.addItem(play);
		mainMenuElement.addItem(hiscore);
		mainMenuElement.addItem(quit);
		
		/*
		 * Sub-menu
		 */
		
		chargenMenuElement = new MenuElement(position,texture);
		HudElement sub1 = new HudElement( new Vector2(0,0), new Texture(new FileHandle("assets/menu/menuSelectCharacter.png")) );
		barbarian = new MenuItem(new Texture(new FileHandle("assets/menu/menuBarbarianNormal.png")), 
				new Texture(new FileHandle("assets/menu/menuBarbarianSelected.png")), 
				new Vector2(200, 300));
		ninja = new MenuItem(new Texture(new FileHandle("assets/menu/menuNinjaNormal.png")), 
				new Texture(new FileHandle("assets/menu/menuNinjaSelected.png")), 
				new Vector2(300, 300));
		sorceress = new MenuItem(new Texture(new FileHandle("assets/menu/menuSorceressNormal.png")), 
				new Texture(new FileHandle("assets/menu/menuSorceressSelected.png")), 
				new Vector2(400, 300));
		back = new MenuItem(new Texture(new FileHandle("assets/menu/menuBackNormal.png")), 
				new Texture(new FileHandle("assets/menu/menuBackSelected.png")), 
				new Vector2(300, 550));
		
		chargenMenuElement.AddHudElement(sub1);
		chargenMenuElement.addItem(barbarian);
		chargenMenuElement.addItem(ninja);
		chargenMenuElement.addItem(sorceress);
		chargenMenuElement.addItem(back);
		
		
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
			activeMenuElement = mainMenuElement;
			this.addElement(activeMenuElement);
			game.paused = true;			
			mainMenuActive = true;			
		} else {
			this.removeElement(activeMenuElement);
			game.paused = false;
			mainMenuActive = false;
		}
		
	}

	public void handleMenuInput() {
		
		if (Gdx.input.isKeyPressed(GameConfiguration.moveLeft) || 
				Gdx.input.isKeyPressed(GameConfiguration.moveUp)) {
				game.keyPressed();
				activeMenuElement.selectPrevious();
		}
		
		if (Gdx.input.isKeyPressed(GameConfiguration.moveRight) || 
				Gdx.input.isKeyPressed(GameConfiguration.moveDown)) {
				game.keyPressed();
				activeMenuElement.selectNext();
		}
		
		if (Gdx.input.isKeyPressed(GameConfiguration.activateMenu)) {			
			game.keyPressed();
			if (activeMenuElement.selected.equals(play)) {
				System.out.println("play!!!");
				this.removeElement(activeMenuElement);
				activeMenuElement = chargenMenuElement;
				this.addElement(chargenMenuElement);				
				
			}
			else if (activeMenuElement.selected.equals(hiscore)) {
				
			}
			else if (activeMenuElement.selected.equals(quit)) {
				
			}
			else if (activeMenuElement.selected.equals(barbarian)) {
				
			}
			else if (activeMenuElement.selected.equals(back)) {
				this.removeElement(activeMenuElement);
				this.addElement(mainMenuElement);
				this.activeMenuElement = mainMenuElement;
				
			}
			
		}
		
	}
	
}
