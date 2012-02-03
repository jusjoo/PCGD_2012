package sov;

import com.badlogic.gdx.math.Vector2;

import sov.SpriteComponent.AnimationState;

public class RangedAttack extends Attack {

	public float flightSpeed;
	public boolean attackingRight;
	
	public RangedAttack(AttackComponent attackComponent, float attackTime,
			float preDamageTime, float damageTime,
			AnimationState attackAnimation, BodyComponent attackBodyComponent, float flightSpeed) {
		super(attackComponent, attackTime, preDamageTime, damageTime, attackAnimation,
				attackBodyComponent);
		this.flightSpeed = flightSpeed;
	}

	protected void startDamage(){
		attackComponent.damaging = true;
		
		float offSet = attackComponent.getOffset();
		
		if (offSet > 0) attackingRight = true;
		 	else attackingRight = false;
		
		attackBodyComponent.addToWorld(attackComponent.bodyComponent.world, new Vector2(attackComponent.bodyComponent.getPosition().x + offSet*16, attackComponent.bodyComponent.getPosition().y ));
		
		// Sets attack bodies user data as this, so that attack sensors can be identified
		attackBodyComponent.setUserData(attackComponent);
		attackBodyComponent.body.setGravityScale(0);
	
	}
}
