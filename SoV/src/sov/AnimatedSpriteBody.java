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

	public AnimatedSpriteBody(World world, Vector2 position, Vector2 size, HashMap<AnimationState, Animation> animations,
			boolean staticBody, float rounding, boolean circle, SlopeShape slopeShape) {
		super(world, position, size,
				staticBody, rounding, circle, slopeShape);
		
		animatedSprite = new AnimatedSprite(animations);
		
	}
	
	public void render(SpriteBatch spriteBatch) {	
		
		int PIXELS_PER_METER = GameConfiguration.PIXELS_PER_METER;
		
		animatedSprite.render(spriteBatch, facingRight,
				
				/*
				 * Warning: + and - 8 somehow center's the Sprites.
				 * If there is trouble, check this one first...
				 * 
				 * TODO: wtf?
				 */
				body.getWorldCenter().x * PIXELS_PER_METER +8,
				body.getWorldCenter().y * PIXELS_PER_METER -8,
				(float) (body.getAngle()*180/Math.PI));
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
		
	}
	
}
