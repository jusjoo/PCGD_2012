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
	}
	
	private TreeSet<AIstate> activeStates;	
	
	BodyComponent target = null;
	BodyComponent bodyComponent;
	
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
					case Follow: handleFollow();
								 break;
					case Attack: handleAttack();
								 break;
					default:
				}
				
			}
		
	}
	
	private void handleAttack() {
		float maximumAttackDistance = 32;
		
		if(	target != null && isTargetVisible() &&
			getTargetDistanceX() <= maximumAttackDistance &&
			target.alive) {
			parent.getComponent(AttackComponent.class).attack(AnimationState.Attack1);
		}
	}
	
	private void handleFollow() {
		
		float minimumDistanceBetweenTarget = 16;
		
		if(target != null && isTargetVisible() && target.alive && getTargetDistanceX() > minimumDistanceBetweenTarget) {
			if(bodyComponent.getPosition().x < target.getPosition().x) {
//				bodyComponent.body.applyLinearImpulse(new Vector2(speed, 0), parent.getComponent(BodyComponent.class)getBodyComponent().body.getWorldCenter());
//				bodyComponent.setFacingRight(true);
				movementComponent.move(true);
			} else {
//				bodyComponent.body.applyLinearImpulse(new Vector2(-speed, 0), getBodyComponent().body.getWorldCenter());
//				bodyComponent.setFacingRight(false);
				movementComponent.move(false);
			}
		}
		
	}
	
	private boolean isTargetVisible() {
		
		// FIXME: Palikkaimplementaatio, lisää visibility detection!
		
		float visibilityX = 100;
		float visibilityY = 100;
		
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
			System.out.println("Added AI state " +state);
		
	}
	
	public void removeAIstate(AIstate state ) {
		
			activeStates.remove(state);		
	}

}
