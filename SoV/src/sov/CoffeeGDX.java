package sov;

import java.util.ArrayList;
import java.util.HashMap;

import sov.AnimatedSprite.AnimationState;

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
	
	Player mrEgg = null;
	
	
	
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
		textureRegions.add(frames[0][3]);
		spriteAnimations.put(AnimationState.JUMP, new Animation(0.1f, textureRegions));
		mrEgg = new Player(world,
				new Vector2(40f, 40f), new Vector2(13,30), spriteAnimations, config.speed, config.jumpHeight);

		//int amountOfFrames = 5; 
		//mrEgg = new Player(new TextureRegion(spritesTexture, 0, 0, amountOfFrames*16, 16), world, new Vector2(19, 4), new Vector2(10,12), config.speed, config.jumpHeight);
		
		//mrEgg.setRegion(0, 0, 16, 16);
		
		//player.
		//mrEgg.setR
		//mrEgg.setScale(1, 2);
		
		//TextureRegion[][] spritesTextures = new TextureRegion(spritesTexture).split(16, 16);
		
		//mrEgg = new Sprite(spritesTextures[0][0]);
		
		
		spriteBatch = new SpriteBatch();
		
		world.setContactListener(new MyContactListener());

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render() {
		update();
		//float colourMultiplier = 1/(600-cam.position.y);
		float colourMultiplier = 1;
		Gdx.gl.glClearColor(0.5f*colourMultiplier, 0.7f*colourMultiplier, 0.88f*colourMultiplier, 1.0f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		
		float interpolationAmount = config.interpolationAmount;
		
		//oldPosition = new Vector2(mrEgg.getPosition().x, mrEgg.getPosition().y);
		oldPosition = new Vector2(cam.position.x, cam.position.y);
		//oldPosition.mul(interpolationAmount);
		
		
		
		Vector2 newPosition = new Vector2(	oldPosition.x + interpolationAmount*(mrEgg.getPosition().x - oldPosition.x),
											oldPosition.y + interpolationAmount*(mrEgg.getPosition().y - oldPosition.y));
		cam.position.set(newPosition.x, newPosition.y, 0);
		
		
        cam.update();
        cam.apply(Gdx.gl10);
        //cam.apply(Gdx.gl20);
        
        	
        
        //cam.project(new Vector3(4,4,4));
        
        spriteBatch.setProjectionMatrix(cam.combined);
		spriteBatch.begin();
		mrEgg.render(spriteBatch);
		for(MovingSprite sprite : map.getDynMapTiles()) {
			sprite.render(spriteBatch);
		}
		spriteBatch.end();
		
		map.getTileMapRenderer().render(cam);
		

		// Debug render
		if (config.debugMode) {
			debugRenderer.render(world, cam.combined.scale(MovingSprite.PIXELS_PER_METER, MovingSprite.PIXELS_PER_METER,
        		MovingSprite.PIXELS_PER_METER));
		}

		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}
	
	public void update() {
		float deltaTime = Gdx.graphics.getDeltaTime();
		
		mrEgg.update(deltaTime);
		
		world.step(Gdx.app.getGraphics().getDeltaTime(), 3, 3);
	}
	
	public static void main (String[] args) {
		
        new LwjglApplication(new CoffeeGDX(), "Game", 800, 600, false);
		//new LwjglApplication(new CoffeeGDX(), "Game", , 600, false);
		//new Lwjg
        //new LwjglApplication(listener, config)
}

}
