package sov;

import java.util.ArrayList;
import java.util.TreeSet;
import java.util.Map.Entry;

import sov.Attack.AttackType;
import sov.SpriteComponent.AnimationState;

import com.badlogic.gdx.math.Vector2;

public class AIComponent extends InputComponent {

	public enum AIstate {
		Follow,
		Attack,
		Alarmed,
		RangedAttacker
	}
	
	private AnimationState meleeAttack = null;
	private AnimationState rangedAttack = null;
	
	private TreeSet<AIstate> activeStates;	
	
	BodyComponent target = null;
	BodyComponent bodyComponent;
	/*
	 * AI Behaviour
	 */
	private float maximumRangedAttackDistanceX = 256;
	private float maximumMeleeAttackDistanceX = 32;
	private float maximumAttackDistanceY = 32;
	private float optimalRangedDistance = 64;
	private float optimalMeleeDistance = 32;
	private float optimalDistanceNow = 16;
	private float minimumDistanceTolerance = 10;
	private float distanceRandomizeTimer = 0;
	private float distanceRandomDuration = 3;
	private float visibilityX = 128;
	private float visibilityY = 128;
	private float alarmTime = 60;
	private float alarmTimer = 0;
	private float attackFrequency = 2;			// average time between attacks (Randomized)
	private float attackTimer = 0;				// Time left between no attack
	
	public AIComponent(Entity parent) {
		super(parent);
		this.movementComponent = parent.getComponent(MovementComponent.class);
		this.bodyComponent = parent.getComponent(BodyComponent.class);
		this.activeStates = new TreeSet<AIstate>();
		
		// get reference for ranged and melee attacks
		AttackComponent attackcompo = parent.getComponent(AttackComponent.class);
		for(Entry<SpriteComponent.AnimationState, Attack> animationEntry: attackcompo.attacks.entrySet()) {
			AttackType temp = animationEntry.getValue().getType();
			
			if ( temp == AttackType.Melee)
				meleeAttack = animationEntry.getKey();
			if ( temp == AttackType.Ranged)
				rangedAttack = animationEntry.getKey();
			if ( temp == AttackType.Magic)
				rangedAttack = animationEntry.getKey();
			
			if (rangedAttack != null)
				activeStates.add(AIstate.RangedAttacker);
			
		}
	}
	
	public AIComponent setTarget(Entity entity) {
		target = entity.getComponent(BodyComponent.class);
		return this;
	}
	
	@Override
	public void update(float deltaTime) {		
		//System.out.println("AI component of " +parent.toString());
			for (AIstate state : activeStates) {
				switch (state) {
					case Follow:													
						handleFollow();
						if (distanceRandomizeTimer > 0) distanceRandomizeTimer -= deltaTime;
						break;
					case Attack: 
						if (attackTimer <= 0 ) {
							if (handleAttack()) {				
								attackTimer = ((float)Math.random() * 2f * attackFrequency);
								System.out.println("Attack timer set to "+attackTimer);
							}
						}
						else {						
							attackTimer -= deltaTime;
						}
						break;						
					default:
				}				
			}
		if (alarmTimer > 0) {
			alarmTimer -= deltaTime;
		}
	}
	
	private boolean handleAttack() {		
		
		if(	target != null &&
			target.alive &&
			isTargetVisible() &&
			getTargetDistanceY() <= maximumAttackDistanceY ) {
			
			if (getTargetDistanceX() <= maximumMeleeAttackDistanceX && meleeAttack != null) {
				parent.getComponent(AttackComponent.class).attack(meleeAttack);
				return true;
			}
			
			if (getTargetDistanceX() <= maximumRangedAttackDistanceX && rangedAttack != null) {
				parent.getComponent(AttackComponent.class).attack(rangedAttack);
				return true;
			}
				
			
		}
		return false;
	}
	
	private boolean handleFollow() {
		
		if (distanceRandomizeTimer <= 0) {
			float optimalDistance;
			if (activeStates.contains(AIstate.RangedAttacker))
				optimalDistance = optimalRangedDistance;
			else optimalDistance = optimalMeleeDistance;
			
			optimalDistanceNow = ((float)Math.random()*1 + 0.5f)*optimalDistance;
			//System.out.println("Min Distance set to "+minimumDistanceNow);
			distanceRandomizeTimer = distanceRandomDuration;
		}
		
		
		// move towards target
		
		if ((activeStates.contains(AIstate.RangedAttacker) && target != null && isTargetVisible() && getTargetDistanceX() > maximumRangedAttackDistanceX) ||	
			(!activeStates.contains(AIstate.RangedAttacker) && target != null && isTargetVisible() && target.alive && getTargetDistanceX() > (optimalDistanceNow + minimumDistanceTolerance)) ) {

			if(bodyComponent.getPosition().x < target.getPosition().x) {
					movementComponent.move(true);
			} else {
					movementComponent.move(false);	
			}
			//return true;
		}
		//move away from target
		else if (target != null && isTargetVisible() && target.alive && getTargetDistanceX() < (optimalDistanceNow - minimumDistanceTolerance)) {
			
			if(bodyComponent.getPosition().x < target.getPosition().x) {
				movementComponent.move(false);
			} else {
				movementComponent.move(true);
			}
			//return true;
		}
		
		if (isTargetVisible()) {
			if(target.getPosition().x <= bodyComponent.getPosition().x) {
				movementComponent.setFacingRight(false);
			} else {
				movementComponent.setFacingRight(true);
			}
		}
		
		return false;
	}
	
	private boolean isTargetVisible() {
		
		// FIXME: Palikkaimplementaatio, lis�� visibility detection!
		float visibilityX = this.visibilityX;
		float visibilityY = this.visibilityY;
		if (activeStates.contains(AIstate.Alarmed))		// if alarmed, follow from further away.
			visibilityX = visibilityX * 10;
		
		float stealth = ((Creature)target.parent).getStealth();
		visibilityX -= stealth;
		visibilityY -= stealth;
		
		if (visibilityX < 0) visibilityX = 0;
		
		if (getTargetDistanceX() < visibilityX &&
			getTargetDistanceY() <= visibilityY)  {
			return true;
		} else return false;
	}
	
	private float getTargetDistanceX() {
		return Math.abs(bodyComponent.getPosition().x - target.getPosition().x);
	}
	
	private float getTargetDistanceY() {
		return Math.abs(bodyComponent.getPosition().y - target.getPosition().y);
	}
	
	
	public void addAIstate(AIstate state ) {
		//we only have one state of each type
		
			activeStates.add(state);
		//	System.out.println("Added AI state " +state);
		
	}
	
	public void removeAIstate(AIstate state ) {
		
			activeStates.remove(state);		
	}

}
