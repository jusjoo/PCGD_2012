package coffeeGDX;

import java.util.ArrayList;

import coffeeGDX.Configuration;
import coffeeGDX.MyContactListener;
import coffeeGDX.Player;
import coffeeGDX.StaticTile;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.tiled.SimpleTileAtlas;
import com.badlogic.gdx.graphics.g2d.tiled.TileAtlas;
import com.badlogic.gdx.graphics.g2d.tiled.TileMapRenderer;
import com.badlogic.gdx.graphics.g2d.tiled.TiledLayer;
import com.badlogic.gdx.graphics.g2d.tiled.TiledLoader;
import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class CoffeeGDX implements ApplicationListener {
	
	Configuration config;
	
	Vector2 oldPosition = new Vector2(0,0);
	
	Player mrEgg = null;
	
	Texture spritesTexture = null;
	SpriteBatch spriteBatch = null;
	
	TiledMap map = null;
	
	Box2DDebugRenderer debugRenderer;
	
	TileMapRenderer tileMapRenderer;
	
	World world;
	
	OrthographicCamera cam;

	@Override
	public void create() {
		
		config = new Configuration();
		
		world = new World(new Vector2(0.0f,-10.0f), true);
		cam = new OrthographicCamera(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
		
		debugRenderer = new Box2DDebugRenderer();
		
		Texture spritesTexture = new Texture(new FileHandle("assets/mrEggEverything.png"));
		
		map = TiledLoader.createMap(new FileHandle("assets/map1.tmx"));
		
		SimpleTileAtlas atlas = new SimpleTileAtlas(map, new FileHandle("assets/"));
		
		tileMapRenderer = new TileMapRenderer(map, atlas, 4, 4);
		
		int[][] tiles = map.layers.get(0).tiles;
		
		ArrayList<StaticTile> maptiles = new ArrayList<StaticTile>();
		
		for(int y=0; y<tiles.length; y++) {
			for(int x=0; x<tiles[y].length; x++) {
				if(tiles[y][x] != 0) {
					maptiles.add(new StaticTile(world, x, -y+tiles.length));
				}
			}
		}
		
		int amountOfFrames = 5; 
		mrEgg = new Player(new TextureRegion(spritesTexture, 0, 0, amountOfFrames*16, 16), world, new Vector2(19, 4), config.getPlayerSpeed(), config.getPlayerJumpHeight());
		
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
		
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		
		float interpolationAmount = config.getInterpolationAmount();
		
		//oldPosition = new Vector2(mrEgg.getPosition().x, mrEgg.getPosition().y);
		oldPosition = new Vector2(cam.position.x, cam.position.y);
		//oldPosition.mul(interpolationAmount);
		
		
		
		Vector2 newPosition = new Vector2(	oldPosition.x + interpolationAmount*(mrEgg.getPosition().x - oldPosition.x),
											oldPosition.y + interpolationAmount*(mrEgg.getPosition().y - oldPosition.y));
		cam.position.set(newPosition.x, newPosition.y, 0);
		
		
        cam.update();
        cam.apply(Gdx.gl10);
        
        	
        
        //cam.project(new Vector3(4,4,4));
        
        spriteBatch.setProjectionMatrix(cam.combined);
		spriteBatch.begin();
		mrEgg.render(spriteBatch);
		spriteBatch.end();
		
		tileMapRenderer.render(cam);
		
		

		
		//debugRenderer.render(world, cam.projection.scale(MovingSprite.PIXELS_PER_METER, MovingSprite.PIXELS_PER_METER,
        //		MovingSprite.PIXELS_PER_METER));
		
		
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
}

}
