package sov;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import sov.SpriteComponent.AnimationState;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.JsonReader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class DynamicObjectFactory {
	
	HashMap<Creature.CreatureType, Creature> creatures = new HashMap<Creature.CreatureType, Creature>();
	
	public DynamicObjectFactory(String directory) {
		
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		
		String filename = "creatures";
		Creature[] creaturePrototypes = null;
		try {
			creaturePrototypes = gson.fromJson(new FileReader("assets/creatures/"+filename+".json"), Creature[].class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		for(Creature creaturePrototype : creaturePrototypes) {
			creatures.put(creaturePrototype.creatureType, creaturePrototype);
			Texture spritesTexture = new Texture(new FileHandle("assets/creatures/" + creaturePrototype.textureName));
			
			HashMap<SpriteComponent.AnimationState, ArrayList<Object>> animationStates =
					creaturePrototype.frames;
			
			HashMap<SpriteComponent.AnimationState, Animation> spriteAnimations = new HashMap<SpriteComponent.AnimationState, Animation>();
			
			
			for(Entry<SpriteComponent.AnimationState, ArrayList<Object>> animationEntry: animationStates.entrySet()) {
				// "Frame size, Origin offset, Start frame coordinates, Length, Speed (ms)"
				
				TextureRegion subRegion = new TextureRegion(spritesTexture);
				
				Vector2 startCoordinates = new Vector2();
				Vector2 totalSize = new Vector2();
				startCoordinates.x = ((int[])(animationEntry.getValue().get(2)))[0]*16;
				startCoordinates.y = ((int[])(animationEntry.getValue().get(2)))[1]*16;
				totalSize.x = Float.parseFloat(animationEntry.getValue().get(3).toString());
				totalSize.y = ((int[])(animationEntry.getValue().get(0)))[1]*16;
				
				subRegion.setRegion(startCoordinates.x, startCoordinates.y, totalSize.x, totalSize.y);
				
				TextureRegion frames[][] =
						TextureRegion.split(spritesTexture, ((int[])(animationEntry.getValue().get(0)))[0]*16,
															((int[])(animationEntry.getValue().get(0)))[1]*16);
				
				ArrayList<TextureRegion> textureRegions = new ArrayList<TextureRegion>();
				for(int y=0; y<frames.length; y++) {
					for(int x=0; x<frames[y].length; x++) {
						textureRegions.add(frames[y][x]);
					}
				}
				
				float frameDelay = Float.parseFloat(animationEntry.getValue().get(4).toString());
				spriteAnimations.put(animationEntry.getKey(), new Animation(frameDelay, textureRegions));
			
			}
			
			creatures.put(creaturePrototype.creatureType, new Creature(new Vector2(13f,30f), spriteAnimations, 0.8f, false));
			//player.addToWorld(world, new Vector2(40f, 60f));
			
			//map.addCreature(player);
			
			
			//System.out.println(creaturePrototype.frames.get(AnimationState.Idle));
		}
		
	}
	
	public Creature spawnCreature(World world, Creature.CreatureType type, Vector2 coordinates) {
		Creature creature = (Creature) creatures.get(type).clone();
		creature.addToWorld(world, coordinates);
		return creature;
	}
}
