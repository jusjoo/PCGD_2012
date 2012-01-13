package coffeeGDX;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Player extends MovingSprite {

	protected float speed;
	protected float jumpHeight;
	
	public Player(TextureRegion textureRegion, World world, Vector2 position, float speed, float jumpHeight) {
		super(textureRegion, world, position);
		this.speed = speed;
		this.jumpHeight = jumpHeight;
		
	}
	
	//@Override
	public void update(float deltaTime) {
		
		//body.
		super.update(deltaTime);
		takeInput();
	}
	
	protected void takeInput() {
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			body.applyLinearImpulse(new Vector2(speed, 0.0f), body.getWorldCenter());

			
		} else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			body.applyLinearImpulse(new Vector2(-speed, 0.0f), body.getWorldCenter());
		}
 
		if (Gdx.input.isKeyPressed(Input.Keys.UP) && Math.abs(body.getLinearVelocity().y) < 1e-6
				&& allowJumping) {
		//if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			body.applyLinearImpulse(new Vector2(0.0f, jumpHeight), body.getWorldCenter());
			setAllowJumping(false);
		}
	}

}
