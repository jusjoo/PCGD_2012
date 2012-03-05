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
	
	ArrayList<TextElement> textElements;
		
	private MenuElement activeMenuElement;
	private MenuElement mainMenuElement;
	private MenuElement chargenMenuElement;
	boolean mainMenuActive = false;
	//private SpriteBatch menuBatch;
	private Sprite menuBackground;
	private HudBarElement playerHealthBar;
	private HudBarElement playerStaminaBar;
	private HudBarElement playerManaBar;
	
	private TextElement textStr;
	private TextElement textDex;
	private TextElement textWis;
	private TextElement score;
	private TextElement ammo;
	private TextElement exp;
	
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
	private TextElement level;
	boolean winScreenActive;
	HudElement winScreenElement;
	private TextElement winScreenText;
	public ArrayList<TextElement> floatingDamageTexts;
	public float damageTextTimer;
	
	public GameHud(CoffeeGDX game) {
		this.game = game;
		this.textElements = new ArrayList<TextElement>();
		floatingDamageTexts = new ArrayList<TextElement>();
		elements = new ArrayList<HudElement>();		
		initMainMenu();
				
		menuMoveSound = Gdx.audio.newSound(new FileHandle(GameConfiguration.menuMoveSoundFile));
		menuConfirmSound = Gdx.audio.newSound(new FileHandle(GameConfiguration.menuConfirmSoundFile));
		startGameSound = Gdx.audio.newSound(new FileHandle(GameConfiguration.startGameSoundFile));
		menuBackSound = Gdx.audio.newSound(new FileHandle(GameConfiguration.menuBackSoundFile));	
		
		GameConfiguration.hud = this;
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
			
			initText();
		}
		
		
	}

	private void initText() {

		// TODO: korjaa purkka!
		if (textElements.contains(textStr)) {
			textElements.remove(textStr);
		}
		if (textElements.contains(textDex)) {
			textElements.remove(textDex);
		}
		if (textElements.contains(textWis)) {
			textElements.remove(textWis);			
		}
		if (textElements.contains(level)) {
			textElements.remove(level);			
		}
		if (textElements.contains(exp)) {
			textElements.remove(exp);			
		}
		if (textElements.contains(ammo)) {
			textElements.remove(ammo);			
		}
		if (textElements.contains(score)) {
			textElements.remove(score);			
		}
		textStr = new TextElement(120, 10);
		textDex = new TextElement(120, 30);
		textWis = new TextElement(120, 50);
		level = new TextElement(600, 10);
		score = new TextElement(600, 30);
		exp = new TextElement(600, 50);
		ammo = new TextElement(600, 70);
		
		
		
		
		textElements.add(textStr);
		textElements.add(textDex);
		textElements.add(textWis);
		textElements.add(level);
		textElements.add(exp);
		textElements.add(ammo);
		textElements.add(score);		
				
		updateStatText();		
		
	}

	public void updateStatText() {
		
		Creature player = game.map.getPlayer();
		textStr.print("Strength: "+ (int)player.getStrength() );
		textDex.print("Dexterity: "+ (int)player.getDexterity());
		textWis.print("Wisdom: "+ (int)player.getWisdom());		
	}
	public void updateCommonText() {
		Creature player = game.map.getPlayer();
		
		level.print("Level: " + (int)player.getComponent(ExperienceComponent.class).getLevel());
		score.print("Score: " + (int)player.getComponent(ExperienceComponent.class).getScore());
		exp.print("Exp: " + (int)player.getComponent(ExperienceComponent.class).getExperience());
		ammo.print("Ammo: " +(int)player.getComponent(AttackComponent.class).getAmmo());
	}
	

	private void initMainMenu() {
		// define the main menu element
		Texture texture = new Texture(new FileHandle("assets/menu/logo_v3.png"));
		Vector2 position = new Vector2(0, 0);
		mainMenuElement = new MenuElement(position, texture);
		
		
		Texture menuTexture = new Texture(new FileHandle("assets/menu/menubackground.jpg"));
		menuBackground = new Sprite(menuTexture);
		menuBackground.setPosition(0, 0);
		game.menuBatch = new SpriteBatch();
		
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
		HudElement sub1 = new HudElement( new Vector2(285,220), new Texture(new FileHandle("assets/menu/menuSelectCharacter.png")) );
		barbarian = new MenuItem(new Texture(new FileHandle("assets/menu/menuBarbarianNormal.png")), 
				new Texture(new FileHandle("assets/menu/menuBarbarianSelected.png")), 
				new Vector2(Gdx.graphics.getWidth()/2-32 -200, 350));
		ninja = new MenuItem(new Texture(new FileHandle("assets/menu/menuNinjaNormal.png")), 
				new Texture(new FileHandle("assets/menu/menuNinjaSelected.png")), 
				new Vector2(Gdx.graphics.getWidth()/2-32 - 0, 350));
		sorceress = new MenuItem(new Texture(new FileHandle("assets/menu/menuSorceressNormal.png")), 
				new Texture(new FileHandle("assets/menu/menuSorceressSelected.png")), 
				new Vector2(Gdx.graphics.getWidth()/2-32 +200, 350));
		back = new MenuItem(new Texture(new FileHandle("assets/menu/menuBackNormal.png")), 
				new Texture(new FileHandle("assets/menu/menuBackSelected.png")), 
				new Vector2(300, 550));
		
		chargenMenuElement.AddHudElement(sub1);
		chargenMenuElement.addItem(barbarian);
		chargenMenuElement.addItem(ninja);
		chargenMenuElement.addItem(sorceress);
		chargenMenuElement.addItem(back);
		
		/*
		 * Hud text
		 */
		
		
		
	}

	/*
	 * Takes in the camera's position in the world, and translates
	 * it to screen (x,y) for the elements. Elements will then have 
	 * (0,0) on the top left corner of the screen 
	 */
	public void render(SpriteBatch spriteBatch, float camX, float camY) {
			float x = camX - Gdx.graphics.getWidth() / 4;
			float y = camY + Gdx.graphics.getHeight() / 4;
			if(game.map == null) {
				
				game.menuBatch.begin();
				menuBackground.draw(game.menuBatch);				
				game.menuBatch.end();				
			}
			
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
			
			if (player.statsUpdated())
				updateStatText();
			
			updateCommonText();
			updateDamageText(deltaTime);
		}
		
		
	}
	
	private void updateDamageText(float deltaTime) {
		if (damageTextTimer > 0){
			damageTextTimer -= deltaTime;
			
			for (TextElement text: floatingDamageTexts ){
				text.setColor(new Color(text.getColor().r, text.getColor().b, text.getColor().g, text.getColor().a - deltaTime/GameConfiguration.floatingDamageTextTimer));
				text.setPositionY(text.getPositionY()-deltaTime*50 );
			}
			
			if (damageTextTimer <= 0) {
				floatingDamageTexts.clear();
			}
		}
		
	}

	public void removeElement(HudElement hudElement) {
		elements.remove(hudElement);
		game.menuBatch.dispose();
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	}
	
	public void toggleMainMenu() {
		if (mainMenuActive == false) {
			//System.out.println("debug");
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

	public void activateWinScreen() {
		winScreenActive = true;
		game.paused = true;
		winScreenElement = new HudElement(new Vector2(0,0), new Texture(new FileHandle("assets/menu/menubackground.jpg")));
		winScreenText = new TextElement(200,200);
		
		
		
		this.textElements.add(winScreenText);
		winScreenText.print("Victorious! Your score is " + (int)player.getComponent(ExperienceComponent.class).getScore());
		this.addElement(winScreenElement);
	}
	
	public void exitWinScreen() {
		winScreenActive = false;
		//this.elements.remove(winScreenElement);
		this.textElements.remove(winScreenText);
		
		toggleMainMenu();
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
					game.createNewGame("ninja_jungle.tmx");
					game.inMenu = false;
				} else {
					game.changeMap("ninja_jungle.tmx");
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
