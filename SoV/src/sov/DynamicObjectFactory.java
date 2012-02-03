package sov;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

/*
 * DynamicObjectFactory will read in configuration JSON's, and create object prototypes from them.
 * These prototypes can then be used via various public Spawn functions.
 * TODO: Read in special tiles as well, not just creatures.
 */
public class DynamicObjectFactory {
	
	// A list of the prototypes
	HashMap<Creature.CreatureType, Creature> creatures = new HashMap<Creature.CreatureType, Creature>();
	
	public DynamicObjectFactory(String directory) {
		
		// Only read in fields which have @Expose in front of them
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		
		String filename = "creatures";
		Creature[] creaturePrototypes = null;
		try {
			creaturePrototypes = gson.fromJson(new FileReader("assets/creatures/"+filename+".json"), Creature[].class);
		
			
			// Go through each entry in creatures.json, and create one prototype
			// from it to store in "creatures".
			for(Creature creaturePrototype : creaturePrototypes) {
				creatures.put(creaturePrototype.creatureType, creaturePrototype);
				Texture spritesTexture = new Texture(new FileHandle("assets/creatures/" + creaturePrototype.textureName));
				
				System.out.println("Creaturetype: " + creaturePrototype.creatureType);
				
				HashMap<SpriteComponent.AnimationState, ArrayList<Object>> animationStates =
						creaturePrototype.frames;
				
				HashMap<SpriteComponent.AnimationState, Animation> spriteAnimations = new HashMap<SpriteComponent.AnimationState, Animation>();
				
				
				/* Go through AnimationStates, and add each one to spriteAnimations.
				 * TODO: Instead of an Object ArrayList, we should have a class that holds all frame related information
				 * in, such as frame size, origin offset, etc etc. This should eliminate all manual
				 * parsing like is done now. (Horribly ugly!)
				 */
				for(Entry<SpriteComponent.AnimationState, ArrayList<Object>> animationEntry: animationStates.entrySet()) {
					// The following properties are inside the ArrayList<Object> now:
					// "Frame size[2], Origin offset, Start frame coordinates[2], Length, Speed, Loops"
					
					// Use only the part of the texture where the keyframes for this particular Animation are.
					TextureRegion subRegion = new TextureRegion(spritesTexture);
					
					// Get the coordinates for the subregion.
					Vector2 startCoordinates = new Vector2();
					Vector2 totalSize = new Vector2();
					startCoordinates.x = ((ArrayList<Double>)(animationEntry.getValue().get(2))).get(0).floatValue()*16f;
					startCoordinates.y = ((ArrayList<Double>)(animationEntry.getValue().get(2))).get(1).floatValue()*16f;
					totalSize.x = (Float.parseFloat(animationEntry.getValue().get(3).toString()))*((ArrayList<Double>)(animationEntry.getValue().get(0))).get(0).floatValue()*16f;
					totalSize.y = ((ArrayList<Double>)(animationEntry.getValue().get(0))).get(1).floatValue()*16;
					
					subRegion.setRegion(((int)startCoordinates.x), ((int)startCoordinates.y), ((int)totalSize.x), ((int)totalSize.y));
					
					// Split the subregion into specific frames.
					TextureRegion frames[][] =
							subRegion.split((int) (((ArrayList<Double>)(animationEntry.getValue().get(0))).get(0)*16),
																(int) (((ArrayList<Double>)(animationEntry.getValue().get(0))).get(1)*16));
					
					// Add each individual frame to textureRegions
					ArrayList<TextureRegion> textureRegions = new ArrayList<TextureRegion>();
					for(int y=0; y<frames.length; y++) {
						for(int x=0; x<frames[y].length; x++) {
							textureRegions.add(frames[y][x]);
						}
					}
					
					float frameDelay = Float.parseFloat(animationEntry.getValue().get(4).toString());
					
					boolean loops = Boolean.parseBoolean(animationEntry.getValue().get(5).toString());
					
					int offset = (int) Float.parseFloat(animationEntry.getValue().get(1).toString());
					
					// Insert all keyframes into spriteAnimations, as an Animation, associated
					// with the correct Animation. (animationEntry.getKey())
					spriteAnimations.put(animationEntry.getKey(), new Animation(frameDelay, textureRegions, loops, offset));
				
				}
				
				/*
				 * Create attacks for the prototype
				 */
				HashMap<SpriteComponent.AnimationState, ArrayList<Object>> attackDefinitions = creaturePrototype.attacks;
				for(Entry<SpriteComponent.AnimationState, ArrayList<Object>> attackEntry: attackDefinitions.entrySet()) {
					String attackType = attackEntry.getValue().get(0).toString();
					float attackTime = Float.parseFloat(attackEntry.getValue().get(1).toString());
					float preDamageTime = Float.parseFloat(attackEntry.getValue().get(2).toString());
					float damageTime = Float.parseFloat(attackEntry.getValue().get(3).toString());
					SpriteComponent.AnimationState animation = SpriteComponent.AnimationState.valueOf(attackEntry.getValue().get(4).toString());
					
					
					//float attackTime, float preDamageTime, float damageTime, SpriteComponent.AnimationState attackAnimation
				}
				
				// Initialize the prototype with the correct animations, and then
				// add the prototype to "creatures".
				creaturePrototype.getComponent(SpriteComponent.class).setAnimations(spriteAnimations);
				
				
				
				
				
				creatures.put(creaturePrototype.creatureType, creaturePrototype);
			}
		
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	// Spawn a creature based on a prototype!
	public Creature spawnCreature(World world, Creature.CreatureType type, Vector2 coordinates) {
		Creature creature = Creature.createFromPrototype(creatures.get(type));
		creature.addToWorld(world, coordinates);
		return creature;
	}
}
