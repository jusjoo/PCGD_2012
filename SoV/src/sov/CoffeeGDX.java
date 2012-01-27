package sov;

import java.util.ArrayList;
import java.util.HashMap;

import sov.SpriteComponent.AnimationState;
import sov.Creature.CreatureType;
import sov.GameMap.LayerType;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class CoffeeGDX implements ApplicationListener {
	
	GameConfiguration config;
	
	Vector2 oldPosition = new Vector2(0,0);
	
	Texture spritesTexture = null;
	SpriteBatch spriteBatch = null;
	
	
	
	Box2DDebugRenderer debugRenderer;
	
	GameMap map;
	
	TiledMap map2;
	
	
	World world;
	
	OrthographicCamera cam;

	@Override
	public void create() {
		
		config = new GameConfiguration();
		
		world = new World(new Vector2(0.0f,-10.0f), true);
		map = new GameMap(config.firstMap, world);
		
		cam = new OrthographicCamera(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
				
		//cam.rotate(45, 0, 0, 1);
		debugRenderer = new Box2DDebugRenderer();
		
		spriteBatch = new SpriteBatch();
		
		world.setContactListener(new MyContactListener());

		DynamicObjectFactory dynamicObjectFactory = new DynamicObjectFactory("assets/creatures");
		
		
		
		map.addCreature(world, dynamicObjectFactory.spawnCreature(world, CreatureType.Barbarian, new Vector2(200, 230)));
		map.addCreature(world, dynamicObjectFactory.spawnCreature(world, CreatureType.Goblin, new Vector2(100, 100)));
		map.addCreature(world, dynamicObjectFactory.spawnCreature(world, CreatureType.Sorceress, new Vector2(200, 100)));
		
		// Add player
		Creature creature = dynamicObjectFactory.spawnCreature(world, CreatureType.Barbarian, new Vector2(300, 250));
		creature.addComponent(new KeyboardInputComponent(creature, creature.getComponent(BodyComponent.class), creature.speed));
		
		//give player an attack component
		creature.addComponent(new AttackComponent(creature, 1f, 0.1f, 0.1f, AnimationState.Attack1 ));
		
		map.setPlayer(creature);
		map.addCreature(world, creature);
		
	
		
		
		// Add a monster that follows the player
		Creature monster = dynamicObjectFactory.spawnCreature(world, CreatureType.Goblin, new Vector2(100, 250));
		monster.addComponent(new AIComponent(monster, monster.getComponent(BodyComponent.class), monster.speed));
		monster.getComponent(AIComponent.class).setToFollow(creature.getComponent(BodyComponent.class));
		map.addCreature(world, monster);
		
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void render() {

		Gdx.graphics.setTitle(Integer.toString(Gdx.graphics.getFramesPerSecond()));
		
		update();
		// Update camera
		cam.update();
		
		
		// spriteBatch.disableBlending();
		// colourMultiplier dictates how bright the sky is
		float colourMultiplier = 1;
		Gdx.gl.glClearColor(0.5f*colourMultiplier, 0.7f*colourMultiplier, 0.88f*colourMultiplier, 1.0f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		float interpolationAmount = config.interpolationAmount;
		
		// Perform camera interpolation on player location
		oldPosition = new Vector2(cam.position.x, cam.position.y);
		Creature player = map.getPlayer();
		
		Vector2 newPosition = new Vector2(	oldPosition.x + interpolationAmount*(player.getPosition().x - oldPosition.x),
											oldPosition.y + interpolationAmount*(player.getPosition().y - oldPosition.y));
		cam.position.set(newPosition.x, newPosition.y, 0);
		
		cam.apply(Gdx.gl10);
		
		map.render(cam, spriteBatch);
		
		

		// Debug render
		if (config.debugMode) {
			debugRenderer.render(world, cam.combined.scale(GameConfiguration.PIXELS_PER_METER, GameConfiguration.PIXELS_PER_METER,
					GameConfiguration.PIXELS_PER_METER).translate(0.25f, -0.25f, 0));
		}

		
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
	
	
	public static void main (String[] args) {
       // new LwjglApplication(new CoffeeGDX(), "Game", 1024, 768, false);
		  new LwjglApplication(new CoffeeGDX(), "Game", 800, 600, false);
}

}
