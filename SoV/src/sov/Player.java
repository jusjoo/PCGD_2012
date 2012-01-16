package sov;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Player extends Creature {

	protected float speed = 0.1f;
	protected float jumpHeight = 3.2f;
	
	public Player(TextureRegion textureRegion, World world, Vector2 position, Vector2 size, float speed, float jumpHeight) {
		super(textureRegion, world, position, size);
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

			
		} else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			body.applyLinearImpulse(new Vector2(-speed, 0.0f), body.getWorldCenter());
		}
 
		if (Gdx.input.isKeyPressed(Input.Keys.UP) && Math.abs(body.getLinearVelocity().y) < 1.7f
				&& allowJumping) {
			body.applyLinearImpulse(new Vector2(0.0f, jumpHeight), body.getWorldCenter());
			setAllowJumping(false);
		}
	}

}
