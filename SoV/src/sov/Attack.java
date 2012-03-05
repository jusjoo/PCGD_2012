package sov;

import sov.Creature.Stats;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class Attack {
	
	public enum AttackType {None,Melee,Ranged,Magic}
	
	private AttackType type;
	
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
	public Attack(AttackType attackType, AttackComponent attackComponent, float attackTime, float preDamageTime, SpriteComponent.AnimationState attackAnimation,  float offSetY, float damage) {				
		
		this.type = attackType;
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
		
		//System.out.println("Starting attack");
		
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
		
		float damage;
		String dmgString;
		
		if (GameConfiguration.randomizedDamage) {
			float minDamage = Math.round(statDamage * 0.2f + baseDamage);
			float maxDamage = Math.round(statDamage * 1.0f + baseDamage*1.5f);
			damage = Math.round( (float)Math.random() * (maxDamage-minDamage) + minDamage );
			dmgString = (minDamage+"-"+maxDamage);
		}
		else {
			damage = Math.round(statDamage + baseDamage);
			dmgString = (statDamage+"+"+baseDamage);
		}
		System.out.println("damage("+dmgString+"):"+damage+" damage");
		return damage;		
	}
	public float getBaseDamage() {
		return baseDamage;
	}

	public abstract boolean consumeResource();
	public AttackType getType () {
		return type; 
	}
	
}
