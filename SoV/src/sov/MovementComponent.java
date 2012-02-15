package sov;

import com.badlogic.gdx.math.Vector2;

public class MovementComponent extends Component {

	protected BodyComponent bodyComponent;
	protected InputComponent inputComponent;
	protected SpriteComponent spriteComponent;
	
	protected Vector2 deltaMove = new Vector2();
	protected float speed;
	protected float jumpHeight;
	protected float jumpTimer = 0;
	protected float jumpDelay = 0.5f;
	protected boolean moving;
	
	boolean allowJumping = true;
	
	public MovementComponent(Entity parent, float speed, float jumpHeight) {
		super(parent);
		bodyComponent = parent.getComponent(BodyComponent.class);
		spriteComponent = parent.getComponent(SpriteComponent.class);
		this.speed = speed;
		this.jumpHeight = jumpHeight;
	}

	public MovementComponent addInputComponent(InputComponent inputComponent) {
		this.inputComponent = inputComponent;
		return this;
	}
	
	public BodyComponent getBodyComponent() {
		return bodyComponent;
	}
	
	public void setBodyComponent(BodyComponent bodyComponent) {
		this.bodyComponent = bodyComponent;
	}
	
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	public void setAllowJumping(boolean allowJumping) {
		this.allowJumping = allowJumping;
	}
	
	public void setJumpHeight(float jumpHeight) {
		this.jumpHeight = jumpHeight;
	}
	
	public void move(boolean towardsRight) {
		bodyComponent.setFacingRight(towardsRight);
		if(towardsRight) { deltaMove.set(speed, 0f); } 
		else { deltaMove.set(-speed, 0f); }
		if(allowJumping) { spriteComponent.setCurrentAnimationState(SpriteComponent.AnimationState.Run); }
		moving = true;
	}
	
	public void setFacingRight(boolean faceRight) {
		bodyComponent.setFacingRight(faceRight);
	}
	
	public void moveAway(boolean towardsRight) {
		//fixx todo
		bodyComponent.setFacingRight(!towardsRight);
		if(!towardsRight) { deltaMove.set(speed, 0f); } 
		else { deltaMove.set(-speed, 0f); }
		if(allowJumping) { spriteComponent.setCurrentAnimationState(SpriteComponent.AnimationState.Run); }
		moving = true;
	}
	
	public void jump() {
		if(allowJumping && jumpTimer >= jumpDelay && ((Creature)parent).modifyStamina(-GameConfiguration.staminaCostJump)) {
			System.out.println("Jumping!");
			spriteComponent.setCurrentAnimationState(SpriteComponent.AnimationState.Jump);
			deltaMove.set(deltaMove.x, jumpHeight);
			setAllowJumping(false);
			jumpTimer = 0;
		}
	}

	@Override
	public void update(float deltaTime) {
		
		jumpTimer += deltaTime;
		
		bodyComponent.applyLinearImpulse(deltaMove);
		if(allowJumping && !moving) {
			spriteComponent.setCurrentAnimationState(SpriteComponent.AnimationState.Idle);
		}
		
		
		if(bodyComponent.getLinearVelocity().y < -0.7f && !allowJumping) { spriteComponent.setCurrentAnimationState(SpriteComponent.AnimationState.Fall); }
		
		moving = false;
		
		deltaMove.set(0, 0);
	}

}
