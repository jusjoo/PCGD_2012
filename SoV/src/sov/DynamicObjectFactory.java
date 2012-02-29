package sov;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import sov.AIComponent.AIstate;
import sov.Attack.AttackType;
import sov.BodyComponent.SlopeShape;
import sov.Collectible.CollectibleType;
import sov.SpriteComponent.AnimationState;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;

/*
 * DynamicObjectFactory will read in configuration JSON's, and create object prototypes from them.
 * These prototypes can then be used via various public Spawn functions.
 * TODO: Read in special tiles as well, not just creatures.
 */
public class DynamicObjectFactory {	

	/*
	 * This Contains all the misc animation types.
	 */
	public enum AnimationType {SlimeBomb, Fireball, Shuriken, RedPotion, BluePotion, SoulOrb, Coin, Diamond};
	
	private GameMap gameMap;
	
	// A list of the prototypes
	HashMap<Creature.CreatureType, Creature> creatures = new HashMap<Creature.CreatureType, Creature>();
	HashMap<AnimationType, HashMap<SpriteComponent.AnimationState, Animation>> miscAnimations;
	HashMap<CollectibleType, Collectible> collectibles;
	
	
	public DynamicObjectFactory(String directory, GameMap map) {
		
		this.gameMap = map;
		
		// Only read in fields which have @Expose in front of them
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		
		String filename = "creatures";
		Creature[] creaturePrototypes = null;
		try {
			// Load the misc animations
			loadMiscAnimations();
			loadCollectibles();
			
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
					
					float offset = Float.parseFloat(animationEntry.getValue().get(1).toString());
					
					// Insert all keyframes into spriteAnimations, as an Animation, associated
					// with the correct Animation. (animationEntry.getKey())
					spriteAnimations.put(animationEntry.getKey(), new Animation(frameDelay, textureRegions, loops, offset));
				
				}
				
				// Initialize the prototype with the correct animations
				
				creaturePrototype.getComponent(SpriteComponent.class).setAnimations(spriteAnimations);
				
				/*
				 * Create attacks for the prototype
				 */
				HashMap<SpriteComponent.AnimationState, ArrayList<Object>> attackDefinitions = creaturePrototype.attacks;
				//creaturePrototype.addComponent(new AttackComponent());
				AttackComponent attackComponent = new AttackComponent(creaturePrototype);
				
				
				for(Entry<SpriteComponent.AnimationState, ArrayList<Object>> attackEntry: attackDefinitions.entrySet()) {
					AnimationState attackName = attackEntry.getKey();
					String temp = attackEntry.getValue().get(0).toString();
					AttackType attackType = AttackType.None; 
					if (temp.equals("Melee")) attackType = AttackType.Melee;
					if (temp.equals("Ranged")) attackType = AttackType.Ranged;
					if (temp.equals("Magic")) attackType = AttackType.Magic;
					
					System.out.println(temp+"..."+attackType);
					
					//String attackType = attackEntry.getValue().get(0).toString();
					
					//float attackTime = Float.parseFloat(attackEntry.getValue().get(1).toString());
					//float frameDelay = Float.parseFloat(animationEntry.getValue().get(4).toString());
					
					// ""AttackType, damageStartFrame, damageEndFrame, attackbox y-offset, attackbox size x, y, damage, (enum AnimationType projectile), (flightSpeed)",
					//	0			1					2				3					4				 5   6			7								8
					float frameDelay = Float.parseFloat(animationStates.get(attackEntry.getKey()).get(4).toString());
					float frameAmount = Float.parseFloat(animationStates.get(attackEntry.getKey()).get(3).toString());
					
					float attackTime = frameDelay * frameAmount + 0.2f;
					
					//System.out.println("Attacktime: " + attackTime);
					
					//System.out.println("state: " + animationStates.get(attackEntry.getKey()) + " Frame delay:" + frameDelay);
					
					
					//float attackTime =
					float damageStartFrame = Float.parseFloat(attackEntry.getValue().get(1).toString());
					float damageEndFrame = Float.parseFloat(attackEntry.getValue().get(2).toString());
					
					float preDamageTime = damageStartFrame * frameDelay;
					float damageTime = (damageEndFrame - damageStartFrame) * frameDelay;
					
					System.out.println("preDamage: " + preDamageTime + " damageTime:" + damageTime);
					
					SpriteComponent.AnimationState animation = SpriteComponent.AnimationState.valueOf(attackEntry.getKey().toString());
					
					// attackbox y-offset, attackbox size x, y, (flightSpeed)
					float attackOffsetY = Float.parseFloat(attackEntry.getValue().get(3).toString());
					float attackBoxSizeX = Float.parseFloat(attackEntry.getValue().get(4).toString());
					float attackBoxSizeY = Float.parseFloat(attackEntry.getValue().get(5).toString());
					
				//	BodyComponent attackBodyComponent = new BodyComponent(attackComponent.parent,
					//		new Vector2(attackBoxSizeX, attackBoxSizeY), false, 1.0f, false, SlopeShape.Even, true);
					//bodyComponent = parent.getComponent(BodyComponent.class);
					
					Attack attack;
					
					
					HashMap<SpriteComponent.AnimationState, Animation> animations = creaturePrototype.getComponent(SpriteComponent.class).animations;
					
					float damage = Float.parseFloat(attackEntry.getValue().get(6).toString());
					
					if(attackType == AttackType.Melee) {
						SpriteBody attackBody = new SpriteBody(new Vector2(attackBoxSizeX, attackBoxSizeY), animations, false, 1.0f, false, SlopeShape.Even, true);
						attack = new MeleeAttack(attackType, attackComponent, attackTime, preDamageTime, animation, attackOffsetY, damageTime, attackBody, damage);
						//attackComponentPrototypes.add(ac);
						attackComponent.addAttack(attackName, attack);
						
					}
					
					if (attackType == AttackType.Ranged || attackType == AttackType.Magic) {
						AnimationType projectileType = AnimationType.valueOf(attackEntry.getValue().get(7).toString());
						float flightSpeed = Float.parseFloat(attackEntry.getValue().get(8).toString());
						
						
						Projectile attackBody = new Projectile(new Vector2(attackBoxSizeX, attackBoxSizeY), miscAnimations.get(projectileType), true);
						
						attack = new RangedAttack(attackType, attackComponent, attackTime, preDamageTime, animation, attackBody, attackOffsetY, damage, flightSpeed);
						if (attackType == AttackType.Magic){		
							int spell = 1;
							((RangedAttack)attack).setSpellType(spell);
						}
						attackComponent.addAttack(attackName, attack);
						
		
						//attackComponentPrototypes.add(ac);
					}	
					
				}
				
				
				/*
				 * Add sounds for the creature prototype
				 */
				if (creaturePrototype.sounds != null) {
					HashMap<AnimationState, String> soundDefinitions = creaturePrototype.sounds;
				
					AudioComponent audioComponent = new AudioComponent(creaturePrototype);
				
					for(Entry<SpriteComponent.AnimationState, String> soundEntry: soundDefinitions.entrySet()) {
						Sound sound = Gdx.audio.newSound(new FileHandle("assets/sound/" + soundEntry.getValue()));
						audioComponent.addSound(soundEntry.getKey(), sound);
					}
					creaturePrototype.addComponent(audioComponent);
				}
					
				
				creaturePrototype.addComponent(attackComponent);
				
				

			
				// add the prototype to "creatures".
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
	
	
	private void loadCollectibles() throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		
		String filename = "collectibles";
		Collectible[] collectibleSet = null;
		
		collectibleSet = gson.fromJson(new FileReader("assets/"+filename+".json"), Collectible[].class);
		
			collectibles = new HashMap<CollectibleType, Collectible>();

			for(Collectible collectible : collectibleSet) {
								

				System.out.println("Collectible: " + collectible.type);
				Sound sound = Gdx.audio.newSound(new FileHandle("assets/sound/" + collectible.soundFile));
				collectible.createAudioComponent(sound);
	
				collectibles.put(collectible.type, collectible);			
			}
	}


	/*
	 * Loads misc animations, animation names need to be defined in AnimationType
	 */
	private void loadMiscAnimations() throws JsonSyntaxException, JsonIOException, FileNotFoundException{
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		
		String filename = "miscAnimations";
		AnimationSet[] animationSets = null;
		
		animationSets = gson.fromJson(new FileReader("assets/"+filename+".json"), AnimationSet[].class);
		
			miscAnimations = new HashMap<AnimationType, HashMap<SpriteComponent.AnimationState, Animation>>();

			for(AnimationSet animationSet : animationSets) {
				
				
				Texture spritesTexture = new Texture(new FileHandle("assets/" + animationSet.textureName));
				

				
				HashMap<SpriteComponent.AnimationState, ArrayList<Object>> animationStates = animationSet.frames;
				
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
					spriteAnimations.put(animationEntry.getKey(),  new Animation(frameDelay, textureRegions, loops, offset) );

				}
				
				miscAnimations.put(AnimationType.valueOf(animationSet.name), spriteAnimations);			
			}
			
	}
	
	// Spawn a creature based on a prototype!
	public Creature spawnCreature(World world, Creature.CreatureType type, Vector2 coordinates) {

		Creature creature = Creature.createFromPrototype(creatures.get(type));
		
		creature.addToWorld(world, coordinates);
		return creature;
	}
	
	public Collectible spawnCollectible(World world, CollectibleType type, Vector2 coordinates, Vector2 impulse) {
		Collectible collectible = Collectible.fromPrototype(collectibles.get(type));
		
		System.out.println("animaatiot: "+ miscAnimations.get(collectible.animations));
		collectible.createComponents(miscAnimations.get(collectible.animations));
		collectible.addToWorld(world, coordinates);
		collectible.getComponent(BodyComponent.class).applyLinearImpulse(impulse);
		return collectible;
	}
	

	class AnimationSet {
		@Expose String name;
		@Expose String textureName;
		@Expose HashMap<SpriteComponent.AnimationState, ArrayList<Object>> frames;
	}


	

}
