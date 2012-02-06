package sov;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class Attack {
	
	AttackComponent attackComponent;
	protected SpriteComponent.AnimationState animation;

	
	/*
	 * preAttackTime is the time where the animation starts, but the attack fixture is not yet active.
	 */
	float preDamageTime;

	/*
	 * attackTime is the total attack animation time
	 */
	float attackTime;
	
	/*
	 * Attack box y offset
	 */
	float offSetY;
	
	protected boolean damaging;
	
	/*
	 * timer keeps track of the whole attack from beginning to end
	 */
	protected float timer;

	
	/*
	 * TODO: Takes in a custom attack fixture shape, which is then handled for attacks on both sides.
	 */
	public Attack(AttackComponent attackComponent, float attackTime, float preDamageTime, SpriteComponent.AnimationState attackAnimation,  float offSetY) {
				
		this.attackTime = attackTime;
		this.preDamageTime = preDamageTime;
		this.damaging = false;
		this.animation = attackAnimation;

		this.attackComponent = attackComponent;
		this.offSetY = offSetY;
	}
	
	
	
	
	protected void startAttack() {
		timer = attackTime;
		
		attackComponent.parent.getComponent(SpriteComponent.class).setCurrentAnimationState(animation);
		//parent.getComponent(SpriteComponent.class).setCurrentAnimationState(attacks.get(attackType).animation);
		
	}
	
	
	
	public abstract void stopDamage();
	public abstract void startDamage();


	public abstract void update(float deltaTime);


	public abstract void render(SpriteBatch spriteBatch) ;


	
}
