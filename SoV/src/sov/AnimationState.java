package sov;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationState extends Animation {

	protected boolean looping;
	protected int offset;
	protected int animationLength;
	
	public AnimationState(float frameDuration, List keyFrames, boolean looping, int offset) {
		super(frameDuration, keyFrames);
		//this.currentState = currentState;
		this.looping = looping;
		this.offset = offset;
		this.animationLength = keyFrames.size();
	}

	public AnimationState(float frameDuration, TextureRegion... keyFrames) {
		super(frameDuration, keyFrames);
	}
	
	public boolean isLastFrame(float stateTime) {
		return (stateTime/this.frameDuration) >= animationLength*frameDuration;
	}
	

}
