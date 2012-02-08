package sov;

import com.badlogic.gdx.math.Vector2;

import sov.SpriteComponent.AnimationState;



public class MeleeAttack extends Attack {
	
	/*
	 * damageTime is how long the attack fixture is active.
	 */
	float damageTime;

	public MeleeAttack(AttackComponent attackComponent, float attackTime,
			float preDamageTime, float damageTime,
			AnimationState attackAnimation, SpriteBody attackSpriteBody,
			float offSetY) {
		super(attackComponent, attackTime, preDamageTime, attackAnimation,
				attackSpriteBody, offSetY);
		this.damageTime = damageTime;
		
		// TODO Auto-generated constructor stub
	}
	
	protected void startDamage(){
		attackComponent.damaging = true;
		
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
		attackBody.body.removeFromWorld();
	}
}
