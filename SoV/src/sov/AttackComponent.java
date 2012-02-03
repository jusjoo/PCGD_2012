package sov;

import sov.BodyComponent.SlopeShape;

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
	
	BodyComponent bodyComponent;
	BodyComponent attackBodyComponent;
	PolygonShape attackSensorShape;
	Fixture attackSensorFixture;

	
	protected SpriteComponent.AnimationState animation;

	
	/*
	 * TODO: Takes in a custom attack fixture shape, which is then handled for attacks on both sides.
	 */
	public AttackComponent(Entity parent, float attackTime, float preDamageTime, float damageTime, SpriteComponent.AnimationState attackAnimation) {
		super(parent);
		
		this.attackTime = attackTime;
		this.preDamageTime = preDamageTime;
		this.damageTime = damageTime;		
		this.animation = attackAnimation;
		
		

		float PIXELS_PER_METER = GameConfiguration.PIXELS_PER_METER;
		
		if (parent != null)	{
			bodyComponent = parent.getComponent(BodyComponent.class);
			attackBodyComponent = new BodyComponent(this.parent,
					new Vector2(20,20), false, 1.0f, false, SlopeShape.Even, true);
		}		
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

			
	
		/*
		 * Update attack body's position relative to the Entity's body
		 */
		if (damaging) {
			float offSet = getOffset();
			attackBodyComponent.setPosition(new Vector2(bodyComponent.getPosition().x + offSet*16, bodyComponent.getPosition().y ));
		}
		if(attackBodyComponent != null){
			attackBodyComponent.update(deltaTime);
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
		
		float offSet = getOffset();
				
		attackBodyComponent.addToWorld(bodyComponent.world, new Vector2(bodyComponent.getPosition().x + offSet*16, bodyComponent.getPosition().y ));
		
		// Sets attack bodies user data as this, so that attack sensors can be identified
		attackBodyComponent.setUserData(this);
		attackBodyComponent.body.setGravityScale(0);
	
	}
	
	protected void stopDamage() {
		if (damaging) {
			damaging = false;
			attackBodyComponent.removeFromWorld();
		}
	}


	/*
	 * Starts the attacking if we are ready to attack.
	 * 
	 * @.pre	Parent must be set! Prototypes cannot attack.
	 */
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
	
	protected float getOffset() {
			
		float offSet = 0;
		if( ((Creature)parent).body.getFacingRight()) offSet = 1.5f;
		else offSet = -1.5f; 
	
		return offSet ;
	}
	
	/*
	 * This is used to set the parent later to a prototype AttackComponent
	 */
	protected void setParent(Entity newParent) {
		this.parent = newParent;
		bodyComponent = parent.getComponent(BodyComponent.class);
		attackBodyComponent = new BodyComponent(this.parent,
				new Vector2(20,20), false, 1.0f, false, SlopeShape.Even, true);
	}
	
}
