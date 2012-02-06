package sov;

import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;

import sov.BodyComponent.SlopeShape;
import sov.SpriteComponent.AnimationState;

public class RangedAttack extends Attack {
	
	public float flightSpeed;
	public boolean attackingRight;
	

	public RangedAttack(AttackComponent attackComponent, float attackTime,	float preDamageTime,
			AnimationState attackAnimation, SpriteBody attackSpriteBody, float offSetY, float flightSpeed) {
		
		super(attackComponent, attackTime, preDamageTime, attackAnimation,	attackSpriteBody, offSetY);
		
		this.flightSpeed = flightSpeed;
		
	}


	public void startDamage(){
		damaging = true;
		
		float offSet = getAttackBoxOffsetX();
		
		if (offSet > 0) {
			attackingRight = true;
			attackBody.body.setFacingRight(true);
		} else {
			attackingRight = false;
			attackBody.body.setFacingRight(false);
		}
		

		
		attackBody.body.addToWorld(attackComponent.bodyComponent.world, 
				new Vector2(attackComponent.bodyComponent.getPosition().x + offSet, 
				attackComponent.bodyComponent.getPosition().y + offSetY ));
		
		attackBody.spriteComponent.setCurrentAnimationState(AnimationState.Idle);
		
		// Sets attack bodies user data as this, so that attack sensors can be identified
		attackBody.body.setUserData(attackComponent);
		attackBody.body.body.setGravityScale(0);
		
	
		if(attackingRight) {
			attackBody.body.applyLinearImpulse(new Vector2( flightSpeed, 0f));
		} else {
			attackBody.body.applyLinearImpulse(new Vector2( -flightSpeed, 0f));
		}
		
	}
	
	public void stopDamage() {
		
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
		
		if (timer < this.attackTime - this.preDamageTime && !damaging){

			this.startDamage();
		}
	
		if (timer < 0) {
			attackComponent.stopAttack();
		}
	}
}
