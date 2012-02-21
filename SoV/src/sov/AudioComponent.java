package sov;

import java.util.HashMap;

import sov.SpriteComponent.AnimationState;


import com.badlogic.gdx.audio.Sound;

public class AudioComponent extends Component {
	
	private HashMap<AnimationState, Sound> sounds;

	private float timer = 0;
	
	public AudioComponent(Entity parent) {
		super(parent);
		
		sounds = new HashMap<AnimationState, Sound>();
	}
	
	/*
	 * @.pre: true
	 */
	public void playSound(AnimationState state) {
		if (sounds.get(state) != null && timer <= 0){
			sounds.get(state).play();
			timer = GameConfiguration.audioComponentTimer;
		}
	}

	@Override
	public void update(float deltaTime) {
		if (timer > 0) timer -= deltaTime;
		
	}
	
	public void addSound(AnimationState animationState, Sound sound) {
		sounds.put(animationState, sound);
	}

}
