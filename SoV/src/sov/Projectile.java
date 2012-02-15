package sov;

import java.util.HashMap;

import sov.BodyComponent.SlopeShape;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.MassData;

public class Projectile extends Entity {

	// Components
	BodyComponent body;
	SpriteComponent spriteComponent;
	private boolean setToDie;
	public boolean setToDestroy;
	private float damage;
	
	/*
	 * Same as the normal constructor, but supports sensory body setting
	 */
	public Projectile(Vector2 size, HashMap<SpriteComponent.AnimationState, Animation> animations, boolean isSensor) {
		
		body = new BodyComponent(this, size, false, 1.0f, false, SlopeShape.Even, isSensor);		
		spriteComponent = new SpriteComponent(this, animations);
		
		addComponent(body);
		addComponent(spriteComponent);
		
		setToDie = false;
		setToDestroy = false;
		
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
		
		if (setToDie) {
			//spriteComponent.setCurrentAnimationState(SpriteComponent.AnimationState.Die);
			body.die();
			//body.removeFromWorld();
			body.setLinearVelocity(0f, 0f);
			setToDie = false;
			setToDestroy = true;
			
		}
		
		
		super.update(deltaTime);
		
		//Vector2 currentVelocity = body.getLinearVelocity();
		/*
		if(currentVelocity.x > body.getMaxVelocity()) {
			body.setLinearVelocity(body.getMaxVelocity(), body.getLinearVelocity().y);
		}
		else if(currentVelocity.x < -body.getMaxVelocity()) {
			body.setLinearVelocity(-body.getMaxVelocity(), body.getLinearVelocity().y);
		}*/
		
		
	}
	
	public Vector2 getPosition() {
		return getComponent(BodyComponent.class).getPosition();
	}
	
	public void dealDamageTo(BodyComponent target) {
		
		target.setToTakeDamage(this.damage);
		this.setToDie();
		
	}

	public void setToDie() {
		if (body.alive) setToDie = true;
	}

	public void setUserData(ContactEvent contact) {
		body.setUserData(contact);
		
	}
	public void setDamage(float damage) {
		this.damage = damage;
	}


}
