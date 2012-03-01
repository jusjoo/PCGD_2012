package sov;

import com.badlogic.gdx.Gdx;

public class PlayerInputComponent extends InputComponent {
	
	GameConfiguration config = new GameConfiguration();
	private static boolean canPressKey = true;
	private static float inputTimer = 0;

	public PlayerInputComponent(Entity parent) {
		super(parent);
		this.movementComponent = parent.getComponent(MovementComponent.class);
		
	}
	
	@Override
	public void update(float deltaTime) {
				if (inputTimer > 0) {
					inputTimer -= deltaTime;
				}
				else
					canPressKey = true;
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
					
					parent.getComponent(AttackComponent.class).attack(SpriteComponent.AnimationState.Attack1);
				
					
					//((Creature)parent).attack(AttackType.Melee);
				}
				if (Gdx.input.isKeyPressed(GameConfiguration.actionAltAttack) && Creature.class.isAssignableFrom(parent.getClass())) {
					
					parent.getComponent(AttackComponent.class).attack(SpriteComponent.AnimationState.Attack2);
			
				
				//((Creature)parent).attack(AttackType.Melee);
				}
				if (canPressKey && Gdx.input.isKeyPressed(GameConfiguration.actionSpecial) && Creature.class.isAssignableFrom(parent.getClass())) {
					
					((Creature)parent).activateSpecialAbility();
				}				
				
				
	}
	public static void keyPressed() {
		canPressKey = false;
		inputTimer = 0.2f;
	}

}
