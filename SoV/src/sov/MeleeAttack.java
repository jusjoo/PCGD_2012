package sov;

import com.badlogic.gdx.math.Vector2;

import sov.SpriteComponent.AnimationState;



public class MeleeAttack extends Attack {
	
	/*
	 * damageTime is how long the attack fixture is active.
	 */
	float damageTime;
	
	

	public MeleeAttack(AttackComponent attackComponent, float attackTime,float preDamageTime, 
			AnimationState attackAnimation, SpriteBody attackSpriteBody, float offSetY, float damageTime) {
		
		super(attackComponent, attackTime, preDamageTime, attackAnimation,	attackSpriteBody, offSetY);
		
		this.damageTime = damageTime;
		
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
		attackBody.body.setUserData(attackComponent);
		attackBody.body.body.setGravityScale(0);
		
	}
	
	public void stopDamage() {
		if (damaging) attackBody.body.removeFromWorld();
		damaging = false;
	}

	@Override
	public void update(float deltaTime) {
		
		// Update the spriteComponent, so we get animations, hooray!
		if (damaging) this.attackBody.spriteComponent.update(deltaTime);
		
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
			System.out.println("pöö");	
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
}
