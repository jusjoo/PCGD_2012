package sov;

//joonaslis�

public interface Animated {
	enum AnimationState {};
	AnimationState getCurrentAnimationState();
	AnimationState setCurrentAnimationState();
}
