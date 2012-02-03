package sov;

import sov.SpriteComponent.AnimationState;

public class RangedAttack extends Attack {

	public float flightSpeed;
	
	public RangedAttack(AttackComponent attackComponent, float attackTime,
			float preDamageTime, float damageTime,
			AnimationState attackAnimation, BodyComponent attackBodyComponent, float flightSpeed) {
		super(attackComponent, attackTime, preDamageTime, damageTime, attackAnimation,
				attackBodyComponent);
		this.flightSpeed = flightSpeed;
		
	}

}
