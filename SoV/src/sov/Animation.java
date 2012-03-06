package sov;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Animation extends com.badlogic.gdx.graphics.g2d.Animation {

	protected boolean looping;
	protected Vector2 offset;
	protected int animationLength;
	
	public Animation(float frameDuration, List keyFrames, boolean looping, Vector2 offset) {
		super(frameDuration, keyFrames);
		//this.currentState = currentState;
		this.looping = looping;
		this.offset = offset;
		this.animationLength = keyFrames.size();
	}

	public Animation(float frameDuration, TextureRegion... keyFrames) {
		super(frameDuration, keyFrames);
	}
	
	public boolean isLastFrame(float stateTime) {
		return (stateTime) >= animationLength*frameDuration;
	}
	public static void play(Entity entity, SpriteComponent.AnimationState animation) {
		entity.getComponent(SpriteComponent.class).setCurrentAnimationState(animation);		
	}
	public float getLength() {
		return (animationLength*frameDuration);
	}

}
