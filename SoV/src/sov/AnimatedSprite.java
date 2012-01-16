package sov;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class AnimatedSprite {
	protected enum AnimationState { IDLE, RUN, JUMP, HURT }
	protected AnimationState currentAnimationState = AnimationState.IDLE;
	
	protected HashMap<AnimationState, Animation> animations = new HashMap<AnimationState, Animation>();
	protected Sprite sprite;
	
	public Sprite getSprite() {
		return sprite;
	}
}
