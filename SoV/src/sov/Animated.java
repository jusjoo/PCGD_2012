package sov;

public interface Animated {
	enum AnimationState {};
	AnimationState getCurrentAnimationState();
	AnimationState setCurrentAnimationState();
}
