package sov;

import sov.Creature.CreatureType;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
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
	
	
	DynamicObjectFactory dynamicObjectFactory;
	
	Box2DDebugRenderer debugRenderer;
	
	GameMap map;
	
	TiledMap map2;
	
	RayHandler rayHandler;
	
	PointLight playerLight;
	
	
	World world;
	
	OrthographicCamera cam;

	@Override
	public void create() {
		
		config = new GameConfiguration();
		
		world = new World(new Vector2(0.0f,-10.0f), true);
		map = new GameMap(GameConfiguration.firstMap, world);
		
		
		
		cam = new OrthographicCamera(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
				
		//cam.rotate(45, 0, 0, 1);
		debugRenderer = new Box2DDebugRenderer();
		
		spriteBatch = new SpriteBatch();
		
		world.setContactListener(new MyContactListener());

		dynamicObjectFactory = new DynamicObjectFactory("assets/creatures");
		
		// Add player
		Creature player = createPlayer(CreatureType.Sorceress, new Vector2(300, 250));
	
		
		for(int i=0; i<20; i++) {
			int random = (int) (Math.random()*4);
			//System.out.println(random);
			CreatureType creatureType = null;
			switch(random) {
			case 0: { creatureType = CreatureType.Barbarian; break; }
			case 1: { creatureType = CreatureType.Goblin; break; }
			case 2: { creatureType = CreatureType.Sorceress; break; }
			case 3: { creatureType = CreatureType.Ninja; break; }
			}
			//System.out.println(creatureType);
			//Creature creature = dynamicObjectFactory.spawnCreature(world, creatureType,
			//		new Vector2(map.map.width*16f, map.map.height*16f));
			Creature creature = dynamicObjectFactory.spawnCreature(world, creatureType,
					new Vector2((float) Math.random()*600f, (float) Math.random()*600f));
			creature.addComponent(new MovementComponent(creature, creature.speed, creature.jumpHeight));
			creature.addComponent(new AIComponent(creature).setToFollow(player));
			map.addCreature(world, creature);
		}	
		
		
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
		playerLight.attachToBody(player.getComponent(BodyComponent.class).body, 0, 1f);
		
		
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void render() {

		Gdx.graphics.setTitle(Integer.toString(Gdx.graphics.getFramesPerSecond()));
		
		// handles the debug input, if devmode is on
		if (GameConfiguration.devMode) handleInput();
		
		update();
		// Update camera
		cam.update();
		
		
		
		// spriteBatch.disableBlending();
		// colourMultiplier dictates how bright the sky is
		float colourMultiplier = 1;
		Gdx.gl.glClearColor(0.5f*colourMultiplier, 0.7f*colourMultiplier, 0.88f*colourMultiplier, 1.0f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		float interpolationAmount = GameConfiguration.interpolationAmount;
		
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
		
		map.update(deltaTime);
		/*
		if(Math.random() > 0.99) {
			addNinjaMonster();
			addGoblin();
		}*/
		
		world.step(Gdx.app.getGraphics().getDeltaTime(), 3, 3);
	}
	
	/*
	 * Handles all the debug button inputs.
	 */
	public void handleInput() {
		
		if (Gdx.input.isKeyPressed(GameConfiguration.debugRenderKey)) {
			if (GameConfiguration.debugMode) {
				GameConfiguration.debugMode = false;
			} else GameConfiguration.debugMode = true;
		}
		
		if (Gdx.input.isKeyPressed(GameConfiguration.selectBarbarianKey)) {
			Vector2 pos = map.getPlayer().getPosition();
			map.removeCreature(map.getPlayer());
			createPlayer(CreatureType.Barbarian, pos);
		}
		if (Gdx.input.isKeyPressed(GameConfiguration.selectNinjaKey)) {
			Vector2 pos = map.getPlayer().getPosition();
			map.removeCreature(map.getPlayer());
			createPlayer(CreatureType.Ninja, pos);
		}
		if (Gdx.input.isKeyPressed(GameConfiguration.selectSorceressKey)) {
			Vector2 pos = map.getPlayer().getPosition();
			map.removeCreature(map.getPlayer());
			createPlayer(CreatureType.Sorceress, pos);
		}
		
		
	}
	
	/*
	 * Creates a new player with controls
	 */
	public Creature createPlayer(CreatureType playerClass, Vector2 position) {
		
		Creature player = this.dynamicObjectFactory.spawnCreature(world, playerClass, position);
		player.addComponent(new MovementComponent(player, player.speed, player.jumpHeight));
		player.addComponent(new PlayerInputComponent(player));
		
		//give player an attack component
		map.addCreature(world, player);
		player.addComponent(new AttackComponent(player, 0.8f, 0.5f, 0.2f, SpriteComponent.AnimationState.Attack1 ));
		map.setPlayer(player);
		
		return player;
	}

	
	public static void main (String[] args) {
        new LwjglApplication(new CoffeeGDX(), "Game", 1024, 768, false);
		 /// new LwjglApplication(new CoffeeGDX(), "Game", 800, 600, false);
}

}
