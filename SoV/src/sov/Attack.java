package sov;

import sov.Creature.Stats;

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
	protected boolean attacking;
	/*
	 * Basic damage of attack, does not include stats!
	 */
	protected float baseDamage;
	
	/*
	 * timer keeps track of the whole attack from beginning to end
	 */
	protected float timer;

	
	/*
	 * TODO: Takes in a custom attack fixture shape, which is then handled for attacks on both sides.
	 */
	public Attack(AttackComponent attackComponent, float attackTime, float preDamageTime, SpriteComponent.AnimationState attackAnimation,  float offSetY, float damage) {				
		
		this.baseDamage=damage;
		this.attackTime = attackTime;
		this.preDamageTime = preDamageTime;
		this.damaging = false;
		this.animation = attackAnimation;

		this.attackComponent = attackComponent;
		this.offSetY = offSetY;
	}

	protected abstract void startDamage();

	
	
	
	
	protected void startAttack() {
		timer = attackTime;
		attacking = true;
		//System.out.println("starting attack... timer:"+timer);
		attackComponent.parent.getComponent(SpriteComponent.class).setCurrentAnimationState(animation);
		//parent.getComponent(SpriteComponent.class).setCurrentAnimationState(attacks.get(attackType).animation);
		
	}
	
	
	
	public abstract void stopDamage();



	public abstract void update(float deltaTime);


	public abstract void render(SpriteBatch spriteBatch) ;

	public void setDamage(float damage) {
		this.baseDamage = damage;
	}
	
	public float getDamage(Stats stat) {
		float statDamage;
		
		switch (stat) {
		case Strength: statDamage = ((Creature)attackComponent.parent).getStrength(); break;
		case Dexterity: statDamage = ((Creature)attackComponent.parent).getDexterity(); break;
		case Wisdom: statDamage = ((Creature)attackComponent.parent).getWisdom(); break;
		default: statDamage = 0;
		}
		
		
		float damage = statDamage + baseDamage;
		System.out.println("damage("+stat+" "+statDamage+"+"+baseDamage+"):"+" damage");
		return damage;		
	}
	
}
