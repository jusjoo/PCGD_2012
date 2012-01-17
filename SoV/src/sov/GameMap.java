package sov;

import java.util.ArrayList;
import java.util.HashMap;

import sov.AnimatedSprite.AnimationState;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.tiled.SimpleTileAtlas;
import com.badlogic.gdx.graphics.g2d.tiled.TileMapRenderer;
import com.badlogic.gdx.graphics.g2d.tiled.TiledLoader;
import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;
import com.badlogic.gdx.graphics.g2d.tiled.TiledObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class GameMap {
	
	TiledMap map;
	ArrayList<MovingSprite> dynMapTiles;
	TileMapRenderer tileMapRenderer;
	
	
	/*
	 * Loads a new map for the game
	 */
	public GameMap(String tmxFile, World world) {
			
		map = TiledLoader.createMap(new FileHandle("assets/maps/" + tmxFile));
		
		SimpleTileAtlas atlas = new SimpleTileAtlas(map, new FileHandle("assets/maps/"));
		
		tileMapRenderer = new TileMapRenderer(map, atlas, 5, 5);
		
		int[][] tiles = map.layers.get(0).tiles;
		
		ArrayList<StaticTile> maptiles = new ArrayList<StaticTile>();
		
		
		// add the static tiles to the world
		for(int y=0; y<tiles.length; y++) {
			for(int x=0; x<tiles[y].length; x++) {
				if(tiles[y][x] != 0) {
					String property = map.getTileProperty(tiles[y][x], "slope");
					if(property != null && property.equals("left")) {
						maptiles.add(new StaticTile(world, x, -y+tiles.length, StaticTile.Shape.LEFT));
					} else if (property != null && property.equals("right")) {
						maptiles.add(new StaticTile(world, x, -y+tiles.length, StaticTile.Shape.RIGHT));
					} else {
						maptiles.add(new StaticTile(world, x, -y+tiles.length, StaticTile.Shape.SQUARE));
					}
					
				}
			}
		}
		
		ArrayList<TiledObject> dynTiles = map.objectGroups.get(0).objects;
		dynMapTiles = new ArrayList<MovingSprite>();		
		
		// add the dynamic tiles to the world
		for(TiledObject object : dynTiles) {
			
			
			HashMap<AnimatedSprite.AnimationState, Animation> tileAnimations = new HashMap<AnimatedSprite.AnimationState, Animation>();
			ArrayList<TextureRegion> textureRegions = new ArrayList<TextureRegion>();
			textureRegions.add(atlas.getRegion(object.gid));
			tileAnimations.put(AnimationState.IDLE, new Animation(0.1f, textureRegions));
			
			dynMapTiles.add(new MovingSprite(world,
					
					//new Vector2(object.x*2, -object.y*2+tiles.length), new Vector2(16f,16f),
					//new Vector2(object.x, (-(object.y*1/16)+tiles.length)), new Vector2(16f,16f),
					new Vector2(object.x, -object.y+(tiles.length+1)*16f), new Vector2(16f,16f),
					
					tileAnimations));
					//new Vector2(object.x, object.y)));
					//new Vector2(object.x, object.y)));
					System.out.println("X: " + object.x + " Y: " + object.y);
					//-(object.y*2/MovingSprite.PIXELS_PER_METER)+tiles.length)
		}
	}

	public TileMapRenderer getTileMapRenderer() {
		return tileMapRenderer;
	}

	public ArrayList<MovingSprite> getDynMapTiles() {
		return dynMapTiles;	
	}
	
	
}
