package sov;

import java.util.HashMap;

import sov.AnimatedSprite.AnimationState;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/*
 * AnimatedSpriteBodies are entities that have both a drawable sprite, and a physics body.
 */
public class AnimatedSpriteBody extends BodyEntity {
	
	boolean facingRight = true;	
	AnimatedSprite animatedSprite;
	
	protected float hitPoints = 1;
	boolean alive = true;
	
	boolean setToDie = false;

	public AnimatedSpriteBody(World world, Vector2 position, Vector2 size, HashMap<AnimationState, Animation> animations,
			boolean staticBody, float rounding, boolean circle, SlopeShape slopeShape) {
		super(world, position, size,
				staticBody, rounding, circle, slopeShape, false);
		
		animatedSprite = new AnimatedSprite(animations);
		
	}
	
	public void render(SpriteBatch spriteBatch) {	
		
		float PIXELS_PER_METER = GameConfiguration.PIXELS_PER_METER;
		
		animatedSprite.render(spriteBatch, facingRight,
				
				/*
				 * +-8 here moves the x, y into the tile's corner, for drawing purposes
				 */
				body.getPosition().x * PIXELS_PER_METER ,
				body.getWorldCenter().y * PIXELS_PER_METER ,
				//body.getPosition().y * PIXELS_PER_METER ,
				(float) (body.getAngle()*180/Math.PI),
				size
				);
	}
	
	public void update(float deltaTime) {
		animatedSprite.animate(deltaTime);
		
		Vector2 currentVelocity = body.getLinearVelocity();
		
		if(currentVelocity.x > maxVelocity) {
			body.setLinearVelocity(maxVelocity, body.getLinearVelocity().y);
		}
		else if(currentVelocity.x < -maxVelocity) {
			body.setLinearVelocity(-maxVelocity, body.getLinearVelocity().y);
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
			body.destroyFixture(bodyFixture);
			body.getFixtureList().clear();
			body.setGravityScale(0);
			body.setLinearVelocity(0f, 0f);
			animatedSprite.setCurrentAnimationState(AnimationState.DIE);
			alive = false;
		}
		
	}
	
}
