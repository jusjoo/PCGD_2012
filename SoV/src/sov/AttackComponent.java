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
	 * active attack type
	 */
	Attack activeAttack;
	
	boolean setToStopDamage;


	
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
			activeAttack.update(deltaTime);
		}
	}
		
	protected void stopAttack() {
		activeAttack.damaging = false;
		activeAttack = null;
	}


	
	protected void stopDamage() {
		if (activeAttack.damaging) {
			activeAttack.stopDamage();	
		}
	}


	/*
	 * Starts the attacking if we are ready to attack.
	 * 
	 * @.pre	Parent must be set! Prototypes cannot attack.
	 */
	public void attack(SpriteComponent.AnimationState attackType) {
		if (activeAttack == null) {
			activeAttack = attacks.get(attackType);
			activeAttack.startAttack();
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
