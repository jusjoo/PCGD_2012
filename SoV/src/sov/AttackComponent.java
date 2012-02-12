package sov;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

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
	TreeMap<SpriteComponent.AnimationState, Attack> attacks;
	
	/*
	 * keeps track of all the projectiles made by this AttackComponent
	 */
	ArrayList<Projectile> projectiles;
	ArrayList<Projectile> removedProjectiles;
	
	
	/*
	 * active attack type
	 */
	Attack activeAttack;
	
	boolean setToStopDamage;
	//boolean damaging;


	
	// The attackers BodyComponent
	protected BodyComponent bodyComponent;

	public AttackComponent(Entity parent){
		super(parent);	
		attacks = new TreeMap<SpriteComponent.AnimationState, Attack>();
		projectiles = new ArrayList<Projectile>();
		removedProjectiles = new ArrayList<Projectile>();
		activeAttack = null;
	}
	
	
	/*
	 * Updates the activeAttack and all projectiles
	 */
	public void update(float deltaTime){
	
		
		for (Projectile projectile: projectiles) {
			projectile.update(deltaTime);
			
			if (projectile.setToDestroy && projectile.spriteComponent.isAtLastFrame()) {
				projectile.body.removeFromWorld();
				removedProjectiles.add(projectile);
			}
		}
		
		projectiles.removeAll(removedProjectiles);
		
		if (setToStopDamage) stopDamage();
		
		if (activeAttack != null) {
			activeAttack.update(deltaTime);
		}
		
		
	}
		
	protected void stopAttack() {
		
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

	
	/*
	 * Gets the offset from parents body center, to the body's edge
	 */
	protected float getOffsetX() {
		float offSet = 0;
		
		offSet = parent.getComponent(BodyComponent.class).getSize().x/2;
		
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
		System.out.println(name.toString() + attack.toString());		
	}


	/*
	 * Renders all the projectiles and attack bodies
	 */
	public void render(SpriteBatch spriteBatch) {
		for (Attack attack: attacks.values()) {
			attack.render(spriteBatch);
		}
		
		for (Projectile projectile: projectiles) {
			projectile.render(spriteBatch);
		}
		
		
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
