package sov;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/*
 * SpriteBodies are entities that have both a drawable sprite, and a physics body.
 */
public class SpriteBody extends Entity {
	
	// Components
	BodyComponent body;
	SpriteComponent spriteComponent;
	
	
	

	
	
	
	public SpriteBody() {
		spriteComponent = new SpriteComponent(this);
		addComponent(spriteComponent);
	}

	public SpriteBody(Vector2 size, HashMap<CreatureComponent.AnimationState, AnimationState> animations,
			boolean staticBody, float rounding, boolean circle, BodyComponent.SlopeShape slopeShape) {
		
		body = new BodyComponent(this, size, staticBody, rounding, circle, slopeShape, false);
		spriteComponent = new SpriteComponent(this, animations);
		
		addComponent(body);
		addComponent(spriteComponent);
	}
	
	public void render(SpriteBatch spriteBatch) {	
		
		spriteComponent.render(spriteBatch, body.getFacingRight(),
				body.getPosition().x,
				body.getPosition().y,
				(float) (body.getAngle()*180/Math.PI),
				body.getSize()
				);
	}
	
	public void update(float deltaTime) {
		
		
		
		super.update(deltaTime);
		Vector2 currentVelocity = body.getLinearVelocity();
		
		
		if(currentVelocity.x > body.getMaxVelocity()) {
			body.setLinearVelocity(body.getMaxVelocity(), body.getLinearVelocity().y);
		}
		else if(currentVelocity.x < -body.getMaxVelocity()) {
			body.setLinearVelocity(-body.getMaxVelocity(), body.getLinearVelocity().y);
		}
		
		
	}
	
	
	
	public Vector2 getPosition() {
		return getComponent(BodyComponent.class).getPosition();
	}
	
	
	
	
	
}
