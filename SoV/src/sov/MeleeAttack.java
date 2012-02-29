package sov;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import sov.Creature.CreatureType;
import sov.Creature.Stats;
import sov.SpriteComponent.AnimationState;



public class MeleeAttack extends Attack {
	
	/*
	 * damageTime is how long the attack fixture is active.
	 */
	float damageTime;
	SpriteBody attackBody;
	ContactEvent contactevent;

	public MeleeAttack(AttackType attackType, AttackComponent attackComponent, float attackTime, float preDamageTime, 
			AnimationState attackAnimation, float offSetY, float damageTime, SpriteBody attackSpriteBody, float damage) {
		
		super(attackType, attackComponent, attackTime, preDamageTime, attackAnimation, offSetY ,damage);
		
		this.damageTime = damageTime;
		this.attackBody = attackSpriteBody;
		
	}
	
	public void startDamage(){
		
		damaging = true;
		
		float offSet = getAttackBoxOffsetX();
		//float offSet = 0;				
		
		// Adds the body in front of attacker
		attackBody.body.addToWorld(attackComponent.bodyComponent.world,
				new Vector2(attackComponent.bodyComponent.getPosition().x + offSet, attackComponent.bodyComponent.getPosition().y + offSetY));
		

		if (offSet > 0) attackBody.body.setFacingRight(true);
	 		else attackBody.body.setFacingRight(false);
		
		// Sets attack bodies user data as this, so that attack sensors can be identified
		contactevent = new ContactEvent(this, "melee");
		attackBody.body.setUserData(contactevent);
		attackBody.body.body.setGravityScale(0);
		
	}
	
	public void stopDamage() {
		if (damaging) attackBody.body.removeFromWorld();
		damaging = false;
	}

	@Override
	public void update(float deltaTime) {
		
		// Update the spriteComponent, so we get animations, hooray!
		//if (damaging) this.attackBody.spriteComponent.update(deltaTime);
		
		/*
		 * Update attack body's position relative to the Entity's body
		 */
		if (damaging) {
			float offSet = this.getAttackBoxOffsetX();
			this.attackBody.body.setPosition(new Vector2(attackComponent.bodyComponent.getPosition().x + offSet, 
					attackComponent.bodyComponent.getPosition().y + offSetY ));
		}
	
		
		timer = timer - deltaTime;
		//System.out.println(timer);
		
		if (timer < this.attackTime - this.preDamageTime && 
				timer > this.attackTime - this.preDamageTime - this.damageTime && !damaging){
			//System.out.println("starting damage");
			this.startDamage();
		}
		if (timer < this.attackTime - this.preDamageTime - this.damageTime){
			//System.out.println("stopping damage");
			stopDamage();
		}
		if (timer < 0) {
			attackComponent.stopAttack();
			//System.out.println("stopping attack");
			stopDamage();
		}
		
	}

	@Override
	public void render(SpriteBatch spriteBatch) {
		// TODO Auto-generated method stub
		
	}
	
	protected float getAttackBoxOffsetX() {
		
		float offset = attackComponent.getOffsetX();
		float offset2 = attackBody.body.getSize().x/2+1; 
		
		if(offset > 0) {
			offset += offset2;
		} else {
			offset -= offset2;
		}	
		return offset;
	}
	
	public void dealDamageTo(BodyComponent target) {
		float damageMultiplier = 1;
		Creature parentCreature = (Creature)attackComponent.parent;
		if ( parentCreature.applyCriticalDamage ) {
			damageMultiplier = 2.5f;
			parentCreature.activateSpecialAbility();
			if (!parentCreature.modifyMana(-25))
				parentCreature.modifyMana(-parentCreature.getMana());
			if (!parentCreature.modifyStamina(-25))
				parentCreature.modifyStamina(-parentCreature.getStamina());
		}
		
		target.setToTakeDamage(getDamage(Stats.Strength)*damageMultiplier);		
	}
	
	public boolean consumeResource() {
		boolean result;
		float consumption = getBaseDamage() * GameConfiguration.staminaCostAttackMultiplier;
		result = ((Creature)attackComponent.parent).modifyStamina(-consumption);		
		
		return result;
	}
}
