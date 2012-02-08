package sov;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import sov.BodyComponent.SlopeShape;
import sov.SpriteComponent.AnimationState;

public class RangedAttack extends Attack {
	
	public float flightSpeed;

	protected ArrayList<Projectile> projectiles;
	protected Projectile projectileProto;
	
	public RangedAttack(AttackComponent attackComponent, float attackTime,	float preDamageTime,
			AnimationState attackAnimation, Projectile projectile, float offSetY, float flightSpeed) {
		
		super(attackComponent, attackTime, preDamageTime, attackAnimation, offSetY);
		
		this.flightSpeed = flightSpeed;
		this.projectileProto = projectile;
		this.projectiles = new ArrayList<Projectile>();
		
	}


	public void startDamage(){
		
		Projectile projectile = new Projectile(projectileProto.body.getSize(), 
				projectileProto.spriteComponent.animations, 
				 true);
		
		
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
		
		// Sets attack bodies user data as the created projectile, so that they can be identified
		projectile.body.setUserData(new ContactEvent(attackComponent, "ranged"));
		projectile.body.body.setGravityScale(0);

		
	
		if(offSet > 0) {
			projectile.body.applyLinearImpulse(new Vector2( flightSpeed, 0f));
		} else {
			projectile.body.applyLinearImpulse(new Vector2( -flightSpeed, 0f));
		}
		
		damaging = true;
		
	}
	
	private float getAttackBoxOffsetX() {
		float offset = attackComponent.getOffsetX();
		
		if(offset > 0) {
			offset += projectileProto.body.getSize().x;
		} else {
			offset -= projectileProto.body.getSize().x;
		}
		
		return offset;
	}


	public void stopDamage() {
		
	}


	@Override
	public void update(float deltaTime) {
		// Update the spriteComponent, so we get animations, hooray!
		for (Projectile projectile: projectiles) {
			projectile.update(deltaTime);
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
		for (Projectile projectile: projectiles) {
			projectile.render(spriteBatch);
		}
	}



	

}
