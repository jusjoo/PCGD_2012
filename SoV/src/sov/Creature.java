package sov;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

import sov.BodyComponent.SlopeShape;
import sov.SpriteComponent.AnimationState;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.google.gson.annotations.Expose;

/*
 * Creatures include all moving object that are controlled either by AI, or Player input.
 */
public class Creature extends SpriteBody implements Cloneable {
	
	public enum CreatureType { Barbarian, Ninja, Sorceress, Goblin, Slime };
	public enum Stats { Strength, Dexterity, Wisdom };

	
	// All the properties which are read from creatures.json must be declared here
	// These properties are then automatically "filled" by DynamicObjectFactory
	@Expose CreatureType creatureType;
	@Expose String textureName;
	@Expose int[] hitboxSize;
	@Expose float dexterity;
	@Expose float strength;
	@Expose float wisdom;
	@Expose HashMap<SpriteComponent.AnimationState, ArrayList<Object>> frames;
	@Expose HashMap<SpriteComponent.AnimationState, ArrayList<Object>> attacks;
	@Expose HashMap<SpriteComponent.AnimationState, String> sounds;
	
	protected float speed;
	protected float jumpHeight;
	protected float staminaMax;
	protected float stamina;
	protected float manaMax;
	protected float mana;
	
	Fixture sensorFixture;
	
	public enum AttackType {Melee, Ranged};
	boolean canAttack = true;
	
	
	public Creature() {
	}
	
	// Deliver size and position of the creature in pixels.
	public Creature(Vector2 size, HashMap<SpriteComponent.AnimationState, Animation> animations, float rounding,
			boolean circle) {
		super(size, animations,
				false, rounding, circle, SlopeShape.Even);	
	}
	
	public static Creature createFromPrototype(Creature prototype) {
		
		Creature creature = new Creature(new Vector2(prototype.hitboxSize[0], prototype.hitboxSize[1]), prototype.spriteComponent.animations, 0.8f, false);
		creature.creatureType = prototype.creatureType;
		creature.dexterity = prototype.dexterity;
		creature.strength = prototype.strength;
		creature.wisdom = prototype.wisdom;
		creature.speed = creature.deriveSpeed();
		//hitpoints are set when adding to world
		creature.jumpHeight = creature.deriveJumpHeight();
		creature.manaMax = prototype.deriveMana();
		creature.staminaMax = prototype.deriveStamina();
		creature.mana = creature.manaMax;
		creature.stamina = creature.staminaMax;
		
		
		creature.body.setMaxVelocity(creature.speed*GameConfiguration.creatureMaxVelocityMultiplier);
		
		// Set the attack component
		if(prototype.getComponent(AttackComponent.class) != null) {
			AttackComponent protoAttackComponent = prototype.getComponent(AttackComponent.class);
			
			AttackComponent newAttackComponent = AttackComponent.attackComponentFromPrototype(protoAttackComponent, creature);
		
			
			creature.addComponent(newAttackComponent);
			creature.getComponent(AttackComponent.class).setParent(creature);
		}
		
		if (prototype.getComponent(AudioComponent.class) != null) {
			creature.addComponent(prototype.getComponent(AudioComponent.class));
		}
		
		
		
		

		
		
		return creature;
	}	
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
				
		if (!body.finalDeath) {
			regenerateStamina(deltaTime);
			regenerateMana(deltaTime);
		}
		
	}
	
	private void regenerateMana(float deltaTime) {
		
		float manaregen;
		
		if (GameConfiguration.manaRegenAsPercentage)
			manaregen = deltaTime * GameConfiguration.manaRegenRatePercentage * manaMax;
		else
			manaregen = deltaTime * GameConfiguration.manaRegenRateStatic;			
			
		modifyMana(manaregen);
		
	}

	private void regenerateStamina(float deltaTime) {
		float staminaregen;
		
		if (GameConfiguration.staminaRegenAsPercentage)
			staminaregen = deltaTime * GameConfiguration.staminaRegenRatePercentage * staminaMax;
		else
			staminaregen = deltaTime * GameConfiguration.staminaRegenRateStatic;
		
		modifyStamina(staminaregen);
		
	}

	public void removeFromWorld(){
		getComponent(BodyComponent.class).removeFromWorld();
		getComponent(BodyComponent.class).body.destroyFixture(sensorFixture);
	}
	
	public void addToWorld(World world, Vector2 position) {
		//set hitpoints		 
		float hp = deriveHitpoints();
		getComponent(BodyComponent.class).setHitPoints(hp);
		getComponent(BodyComponent.class).heal(hp);
		getComponent(BodyComponent.class).addToWorld(world, position);
		//super.addToWorld(world, position);
		float PIXELS_PER_METER = GameConfiguration.PIXELS_PER_METER;
		
		// Create a sensor at the level of the feet for detecting if a creature is touching the ground.
		// TODO: Transfer creating the body, shape and fixture to a static helper function.
		PolygonShape sensorShape = new PolygonShape();
		sensorShape.setAsBox(body.getSize().x / (3 * PIXELS_PER_METER), body.getSize().y / (12 * PIXELS_PER_METER),
				new Vector2(0, -body.getSize().y/PIXELS_PER_METER/2.23f), 0);
		FixtureDef sensorFixtureDef = new FixtureDef();
		sensorFixtureDef.shape = sensorShape;
		sensorFixtureDef.isSensor = true;
		sensorFixtureDef.density = 0;
		
		
		
		
		// Attach the foot-sensor on the body.
		sensorFixture = getComponent(BodyComponent.class).body.createFixture(sensorFixtureDef);
		//System.out.println(this);
		sensorFixture.setUserData(new ContactEvent(this, "sensor"));
		
		
		// Creatures shall not rotate according to physics!
		getComponent(BodyComponent.class).body.setFixedRotation(true);
		
		
	}

	public float getDexterity() {
		System.out.println("Dexterity "+dexterity);
		return dexterity; 
	}
	public float getStrength() {
		System.out.println("Strength "+strength);
		return strength;
	}
	public float getWisdom() {
		System.out.println("Wisdom "+wisdom);
		return wisdom;				
	}
	public float getSpeed(){
		return speed;
	}
	public float getJumpHeight(){
		return jumpHeight;				
	}
	public float getMana() {
		return mana;
	}
	public float getStamina() {
		return stamina;
	}
	public float deriveSpeed() {
		return this.dexterity * GameConfiguration.dexSpeedMultiplier + GameConfiguration.speedBaseModifier;		
	}
	
	public float deriveJumpHeight() {
		return this.dexterity * GameConfiguration.dexJumpHeightMultiplier + GameConfiguration.jumpHeightBaseModifier;
	}
	public float deriveMana(){
		float value = wisdom * GameConfiguration.wisManaMultiplier + GameConfiguration.manaBaseModifier;
		return value;
	}
	public float deriveStamina(){
		float value = dexterity * GameConfiguration.dexStaminaMultiplier + GameConfiguration.staminaBaseModifier;
		return value;				
	}
	private float deriveHitpoints() {
		float value = strength * GameConfiguration.strHealthMultiplier + GameConfiguration.healthBaseModifier; 
		return value;
	}
	// return true if legal operation
	public boolean modifyMana(float value){
		
		float newValue = mana+value;
		if (newValue < 0)
			return false;
		else if (newValue > manaMax)
			mana = manaMax;
		else mana = newValue;
		
		//System.out.println("Mana("+mana+"/"+manaMax+"): "+value);
		return true;
	}
	
	public boolean modifyStamina(float value){
		
		float newValue = stamina+value;
		if (newValue < 0)
			return false;
		else if (newValue > staminaMax)
			stamina = staminaMax;
		else stamina = newValue;
		
		//System.out.println("Stamina("+stamina+"/"+staminaMax+"): "+value);
		
		return true;	
		
	}	
}
