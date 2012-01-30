package sov;

import com.badlogic.gdx.math.Vector2;

public class AIComponent extends InputComponent {

	BodyComponent entityToFollow = null;
	
	public AIComponent(Entity parent, float speed) {
		super(parent, speed);
	}
	
	public AIComponent setToFollow(Entity entity) {
		entityToFollow = entity.getComponent(BodyComponent.class);
		return this;
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		
			if(entityToFollow != null) {
				if(getBodyComponent().getPosition().x < entityToFollow.getPosition().x) {
					getBodyComponent().body.applyLinearImpulse(new Vector2(speed, 0), getBodyComponent().body.getWorldCenter());
					getBodyComponent().setFacingRight(true);
				} else {
					getBodyComponent().body.applyLinearImpulse(new Vector2(-speed, 0), getBodyComponent().body.getWorldCenter());
					getBodyComponent().setFacingRight(false);
				}
			}		
		
	}

}
