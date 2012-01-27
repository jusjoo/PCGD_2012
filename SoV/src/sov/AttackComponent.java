package sov;

import sov.SpriteComponent.AnimationState;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;

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
	boolean damaging;
	
	Shape attackSensorShape;
	//attackSensorShape.setAsBox(body.getSize().x / PIXELS_PER_METER / 1.5f, body.getSize().y / PIXELS_PER_METER / 1.5f, new Vector2(body.getSize().x / PIXELS_PER_METER * offSet ,  0) , 0f);
	Fixture attackSensorFixture;

	protected AnimationState animation;

	
	public AttackComponent(Object parent, float attackTime, float preDamageTime, float damageTime, Shape attackSensorShape, SpriteComponent.AnimationState attackAnimation) {
		super(parent);
		
		this.attackTime = attackTime;
		this.preDamageTime = preDamageTime;
		this.damageTime = damageTime;
		this.attackSensorShape = attackSensorShape;
		
		this.animation = attackAnimation;
	}
	
	
	public void update(float deltaTime){
		if (attacking) {
			timer -= deltaTime;
			
			if (timer < attackTime-preDamageTime && timer > attackTime-preDamageTime-damageTime && !damaging){
				startDamage();
			}
			if (timer < attackTime-preDamageTime-damageTime){
				stopDamage();
			}
			if (timer < 0) {
				stopAttack();
			}
		}
	}
	
	
	
	protected void stopAttack() {
		attacking = false;
		
		((Creature)parent).getComponent(SpriteComponent.class).currentAnimationState = animation; 
	}



	protected void startAttack(){
		attacking = true;
		timer = attackTime;
		
	}
	
	protected void startDamage(){
		damaging = true;
		FixtureDef attackSensorFixtureDef = new FixtureDef();
		attackSensorFixtureDef.shape = attackSensorShape;
		attackSensorFixtureDef.isSensor = true;
		attackSensorFixtureDef.density = 0;
		
		this.attackSensorFixture = ((Creature) parent).getComponent(BodyComponent.class).body.createFixture(attackSensorFixtureDef);
	}
	
	protected void stopDamage() {
		damaging = false;
		((Creature) parent).getComponent(BodyComponent.class).body.destroyFixture(attackSensorFixture);
	}


	
	public void attack() {
		if (!attacking) {
			startAttack();
		}
	}
	
	
	
}
