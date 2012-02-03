package sov;

import com.badlogic.gdx.math.Vector2;

import sov.SpriteComponent.AnimationState;

public class RangedAttackComponent extends AttackComponent {

	private float flightSpeed;
	protected boolean attackingRight;

	public RangedAttackComponent(Entity parent, float attackTime, float preDamageTime, float damageTime, AnimationState attackAnimation, float flightSpeed) {
		super(parent, attackTime, preDamageTime, damageTime, attackAnimation);
		
		this.flightSpeed = flightSpeed;
		
		// TODO Auto-generated constructor stub
	}
	
	
	public void update(float deltaTime) {
		if (setToStopDamage) stopDamage();
				
		if (attacking) {
				
			((Creature)parent).getComponent(SpriteComponent.class).setCurrentAnimationState(animation);
			timer = timer - deltaTime;
			
			if (timer < attackTime-preDamageTime && timer > attackTime-preDamageTime-damageTime && !damaging){
				startDamage();
			}
			if (timer < attackTime-preDamageTime-damageTime){
				stopDamage();
			}
			if (timer < 0) {
				stopAttack();
			}
			
			//Vector2 currentVelocity = parent.getComponent(BodyComponent.class).body.getLinearVelocity();		
			
			
		}
		
		
			
	}
	
	protected void startDamage(){
		damaging = true;
		
		float offSet = this.getOffset();
		if (offSet < 0) attackingRight = false;
		else attackingRight = true;
				
		attackBodyComponent.addToWorld(bodyComponent.world, new Vector2(bodyComponent.getPosition().x + offSet*16, bodyComponent.getPosition().y ));
		
		// Sets attack bodies user data as this, so that attack sensors can be identified
		attackBodyComponent.setUserData(this);
		attackBodyComponent.body.setGravityScale(0);
	
	}
	
	
}
