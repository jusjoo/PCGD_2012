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
				staticBody, rounding, circle, slopeShape, false);
		
		animatedSprite = new AnimatedSprite(animations);
		
	}
	
	public void render(SpriteBatch spriteBatch) {	
		
		float PIXELS_PER_METER = GameConfiguration.PIXELS_PER_METER;
		
		/*animatedSprite.render(spriteBatch, facingRight,
				//body.getWorldCenter().x * PIXELS_PER_METER,
				body.getWorldCenter().x * PIXELS_PER_METER - (1-(size.x / animatedSprite.getSprite().getWidth()))*24f ,
				//body.getWorldCenter().x * PIXELS_PER_METER - (1-(size.x / animatedSprite.getSprite().getRegionWidth()*animatedSprite.getSprite().getRegionWidth())),
				body.getWorldCenter().y * PIXELS_PER_METER,
				(float) (body.getAngle()*180/Math.PI));*/
		/*animatedSprite.render(spriteBatch, facingRight,
		body.getWorldCenter().x * PIXELS_PER_METER,
		body.getWorldCenter().y * PIXELS_PER_METER,
		//body.getPosition().x * PIXELS_PER_METER - (size.x - animatedSprite.getSprite().getWidth())/2,
		//body.getPosition().y * PIXELS_PER_METER,
		(float) (body.getAngle()*180/Math.PI));
		if(this.getClass() == Player.class || this.getClass() == Monster.class) {
			System.out.println(animatedSprite.getSprite().getOriginX());
		}*/
		
		animatedSprite.render(spriteBatch, facingRight,
				
				/*
				 * +-8 here moves the x, y into the tile's corner, for drawing purposes
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
