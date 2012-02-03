package sov;

import java.util.ArrayList;
import java.util.HashMap;

import sov.BodyComponent.SlopeShape;
import sov.Creature.CreatureType;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
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
import com.badlogic.gdx.graphics.g2d.tiled.TiledObject;
import com.badlogic.gdx.graphics.g2d.tiled.TiledObjectGroup;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class GameMap {
	
	// LayerTypes correspond to layers in Tiled-maps
	enum LayerType { Foreground, Background, StaticTiles, DynamicTiles, Creatures };
	
	// Hold a reference to the player, for camera positioning etc.
	Creature player;
	
	// Map which is loaded from the Tiled map, has information on nearly everything!
	TiledMap map;
	
	// Background image
	Sprite backgroundImage;
	AssetManager assetManager;
	OrthographicCamera parallaxCamera;
	
	// Dynamic map tiles are tiles which can be pushed around in the map.
	ArrayList<SpriteBody> dynMapTiles = new ArrayList<SpriteBody>();
	
	// Holds all creatures, including the player.
	ArrayList<Creature> creatures = new ArrayList<Creature>();
	
	// Renders only static tiles, dynamic tiles and creatures are rendered manually
	// Excellent for static tiles because of optimizations.
	TileMapRenderer tileMapRenderer;
	
	// Layer id-numbers to use with tileMapRenderer.
	HashMap<LayerType, Integer> layerIds = new HashMap<LayerType, Integer>();
	
	public DynamicObjectFactory factory;
	
	// Tile size in pixels.
	protected float tileSize = 16f;
	
	
	/*
	 * Loads a new map for the game
	 */
	public GameMap(String tmxFile, World world) {
		assetManager = new AssetManager();
		//assetManager.load("assets/creatures/sprites_human_barbarian.png", "File");
		map = TiledLoader.createMap(new FileHandle("assets/maps/" + tmxFile));
		
		// Holds the textures for all tiles.
		SimpleTileAtlas atlas = new SimpleTileAtlas(map, new FileHandle("assets/maps/"));
		
		tileMapRenderer = new TileMapRenderer(map, atlas, 5, 5);
		
		Texture backgroundTexture = new Texture(new FileHandle("assets/maps/background.jpg"));
		
		//new Texture(new FileHandle("assets/creatures/sprites_human_barbarian.png"));
		backgroundImage = new Sprite(backgroundTexture);
		//backgroundImage.setColor(new Color(0.5f, 0.5f, 0.5f, 0.6f));
		//backgroundImage.setRegion(backgroundTexture);
		
		parallaxCamera = new OrthographicCamera(Gdx.graphics.getWidth()/1.2f, Gdx.graphics.getHeight()/1.2f);
		
		factory = new DynamicObjectFactory("assets/creatures");
		
		createStaticTiles(world);
		createDynamicTiles(world, atlas);
		spawnCreatures(world);
		
	}
	
	
	/*  Create BodyEntities based on static tile positions. Used so that
	 *  we can use tileMapRenderer for rendering, and Box2D for collision
	 *  and physics handling.
	 *  
	 *  Background and foreground layers don't have collisions at all,
	 *  for them we just assign with the correct LayerType based on their
	 *  name in the TMX-file.
	 */
	protected void createStaticTiles(World world) {
		for(int i=0; i<map.layers.size(); i++) {
			
			TiledLayer layer = map.layers.get(i);
			
			int[][] tiles = layer.tiles;
			
			if(layer.name.equals("StaticTiles")) {
				layerIds.put(LayerType.StaticTiles, i);
				ArrayList<BodyComponent> maptiles = new ArrayList<BodyComponent>();
				
				for(int y=0; y<tiles.length; y++) {
					for(int x=0; x<tiles[y].length; x++) {
						if(tiles[y][x] != 0) {
							String property = map.getTileProperty(tiles[y][x], "slope");
							BodyComponent.SlopeShape shape = SlopeShape.Even;
							if(property != null && property.equals("left")) { shape = SlopeShape.Left; }
							else if (property != null && property.equals("right")) { shape = SlopeShape.Right; }
							BodyComponent tile = new BodyComponent(null, new Vector2(tileSize, tileSize), true, 1.0f, false, shape, false);
							
							tile.addToWorld(world, new Vector2(x*tileSize, -y*tileSize+map.height*tileSize));
									
							maptiles.add(tile);
							}
							
						}
					}
				
			} // END StaticTiles
			
			else if(layer.name.equals("Background")) {
				layerIds.put(LayerType.Background, i);
			} 
			else if(layer.name.equals("Foreground")) {
				layerIds.put(LayerType.Foreground, i);
			}
			
			
		}
	}
	
	// Create dynamic tiles.
	protected void createDynamicTiles(World world, TileAtlas atlas) {
		ArrayList<TiledObjectGroup> objectGroups = map.objectGroups;
		
		for(TiledObjectGroup objectGroup : objectGroups) {
			if(objectGroup.name.equals("DynamicTiles")) {
				ArrayList<TiledObject> dynTiles = map.objectGroups.get(0).objects;
				
				for(TiledObject object : dynTiles) {
					
					
					HashMap<SpriteComponent.AnimationState, Animation> tileAnimations = new HashMap<SpriteComponent.AnimationState, Animation>();
					ArrayList<TextureRegion> textureRegions = new ArrayList<TextureRegion>();
					textureRegions.add(atlas.getRegion(object.gid));
					tileAnimations.put(SpriteComponent.AnimationState.Idle, new Animation(0.1f, textureRegions, false, 0));
					
					SpriteBody asb = new SpriteBody(new Vector2(16f,16f),tileAnimations, false, 1.0f, false, SlopeShape.Even);
					asb.getComponent(BodyComponent.class).addToWorld(world, new Vector2(object.x, -object.y+(map.height+1)*map.tileHeight));
					dynMapTiles.add(asb);
				}
			}
		}
	}
	
	// Create monsters
	protected void spawnCreatures(World world) {
		ArrayList<TiledObjectGroup> objectGroups = map.objectGroups;
		
		for(TiledObjectGroup objectGroup : objectGroups) {
			if(objectGroup.name.equals("Creatures")) {
				ArrayList<TiledObject> dynTiles = map.objectGroups.get(1).objects;
				
				for(TiledObject object : dynTiles) {
					
					
					//HashMap<SpriteComponent.AnimationState, Animation> tileAnimations = new HashMap<SpriteComponent.AnimationState, Animation>();
					//ArrayList<TextureRegion> textureRegions = new ArrayList<TextureRegion>();
					//textureRegions.add(atlas.getRegion(object.gid));
					//tileAnimations.put(SpriteComponent.AnimationState.Idle, new Animation(0.1f, textureRegions, false, 0));
					
					//SpriteBody asb = new SpriteBody(new Vector2(16f,16f),tileAnimations, false, 1.0f, false, SlopeShape.Even);
					
					
					//asb.getComponent(BodyComponent.class).addToWorld(world, new Vector2(object.x, -object.y+(map.height+1)*map.tileHeight));
					//dynMapTiles.add(asb);
					Creature creature = factory.spawnCreature(world, Creature.CreatureType.valueOf(object.type),
							new Vector2(object.x, -object.y+(map.height+1)*map.tileHeight));
					creature.addComponent(new MovementComponent(creature, creature.speed, creature.jumpHeight));
					if(object.properties.get("IsPlayer") != null) {
						creature.addComponent(new PlayerInputComponent(creature));
						this.setPlayer(creature);
					} else {
						creature.addComponent(new AIComponent(creature));	
					}
					
					this.addCreature(world, creature);
					
				}
			}
		}
	}

	public TileMapRenderer getTileMapRenderer() {
		return tileMapRenderer;
	}

	public ArrayList<SpriteBody> getDynMapTiles() {
		return dynMapTiles;	
	}
	
	public void removeCreature(Creature creature) {
		
		creature.removeFromWorld();
		creatures.remove(creature);
		
	}
	
	public void addCreature(World world, Creature creature) {
		creatures.add(creature);
		//System.out.println(creature.creatureType);
		/*if(creature.creatureType == CreatureType.Ninja) {
			//player = (Player) creature;
			player = new Player(new Vector2(14f, 30f), creature.spriteComponent.animations, 0.8f, false);
			player.addToWorld(world, new Vector2(300f, 330f));
			creatures.add(player);
			
			//creature.addComponent(inputComponent);
		}*/
	}
	
	public Creature getPlayer() {
		return player;
	}
	
	public void setPlayer(Creature creature) {
		this.player = creature;
	}
	
	public void renderLayer(LayerType type, OrthographicCamera cam, SpriteBatch spriteBatch) {
		if(type == LayerType.Foreground) tileMapRenderer.render(cam, new int[] {layerIds.get(LayerType.Foreground)});
		if(type == LayerType.Background) tileMapRenderer.render(cam, new int[] {layerIds.get(LayerType.Background)});
		if(type == LayerType.StaticTiles) tileMapRenderer.render(cam, new int[] {layerIds.get(LayerType.StaticTiles)});
        
		if(type == LayerType.DynamicTiles) {
			
			spriteBatch.begin();
			for(SpriteBody sprite : dynMapTiles) {
				sprite.render(spriteBatch);
			}
			spriteBatch.end();
		}
		
		if(type == LayerType.Creatures) {
			//spriteBatch.setProjectionMatrix(cam.combined);
			spriteBatch.begin();
			for(Creature creature : creatures) {
				creature.render(spriteBatch);
			}
			spriteBatch.end();
		}
		
	}
	
	public void update(float deltaTime) {
		for(Creature creature : creatures) {
			creature.update(deltaTime);
		}
	}
	
	// TODO: Add layer rendering order in an ordered list or array.
	public void render(OrthographicCamera cam, SpriteBatch spriteBatch) {
		
		/*parallaxCamera.update();
		parallaxCamera.apply(Gdx.gl10);
		
		//parallaxCamera.position.set(player.getPosition().x-backgroundImage.getWidth()/2, player.getPosition().y-backgroundImage.getHeight()/2, 1f);
		//parallaxCamera.position.set(cam.position.scale(0.7f, 1f, 1f));
		parallaxCamera.position.set(cam.position);
		parallaxCamera.position.scale(1.5f, 1f, 1f);
		spriteBatch.setProjectionMatrix(parallaxCamera.combined);
		*/
		spriteBatch.begin();
		//backgroundImage.setPosition((player.getPosition().x-backgroundImage.getWidth()/2)*0.5f, player.getPosition().y-backgroundImage.getHeight()/2);
		for(int y=0; y<3; y++) {
			for(int x=0; x<3; x++) {
				backgroundImage.setPosition(x*backgroundImage.getWidth()-backgroundImage.getWidth()*1.25f,
						y*backgroundImage.getHeight()-backgroundImage.getHeight()*1.25f);
				backgroundImage.draw(spriteBatch);	
			}
			
		}
		//backgroundImage.setPosition(player.getPosition().x, player.getPosition().y);
		spriteBatch.end();
		
		cam.update();
		cam.apply(Gdx.gl10);
		
		spriteBatch.setProjectionMatrix(cam.combined);
		
		
		
		renderLayer(LayerType.Background, cam, spriteBatch);
		renderLayer(LayerType.Creatures, cam, spriteBatch);
		renderLayer(LayerType.StaticTiles, cam, spriteBatch);
		renderLayer(LayerType.DynamicTiles, cam, spriteBatch);
		renderLayer(LayerType.Foreground, cam, spriteBatch);
		
		
	}
	

}
