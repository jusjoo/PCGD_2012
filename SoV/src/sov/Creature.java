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

/*
 * Creatures include all moving object that are controlled either by AI, or Player input.
 */
public class Creature extends AnimatedSpriteBody {
	
	protected AttackTimer activeAttackTimer;
	protected Fixture attackSensorFixture;
	
	float hitPoints = 5;
	
	public enum AttackType {Melee, Ranged};
	boolean allowJumping = true;
	boolean canAttack = true;
	boolean alive = true;
	
	// Deliver size and position of the creature in pixels.
	public Creature(World world, Vector2 position, Vector2 size, HashMap<AnimationState, Animation> animations, float rounding,
			boolean circle) {
		super(world, position, size, animations,
				false, rounding, circle, SlopeShape.Even);
		
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
		
		
		activeAttackTimer = new AttackTimer(this, 1.0f);

		
		
		
		
		// Set userdata for body, used to find out which object is touching the ground in MyContactListener
		body.setUserData(this);
		
		// Creatures shall not rotate according to physics!
		body.setFixedRotation(true);
		
		die();
		
	}
	
	public void update(float deltaTime) {
		
		super.update(deltaTime);
		
		Vector2 currentVelocity = body.getLinearVelocity();
		
		// Set animation states
		if(alive) {
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
			
			this.activeAttackTimer.update(deltaTime);
		}
		
		
		
	}
	
	public void setAllowJumping(boolean allowJumping) {
		this.allowJumping = allowJumping;
	}
	
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
			
			
			animatedSprite.currentAnimationState = AnimationState.JUMP;
			canAttack = false;
		}
		
		
	}
	
	public void stopAttack(){
		body.destroyFixture(attackSensorFixture);
		canAttack = true;
		//body.getFixtureList().remove(attackSensorFixture);
	}
	
	public void takeDamage(float damage) {
		hitPoints -= damage;
		if(hitPoints <= 0) {
			die();
		}
	}
	
	public void die() {
		alive = false;
		body.destroyFixture(body.getFixtureList().get(0));
		body.setGravityScale(0);
		animatedSprite.setCurrentAnimationState(AnimationState.DIE);
	}


}
