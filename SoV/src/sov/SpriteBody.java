package sov;

import java.util.HashMap;

import sov.BodyComponent.SlopeShape;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/*
 * SpriteBodies are entities that have both a drawable sprite, and a physics body.
 */
public class SpriteBody extends Entity {
	
	// Components
	BodyComponent body;
	SpriteComponent spriteComponent;
	
	
	

	
	
	
	public SpriteBody() {
		spriteComponent = new SpriteComponent(this);
		addComponent(spriteComponent);
	}

	public SpriteBody(Vector2 size, HashMap<SpriteComponent.AnimationState, Animation> animations,
			boolean staticBody, float rounding, boolean circle, BodyComponent.SlopeShape slopeShape) {
		
		body = new BodyComponent(this, size, staticBody, rounding, circle, slopeShape, false);
		spriteComponent = new SpriteComponent(this, animations);
		
		addComponent(body);
		addComponent(spriteComponent);
	}
	
	/*
	 * Same as the normal constructor, but supports sensory body setting
	 */
	public SpriteBody(Vector2 size, HashMap<SpriteComponent.AnimationState, Animation> animations,
			boolean staticBody, float rounding, boolean circle, BodyComponent.SlopeShape slopeShape, boolean isSensor) {
		
		body = new BodyComponent(this, size, staticBody, rounding, circle, slopeShape, isSensor);
		spriteComponent = new SpriteComponent(this, animations);
		
		addComponent(body);
		addComponent(spriteComponent);
	}
	
	public void render(SpriteBatch spriteBatch) {	
		
		spriteComponent.render(spriteBatch, body.getFacingRight(),
				body.getPosition().x,
				body.getPosition().y,
				(float) (body.getAngle()*180/Math.PI),
				body.getSize()
				);
		
		// Render the possible attack component
		if (this.getComponent(AttackComponent.class) != null)
			getComponent(AttackComponent.class).render(spriteBatch);
		
	}
	
	@Override
	public void update(float deltaTime) {
		
		
		
		super.update(deltaTime);
		Vector2 currentVelocity = body.getLinearVelocity();
		
		
		if(currentVelocity.x > body.getMaxVelocity()) {
			body.setLinearVelocity(body.getMaxVelocity(), body.getLinearVelocity().y);
		}
		else if(currentVelocity.x < -body.getMaxVelocity()) {
			body.setLinearVelocity(-body.getMaxVelocity(), body.getLinearVelocity().y);
		}
		
		
	}
	
	
	
	public Vector2 getPosition() {
		return getComponent(BodyComponent.class).getPosition();
	}

	/*
	 * Note! Currently works correctly only for attack bodies, remake appropriately for other usage!
	 */
	public static SpriteBody dynamicSpriteBodyFromProto(SpriteBody proto) {
		
		SpriteBody spriteBody = new SpriteBody();
		
		BodyComponent body = new BodyComponent(spriteBody, proto.body.size, false, 1.0f, false, SlopeShape.Even, true);
		SpriteComponent spriteComponent = new SpriteComponent(spriteBody, proto.spriteComponent.animations);
		
		spriteBody.addComponent(body);
		spriteBody.addComponent(spriteComponent);
		
		spriteBody.body = body;
		spriteBody.spriteComponent = spriteComponent;
		
		return spriteBody;
	}
	
	
	
	
	
}
