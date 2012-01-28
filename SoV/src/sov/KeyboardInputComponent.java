package sov;

import sov.Creature.AttackType;
import sov.SpriteComponent.AnimationState;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class KeyboardInputComponent extends InputComponent {
	
	GameConfiguration config = new GameConfiguration();

	public KeyboardInputComponent(Entity parent, BodyComponent bodyComponent, float speed) {
		super(parent, bodyComponent, speed);
	}
	
	@Override
	public void update(float deltaTime) {
				super.update(deltaTime);
				deltaMove.set(0, 0);
				if (Gdx.input.isKeyPressed(config.moveRight)) {
					deltaMove.set(speed, 0f);
					bodyComponent.setFacingRight(true);
					
				} else if (Gdx.input.isKeyPressed(config.moveLeft)) {
					deltaMove.set(-speed, 0f);
					bodyComponent.setFacingRight(false);
				}
				
				// Jumping
				// Check for a maximum vertical speed and allowJumping to make sure jumping is allowed
				if (Gdx.input.isKeyPressed(config.actionJump) && Math.abs(bodyComponent.body.getLinearVelocity().y) < 1.7f
						&& Creature.class.isAssignableFrom(parent.getClass())) {
					((Creature)parent).jump();
				}
				
				// Attacking
				if (Gdx.input.isKeyPressed(config.actionAttack) && Creature.class.isAssignableFrom(parent.getClass())) {
					((Creature)parent).getComponent(AttackComponent.class).attack();
					//((Creature)parent).attack(AttackType.Melee);
				}
	}

}
