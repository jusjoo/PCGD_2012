package sov;

import java.util.HashMap;

import sov.BodyComponent.SlopeShape;
import sov.Creature.Stats;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
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
	private int damageType;
	Sound impactSound;
	
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
		if (damageType == 2) {
			//TODO: need to find a way to figure out if target is creature and then apply the slowdonw buff...
			
			if ( target.parent instanceof Creature ) {
			Creature targetCreature = (Creature)target.parent;
			targetCreature.applyBuff(new Buff(Stats.Dexterity, -3.0f, 6.0f));
			targetCreature.applyBuff(new Buff(Stats.Freeze, 0.0f, 6.0f));			
			}
		}
		System.out.println("yritetään toistaa ääni");
		if (impactSound != null) {
			System.out.println("we're in!");
			impactSound.play();
		}
		
		this.setToDie();
		
	}

	public void setToDie() {
		if (body.alive) setToDie = true;
	}

	public void setUserData(ContactEvent contact) {
		body.setUserData(contact);
		
	}
	public void setDamage(float damage, int damageType) {
		this.damage = damage;
		this.damageType = damageType;
	}

	public void addImpactSound(String filename) {
		this.impactSound = Gdx.audio.newSound(new FileHandle("assets/sound/"+filename));
	}
	public void addImpactSound(Sound sound){
		this.impactSound = sound;
	}

}
