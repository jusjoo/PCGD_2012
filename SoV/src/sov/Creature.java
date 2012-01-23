package sov;

import java.util.HashMap;

import sov.AnimatedSprite.AnimationState;
import sov.BodyEntity.SlopeShape;

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
	
	@Expose
	CreatureType creatureType;
	
	@Expose
	
	
	
	protected AttackTimer activeAttackTimer;
	protected Fixture attackSensorFixture;
	
	
	public enum AttackType {Melee, Ranged};
	boolean allowJumping = true;
	boolean canAttack = true;
	
	protected float speed = 0.5f;
	
	
	// Deliver size and position of the creature in pixels.
	public Creature(Vector2 size, HashMap<AnimationState, Animation> animations, float rounding,
			boolean circle) {
		super(size, animations,
				false, rounding, circle, SlopeShape.Even);
		

		
		
	}
	
	public void update(float deltaTime) {
		
		super.update(deltaTime);
		
		Vector2 currentVelocity = body.getLinearVelocity();
		
		// Set animation states
		if(alive && canAttack) {
			if(!allowJumping) {			
				if (currentVelocity.y < 0.0f)
					animatedSprite.setCurrentAnimationState(AnimationState.FALL);
				else
					animatedSprite.setCurrentAnimationState(AnimationState.JUMP);
			} else if(Math.abs(currentVelocity.x) > 0.5f) {
				animatedSprite.setCurrentAnimationState(AnimationState.RUN);
			} else {
				animatedSprite.setCurrentAnimationState(AnimationState.IDLE);
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
			activeAttackTimer.startAttackTimer();
			
			
			float PIXELS_PER_METER = GameConfiguration.PIXELS_PER_METER;
			PolygonShape attackSensorShape = new PolygonShape();
			int offSet = 0;
			if(facingRight) offSet = 1;
			else offSet = -1;
			attackSensorShape.setAsBox(size.x / PIXELS_PER_METER / 1.5f, size.y / PIXELS_PER_METER / 1.5f, new Vector2(size.x / PIXELS_PER_METER * offSet ,  0) , 0f);
			FixtureDef attackSensorFixtureDef = new FixtureDef();
			attackSensorFixtureDef.shape = attackSensorShape;
			attackSensorFixtureDef.isSensor = true;
			attackSensorFixtureDef.density = 0;
			
			this.attackSensorFixture = body.createFixture(attackSensorFixtureDef);
			
			
			animatedSprite.setCurrentAnimationState(AnimationState.WEAPON_ATTACK);
			canAttack = false;
		}
		
		
	}
	
	public void stopAttack(){
		body.destroyFixture(attackSensorFixture);
		canAttack = true;
		//body.getFixtureList().remove(attackSensorFixture);
	}
	
	public Fixture getAttackFixture() {
		return attackSensorFixture;
	}
	
	public Creature clone() {
		Creature clone = new Creature(size, animatedSprite.animations, 1.0f, false);
		return clone;
	}
	
	public void addToWorld(World world, Vector2 position) {
		super.addToWorld(world, position);
		float PIXELS_PER_METER = GameConfiguration.PIXELS_PER_METER;
		
		// Create a sensor at the level of the feet for detecting if a creature is touching the ground.
		// TODO: Transfer creating the body, shape and fixture to a static helper function.
		PolygonShape sensorShape = new PolygonShape();
		sensorShape.setAsBox(size.x / (3 * PIXELS_PER_METER), size.y / (12 * PIXELS_PER_METER),
				new Vector2(0, -size.y/PIXELS_PER_METER/2), 0);
		FixtureDef sensorFixture = new FixtureDef();
		sensorFixture.shape = sensorShape;
		sensorFixture.isSensor = true;
		sensorFixture.density = 0;
		
		
		
		// Attach the foot-sensor on the body.
		body.createFixture(sensorFixture);
		
		
		activeAttackTimer = new AttackTimer(this, 0.5f);

		// Creatures shall not rotate according to physics!
		body.setFixedRotation(true);
	}

}
