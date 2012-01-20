package sov;

import java.util.HashMap;

import sov.AnimatedSprite.AnimationState;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Player extends Creature {

	protected enum CharacterClass { Barbarian, Sorceress, Ninja };
	
	
	protected float jumpHeight;
	protected float attackImpulse;
	protected GameConfiguration config;
	
	protected CharacterClass characterClass;

	// Deliver size and position of the player in pixels.
	public Player(World world, Vector2 position, Vector2 size, HashMap<AnimationState, Animation> animations, float rounding,
			boolean circle, GameConfiguration config, CharacterClass characterClass) {
		super(world, position, size, animations, rounding,
				circle);
		this.speed = config.speed;
		this.jumpHeight = config.jumpHeight;
		this.attackImpulse = config.attackImpulse;
		this.config = config;
		this.characterClass = characterClass;
	}
	
	//@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		takeInput();
	}
	
	protected void takeInput() {
		
		// Movement left/right
		if (Gdx.input.isKeyPressed(config.moveRight)) {
			body.applyLinearImpulse(new Vector2(speed, 0.0f), body.getWorldCenter());
			facingRight = true;
		} else if (Gdx.input.isKeyPressed(config.moveLeft)) {
			body.applyLinearImpulse(new Vector2(-speed, 0.0f), body.getWorldCenter());
			facingRight = false;
		}
		
		// Jumping
		// Check for a maximum vertical speed and allowJumping to make sure jumping is allowed
		if (Gdx.input.isKeyPressed(config.actionJump) && Math.abs(body.getLinearVelocity().y) < 1.7f
				&& allowJumping) {
			body.applyLinearImpulse(new Vector2(0.0f, jumpHeight), body.getWorldCenter());
			setAllowJumping(false);
		}
		
		// Attacking
		if (Gdx.input.isKeyPressed(config.actionAttack)) {
			this.attack(AttackType.Melee);
			
		}

	}

}
