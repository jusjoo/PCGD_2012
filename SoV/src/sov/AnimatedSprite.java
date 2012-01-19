package sov;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/*
 * Animated sprite.
 */
public class AnimatedSprite {
	
	// AnimationState dictates which Animation is going to be drawn and updated
	public enum AnimationState { IDLE, RUN, JUMP, FALL, HURT }
	protected AnimationState currentAnimationState = AnimationState.IDLE;
	
	// Links Animations with AnimationStates
	protected HashMap<AnimationState, Animation> animations = new HashMap<AnimationState, Animation>();
	
	// Sprite used to hold the current frame
	protected Sprite currentFrame;
	
	// Used to keep track of animation time
	float stateTime = 0;
	
	// Size of the sprite in pixels
	//protected Vector2 size;
	
	public Sprite getSprite() {
		return currentFrame;
	}
	
	/* Animations need to be created somewhere else. Could be a static factory method
	 * in this class, eventually.
	 */
	public AnimatedSprite(HashMap<AnimationState, Animation> animations) {
		this.animations.putAll(animations);
		currentFrame = new Sprite(animations.get(currentAnimationState).getKeyFrame(0, true));
		//currentFrame.setOrigin(currentFrame.getWidth()/2, currentFrame.getHeight()/2);
		//currentFrame.setOrigin(currentFrame.getU(), currentFrame.getV());
		//currentFrame.setOrigin(currentFrame.getWidth()/2, currentFrame.getHeight());
	}
	
	// Update current frame based on deltaTime.
	public void animate(float deltaTime) {
		stateTime += deltaTime;
		currentFrame.setRegion(animations.get(currentAnimationState).getKeyFrame(stateTime, true));
	}
	
	// Position, rotate, flip the sprite, and then render it.
	// @Pre: SpriteBatch needs to be enabled before calling render.
	public void render(SpriteBatch spriteBatch, boolean facingRight, float x, float y, float angle) {
		if(facingRight) {
			currentFrame.flip(false, false);
		} else {
			currentFrame.flip(true, false);
		}
		/*currentFrame.setPosition(x,
				y - currentFrame.getOriginY() - currentFrame.getHeight()/4);*/
		//currentFrame.setPosition(x,y);
		currentFrame.setRotation(angle);
		currentFrame.setPosition(x,y);
		//currentFrame.setBounds(x,y, 48f, 16f);
		
		currentFrame.draw(spriteBatch);
	}
	
	public void setCurrentAnimationState(AnimationState state) {
		currentAnimationState = state;
	}
	
	public void addAnimation(AnimationState animationState, Animation animation) {
		animations.put(animationState, animation);
	}
}
