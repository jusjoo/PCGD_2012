package sov;

public class CreatureComponent extends Component {
	
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
						spriteComponent.setCurrentAnimationState(Animation.Fall);
					else
						spriteComponent.setCurrentAnimationState(Animation.Jump);
				} else if(Math.abs(currentVelocity.x) > 0.5f) {
					spriteComponent.setCurrentAnimationState(Animation.Run);
				} else {
					spriteComponent.setCurrentAnimationState(Animation.Idle);
				}
			} else {
				if(!allowJumping) {			
					if (currentVelocity.y < 0.0f)
						spriteComponent.setCurrentAnimationState(Animation.Fall);
					else
						spriteComponent.setCurrentAnimationState(Animation.Jump);
				} else if(Math.abs(currentVelocity.x) > 0.5f) {
					spriteComponent.setCurrentAnimationState(Animation.Run);
				} else {
					spriteComponent.setCurrentAnimationState(Animation.Idle);
				}
				
			}
			
				
		}
		*/
	}

}
