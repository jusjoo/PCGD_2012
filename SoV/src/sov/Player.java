package sov;

import java.util.HashMap;

import sov.SpriteComponent.AnimationState;

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

	//protected enum CharacterClass { Barbarian, Sorceress, Ninja };
	
	protected GameConfiguration config = new GameConfiguration();
	
	protected float attackImpulse;
	
	//protected CharacterClass characterClass;

	// Deliver size and position of the player in pixels.
	public Player(Vector2 size, HashMap<AnimationState, Animation> animations, float rounding,
			boolean circle) {
		super(size, animations, rounding,
				circle);
		this.attackImpulse = config.attackImpulse;
	}
	
	//@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		takeInput();
	}
	
	protected void takeInput() {
		
		// Movement left/right
		if (Gdx.input.isKeyPressed(config.moveRight)) {
			getBodyComponent().body.applyLinearImpulse(new Vector2(speed, 0.0f), getBodyComponent().body.getWorldCenter());
			facingRight = true;
		} else if (Gdx.input.isKeyPressed(config.moveLeft)) {
			getBodyComponent().body.applyLinearImpulse(new Vector2(-speed, 0.0f), getBodyComponent().body.getWorldCenter());
			facingRight = false;
		}
		
		// Jumping
		// Check for a maximum vertical speed and allowJumping to make sure jumping is allowed
		if (Gdx.input.isKeyPressed(config.actionJump) && Math.abs(body.getLinearVelocity().y) < 1.7f
				&& allowJumping) {
			System.out.println("JUMPING!");
			jump();
		}
		
		// Attacking
		if (Gdx.input.isKeyPressed(config.actionAttack)) {
			this.attack(AttackType.Melee);
			
		}

	}

}
