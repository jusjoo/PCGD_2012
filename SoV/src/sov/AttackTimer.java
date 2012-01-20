package sov;

public class AttackTimer {
	protected float attackTime;
	protected float timer;
	protected boolean timerActive;
	protected Creature creature;
	
	
	public AttackTimer(Creature creature, float attackTime) {
		this.attackTime = attackTime;
		this.creature = creature;
		this.timerActive = false;
		this.timer = 0;
	}
	
	// Update the attack timer
	public void update(float deltaTime) {
		if (timerActive) {
			timer -= deltaTime;
			
			// Initiate stopAttack() when attackTimer has passed
			if (timer <= 0) {
				stopAttack();
				timerActive = false;
			}	
		}
	}

	
	
	private void stopAttack() {
		creature.stopAttack();
	}



	public void startAttackTimer(){
		
		timer = attackTime;
		timerActive = true;
	}
}
