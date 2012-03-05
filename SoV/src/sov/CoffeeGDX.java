package sov;

import sov.Creature.CreatureType;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class CoffeeGDX implements ApplicationListener {
		
	GameConfiguration config;
	
	Vector2 oldPosition = new Vector2(0,0);
	
	Texture spritesTexture = null;
	SpriteBatch spriteBatch = null;
	SpriteBatch menuBatch = null;
	
	
	//DynamicObjectFactory dynamicObjectFactory;
	
	public boolean canPressKey = true;
	public float inputTimer = 0;
	Box2DDebugRenderer debugRenderer;
	
	GameMap map;
	GameHud hud;
	
	TiledMap map2;
	
	RayHandler rayHandler;
	
	PointLight playerLight;
	
	
	World world;
	
	OrthographicCamera cam;

	private HudElement mainMenuElement;

	boolean paused;
	
	boolean inMenu;
	TextElement hudText;
	TextElement version;
	
	boolean hudTextDisplay = false;

	

	@Override
	public void create() {
		
		config = new GameConfiguration();
		cam = new OrthographicCamera(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
		cam.position.x = Gdx.graphics.getWidth()/4;
		cam.position.y = Gdx.graphics.getHeight()/4;
		cam.update();
		hud = new GameHud(this);
		hud.toggleMainMenu();
		
				
		//cam.rotate(45, 0, 0, 1);
		debugRenderer = new Box2DDebugRenderer();
		spriteBatch = new SpriteBatch();
		
		//cam.apply(Gdx.gl10);
		menuBatch.setProjectionMatrix(cam.combined);
		spriteBatch.setProjectionMatrix(cam.combined);
		
		inMenu = true;
		
		hudText = new TextElement(0, 200);		
		version = new TextElement(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()-32);
		hud.textElements.add(hudText);
		hud.textElements.add(version);
		
		version.print("SoV v0.4");
		
	}	
	
	public void createNewGame(String mapName) {
		hud.toggleMainMenu();
		world = new World(new Vector2(0.0f,GameConfiguration.physicsWorldGravity), true);
		map = new GameMap(mapName, world);
		hud.setPlayer(map.getPlayer());		
		world.setContactListener(new MyContactListener());

		
		
		// Add player
		//Player(CreatureType.Barbarian, new Vector2(300, 250));
		
		rayHandler = new RayHandler(world);
		//rayHandler.setAmbientLight(new Color(0f, 0f, 0f, 0.25f));
		rayHandler.setCombinedMatrix(cam.combined.scale(GameConfiguration.PIXELS_PER_METER, GameConfiguration.PIXELS_PER_METER,
				GameConfiguration.PIXELS_PER_METER).translate(0.0f, 0.0f, 0));
		
		playerLight = new PointLight(rayHandler, 40, new Color(1,1,1,0.95f), 4.0f, 0f, 0f);
		//playerLight.setSoftnessLenght(3.0f);
		//playerLight.setSoftnessLenght(0);
		//playerLight.setSoft(true);
		//playerLight.setStaticLight(true);
		//playerLight.setSoft(true);
		//playerLight.setXray(true);
		//playerLight.attachToBody(player.getComponent(BodyComponent.class).body, 0, 1f);
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void render() {

		Gdx.graphics.setTitle(Integer.toString(Gdx.graphics.getFramesPerSecond()));
		
		// handle the input
		if (canPressKey) {
			handleInput();
		}
			
		update();
		// Update camera
		cam.update();
		
		
		
		
		// spriteBatch.disableBlending();
		// colourMultiplier dictates how bright the sky is
		float colourMultiplier = 1;
		Gdx.gl.glClearColor(0.5f*colourMultiplier, 0.7f*colourMultiplier, 0.88f*colourMultiplier, 1.0f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		float interpolationAmount = GameConfiguration.interpolationAmount;
		
		if(map != null) {
			// Perform camera interpolation on player location
			oldPosition = new Vector2(cam.position.x, cam.position.y);
			Creature player = map.getPlayer();
		
			Vector2 newPosition = new Vector2(	oldPosition.x + interpolationAmount*(player.getPosition().x - oldPosition.x),
											oldPosition.y + interpolationAmount*(player.getPosition().y - oldPosition.y));
			cam.position.set(newPosition.x, newPosition.y, 0);
		
			cam.apply(Gdx.gl10);
		
			map.render(cam, spriteBatch);
			
			// Debug render
			if (GameConfiguration.debugMode) {
				debugRenderer.render(world, cam.combined.scale(GameConfiguration.PIXELS_PER_METER, GameConfiguration.PIXELS_PER_METER,
						GameConfiguration.PIXELS_PER_METER).translate(0.25f, -0.25f, 0));
			}
			
			hud.render(spriteBatch, cam.position.x, cam.position.y);
		}
		else {
			hud.render(spriteBatch, Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight()/4);
		}
		
		for (TextElement text : hud.textElements) {
			if (text.isVisible())
				text.render(spriteBatch);	
		}		
		
		// TODO: Lighting renderer
		/*if (GameConfiguration.lightRendering) {
			/*rayHandler.setCombinedMatrix(cam.combined.scale(GameConfiguration.PIXELS_PER_METER, GameConfiguration.PIXELS_PER_METER,
			GameConfiguration.PIXELS_PER_METER).translate(0.25f, -0.25f, 0));
			rayHandler.setCombinedMatrix(cam.combined.scale(GameConfiguration.PIXELS_PER_METER, GameConfiguration.PIXELS_PER_METER,
			GameConfiguration.PIXELS_PER_METER).translate(0.5f, -0.25f, 0f));
			rayHandler.updateAndRender();
		}*/
		
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}
	
	public void update() {
		float deltaTime = Gdx.graphics.getDeltaTime();
						
		if (map !=null && !paused) {
			map.update(deltaTime);
			world.step(Gdx.app.getGraphics().getDeltaTime(), 3, 3);
			hud.update(deltaTime);
		}
		
		if (!canPressKey) {
			inputTimer -= deltaTime;
			if (inputTimer < 0)
				canPressKey = true;
		}
		
		
		/*
		if(Math.random() > 0.99) {
			addNinjaMonster();
			addGoblin();
		}*/
		
		//hudText.print("Moi taas! "+deltaTime);
		
	}
	
	/*
	 * Handles all the button inputs.
	 */
	public void keyPressed() {
		canPressKey = false;
		inputTimer = 0.2f;
	}
	
	public void handleInput() {
		if (!inMenu && Gdx.input.isKeyPressed(GameConfiguration.escapeKey)) {
			keyPressed();
			hud.toggleMainMenu();

		}
		
		if (GameConfiguration.devMode) handleDebugInput();
		
		if (hud.mainMenuActive){
			hud.handleMenuInput();
		}
		
		if(hud.winScreenActive && Gdx.input.isKeyPressed(GameConfiguration.activateMenu)) {
			hud.exitWinScreen();
		}
	}
	


	public void handleDebugInput() {
		
		if (Gdx.input.isKeyPressed(GameConfiguration.debugRenderKey)) {
			keyPressed();
			if (GameConfiguration.debugMode) {
				GameConfiguration.debugMode = false;				
			} else GameConfiguration.debugMode = true;
		}
		
		if (Gdx.input.isKeyPressed(GameConfiguration.selectBarbarianKey)) {
			keyPressed();
			Vector2 pos = map.getPlayer().getPosition();
			map.removeCreature(map.getPlayer());
			//map.getPlayer().removeFromWorld();
			createPlayer(CreatureType.Barbarian, pos);
			map.setAItargets(map.getPlayer());
			System.out.println(map.getPlayer().getComponent(AIComponent.class));
			hud.setPlayer(map.getPlayer());
		}
		if (Gdx.input.isKeyPressed(GameConfiguration.selectNinjaKey)) {
			keyPressed();
			Vector2 pos = map.getPlayer().getPosition();
			map.removeCreature(map.getPlayer());
			Creature player = createPlayer(CreatureType.Ninja, pos);
			map.setAItargets(map.getPlayer());
			//player.addComponent(new AttackComponent(player, 0.8f, 0.5f, 0.2f, SpriteComponent.AnimationState.Attack1 ));
			System.out.println(map.getPlayer().getComponent(AIComponent.class));
			hud.setPlayer(map.getPlayer());
		}
		if (Gdx.input.isKeyPressed(GameConfiguration.selectSorceressKey)) {
			keyPressed();
			Vector2 pos = map.getPlayer().getPosition();
			map.removeCreature(map.getPlayer());
			Creature player = createPlayer(CreatureType.Sorceress, pos);
			map.setAItargets(map.getPlayer());
			System.out.println(map.getPlayer().getComponent(AIComponent.class));
			hud.setPlayer(map.getPlayer());
			
			
		}
		
		if (Gdx.input.isKeyPressed(GameConfiguration.mapSelectKey)) {
			keyPressed();
			changeMap("barbarian_village_hollowed.tmx");
		}
		
		if (Gdx.input.isKeyPressed(GameConfiguration.showStats)) {
			keyPressed();
			
			hudTextDisplay = !hudTextDisplay;
			hudText.print(map.getPlayer().getStatsAsString());
			
			if (hudTextDisplay) {
				hudText.setVisibility(true);
			}
			else {
				hudText.setVisibility(false);
			}
		}
		
		if (Gdx.input.isKeyPressed(GameConfiguration.giveExp10)) {
			keyPressed();
			map.getPlayer().getComponent(ExperienceComponent.class).giveExperience(50);			
		}
		if (Gdx.input.isKeyPressed(GameConfiguration.giveExp50)) {
			keyPressed();
			map.getPlayer().getComponent(ExperienceComponent.class).giveExperience(500);			
		}
		
		
	}
	
	public void changeMap(String newMap) {
		world.dispose();
		world = new World(new Vector2(0.0f,GameConfiguration.physicsWorldGravity), true);
		map.stopMusic();
		map = new GameMap(newMap, world);
		hud.setPlayer(map.getPlayer());
		world.setContactListener(new MyContactListener());
		if(hud.winScreenElement != null){
			hud.removeElement(hud.winScreenElement);
		}
	}
	
	/*
	 * Creates a new player with controls
	 */
	public Creature createPlayer(CreatureType playerClass, Vector2 position) {
				
		Creature player = map.factory.spawnCreature(world, playerClass, position);
		player.addComponent( new MovementComponent(player, player.speed, player.jumpHeight) );
		player.addComponent( new PlayerInputComponent(player) );
		
		//give player an experience component
		player.addComponent( new ExperienceComponent(player) );
		switch (playerClass) {
			case Barbarian:
				player.getComponent(ExperienceComponent.class).setStatBonuses(GameConfiguration.BarbarianLevelUpStr,
																				GameConfiguration.BarbarianLevelUpDex,
																				GameConfiguration.BarbarianLevelUpWis);
				break;
			case Ninja:
				player.getComponent(ExperienceComponent.class).setStatBonuses(GameConfiguration.NinjaLevelUpStr,
																				GameConfiguration.NinjaLevelUpDex,
																				GameConfiguration.NinjaLevelUpWis);
				break;
			case Sorceress:
				player.getComponent(ExperienceComponent.class).setStatBonuses(GameConfiguration.SorceressLevelUpStr,
																				GameConfiguration.SorceressLevelUpDex,
																				GameConfiguration.SorceressLevelUpWis);
				break;
			default:
				player.getComponent(ExperienceComponent.class).setStatBonuses(1.0f, 1.0f, 1.0f);
		}
		
		//give player an attack component
		map.addCreature(world, player);
		
		map.setPlayer(player);
		
		return player;
	}
	
	public void startApplication() {
		
	}
	
	public static void main (String[] args) {
		GameConfiguration.instance = new LwjglApplication(new CoffeeGDX(), "Game", GameConfiguration.windowSizeX, GameConfiguration.windowSizeY, false);
		 /// new LwjglApplication(new CoffeeGDX(), "Game", 800, 600, false);
}

}
