package sov;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

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
	boolean canAttack;
	boolean attacking;
	boolean damaging;
	
	Body attackBody;
	PolygonShape attackSensorShape;
	Fixture attackSensorFixture;

	protected CreatureComponent.AnimationState animation;

	
	/*
	 * TODO: Takes in a custom attack fixture shape, which is then handled for attacks on both sides.
	 */
	public AttackComponent(Entity parent, float attackTime, float preDamageTime, float damageTime, CreatureComponent.AnimationState attackAnimation) {
		super(parent);
		
		this.attackTime = attackTime;
		this.preDamageTime = preDamageTime;
		this.damageTime = damageTime;		
		this.animation = attackAnimation;
	}
	
	
	@Override
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
			
			//Vector2 currentVelocity = parent.getComponent(BodyComponent.class).body.getLinearVelocity();

			
		
		
		
			
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
		
		BodyComponent bodyComponent = parent.getComponent(BodyComponent.class);
		

		
		BodyDef attackBodyDef = new BodyDef();
		attackBodyDef.gravityScale = 0;
		attackBodyDef.position.set( bodyComponent.getPosition().x/PIXELS_PER_METER , bodyComponent.getPosition().y/PIXELS_PER_METER );		
		
		
		
		int offSet = 0;
		if( ((Creature)parent).body.getFacingRight()) offSet = 1;
		else offSet = -1; 
		
		PolygonShape attackSensorShape = new PolygonShape();
		attackSensorShape.setAsBox(bodyComponent.getSize().x / PIXELS_PER_METER / 1.5f, bodyComponent.getSize().y / PIXELS_PER_METER / 1.5f, new Vector2(bodyComponent.getSize().x / PIXELS_PER_METER * offSet ,  0) , 0f);

		
		
		FixtureDef attackSensorFixtureDef = new FixtureDef();
		attackSensorFixtureDef.shape = attackSensorShape;
		attackSensorFixtureDef.isSensor = true;
		attackSensorFixtureDef.density = 0;
		
		
		
		attackBody = bodyComponent.world.createBody(attackBodyDef);
		attackBody.setUserData(this);
		this.attackSensorFixture = attackBody.createFixture(attackSensorFixtureDef);
	}
	
	protected void stopDamage() {
		if (damaging) {
			damaging = false;
			attackBody.destroyFixture(attackSensorFixture);
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
