package sov;

import com.badlogic.gdx.math.Vector2;

public class AIComponent extends InputComponent {

	BodyComponent entityToFollow = null;
	BodyComponent bodyComponent;
	
	public AIComponent(Entity parent) {
		super(parent);
		this.movementComponent = parent.getComponent(MovementComponent.class);
		this.bodyComponent = parent.getComponent(BodyComponent.class);
	}
	
	public AIComponent setToFollow(Entity entity) {
		entityToFollow = entity.getComponent(BodyComponent.class);
		return this;
	}
	
	@Override
	public void update(float deltaTime) {		
		if(entityToFollow != null) {
				if(bodyComponent.getPosition().x < entityToFollow.getPosition().x) {
					//bodyComponent.body.applyLinearImpulse(new Vector2(speed, 0), getBodyComponent().body.getWorldCenter());
					//bodyComponent.setFacingRight(true);
					movementComponent.move(true);
				} else {
					//bodyComponent.body.applyLinearImpulse(new Vector2(-speed, 0), getBodyComponent().body.getWorldCenter());
					//bodyComponent.setFacingRight(false);
					movementComponent.move(false);
				}
			}	
		
	}

}
