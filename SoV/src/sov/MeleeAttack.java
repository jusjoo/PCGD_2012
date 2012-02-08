package sov;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import sov.SpriteComponent.AnimationState;



public class MeleeAttack extends Attack {
	
	/*
	 * damageTime is how long the attack fixture is active.
	 */
	float damageTime;
	SpriteBody attackBody;
	ContactEvent contactevent;

	public MeleeAttack(AttackComponent attackComponent, float attackTime,float preDamageTime, 
			AnimationState attackAnimation,  float offSetY, float damageTime, SpriteBody attackSpriteBody) {
		
		super(attackComponent, attackTime, preDamageTime, attackAnimation, offSetY);
		
		this.damageTime = damageTime;
		this.attackBody = attackSpriteBody;
		
		
		// TODO Auto-generated constructor stub
	}
	
	public void startDamage(){
		
		damaging = true;
		
		float offSet = getAttackBoxOffsetX();
				
		
		// Adds the body in front of attacker
		attackBody.body.addToWorld(attackComponent.bodyComponent.world, new Vector2(attackComponent.bodyComponent.getPosition().x + offSet, attackComponent.bodyComponent.getPosition().y + offSetY));

		if (offSet > 0) attackBody.body.setFacingRight(true);
	 		else attackBody.body.setFacingRight(false);
		
		// Sets attack bodies user data as this, so that attack sensors can be identified
		contactevent = new ContactEvent(this, "melee");
		attackBody.body.setUserData(contactevent);
		attackBody.body.body.setGravityScale(0);
		
	}
	
	public void stopDamage() {
		if (damaging) attackBody.body.removeFromWorld();
		damaging = false;
	}

	@Override
	public void update(float deltaTime) {
		
		// Update the spriteComponent, so we get animations, hooray!
		//if (damaging) this.attackBody.spriteComponent.update(deltaTime);
		
		/*
		 * Update attack body's position relative to the Entity's body
		 */
		if (damaging) {
			float offSet = this.getAttackBoxOffsetX();
			this.attackBody.body.setPosition(new Vector2(attackComponent.bodyComponent.getPosition().x + offSet, 
					attackComponent.bodyComponent.getPosition().y + attackComponent.activeAttack.offSetY ));
		}
	
		
		timer = timer - deltaTime;
		
		if (timer < this.attackTime - this.preDamageTime && 
				timer > this.attackTime - this.preDamageTime - this.damageTime && !damaging){
			
			this.startDamage();
		}
		if (timer < this.attackTime - this.preDamageTime - this.damageTime){
			stopDamage();
		}
		if (timer < 0) {
			attackComponent.stopAttack();
			stopDamage();
		}
		
	}

	@Override
	public void render(SpriteBatch spriteBatch) {
		// TODO Auto-generated method stub
		
	}
	
	protected float getAttackBoxOffsetX() {
		
		float offset = attackComponent.getOffsetX();
		
		if(offset > 0) {
			offset += attackBody.body.getSize().x;
		} else {
			offset -= attackBody.body.getSize().x;
		}	
		return offset;
	}
	
	public void dealDamageTo(BodyComponent target) {
		target.setToTakeDamage(1f);
	}
}
