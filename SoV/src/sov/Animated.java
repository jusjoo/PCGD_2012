package sov;

//joonaslisä

public interface Animated {
	enum AnimationState {};
	AnimationState getCurrentAnimationState();
	AnimationState setCurrentAnimationState();
}
