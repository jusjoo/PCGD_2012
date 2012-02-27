package sov;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class GameHud {

	CoffeeGDX game;
	ArrayList<HudElement> elements;
		
	private MenuElement activeMenuElement;
	private MenuElement mainMenuElement;
	private MenuElement chargenMenuElement;
	boolean mainMenuActive = false;
	private Sprite menuBackground;
	private SpriteBatch menuBatch;
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
	
	
	private Sound menuMoveSound;
	private Sound menuConfirmSound;
	private Sound startGameSound;
	private Sound menuBackSound;
	
	private boolean startGameSoundPlayed = false;
	
	public GameHud(CoffeeGDX game) {
		this.game = game;
			
		elements = new ArrayList<HudElement>();		
		initMainMenu();
				
		menuMoveSound = Gdx.audio.newSound(new FileHandle(GameConfiguration.menuMoveSoundFile));
		menuConfirmSound = Gdx.audio.newSound(new FileHandle(GameConfiguration.menuConfirmSoundFile));
		startGameSound = Gdx.audio.newSound(new FileHandle(GameConfiguration.startGameSoundFile));
		menuBackSound = Gdx.audio.newSound(new FileHandle(GameConfiguration.menuBackSoundFile));
	}
	
	private void initHud() {
		if(game.map != null) {
			Texture tex = new Texture(new FileHandle("assets/menu/hudForeground.png"));
			HudElement hudElement = new HudElement(new Vector2(0,0), tex);
			//hud foreground
		
		
			elements.add(playerHealthBar);
			elements.add(playerStaminaBar);
			elements.add(playerManaBar);
			elements.add(hudElement);
		}
		
		
	}

	private void initMainMenu() {
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
		
		Texture backgroundTexture = new Texture(new FileHandle("assets/menu/menubackground.jpg"));
		menuBackground = new Sprite(backgroundTexture);
		menuBatch = new SpriteBatch();
				
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
			spriteBatch.begin();
			//System.out.println(Gdx.graphics.getWidth());
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
		playerHealthBar = new HudBarElement(new Vector2(4,8), texture, new Vector2(0,0), new Vector2(32,64), player.body.getHitPoints(), Color.GREEN, true);
		playerStaminaBar = new HudBarElement(new Vector2(224,8), texture, new Vector2(0,0), new Vector2(64,32), player.getStamina(), new Color(0.9f, 0.8f, 0.0f,1.0f), true);
		playerManaBar = new HudBarElement(new Vector2(476,8), texture, new Vector2(0,0), new Vector2(32,64), player.getMana(), new Color(0.40f, 0.69f, 1.0f, 1.0f), true);
		
//		playerHealthBar = new HudBarElement(new Vector2(4,7), texture, new Vector2(0,0), new Vector2(31,47), player.body.getHitPoints());
//		playerStaminaBar = new HudBarElement(new Vector2(227,6), texture, new Vector2(0,0), new Vector2(58,33), player.getStamina());
//		playerManaBar = new HudBarElement(new Vector2(477,7), texture, new Vector2(0,0), new Vector2(31,47), player.getMana());
		
		player.body.addHealthBar(playerHealthBar.bar);
		
		initHud();		
	}

	public void update(float deltaTime) {
		if(game.map != null) {
			playerHealthBar.bar.setCurrentValue(player.body.getHitPoints());	
			playerStaminaBar.bar.setCurrentValue(player.getStamina());
			playerManaBar.bar.setCurrentValue(player.getMana());
			
			playerHealthBar.bar.setMaxValue(player.body.getHitPointsMax());
			playerStaminaBar.bar.setMaxValue(player.getStaminaMax());
			playerManaBar.bar.setMaxValue(player.getManaMax());
		}
	}
	
	public void removeElement(HudElement hudElement) {
		elements.remove(hudElement);
		menuBatch.dispose();
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	}
	
	public void toggleMainMenu() {
		if (mainMenuActive == false) {
			//System.out.println("debug");
			//menuBackground.setPosition(0, 0);
			//menuBatch.begin();
			//menuBackground.draw(menuBatch);
			//menuBatch.end();
			activeMenuElement = mainMenuElement;
			this.addElement(activeMenuElement);
			game.paused = true;			
			mainMenuActive = true;	
			startGameSoundPlayed = false;
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
				menuMoveSound.play();
		}
		
		if (Gdx.input.isKeyPressed(GameConfiguration.moveRight) || 
				Gdx.input.isKeyPressed(GameConfiguration.moveDown)) {
				game.keyPressed();
				activeMenuElement.selectNext();
				menuMoveSound.play();
		}
		
		if (Gdx.input.isKeyPressed(GameConfiguration.activateMenu)) {			
			game.keyPressed();
			
			//Main menu elements
			if (activeMenuElement.selected.equals(play)) {
				this.removeElement(activeMenuElement);
				activeMenuElement = chargenMenuElement;
				this.addElement(chargenMenuElement);
				menuConfirmSound.play();
				
			}
			else if (activeMenuElement.selected.equals(hiscore)) {
				menuConfirmSound.play();
			}
			else if (activeMenuElement.selected.equals(quit)) {
				GameConfiguration.instance.exit();
			}
			else if (activeMenuElement.selected.equals(back)) {
				this.removeElement(activeMenuElement);
				this.addElement(mainMenuElement);
				this.activeMenuElement = mainMenuElement;
				menuBackSound.play();			
			}
			
			//Chargen menu elements
			else if (activeMenuElement.selected.equals(barbarian)) {
				playStartGameSound();
				
				if (game.inMenu) {
					game.createNewGame("barbarian_village_hollowed.tmx");
					game.inMenu = false;
				} else {
					game.changeMap("barbarian_village_hollowed.tmx");
					toggleMainMenu();
				}
				
			}
			else if (activeMenuElement.selected.equals(ninja)) {
				playStartGameSound();
				
				if (game.inMenu) {
					game.createNewGame("barbarian_cave_hollowed.tmx");
					game.inMenu = false;
				} else {
					game.changeMap("barbarian_cave_hollowed.tmx");
					toggleMainMenu();
				}
				
				
			}
			else if (activeMenuElement.selected.equals(sorceress)) {
				playStartGameSound();

				if (game.inMenu) {
					game.createNewGame("barbarian_cave_hollowed.tmx");
					game.inMenu = false;
				} else {
					game.changeMap("barbarian_cave_hollowed.tmx");
					toggleMainMenu();
				}
			
			}
			
			
		}
	
	}
	
	private void playStartGameSound() {
		if (!startGameSoundPlayed) {
			startGameSound.play();
			startGameSoundPlayed = true;
		}
	}
}
