package sov;

import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.HashMap;

import sov.Component.ComponentType;
import sov.SpriteComponent.AnimationState;
import sov.BodyComponent.SlopeShape;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.google.gson.annotations.Expose;

/*
 * Creatures include all moving object that are controlled either by AI, or Player input.
 */
public class Creature extends SpriteBody implements Cloneable {
	
	public enum CreatureType { Barbarian, Ninja, Sorceress, Goblin };

	@Expose CreatureType creatureType;
	@Expose String textureName;
	@Expose int[] hitboxSize;
	@Expose float dexterity;
	@Expose float strength;
	@Expose float wisdom;
	@Expose HashMap<AnimationState, ArrayList<Object>> frames;
	
	
	
	
	protected AttackTimer activeAttackTimer;
	protected Fixture attackSensorFixture;
	
	protected float speed;
	protected float jumpHeight;
	
	
	public enum AttackType {Melee, Ranged};
	boolean allowJumping = true;
	boolean canAttack = true;
	
	public Creature() {
	}
	
	// Deliver size and position of the creature in pixels.
	public Creature(Vector2 size, HashMap<AnimationState, Animation> animations, float rounding,
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
		
		return creature;
	}
	
	public void update(float deltaTime) {
		
		super.update(deltaTime);
		
		Vector2 currentVelocity = body.getLinearVelocity();
		
		// Set animation states
		if(alive && canAttack) {
			if(!allowJumping) {			
				if (currentVelocity.y < 0.0f)
					spriteComponent.setCurrentAnimationState(AnimationState.Fall);
				else
					spriteComponent.setCurrentAnimationState(AnimationState.Jump);
			} else if(Math.abs(currentVelocity.x) > 0.5f) {
				spriteComponent.setCurrentAnimationState(AnimationState.Run);
			} else {
				spriteComponent.setCurrentAnimationState(AnimationState.Idle);
			}
		}
		
		this.activeAttackTimer.update(deltaTime);
		
	}
	
	public void setAllowJumping(boolean allowJumping) {
		this.allowJumping = allowJumping;
	}
	
	/*
	 * Attacks with the given attackType
	 * 
	 * TODO: 	Handle different activeAttackTimers, when attack
	 * 			is not the same type. Player shouldn't be able to 
	 * 			use a new attack before another attack is still processing.
	 * 
	 */
	public void attack(AttackType attackType) {
		
		if(canAttack) {
			
			
			
			float PIXELS_PER_METER = GameConfiguration.PIXELS_PER_METER;
			PolygonShape attackSensorShape = new PolygonShape();
			int offSet = 0;
			if(body.getFacingRight()) offSet = 1;
			else offSet = -1;
			attackSensorShape.setAsBox(body.getSize().x / PIXELS_PER_METER / 1.5f, body.getSize().y / PIXELS_PER_METER / 1.5f, new Vector2(body.getSize().x / PIXELS_PER_METER * offSet ,  0) , 0f);
			FixtureDef attackSensorFixtureDef = new FixtureDef();
			attackSensorFixtureDef.shape = attackSensorShape;
			attackSensorFixtureDef.isSensor = true;
			attackSensorFixtureDef.density = 0;
			
			this.attackSensorFixture = getComponent(BodyComponent.class).body.createFixture(attackSensorFixtureDef);
			
			
			spriteComponent.setCurrentAnimationState(AnimationState.Attack1);
			canAttack = false;
		}
		
		
	} 
	
	public void stopAttack(){
		getComponent(BodyComponent.class).body.destroyFixture(attackSensorFixture);
		canAttack = true;
		//body.getFixtureList().remove(attackSensorFixture);
	}
	
	public Fixture getAttackFixture() {
		return attackSensorFixture;
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
		FixtureDef sensorFixture = new FixtureDef();
		sensorFixture.shape = sensorShape;
		sensorFixture.isSensor = true;
		sensorFixture.density = 0;
		
		
		
		// Attach the foot-sensor on the body.
		getComponent(BodyComponent.class).body.createFixture(sensorFixture);
		
		
		
		// Creatures shall not rotate according to physics!
		getComponent(BodyComponent.class).body.setFixedRotation(true);
		
		
	}
	
	public void jump() {
		if(allowJumping) {
			getComponent(BodyComponent.class).body.applyLinearImpulse(new Vector2(0.0f, jumpHeight), getComponent(BodyComponent.class).body.getWorldCenter());
			setAllowJumping(false);
		}
		
	}
	
	public Vector2 getPosition(){
		
		// FIXME: Is this position correct?		
		return body.getPosition();
	}

}
