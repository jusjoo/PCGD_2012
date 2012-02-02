package sov;

import com.badlogic.gdx.Gdx;

public class PlayerInputComponent extends InputComponent {
	
	GameConfiguration config = new GameConfiguration();

	public PlayerInputComponent(Entity parent) {
		super(parent);
		this.movementComponent = parent.getComponent(MovementComponent.class);
		
	}
	
	@Override
	public void update(float deltaTime) {
				if (Gdx.input.isKeyPressed(GameConfiguration.moveRight)) {
					movementComponent.move(true);
				} else if (Gdx.input.isKeyPressed(GameConfiguration.moveLeft)) {
					movementComponent.move(false);
				}
				
				// Jumping
				// Check for a maximum vertical speed and allowJumping to make sure jumping is allowed
				if (Gdx.input.isKeyPressed(GameConfiguration.actionJump)) {
					movementComponent.jump();
				}
				
				// Attacking
				if (Gdx.input.isKeyPressed(GameConfiguration.actionAttack) && Creature.class.isAssignableFrom(parent.getClass())) {
					
					// FIXME: quickndirty
					if (((Creature)parent).getComponent(AttackComponent.class) != null) {
						((Creature)parent).getComponent(AttackComponent.class).attack();
					} else if (((Creature)parent).getComponent(RangedAttackComponent.class) != null) {
						((Creature)parent).getComponent(RangedAttackComponent.class).attack();
					}
					
					//((Creature)parent).attack(AttackType.Melee);
				}
				
				
	}

}
