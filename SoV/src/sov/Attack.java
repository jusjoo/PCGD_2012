package sov;

import com.badlogic.gdx.math.Vector2;

public class Attack {
	
	AttackComponent attackComponent;
	
	protected SpriteComponent.AnimationState animation;

	public SpriteBody attackBody;
	
	/*
	 * preAttackTime is the time where the animation starts, but the attack fixture is not yet active.
	 */
	float preDamageTime;
	
	/*
	 * damageTime is how long the attack fixture is active.
	 */
	float damageTime;
	
	/*
	 * attackTime is the total attack animation time
	 */
	float attackTime;
	
	/*
	 * Attack box y offset
	 */
	float offSetY;
	
	/*
	 * TODO: Takes in a custom attack fixture shape, which is then handled for attacks on both sides.
	 */
	public Attack(AttackComponent attackComponent, float attackTime, float preDamageTime, float damageTime, SpriteComponent.AnimationState attackAnimation, SpriteBody attackSpriteBody, float offSetY) {
				
		this.attackTime = attackTime;
		this.preDamageTime = preDamageTime;
		this.damageTime = damageTime;		
		this.animation = attackAnimation;
		this.attackBody = attackSpriteBody;
		this.attackComponent = attackComponent;
		this.offSetY = offSetY;
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
	
	protected float getAttackBoxOffsetX() {
		
		float offset = attackComponent.getOffsetX();
		
		if(offset > 0) {
			offset += attackBody.body.getSize().x;
		} else {
			offset -= attackBody.body.getSize().x;
		}
		
		return offset;
	}

}
