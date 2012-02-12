package sov;

import java.util.ArrayList;
import java.util.TreeSet;

import sov.Creature.AttackType;
import sov.SpriteComponent.AnimationState;

import com.badlogic.gdx.math.Vector2;

public class AIComponent extends InputComponent {

	public enum AIstate {
		Follow,
		Attack,
		Alarmed
	}
	
	private TreeSet<AIstate> activeStates;	
	
	BodyComponent target = null;
	BodyComponent bodyComponent;
	/*
	 * AI Behaviour
	 */
	private float maximumAttackDistanceX = 48;
	private float maximumAttackDistanceY = 16;
	private float minimumDistanceBetweenTarget = 16;
	private float minimumDistanceNow = 16;
	private float minimumDistanceTolerance = 10;
	private float distanceRandomizeTimer = 0;
	private float distanceRandomDuration = 3;
	private float visibilityX = 100;
	private float visibilityY = 100;
	private float alarmTime = 60;
	private float alarmTimer = 0;
	private float attackFrequency = 2;			// average time between attacks (Randomized)
	private float attackTimer = 0;				// Time left between no attack
	
	public AIComponent(Entity parent) {
		super(parent);
		this.movementComponent = parent.getComponent(MovementComponent.class);
		this.bodyComponent = parent.getComponent(BodyComponent.class);
		this.activeStates = new TreeSet<AIstate>();
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
			isTargetVisible() &&
			getTargetDistanceX() <= maximumAttackDistanceX &&
			getTargetDistanceY() <= maximumAttackDistanceY &&
			target.alive) {
			parent.getComponent(AttackComponent.class).attack(AnimationState.Attack1);
			return true;
		}
		else return false;
	}
	
	private boolean handleFollow() {
		
		if (distanceRandomizeTimer <= 0) {
			minimumDistanceNow = ((float)Math.random()*3+1)*minimumDistanceBetweenTarget;
			System.out.println("Min Distance set to "+minimumDistanceNow);
			distanceRandomizeTimer = distanceRandomDuration;
		}
		
		
		// move towards target
		if(target != null && isTargetVisible() && target.alive && getTargetDistanceX() > (minimumDistanceNow + minimumDistanceTolerance)) {

			if(bodyComponent.getPosition().x < target.getPosition().x) {
					movementComponent.move(true);
			} else {
					movementComponent.move(false);	
			}
			return true;
		}
		//move away from target
		else if (target != null && isTargetVisible() && target.alive && getTargetDistanceX() < (minimumDistanceNow - minimumDistanceTolerance)) {
			
			if(bodyComponent.getPosition().x < target.getPosition().x) {
				movementComponent.move(false);
			} else {
				movementComponent.move(true);
			}
			return true;
		}	
		return false;
	}
	
	private boolean isTargetVisible() {
		
		// FIXME: Palikkaimplementaatio, lisää visibility detection!
		float visibilityX = this.visibilityX;
		
		if (activeStates.contains(AIstate.Alarmed))		// if alarmed, follow from further away.
			visibilityX = visibilityX * 10;
		
		if (getTargetDistanceX() <= visibilityX &&
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
