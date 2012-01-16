package sov;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class AnimatedSprite {
	public enum AnimationState { IDLE, RUN, JUMP, HURT }
	protected AnimationState currentAnimationState = AnimationState.IDLE;
	
	protected HashMap<AnimationState, Animation> animations = new HashMap<AnimationState, Animation>();
	protected Sprite sprite;
	
	protected Vector2 size;
	
	public Sprite getSprite() {
		return sprite;
	}
	
	public AnimatedSprite(HashMap<AnimationState, Animation> animations) {
		this.animations.putAll(animations);
		
		sprite = new Sprite(animations.get(currentAnimationState).getKeyFrame(0, true));
		
	}
	
	/*public AnimatedSprite() {
		
	}*/
	
	public void addAnimation(AnimationState animationState, Animation animation) {
		animations.put(animationState, animation);
	}
}
