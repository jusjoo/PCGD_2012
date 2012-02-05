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
	 * TODO: Takes in a custom attack fixture shape, which is then handled for attacks on both sides.
	 */
	public Attack(AttackComponent attackComponent, float attackTime, float preDamageTime, float damageTime, SpriteComponent.AnimationState attackAnimation, SpriteBody attackSpriteBody) {
				
		this.attackTime = attackTime;
		this.preDamageTime = preDamageTime;
		this.damageTime = damageTime;		
		this.animation = attackAnimation;
		this.attackBody = attackSpriteBody;
		this.attackComponent = attackComponent;
	}
	protected void startDamage(){
		attackComponent.damaging = true;
		
		float offSet = attackComponent.getOffset();
				
		// Adds the body in front of attacker
		attackBody.body.addToWorld(attackComponent.bodyComponent.world, new Vector2(attackComponent.bodyComponent.getPosition().x + offSet*16, attackComponent.bodyComponent.getPosition().y ));
		
		if (offSet > 0) attackBody.body.setFacingRight(true);
	 		else attackBody.body.setFacingRight(false);
		
		// Sets attack bodies user data as this, so that attack sensors can be identified
		attackBody.body.setUserData(attackComponent);
		attackBody.body.body.setGravityScale(0);
	
	}

}
