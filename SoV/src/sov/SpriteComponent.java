package sov;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/*
 * Animated sprite.
 */
public class SpriteComponent extends Component {
	
	// AnimationState dictates which Animation is going to be drawn and updated
	//public enum AnimationState { Idle, Run, Jump, Fall, Hurt, WeaponRun, Attack1, Attack2, Die }
	protected CreatureComponent.AnimationState currentAnimationState = CreatureComponent.AnimationState.Idle;
	//protected CreatureComponent.AnimationState previousAnimationState = null;
	
	// Links Animations with AnimationStates
	protected HashMap<CreatureComponent.AnimationState, AnimationState> animations = new HashMap<CreatureComponent.AnimationState, AnimationState>();
	
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
	public SpriteComponent(Entity parent, HashMap<CreatureComponent.AnimationState, AnimationState> animations) {
		super(parent);
		this.animations.putAll(animations);
		currentFrame = new Sprite(animations.get(currentAnimationState).getKeyFrame(0, true));
	}
	
	public SpriteComponent(Entity parent) {
		super(parent);
	}
	
	// Position, rotate, flip the sprite, and then render it.
	// @Pre: SpriteBatch needs to be enabled before calling render.
	public void render(SpriteBatch spriteBatch, boolean facingRight, float x, float y, float angle, Vector2 collisionBoxSize) {
		currentFrame.setSize(currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
		
		int offSet = 0;
		
		// FIXME: 16 should really be the... unit size or something?
		if(facingRight) {
			currentFrame.flip(false, false);
			offSet = animations.get(currentAnimationState).offset * 16;
		} else {
			currentFrame.flip(true, false);
			offSet = -animations.get(currentAnimationState).offset * 16;
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
	
	public void setCurrentAnimationState(CreatureComponent.AnimationState state) {
		//previousAnimationState = currentAnimationState;
	
		
		
		
		
		if(animations.get(currentAnimationState).looping || animations.get(currentAnimationState).isLastFrame(stateTime)) {
			currentAnimationState = state;
		}
		
		//if(previousAnimationState != currentAnimationState) { stateTime = 0; }
		
	}
	
	public void setAnimations(HashMap<CreatureComponent.AnimationState, AnimationState> animations) {
		this.animations = animations;
	}
	
	public void addAnimation(CreatureComponent.AnimationState animationState, AnimationState animation) {
		animations.put(animationState, animation);
	}

	@Override
	public void update(float deltaTime) {
		stateTime += deltaTime;
		//boolean looping = true;
		/*if(currentAnimationState == AnimationState.Die ||
				currentAnimationState == AnimationState.Attack1) looping = false;*/
		currentFrame.setRegion(animations.get(currentAnimationState).getKeyFrame(stateTime, animations.get(currentAnimationState).looping));
		
	}
}
