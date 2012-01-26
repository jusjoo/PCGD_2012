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
			//Texture spritesTexture = new Texture(new FileHandle("assets/creatures/" + creaturePrototype.textureName));
			Texture spritesTexture = new Texture(new FileHandle("assets/creatures/sprites_human_barbarian.png"));
			
			System.out.println("Creaturetype: " + creaturePrototype.creatureType);
			
			HashMap<SpriteComponent.AnimationState, ArrayList<Object>> animationStates =
					creaturePrototype.frames;
			
			HashMap<SpriteComponent.AnimationState, Animation> spriteAnimations = new HashMap<SpriteComponent.AnimationState, Animation>();
			
			
			
			for(Entry<SpriteComponent.AnimationState, ArrayList<Object>> animationEntry: animationStates.entrySet()) {
				// "Frame size, Origin offset, Start frame coordinates, Length, Speed (ms)"
				
				TextureRegion subRegion = new TextureRegion(spritesTexture);
				
				Vector2 startCoordinates = new Vector2();
				Vector2 totalSize = new Vector2();
				startCoordinates.x = ((ArrayList<Double>)(animationEntry.getValue().get(2))).get(0).floatValue()*16f;
				startCoordinates.y = ((ArrayList<Double>)(animationEntry.getValue().get(2))).get(1).floatValue()*16f;
				totalSize.x = (Float.parseFloat(animationEntry.getValue().get(3).toString()))*((ArrayList<Double>)(animationEntry.getValue().get(0))).get(0).floatValue()*16f;
				totalSize.y = ((ArrayList<Double>)(animationEntry.getValue().get(0))).get(1).floatValue()*16;
				
				subRegion.setRegion(((int)startCoordinates.x), ((int)startCoordinates.y), ((int)totalSize.x), ((int)totalSize.y));
				
				System.out.println(startCoordinates.x + " " + startCoordinates.y + " "  + totalSize.x + " "  + totalSize.y);
				
				TextureRegion frames[][] =
						subRegion.split((int) (((ArrayList<Double>)(animationEntry.getValue().get(0))).get(0)*16),
															(int) (((ArrayList<Double>)(animationEntry.getValue().get(0))).get(1)*16));
															
				
				//TextureRegion frames[][] =
						//subRegion.split(16, 32);
				
				//System.out.println(frames.length);
				
				ArrayList<TextureRegion> textureRegions = new ArrayList<TextureRegion>();
				for(int y=0; y<frames.length; y++) {
					for(int x=0; x<frames[y].length; x++) {
						textureRegions.add(frames[y][x]);
						//System.out.println("HI! x: " + x + " y: " + y);
					}
				}
				
				float frameDelay = Float.parseFloat(animationEntry.getValue().get(4).toString());
				spriteAnimations.put(animationEntry.getKey(), new Animation(frameDelay, textureRegions));
			
			}
			
			creaturePrototype.getSpriteComponent().setAnimations(spriteAnimations);
			
			//creatures.put(creaturePrototype.creatureType, new Creature(new Vector2(13f,30f), spriteAnimations, 0.8f, false));
			creatures.put(creaturePrototype.creatureType, creaturePrototype);
		}
		
	}
	
	public Creature spawnCreature(World world, Creature.CreatureType type, Vector2 coordinates) {
		Creature creature = Creature.createFromPrototype(creatures.get(type));
		creature.addToWorld(world, coordinates);
		return creature;
	}
}
