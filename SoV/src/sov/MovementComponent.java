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
	protected float jumpDelay = 0.3f;
	protected boolean moving;
	
	protected int maxJumps;
	protected int jumpsLeft;
	
	public MovementComponent(Entity parent, float speed, float jumpHeight) {
		super(parent);
		int jumps = 2;		
		bodyComponent = parent.getComponent(BodyComponent.class);
		spriteComponent = parent.getComponent(SpriteComponent.class);
		this.speed = speed;
		this.jumpHeight = jumpHeight;
		this.maxJumps = jumps;
		this.jumpsLeft = maxJumps;
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

		if (allowJumping == true){
			if(jumpTimer >= jumpDelay) {
				jumpsLeft = maxJumps;
			}			
		}
		else jumpsLeft--;
	}
	
	public void setJumpHeight(float jumpHeight) {
		this.jumpHeight = jumpHeight;
	}
	
	public void move(boolean towardsRight) {
		bodyComponent.setFacingRight(towardsRight);
		if(towardsRight) { deltaMove.add(speed, 0f); } 
		else { deltaMove.add(-speed, 0f); }
		if(onGround()) { spriteComponent.setCurrentAnimationState(SpriteComponent.AnimationState.Run); }
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
		if(onGround()) { spriteComponent.setCurrentAnimationState(SpriteComponent.AnimationState.Run); }
		moving = true;
	}
	
	public void jump() {
		/*
		 * Set correct staminacost - different for second jump.
		 */
		float staminacost;
		if (jumpsLeft > 0 && jumpsLeft < maxJumps)
			staminacost = GameConfiguration.staminaCostDoubleJump;
		else staminacost = GameConfiguration.staminaCostJump;
		
		if(allowJumping() && jumpTimer >= jumpDelay && ((Creature)parent).modifyStamina(-staminacost)) {
			System.out.println("Jumping! "+jumpHeight);			
			spriteComponent.setCurrentAnimationState(SpriteComponent.AnimationState.Jump);
			deltaMove.set(deltaMove.x, jumpHeight);
			setAllowJumping(false);
			jumpTimer = 0;
		}
		
	}

	@Override
	public void update(float deltaTime) {
		
		jumpTimer += deltaTime;
				
		deltaMove.mul(deltaTime).mul(10);
		
		bodyComponent.applyLinearImpulse(deltaMove);
		
		
		if(allowJumping() && !moving) {
			spriteComponent.setCurrentAnimationState(SpriteComponent.AnimationState.Idle);
		}
		
		
		if(bodyComponent.getLinearVelocity().y < -0.7f && jumpsLeft < maxJumps) { spriteComponent.setCurrentAnimationState(SpriteComponent.AnimationState.Fall); }
		
		moving = false;
		
		deltaMove.set(0, 0);
	}
	
	public boolean allowJumping() {
		if (jumpsLeft > 0) {
			return true;
		}
		return false;
	}
	
	public boolean onGround() {
		Vector2 velocity = getBodyComponent().getLinearVelocity();
		if (Math.abs(velocity.x) > 0.1 || Math.abs(velocity.y) > 0.1 || jumpsLeft == 0 || jumpsLeft > 0 && jumpsLeft < maxJumps) {
			return false;
		}
		else return true;
			
//		if (jumpsLeft == 0 || jumpsLeft > 0 && jumpsLeft < maxJumps)
//			return false;		
//		else
//			return true;
	}
	public float getSpeed() {
		return speed;
	}
	public float getJumpHeight() {
		return this.jumpHeight;
	}

}
