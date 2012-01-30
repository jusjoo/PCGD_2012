package sov;

import sov.SpriteComponent.AnimationState;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
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
	
	boolean setToStopDamage;
	boolean attacking;
	boolean damaging;
	
	PolygonShape attackSensorShape;
	Fixture attackSensorFixture;

	protected AnimationState animation;

	
	/*
	 * TODO: Takes in a custom attack fixture shape, which is then handled for attacks on both sides.
	 */
	public AttackComponent(Object parent, float attackTime, float preDamageTime, float damageTime, SpriteComponent.AnimationState attackAnimation) {
		super(parent);
		
		this.attackTime = attackTime;
		this.preDamageTime = preDamageTime;
		this.damageTime = damageTime;		
		this.animation = attackAnimation;
		
		((Creature)parent).setAttackComponent(this);
	}
	
	
	public void update(float deltaTime){
		
		if (setToStopDamage) stopDamage();
		
		if (attacking) {
			((Creature)parent).getComponent(SpriteComponent.class).setCurrentAnimationState(animation);
			timer = timer - deltaTime;
			
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
		damaging = false;
	}



	protected void startAttack(){
		attacking = true;
		timer = attackTime;
		
	}
	
	protected void startDamage(){
		damaging = true;
		
		float PIXELS_PER_METER = GameConfiguration.PIXELS_PER_METER;
		
		int offSet = 0;
		if( ((Creature)parent).body.getFacingRight()) offSet = 1;
		else offSet = -1;
		
		PolygonShape attackSensorShape = new PolygonShape();
		attackSensorShape.setAsBox(((Creature)parent).body.getSize().x / PIXELS_PER_METER / 1.5f, ((Creature)parent).body.getSize().y / PIXELS_PER_METER / 1.5f, new Vector2(((Creature)parent).body.getSize().x / PIXELS_PER_METER * offSet ,  0) , 0f);
		
		FixtureDef attackSensorFixtureDef = new FixtureDef();
		attackSensorFixtureDef.shape = attackSensorShape;
		attackSensorFixtureDef.isSensor = true;
		attackSensorFixtureDef.density = 0;
		
		this.attackSensorFixture = ((Creature) parent).getComponent(BodyComponent.class).body.createFixture(attackSensorFixtureDef);
		
	}
	
	protected void stopDamage() {
		if (damaging) {
			damaging = false;
			((Creature) parent).getComponent(BodyComponent.class).body.destroyFixture(attackSensorFixture);
		}
	}


	
	public void attack() {
		if (!attacking) {
			startAttack();
		}
	}


	public Fixture getAttackFixture() {
		
		return attackSensorFixture;
	}


	public void setToStopDamage() {
		setToStopDamage = true;		
	}
	
	
	
}
