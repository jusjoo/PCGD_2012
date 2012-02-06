package sov;

import com.badlogic.gdx.math.Vector2;

public class MovementComponent extends Component {

	protected BodyComponent bodyComponent;
	protected InputComponent inputComponent;
	protected SpriteComponent spriteComponent;
	
	protected Vector2 deltaMove = new Vector2();
	protected float speed;
	protected float jumpHeight;
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
	
	public void jump() {
		if(allowJumping) {
			spriteComponent.setCurrentAnimationState(SpriteComponent.AnimationState.Jump);
			deltaMove.set(deltaMove.x, jumpHeight);
			setAllowJumping(false);
		}
	}

	@Override
	public void update(float deltaTime) {
		bodyComponent.applyLinearImpulse(deltaMove);
		if(allowJumping && !moving) {
			spriteComponent.setCurrentAnimationState(SpriteComponent.AnimationState.Idle);
		}
		
		
		if(bodyComponent.getLinearVelocity().y < -0.7f) { spriteComponent.setCurrentAnimationState(SpriteComponent.AnimationState.Fall); }
		
		moving = false;
		
		deltaMove.set(0, 0);
	}

}
