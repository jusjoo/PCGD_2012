package sov;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import sov.BodyComponent.SlopeShape;
import sov.SpriteComponent.AnimationState;

public class RangedAttack extends Attack {
	
	public float flightSpeed;

	private ArrayList<SpriteBody> projectiles;
	

	public RangedAttack(AttackComponent attackComponent, float attackTime,	float preDamageTime,
			AnimationState attackAnimation, SpriteBody attackSpriteBody, float offSetY, float flightSpeed) {
		
		super(attackComponent, attackTime, preDamageTime, attackAnimation,	attackSpriteBody, offSetY);
		
		this.flightSpeed = flightSpeed;
		
		projectiles = new ArrayList<SpriteBody>();
		
	}


	public void startDamage(){
		
		SpriteBody projectile = new SpriteBody(attackBody.body.getSize(), 
				attackBody.spriteComponent.animations, 
				false, 1.0f, false, SlopeShape.Even, true);
		
		float offSet = getAttackBoxOffsetX();
		
		if (offSet > 0) {
			projectile.body.setFacingRight(true);
		} else {
			projectile.body.setFacingRight(false);
		}
		
		
		
		projectiles.add(projectile);
		
		projectile.body.addToWorld(attackComponent.bodyComponent.world, 
				new Vector2(attackComponent.bodyComponent.getPosition().x + offSet, 
				attackComponent.bodyComponent.getPosition().y + offSetY ));
		
		projectile.spriteComponent.setCurrentAnimationState(AnimationState.Idle);
		
		// Sets attack bodies user data as this, so that attack sensors can be identified
		projectile.body.setUserData(attackComponent);
		projectile.body.body.setGravityScale(0);
		
	
		if(offSet > 0) {
			projectile.body.applyLinearImpulse(new Vector2( flightSpeed, 0f));
		} else {
			projectile.body.applyLinearImpulse(new Vector2( -flightSpeed, 0f));
		}
		
		damaging = true;
		
	}
	
	public void stopDamage() {
		
	}


	@Override
	public void update(float deltaTime) {
		// Update the spriteComponent, so we get animations, hooray!
		for (SpriteBody body: projectiles) {
			body.spriteComponent.update(deltaTime);
		}
		
		/*
		 * Update attack body's position relative to the Entity's body
		 */
		
		
		
		timer = timer - deltaTime;
		
		if (timer < this.attackTime - this.preDamageTime && !damaging){
			
			this.startDamage();
		}
	
		if (timer < 0) {
			
			attackComponent.stopAttack();
		}
	}


	@Override
	public void render(SpriteBatch spriteBatch) {
		for (SpriteBody body: projectiles) {
			body.render(spriteBatch);
		}
	}
}
