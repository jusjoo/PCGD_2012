package sov;

import java.util.ArrayList;
import java.util.HashMap;

import sov.AnimatedSprite.AnimationState;
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
				
		debugRenderer = new Box2DDebugRenderer();

		
		//int amountOfFrames = 5; 
		Texture spritesTexture = new Texture(new FileHandle("assets/creatures/sprites_human_barbarian.png"));
		HashMap<AnimatedSprite.AnimationState, Animation> spriteAnimations = new HashMap<AnimatedSprite.AnimationState, Animation>();
		ArrayList<TextureRegion> textureRegions = new ArrayList<TextureRegion>();
		TextureRegion frames[][] = TextureRegion.split(spritesTexture, 16, 32);
		textureRegions.add(frames[0][2]);
		//textureRegions.add(atlas.getRegion(object.gid));
		spriteAnimations.put(AnimationState.IDLE, new Animation(0.1f, textureRegions));
		textureRegions.clear();
		textureRegions.add(frames[0][3]);
		textureRegions.add(frames[0][4]);
		textureRegions.add(frames[0][5]);
		textureRegions.add(frames[0][6]);
		textureRegions.add(frames[0][7]);
		textureRegions.add(frames[0][8]);
		spriteAnimations.put(AnimationState.RUN, new Animation(0.1f, textureRegions));
		textureRegions.clear();
		textureRegions.add(frames[0][9]);
		spriteAnimations.put(AnimationState.JUMP, new Animation(0.1f, textureRegions));
		textureRegions.clear();
		textureRegions.add(frames[0][10]);
		spriteAnimations.put(AnimationState.FALL, new Animation(0.1f, textureRegions));
		
		//Attack
		TextureRegion subTexture = new TextureRegion(spritesTexture);
		subTexture.setRegion(0, 224, 360, 200);
		TextureRegion frames2[][] = subTexture.split(64, 48);
		textureRegions.clear();
		textureRegions.add(frames2[0][0]);
		textureRegions.add(frames2[0][1]);
		textureRegions.add(frames2[0][2]);
		textureRegions.add(frames2[0][3]);
		spriteAnimations.put(AnimationState.WEAPON_ATTACK, new Animation(0.1f, textureRegions));
		
		// DEATH
		textureRegions.clear();

		textureRegions.add(frames[0][1]);
		textureRegions.add(frames[0][2]);
		textureRegions.add(frames[0][3]);

		
		spriteAnimations.put(AnimationState.DIE, new Animation(0.1f, textureRegions));
		
		// Add some attack animations
		// TODO: For this we need a way to skip the first row, or to have another sprite file for attack animations		
		TextureRegion attackframes[][] = TextureRegion.split(spritesTexture, 64, 48);
		
		Player player = new Player(new Vector2(13f,30f), spriteAnimations, 0.8f, false, config, Player.CharacterClass.Barbarian);
		player.addToWorld(world, new Vector2(40f, 60f));
		
		map.addCreature(player);
				
		
		
		//int amountOfFrames = 5; 
		this.addNinjaMonster();
		this.addGoblin();
		
		spriteBatch = new SpriteBatch();
		
		world.setContactListener(new MyContactListener());

	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void render() {
		// Update all world entities
		update();
		// Update camera
		cam.update();
		
		// colourMultiplier dictates how bright the sky is
		float colourMultiplier = 1;
		Gdx.gl.glClearColor(0.5f*colourMultiplier, 0.7f*colourMultiplier, 0.88f*colourMultiplier, 1.0f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		float interpolationAmount = config.interpolationAmount;
		
		// Perform camera interpolation on player location
		oldPosition = new Vector2(cam.position.x, cam.position.y);
		Player player = map.getPlayer();
		
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
		
		if(Math.random() > 0.99) {
			addNinjaMonster();
			addGoblin();
		}
		
		world.step(Gdx.app.getGraphics().getDeltaTime(), 3, 3);
	}
	
	
	/*
	 * Adds a new goblin to the game
	 * 
	 * TODO: 	- add parameters for position
	 * 			- move into gameMap?
	 */
	public void addGoblin() {
		Texture monsterSpritesTexture = new Texture(new FileHandle("assets/creatures/sprites_monster_goblin.png"));
		HashMap<AnimatedSprite.AnimationState, Animation> monsterSpriteAnimations = new HashMap<AnimatedSprite.AnimationState, Animation>();
		ArrayList<TextureRegion> monsterTextureRegions = new ArrayList<TextureRegion>();
		TextureRegion monsterframes[][] = TextureRegion.split(monsterSpritesTexture, 48, 32);
		monsterTextureRegions.add(monsterframes[0][0]);

		//textureRegions.add(atlas.getRegion(object.gid));
		monsterSpriteAnimations.put(AnimationState.IDLE, new Animation(0.3f, monsterTextureRegions));
		monsterTextureRegions.clear();

		monsterTextureRegions.add(monsterframes[0][1]);
		monsterTextureRegions.add(monsterframes[0][2]);
		monsterTextureRegions.add(monsterframes[0][3]);

		
		monsterSpriteAnimations.put(AnimationState.RUN, new Animation(0.1f, monsterTextureRegions));
		monsterTextureRegions.clear();
		monsterTextureRegions.add(monsterframes[0][0]);
		monsterSpriteAnimations.put(AnimationState.JUMP, new Animation(0.1f, monsterTextureRegions));

		// DEATH
		monsterTextureRegions.clear();

		monsterTextureRegions.add(monsterframes[1][4]);
		monsterTextureRegions.add(monsterframes[2][0]);
		monsterTextureRegions.add(monsterframes[2][1]);
		monsterTextureRegions.add(monsterframes[2][2]);
		monsterTextureRegions.add(monsterframes[2][3]);
		monsterTextureRegions.add(monsterframes[2][4]);
		monsterTextureRegions.add(monsterframes[3][0]);
		monsterTextureRegions.add(monsterframes[3][1]);
		monsterTextureRegions.add(monsterframes[3][2]);
		monsterTextureRegions.add(monsterframes[3][3]);
		monsterTextureRegions.add(monsterframes[3][4]);
		
		monsterSpriteAnimations.put(AnimationState.DIE, new Animation(0.09f, monsterTextureRegions));
		
		Monster monster = new Monster(new Vector2(16f, 24f), monsterSpriteAnimations, 0.8f, false);
		monster.addToWorld(world, new Vector2((float)Math.random()*map.map.width*4, (float)Math.random()*map.map.height*4 + 32f));
		monster.setToFollow(map.player);
		
		map.addCreature(monster);
	}
	
	public void addNinjaMonster() {
		Texture monsterSpritesTexture = new Texture(new FileHandle("assets/creatures/sprites_human_ninja.png"));
		HashMap<AnimatedSprite.AnimationState, Animation> monsterSpriteAnimations = new HashMap<AnimatedSprite.AnimationState, Animation>();
		ArrayList<TextureRegion> monsterTextureRegions = new ArrayList<TextureRegion>();
		TextureRegion monsterframes[][] = TextureRegion.split(monsterSpritesTexture, 16, 32);
		monsterTextureRegions.add(monsterframes[0][0]);

		//textureRegions.add(atlas.getRegion(object.gid));
		monsterSpriteAnimations.put(AnimationState.IDLE, new Animation(0.3f, monsterTextureRegions));
		monsterTextureRegions.clear();
		monsterTextureRegions.add(monsterframes[0][0]);
		
		monsterSpriteAnimations.put(AnimationState.RUN, new Animation(0.1f, monsterTextureRegions));
		monsterTextureRegions.clear();
		monsterTextureRegions.add(monsterframes[0][1]);
		monsterSpriteAnimations.put(AnimationState.JUMP, new Animation(0.1f, monsterTextureRegions));

		// DEATH
		monsterTextureRegions.clear();

		monsterTextureRegions.add(monsterframes[0][1]);
		monsterTextureRegions.add(monsterframes[0][2]);
		monsterTextureRegions.add(monsterframes[0][3]);

		
		monsterSpriteAnimations.put(AnimationState.DIE, new Animation(0.1f, monsterTextureRegions));
		
		
		Monster monster = new Monster(new Vector2(16f, 32f), monsterSpriteAnimations, 0.8f, false);
		monster.addToWorld(world, new Vector2((float)Math.random()*map.map.width*4, (float)Math.random()*map.map.height*4 + 32f));
		monster.setToFollow(map.player);
		
		map.addCreature(monster);
	}
	
	
	public static void main (String[] args) {
        new LwjglApplication(new CoffeeGDX(), "Game", 800, 600, false);
}

}
