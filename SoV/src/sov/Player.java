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

	protected float speed;
	protected float jumpHeight;

	// Deliver size and position of the player in pixels.
	public Player(World world, Vector2 position, Vector2 size, HashMap<AnimationState, Animation> animations, float rounding,
			boolean circle, float speed, float jumpHeight) {
		super(world, position, size, animations, rounding,
				circle);
		this.speed = speed;
		this.jumpHeight = jumpHeight;
	}
	
	//@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		takeInput();
	}
	
	protected void takeInput() {
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			body.applyLinearImpulse(new Vector2(speed, 0.0f), body.getWorldCenter());
			facingRight = true;
		} else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			body.applyLinearImpulse(new Vector2(-speed, 0.0f), body.getWorldCenter());
			facingRight = false;
		}
		
		// Check for a maximum vertical speed and allowJumping to make sure jumping is allowed
		if (Gdx.input.isKeyPressed(Input.Keys.UP) && Math.abs(body.getLinearVelocity().y) < 1.7f
				&& allowJumping) {
			body.applyLinearImpulse(new Vector2(0.0f, jumpHeight), body.getWorldCenter());
			setAllowJumping(false);
		}
	}

}
