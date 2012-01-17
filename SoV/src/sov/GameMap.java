package sov;

import java.util.ArrayList;
import java.util.HashMap;

import sov.AnimatedSprite.AnimationState;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.tiled.SimpleTileAtlas;
import com.badlogic.gdx.graphics.g2d.tiled.TileMapRenderer;
import com.badlogic.gdx.graphics.g2d.tiled.TiledLayer;
import com.badlogic.gdx.graphics.g2d.tiled.TiledLoader;
import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;
import com.badlogic.gdx.graphics.g2d.tiled.TiledObject;
import com.badlogic.gdx.graphics.g2d.tiled.TiledObjectGroup;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class GameMap {
	
	enum LayerType { Foreground, Background, StaticTiles, DynamicTiles, Creatures };
	
	Player player;
	
	TiledMap map;
	ArrayList<MovingSprite> dynMapTiles = new ArrayList<MovingSprite>();
	ArrayList<Creature> creatures = new ArrayList<Creature>();
	TileMapRenderer tileMapRenderer;
	
	HashMap<LayerType, Integer> layerIds = new HashMap<LayerType, Integer>();
	
	
	/*
	 * Loads a new map for the game
	 */
	public GameMap(String tmxFile, World world) {
		
		
		map = TiledLoader.createMap(new FileHandle("assets/maps/" + tmxFile));
		
		
		SimpleTileAtlas atlas = new SimpleTileAtlas(map, new FileHandle("assets/maps/"));
		
		tileMapRenderer = new TileMapRenderer(map, atlas, 5, 5);
		
		
			//layerIds.put(LayerType.Foreground, layer.)
		
		
		for(int i=0; i<map.layers.size(); i++) {
			
			TiledLayer layer = map.layers.get(i);
			
			int[][] tiles = layer.tiles;
			
			if(layer.name.equals("StaticTiles")) {
				layerIds.put(LayerType.StaticTiles, i);
				ArrayList<StaticTile> maptiles = new ArrayList<StaticTile>();
				
				for(int y=0; y<tiles.length; y++) {
					for(int x=0; x<tiles[y].length; x++) {
						if(tiles[y][x] != 0) {
							String property = map.getTileProperty(tiles[y][x], "slope");
							if(property != null && property.equals("left")) {
								maptiles.add(new StaticTile(world, x, -y+map.height, StaticTile.Shape.LEFT));
							} else if (property != null && property.equals("right")) {
								maptiles.add(new StaticTile(world, x, -y+map.height, StaticTile.Shape.RIGHT));
							} else {
								maptiles.add(new StaticTile(world, x, -y+map.height, StaticTile.Shape.SQUARE));
							}
							
						}
					}
				}
				
			} // END Foreground
			
			else if(layer.name.equals("Background")) {
				layerIds.put(LayerType.Background, i);
			} 
			else if(layer.name.equals("Foreground")) {
				layerIds.put(LayerType.Foreground, i);
			}
			
			
		}
		
		ArrayList<TiledObjectGroup> objectGroups = map.objectGroups;
		
		for(TiledObjectGroup objectGroup : objectGroups) {
			if(objectGroup.name.equals("DynamicTiles")) {
				ArrayList<TiledObject> dynTiles = map.objectGroups.get(0).objects;
				
				for(TiledObject object : dynTiles) {
					
					
					HashMap<AnimatedSprite.AnimationState, Animation> tileAnimations = new HashMap<AnimatedSprite.AnimationState, Animation>();
					ArrayList<TextureRegion> textureRegions = new ArrayList<TextureRegion>();
					textureRegions.add(atlas.getRegion(object.gid));
					tileAnimations.put(AnimationState.IDLE, new Animation(0.1f, textureRegions));
					
					dynMapTiles.add(new MovingSprite(world,
							new Vector2(object.x, -object.y+(map.height+1)*map.tileHeight),
							new Vector2(16f,16f),tileAnimations));
				}
			} // END DynamicTiles
		}
	}

	public TileMapRenderer getTileMapRenderer() {
		return tileMapRenderer;
	}

	public ArrayList<MovingSprite> getDynMapTiles() {
		return dynMapTiles;	
	}
	
	public void addCreature(Creature creature) {
		creatures.add(creature);
		if(creature.getClass() == Player.class) {
			player = (Player) creature;
		}
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void renderLayer(LayerType type, OrthographicCamera cam, SpriteBatch spriteBatch) {
		if(type == LayerType.Foreground) tileMapRenderer.render(cam, new int[] {layerIds.get(LayerType.Foreground)});
		if(type == LayerType.Background) tileMapRenderer.render(cam, new int[] {layerIds.get(LayerType.Background)});
		if(type == LayerType.StaticTiles) tileMapRenderer.render(cam, new int[] {layerIds.get(LayerType.StaticTiles)});
        
		if(type == LayerType.DynamicTiles) {
			spriteBatch.setProjectionMatrix(cam.combined);
			spriteBatch.begin();
			for(MovingSprite sprite : dynMapTiles) {
				sprite.render(spriteBatch);
			}
			spriteBatch.end();
		}
		
		if(type == LayerType.Creatures) {
			spriteBatch.setProjectionMatrix(cam.combined);
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
	
	public void render(OrthographicCamera cam, SpriteBatch spriteBatch) {
		renderLayer(LayerType.Background, cam, spriteBatch);
		renderLayer(LayerType.Creatures, cam, spriteBatch);
		renderLayer(LayerType.StaticTiles, cam, spriteBatch);
		renderLayer(LayerType.DynamicTiles, cam, spriteBatch);
		renderLayer(LayerType.Foreground, cam, spriteBatch);
	}
	
	
}
