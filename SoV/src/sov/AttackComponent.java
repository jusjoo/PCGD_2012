package sov;

public class AttackComponent extends Component {

	/*
	 * preAttackTime is the time where the animation starts, but the attack fixture is not yet active.
	 */
	float preDamageTime;
	
	/*
	 * damageTime is how long the attack fixture is active.
	 */
	float damageTime;
	
	/*
	 * attackTime is the total attack animation time
	 */
	float attackTime;
	
	/*
	 * timer keeps track of the whole attack from beginning to end
	 */
	float timer;
	
	
	boolean attacking;

	
	public AttackComponent(Object parent, float attackTime, float preDamageTime, float damageTime) {
		super(parent);
		
		this.attackTime = attackTime;
		this.preDamageTime = preDamageTime;
		this.damageTime = damageTime;
	}
	
	// Update the attack timer
	public void update(float deltaTime){
		if (attacking) {
			timer -= deltaTime;
		}
	}

	
	
	protected void stopAttack() {
		attacking = false;
	}



	protected void startAttack(){
		attacking = true;
		
	}
	
	


	
	public void attack() {
		if (!attacking) {
			startAttack();
		}
	}
	
	
	
}
