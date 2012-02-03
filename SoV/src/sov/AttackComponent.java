package sov;

import java.util.ArrayList;

import sov.BodyComponent.SlopeShape;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class AttackComponent extends Component {

	/*
	 * These keep track of the attacks stored in this component
	 */
	ArrayList<Attack> attacks;
	
	
	
	/*
	 * timer keeps track of the whole attack from beginning to end
	 */
	float timer;
	
	/*
	 * active attack type
	 */
	Attack activeAttack;
	
	boolean setToStopDamage;
	boolean canAttack;
	boolean damaging;

	
	protected BodyComponent bodyComponent;
	/*
	protected BodyComponent attackBodyComponent;
	protected PolygonShape attackSensorShape;
	protected Fixture attackSensorFixture;
	protected SpriteComponent.AnimationState animation;*/
	
	public AttackComponent(Entity parent){
		super(parent);	
		attacks = new ArrayList<Attack>();
		activeAttack = null;
	}
	
	
	@Override
	public void update(float deltaTime){
		
		if (setToStopDamage) stopDamage();
		
		if (activeAttack != null) {
			
			/*
			 * Update attack body's position relative to the Entity's body
			 */
			if (activeAttack.getClass() == Attack.class) {
				if (damaging) {
					float offSet = getOffset();
					activeAttack.attackBodyComponent.setPosition(new Vector2(bodyComponent.getPosition().x + offSet*16, bodyComponent.getPosition().y ));
				}
			} 
			
			((Creature)parent).getComponent(SpriteComponent.class).setCurrentAnimationState(activeAttack.animation);
			timer = timer - deltaTime;
			
			if (timer < activeAttack.attackTime-activeAttack.preDamageTime && 
					timer > activeAttack.attackTime-activeAttack.preDamageTime-activeAttack.damageTime && !damaging){
				
				System.out.println("p‰‰st‰‰n t‰nne");
				activeAttack.startDamage();
			}
			if (timer < activeAttack.attackTime-activeAttack.preDamageTime-activeAttack.damageTime){
				stopDamage();
			}
			if (timer < 0) {
				stopAttack();
			}

			
				
			
			/*if(attackBodyComponent != null){
				attackBodyComponent.update(deltaTime);
			}	*/		
		}
	}
		
	protected void stopAttack() {
		damaging = false;
		activeAttack = null;
	}

	protected void startAttack(int attackType) {
		timer = attacks.get(attackType).attackTime;
		activeAttack = attacks.get(attackType);
		
		//parent.getComponent(SpriteComponent.class).setCurrentAnimationState(attacks.get(attackType).animation);
		
	}
	
	protected void stopDamage() {
		if (damaging) {
			damaging = false;
			activeAttack.attackBodyComponent.removeFromWorld();
		}
	}


	/*
	 * Starts the attacking if we are ready to attack.
	 * 
	 * @.pre	Parent must be set! Prototypes cannot attack.
	 */
	public void attack(int attackType) {
		if (activeAttack == null) {
			startAttack(attackType);
		}
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

	}


	public void addAttack(Attack attack) {
		attacks.add(attack);
		
	}
	
}
