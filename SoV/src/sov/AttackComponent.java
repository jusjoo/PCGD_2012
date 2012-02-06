package sov;

import java.util.ArrayList;
import java.util.HashMap;

import sov.BodyComponent.SlopeShape;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class AttackComponent extends Component {

	/*
	 * This keeps track of the attacks stored in this component
	 */
	HashMap<SpriteComponent.AnimationState, Attack> attacks;
	
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

	
	// The attackers BodyComponent
	protected BodyComponent bodyComponent;

	public AttackComponent(Entity parent){
		super(parent);	
		attacks = new HashMap<SpriteComponent.AnimationState, Attack>();
		activeAttack = null;
	}
	
	
	@Override
	public void update(float deltaTime){
		
		
		if (setToStopDamage) stopDamage();
		
		if (activeAttack != null) {
			
			// Update the spriteComponent, so we get animations, hooray!
			if (damaging) activeAttack.attackBody.spriteComponent.update(deltaTime);
			
			/*
			 * Update attack body's position relative to the Entity's body
			 */
			if (activeAttack.getClass() == Attack.class) {
				if (damaging) {
					float offSet = activeAttack.getAttackBoxOffsetX();
					activeAttack.attackBody.body.setPosition(new Vector2(bodyComponent.getPosition().x + offSet, bodyComponent.getPosition().y + activeAttack.offSetY ));
				}
			} 
			
			timer = timer - deltaTime;
			
			if (timer < activeAttack.attackTime-activeAttack.preDamageTime && 
					timer > activeAttack.attackTime-activeAttack.preDamageTime-activeAttack.damageTime && !damaging){

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

	protected void startAttack(SpriteComponent.AnimationState attackType) {
		timer = attacks.get(attackType).attackTime;
		activeAttack = attacks.get(attackType);
		
		((Creature)parent).getComponent(SpriteComponent.class).setCurrentAnimationState(activeAttack.animation);
		//parent.getComponent(SpriteComponent.class).setCurrentAnimationState(attacks.get(attackType).animation);
		
	}
	
	protected void stopDamage() {
		if (damaging) {
			damaging = false;
			activeAttack.attackBody.body.removeFromWorld();
			
		}
	}


	/*
	 * Starts the attacking if we are ready to attack.
	 * 
	 * @.pre	Parent must be set! Prototypes cannot attack.
	 */
	public void attack(SpriteComponent.AnimationState attackType) {
		if (activeAttack == null) {
			startAttack(attackType);
		}
	}

	public void setToStopDamage() {
		setToStopDamage = true;		
	}
	
	
	/*
	 * Gets the offset from parents body center, to the body's edge
	 */
	protected float getOffsetX() {
		float offSet = 0;
		
		offSet = parent.getComponent(BodyComponent.class).getSize().x/GameConfiguration.PIXELS_PER_METER/2;
		
		if( !parent.getComponent(BodyComponent.class).getFacingRight()) offSet = -offSet;
		
		return offSet ;
	}
	
	/*
	 * This is used to set the parent later to a prototype AttackComponent
	 */
	protected void setParent(Entity newParent) {
		this.parent = newParent;
		bodyComponent = parent.getComponent(BodyComponent.class);

	}


	public void addAttack(SpriteComponent.AnimationState name, Attack attack) {
		attacks.put(name, attack);
		
	}


	/*
	 * Renders the possible attack SpriteBody
	 */
	public void render(SpriteBatch spriteBatch) {
		/*if (damaging) {
			activeAttack.attackBody.spriteComponent.render(spriteBatch, activeAttack.attackBody.body.getFacingRight(),
					activeAttack.attackBody.body.getPosition().x,
					activeAttack.attackBody.body.getPosition().y,
					(float) (activeAttack.attackBody.body.getAngle()*180/Math.PI),
					activeAttack.attackBody.body.getSize()
					);
		}*/
		
	}
	
}
