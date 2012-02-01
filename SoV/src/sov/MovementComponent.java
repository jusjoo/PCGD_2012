package sov;

import com.badlogic.gdx.math.Vector2;

public class MovementComponent extends Component {

	protected BodyComponent bodyComponent;
	protected InputComponent inputComponent;
	protected SpriteComponent spriteComponent;
	
	protected Vector2 deltaMove = new Vector2();
	protected float speed;
	protected float jumpHeight;
	
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
	
	public void setJumpHeight(float jumpHeight) {
		this.jumpHeight = jumpHeight;
	}
	
	public void move(boolean towardsRight) {
		bodyComponent.setFacingRight(towardsRight);
		if(towardsRight) { deltaMove.set(speed, 0f); }
		else { deltaMove.set(-speed, 0f); }
	}
	
	public void jump() {
		spriteComponent.setCurrentAnimationState(SpriteComponent.AnimationState.Jump);
		deltaMove.set(deltaMove.x, jumpHeight);
	}

	@Override
	public void update(float deltaTime) {
		spriteComponent.setCurrentAnimationState(SpriteComponent.AnimationState.Run);
		bodyComponent.applyLinearImpulse(deltaMove);
		deltaMove.set(0, 0);
	}

}