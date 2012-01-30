package sov;

public class CreatureComponent extends Component {
	
	public enum AnimationState { Idle, Run, Jump, Fall, Hurt, WeaponRun, Attack1, Attack2, Die }
	
	public CreatureComponent(Entity parent) {
		super(parent);
	}

	@Override
	public void update(float deltaTime) {
		// Set animation states
		/*
			if (this.attackComponent != null) {
				if (this.attackComponent.attacking) {
					spriteComponent.setCurrentAnimationState(attackComponent.animation);
				} else if(!allowJumping) {			
					if (currentVelocity.y < 0.0f)
						spriteComponent.setCurrentAnimationState(AnimationState.Fall);
					else
						spriteComponent.setCurrentAnimationState(AnimationState.Jump);
				} else if(Math.abs(currentVelocity.x) > 0.5f) {
					spriteComponent.setCurrentAnimationState(AnimationState.Run);
				} else {
					spriteComponent.setCurrentAnimationState(AnimationState.Idle);
				}
			} else {
				if(!allowJumping) {			
					if (currentVelocity.y < 0.0f)
						spriteComponent.setCurrentAnimationState(AnimationState.Fall);
					else
						spriteComponent.setCurrentAnimationState(AnimationState.Jump);
				} else if(Math.abs(currentVelocity.x) > 0.5f) {
					spriteComponent.setCurrentAnimationState(AnimationState.Run);
				} else {
					spriteComponent.setCurrentAnimationState(AnimationState.Idle);
				}
				
			}
			
				
		}
		*/
	}

}
