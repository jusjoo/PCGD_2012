package sov;

import com.badlogic.gdx.math.Vector2;

public class CreatureComponent extends Component {
	
	public enum AnimationState { Idle, Run, Jump, Fall, Hurt, WeaponRun, Attack1, Attack2, Die }
	
	public Vector2 deltaMove = new Vector2(0,0);
	
	public CreatureComponent(Entity parent) {
		super(parent);
	}

	@Override
	public void update(float deltaTime) {
		
		if( !((Creature)parent).allowJumping) {			
			if (deltaMove.y < 0.0f)
				parent.getComponent(SpriteComponent.class).setCurrentAnimationState(CreatureComponent.AnimationState.Fall);
			else
				parent.getComponent(SpriteComponent.class).setCurrentAnimationState(CreatureComponent.AnimationState.Jump);
		} else if(Math.abs(deltaMove.x) > 0.5f) {
			parent.getComponent(SpriteComponent.class).setCurrentAnimationState(CreatureComponent.AnimationState.Run);
		} else {
			parent.getComponent(SpriteComponent.class).setCurrentAnimationState(CreatureComponent.AnimationState.Idle);
		}
		
		
		
		
		
		
		
		
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
