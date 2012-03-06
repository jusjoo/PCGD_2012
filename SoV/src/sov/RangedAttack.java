package sov;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import sov.BodyComponent.SlopeShape;
import sov.Creature.Stats;
import sov.SpriteComponent.AnimationState;

public class RangedAttack extends Attack {
	
	public float flightSpeed;
	private int spellType = -1;

	protected Projectile projectileProto;
	private float ammo;
	private float ammoMax;
	
	public RangedAttack(AttackType attackType, AttackComponent attackComponent, float attackTime,	float preDamageTime,
			AnimationState attackAnimation, Projectile projectile, float offSetY, float damage, float flightSpeed) {
		
		super(attackType, attackComponent, attackTime, preDamageTime, attackAnimation, offSetY,damage);
		
		this.flightSpeed = flightSpeed;
		this.projectileProto = projectile;
		this.ammo = 20;
		this.ammoMax = ammo;
				
	}
	public void setSpellType(int type) {
		this.spellType = type;
	}
	
	public boolean isSpell(){
		System.out.println("Spelltype: "+spellType);
		if (spellType < 0) {
			return false;
		}
					
		else return true;
	}
	


	public void startDamage(){		
		
		Projectile projectile = new Projectile(projectileProto.body.getSize(), 
				projectileProto.spriteComponent.animations, 
				 true);

		projectile.setOwner(this.attackComponent.parent);
		

		if (projectileProto.impactSound != null) {
			projectile.addImpactSound(projectileProto.impactSound);
		}

		
		projectile.setDamage(getDamage(Stats.Wisdom), spellType);		
		
		float offSet = getAttackBoxOffsetX();
		
		if (offSet > 0) {
			projectile.body.setFacingRight(true);
		} else {
			projectile.body.setFacingRight(false);
		}
		
		
		//adding new projectile
		attackComponent.projectiles.add(projectile);
		//System.out.println("Adding new projectile");
		
		projectile.body.addToWorld(attackComponent.bodyComponent.world, 
				new Vector2(attackComponent.bodyComponent.getPosition().x + offSet, 
				attackComponent.bodyComponent.getPosition().y + offSetY ));
		
		//projectile.body.body.getFixtureList().get(0).set(10);
		

		projectile.spriteComponent.setCurrentAnimationState(AnimationState.Idle);
		
		// Sets attack bodies user data as the created projectile, so that they can be identified
		projectile.body.setUserData(new ContactEvent(projectile, "projectile"));
		projectile.body.body.setGravityScale(0);

		
	
		if(offSet > 0) {
			projectile.body.applyLinearImpulse(new Vector2( flightSpeed, 0f));

		} else {
			projectile.body.applyLinearImpulse(new Vector2( -flightSpeed, 0f));
		}
		
		damaging = true;
		
	}
	
	public boolean consumeResource() {
		boolean result;
		float consumption = 1;
		if (isSpell()) {
			consumption = getBaseDamage() * GameConfiguration.manaCostAttackMultiplier;			
			result = ((Creature)attackComponent.parent).modifyMana(-consumption);
		}
		else result = modifyAmmo(-consumption);
		
		return result;
		
	}
	private boolean modifyAmmo(float mod) {
		boolean result;
		if (ammo+mod >= 0 && ammo+mod <= ammoMax) {
			ammo += mod;
			result = true;
		}
		else result = false;
		System.out.println("Ammo: "+ammo+"/"+ammoMax);
		return result;
	}
	private float getAttackBoxOffsetX() {
		float offset = attackComponent.getOffsetX();
				
		float projectileOffset = projectileProto.body.getSize().x/2+1;
		
		if(offset > 0) {
			offset += projectileOffset;
		} else {
			offset -= projectileOffset;
		}
		
		System.out.println("offset:"+ offset);
		
		return offset;
	}


	public void stopDamage() {
		damaging = false;
	}
	
	public int getSpellType(){
		return this.spellType;
	}


	@Override
	public void update(float deltaTime) {
		
		
		timer = timer - deltaTime;
		
		if (timer < this.attackTime - this.preDamageTime && attacking){
		//if (timer < this.attackTime - this.preDamageTime && !damaging){
			
			this.startDamage();
			attacking = false;
		}		
		
		
		if (timer < 0) {
			
			attackComponent.stopAttack();
			
		}
	}
	


	@Override
	public void render(SpriteBatch spriteBatch) {
		
	}
	
	public float getAmmo() {		
		 return ammo;
	}
	public void giveAmmo(float ammo) {
		this.ammo += ammo;
		if (this.ammo > ammoMax)
			this.ammo = ammoMax;
	}

}
