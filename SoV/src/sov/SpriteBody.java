package sov;

import java.util.HashMap;

import sov.SpriteComponent.AnimationState;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/*
 * SpriteBodies are entities that have both a drawable sprite, and a physics body.
 */
public class SpriteBody {
	
	// Components
	BodyComponent body;
	SpriteComponent spriteComponent;
	
	boolean facingRight = true;	
	
	
	protected float hitPoints = 1;
	boolean alive = true;
	
	boolean setToDie = false;
	
	public SpriteBody() {
		spriteComponent = new SpriteComponent(this);
	}

	public SpriteBody(Vector2 size, HashMap<AnimationState, Animation> animations,
			boolean staticBody, float rounding, boolean circle, BodyComponent.SlopeShape slopeShape) {
		
		body = new BodyComponent(this, size, staticBody, rounding, circle, slopeShape, false);
		spriteComponent = new SpriteComponent(this, animations);
		
	}
	
	public SpriteComponent getSpriteComponent() {
		return spriteComponent;
	}
	
	protected BodyComponent getBodyComponent() {
		return body;
	}
	
	public void render(SpriteBatch spriteBatch) {	
		
		//float PIXELS_PER_METER = GameConfiguration.PIXELS_PER_METER;
		
		spriteComponent.render(spriteBatch, facingRight,
				
				/*
				 * +-8 here moves the x, y into the tile's corner, for drawing purposes
				 */
				body.getPosition().x,
				body.getPosition().y,
				//body.getPosition().y * PIXELS_PER_METER ,
				(float) (body.getAngle()*180/Math.PI),
				body.getSize()
				);
	}
	
	public void update(float deltaTime) {
		spriteComponent.animate(deltaTime);
		
		Vector2 currentVelocity = body.getLinearVelocity();
		
		if(currentVelocity.x > body.getMaxVelocity()) {
			body.setLinearVelocity(body.getMaxVelocity(), body.getLinearVelocity().y);
		}
		else if(currentVelocity.x < -body.getMaxVelocity()) {
			body.setLinearVelocity(-body.getMaxVelocity(), body.getLinearVelocity().y);
		}
		
		if(setToDie) { die(); }
	}
	
	public void takeDamage(float damage) {
		hitPoints -= damage;
		if(hitPoints <= 0) {
			setToDie = true;
		}
	}
	
	protected void die() {
		if(alive) {
			//body.destroyFixture(body.getFixtureList().get(0));
			//body.getFixtureList().clear();
			body.die();
			spriteComponent.setCurrentAnimationState(AnimationState.Die);
			alive = false;
		}
		
	}
	
}
