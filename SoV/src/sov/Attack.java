package sov;

import com.badlogic.gdx.math.Vector2;

public class Attack {
	
	AttackComponent attackComponent;
	
	protected SpriteComponent.AnimationState animation;

	public BodyComponent attackBodyComponent;
	
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
	public Attack(AttackComponent attackComponent, float attackTime, float preDamageTime, float damageTime, SpriteComponent.AnimationState attackAnimation, BodyComponent attackBodyComponent) {
				
		this.attackTime = attackTime;
		this.preDamageTime = preDamageTime;
		this.damageTime = damageTime;		
		this.animation = attackAnimation;
		this.attackBodyComponent = attackBodyComponent;
		this.attackComponent = attackComponent;
	}
	protected void startDamage(){
		attackComponent.damaging = true;
		
		float offSet = attackComponent.getOffset();
				
		attackBodyComponent.addToWorld(attackComponent.bodyComponent.world, new Vector2(attackComponent.bodyComponent.getPosition().x + offSet*16, attackComponent.bodyComponent.getPosition().y ));
		
		// Sets attack bodies user data as this, so that attack sensors can be identified
		attackBodyComponent.setUserData(attackComponent);
		attackBodyComponent.body.setGravityScale(0);
	
	}

}
