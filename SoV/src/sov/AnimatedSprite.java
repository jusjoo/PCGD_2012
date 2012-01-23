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
	public enum AnimationState { IDLE, RUN, JUMP, FALL, HURT, WEAPON_RUN, WEAPON_ATTACK, DIE }
	protected AnimationState currentAnimationState = AnimationState.IDLE;
	protected AnimationState previousAnimationState = null;
	
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
	}
	
	// Update current frame based on deltaTime.
	public void animate(float deltaTime) {
		stateTime += deltaTime;
		boolean looping = true;
		if(currentAnimationState == AnimationState.DIE ||
				currentAnimationState == AnimationState.WEAPON_ATTACK) looping = false;
		currentFrame.setRegion(animations.get(currentAnimationState).getKeyFrame(stateTime, looping));
	}
	
	// Position, rotate, flip the sprite, and then render it.
	// @Pre: SpriteBatch needs to be enabled before calling render.
	public void render(SpriteBatch spriteBatch, boolean facingRight, float x, float y, float angle, Vector2 collisionBoxSize) {
		currentFrame.setSize(currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
		
		int offSet = 0;
		
		if(facingRight) {
			currentFrame.flip(false, false);
			if(currentAnimationState == AnimationState.WEAPON_ATTACK) {
				offSet = 16;
			}
		} else {
			currentFrame.flip(true, false);
			if(currentAnimationState == AnimationState.WEAPON_ATTACK) {
				offSet = -16;
			}
		}
		
		
		/*
		 *  Moves the position from the center to the corner tile
		 *  
		 *  This is full of shit!
		 */
		currentFrame.setPosition(x + 8 - currentFrame.getWidth()/2 + offSet/2,
								y -8 - currentFrame.getHeight()/2 + (currentFrame.getHeight() - collisionBoxSize.y)/2 ); 
		//currentFrame.setPosition(x ,y);
		
		currentFrame.setRotation(angle);
		currentFrame.draw(spriteBatch);
	}
	
	public void setCurrentAnimationState(AnimationState state) {
		previousAnimationState = currentAnimationState;
		currentAnimationState = state;
		if(previousAnimationState != currentAnimationState) { stateTime = 0; }
	}
	
	public void addAnimation(AnimationState animationState, Animation animation) {
		animations.put(animationState, animation);
	}
}
