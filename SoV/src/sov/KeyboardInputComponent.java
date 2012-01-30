package sov;

import sov.Creature.AttackType;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class KeyboardInputComponent extends InputComponent {
	
	GameConfiguration config = new GameConfiguration();

	public KeyboardInputComponent(Entity parent, float speed) {
		super(parent, speed);
	}
	
	@Override
	public void update(float deltaTime) {
				super.update(deltaTime);
				deltaMove.set(0, 0);
				if (Gdx.input.isKeyPressed(GameConfiguration.moveRight)) {
					deltaMove.set(speed, 0f);
					bodyComponent.setFacingRight(true);
					
				} else if (Gdx.input.isKeyPressed(GameConfiguration.moveLeft)) {
					deltaMove.set(-speed, 0f);
					bodyComponent.setFacingRight(false);
				}
				
				// Jumping
				// Check for a maximum vertical speed and allowJumping to make sure jumping is allowed
				if (Gdx.input.isKeyPressed(GameConfiguration.actionJump) && Math.abs(bodyComponent.body.getLinearVelocity().y) < 1.7f
						&& Creature.class.isAssignableFrom(parent.getClass())) {
					((Creature)parent).jump();
				}
				
				// Attacking
				if (Gdx.input.isKeyPressed(GameConfiguration.actionAttack) && Creature.class.isAssignableFrom(parent.getClass())) {
					((Creature)parent).getComponent(AttackComponent.class).attack();
					//((Creature)parent).attack(AttackType.Melee);
				}
				
				
	}

}
