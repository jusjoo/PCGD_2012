package sov;

import java.util.ArrayList;
import java.util.HashMap;

import sov.BodyComponent.SlopeShape;

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
	
	protected float speed;
	protected float jumpHeight;
	
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
		creature.speed = creature.dexterity  / 2f;
		creature.jumpHeight = prototype.dexterity * 1.5f;
		
		creature.body.setMaxVelocity(creature.speed*1.15f);
		
		// Set the attack component
		if(prototype.getComponent(AttackComponent.class) != null) {
			creature.addComponent(prototype.getComponent(AttackComponent.class));
			creature.getComponent(AttackComponent.class).setParent(creature);
		}
		if(prototype.getComponent(RangedAttackComponent.class) != null) {
			creature.addComponent(prototype.getComponent(RangedAttackComponent.class));
			creature.getComponent(RangedAttackComponent.class).setParent(creature);
		}
		
		
		return creature;
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);

	}
	
	public void removeFromWorld(){
		getComponent(BodyComponent.class).removeFromWorld();
		getComponent(BodyComponent.class).body.destroyFixture(sensorFixture);
	}
	
	public void addToWorld(World world, Vector2 position) {
		getComponent(BodyComponent.class).addToWorld(world, position);
		//super.addToWorld(world, position);
		float PIXELS_PER_METER = GameConfiguration.PIXELS_PER_METER;
		
		// Create a sensor at the level of the feet for detecting if a creature is touching the ground.
		// TODO: Transfer creating the body, shape and fixture to a static helper function.
		PolygonShape sensorShape = new PolygonShape();
		sensorShape.setAsBox(body.getSize().x / (3 * PIXELS_PER_METER), body.getSize().y / (12 * PIXELS_PER_METER),
				new Vector2(0, -body.getSize().y/PIXELS_PER_METER/2), 0);
		FixtureDef sensorFixtureDef = new FixtureDef();
		sensorFixtureDef.shape = sensorShape;
		sensorFixtureDef.isSensor = true;
		sensorFixtureDef.density = 0;
		
		
		
		
		// Attach the foot-sensor on the body.
		sensorFixture = getComponent(BodyComponent.class).body.createFixture(sensorFixtureDef);
		
		
		
		// Creatures shall not rotate according to physics!
		getComponent(BodyComponent.class).body.setFixedRotation(true);
		
		
	}

}
