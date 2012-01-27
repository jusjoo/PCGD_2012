package sov;

import com.badlogic.gdx.math.Vector2;

public abstract class InputComponent extends Component {

	protected BodyComponent bodyComponent;
	
	protected Vector2 deltaMove = new Vector2();
	protected float speed;
	
	public InputComponent(Object parent, BodyComponent bodyComponent, float speed) {
		super(parent);
		this.speed = speed;
		this.bodyComponent = bodyComponent;
	}
	
	public InputComponent(Object parent) {
		super(parent);
	}
	
	public void setBodyComponent(BodyComponent bodyComponent) {
		this.bodyComponent = bodyComponent;
	}
	
	public void setSpeed(float speed) {
		this.speed = speed;
	}

	@Override
	public void update(float deltaTime) {
		bodyComponent.applyLinearImpulse(deltaMove);
	}

}
