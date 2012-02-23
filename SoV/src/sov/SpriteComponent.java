package sov;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/*
 * Animated sprite.
 */
public class SpriteComponent extends Component {
	
	// Animation dictates which Animation is going to be drawn and updated - AnimationStates are in priority order
	public enum AnimationState {
		Die,
		Attack1,
		Attack2,
		Hurt,
		Jump,
		Fall,
		Run,
		Idle,
		WeaponRun
		
		};
		
	private Sound sound;
	
	protected SpriteComponent.AnimationState currentAnimationState = SpriteComponent.AnimationState.Idle;
	//protected SpriteComponent.AnimationState previousAnimationState = null;
	
	// Links Animations with AnimationStates
	protected HashMap<SpriteComponent.AnimationState, Animation> animations = new HashMap<SpriteComponent.AnimationState, Animation>();
	
	// Sprite used to hold the current frame
	protected Sprite currentFrame;
	
	// Used to keep track of animation time
	float stateTime = 0;
	
	// Current frame offset
	float currentOffset = 0;
	
	// Size of the sprite in pixels
	//protected Vector2 size;
	
	public Sprite getSprite() {
		return currentFrame;
	}
	
	/* Animations need to be created somewhere else. Could be a static factory method
	 * in this class, eventually.
	 */
	public SpriteComponent(Entity parent, HashMap<SpriteComponent.AnimationState, Animation> animations) {
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
		
		//float offSet = 0;
		
		
		
		
		/*
		 *  Moves the position from the center to the corner tile
		 *  
		 *  This is full of shit!
		 */
		currentFrame.setPosition(x + 8 - currentFrame.getWidth()/2 - currentOffset,
								y -8 - currentFrame.getHeight()/2 + (currentFrame.getHeight() - collisionBoxSize.y)/2 ); 
		//currentFrame.setPosition(x ,y);
		
		// FIXME: 16 should really be the... unit size or something? (put it into a static variable somewhere)
				if(facingRight) {
					currentFrame.flip(false, false);
					currentOffset = animations.get(currentAnimationState).offset * 16;
				} else {
					currentFrame.flip(true, false);
					currentOffset = -animations.get(currentAnimationState).offset * 16;
				}
		
		currentFrame.setRotation(angle);
		currentFrame.draw(spriteBatch);
		
		// If we have a health bar attached to bodycomponent, draw that too
		if(parent.getComponent(BodyComponent.class) != null) {
			BodyComponent body = parent.getComponent(BodyComponent.class);
				if(body.healthBar != null) {
					
					// Check that the player's health bar isn't drawn, since it's done in the hud-class.
					if (body.parent.getComponent(PlayerInputComponent.class) == null) {
						body.healthBar.render(spriteBatch, body.getPosition().x, body.getPosition().y);
					}
				}
		}
	}
	
	public void setCurrentAnimationState(SpriteComponent.AnimationState state) {
		
		//if(animations.get(currentAnimationState).animationLength < stateTime) {
		
		AnimationState previousAnimationState = currentAnimationState;
		
		//System.out.println(currentAnimationState.ordinal());
		
		/*
		 * Play a sound from audioComponent
		 */
		if (parent.getComponent(AudioComponent.class) != null) {
			parent.getComponent(AudioComponent.class).playSound(state);
		}
		
		//if(animations.get(currentAnimationState).looping || animations.get(currentAnimationState).animationLength < stateTime ) {
		//if(currentAnimationState.ordinal() < previousAnimationState.ordinal() && animations.get(currentAnimationState).looping || animations.get(currentAnimationState).isLastFrame(stateTime)) {
		if(state.ordinal() <= previousAnimationState.ordinal() || (animations.get(currentAnimationState).looping && animations.get(state).looping) || animations.get(currentAnimationState).isLastFrame(stateTime)) {
			currentAnimationState = state;
			
			if(previousAnimationState != currentAnimationState) { 
				
				
				stateTime = 0; 
			}	
			
		}
		
		
		
	}
	
	public void setAnimations(HashMap<SpriteComponent.AnimationState, Animation> animations) {
		this.animations = animations;
	}
	
	public void addAnimation(SpriteComponent.AnimationState animationState, Animation animation) {
		animations.put(animationState, animation);
	}

	@Override
	public void update(float deltaTime) {
		stateTime += deltaTime;
		//boolean looping = true;
		/*if(currentAnimationState == Animation.Die ||
				currentAnimationState == Animation.Attack1) looping = false;*/
		//System.out.println(currentAnimationState);
		currentFrame.setRegion(animations.get(currentAnimationState).getKeyFrame(stateTime, animations.get(currentAnimationState).looping));
		
	} 
	
	public boolean isAtLastFrame() {
		return animations.get(currentAnimationState).isLastFrame(stateTime);
	}
}


