package sov;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.DebugGraphics;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

import sov.BodyComponent.SlopeShape;
import sov.SpriteComponent.AnimationState;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.google.gson.annotations.Expose;

/*
 * Creatures include all moving object that are controlled either by AI, or Player input.
 */
public class Creature extends SpriteBody implements Cloneable {
	
	public enum CreatureType { Barbarian, Ninja, Sorceress, Goblin, Slime, Troll };
	public enum Stats { Strength, Dexterity, Wisdom, HealthRegen, StaminaRegen, ManaRegen,Stealth,Freeze};	

	
	// All the properties which are read from creatures.json must be declared here
	// These properties are then automatically "filled" by DynamicObjectFactory
	@Expose CreatureType creatureType;
	@Expose String textureName;
	@Expose int[] hitboxSize;
	@Expose float dexterity;
	@Expose float strength;
	@Expose float wisdom;
	@Expose HashMap<SpriteComponent.AnimationState, ArrayList<Object>> frames;
	@Expose HashMap<SpriteComponent.AnimationState, ArrayList<Object>> attacks;
	@Expose HashMap<SpriteComponent.AnimationState, String> sounds;
	
	private float chargeTimer = 0f;
	private boolean specialHasCharged = false;
	
	protected float speed;
	protected float jumpHeight;
	protected float staminaMax;
	protected float stamina;
	protected float manaMax;
	protected float mana;
	
	protected float stealth = 0; // determines how much enemy visibility is reduced
	
	protected float healthRegenBonus = 0f;
	protected float staminaRegenBonus = 0f;
	protected float manaRegenBonus = 0f;
	protected ArrayList<Buff> activeBuffs;
		
	protected boolean statsUpdated = false;
	protected boolean specialActive = false;
	protected float specialTimer;
	
	Fixture sensorFixture;	
	
	boolean canAttack = true;
	protected float manaDrain = 0;
	private boolean activatedAbility = false;
	boolean applyCriticalDamage = false;
	
	private HudBarElement barbarianSpecialBar;
	private float barbarianSpecialBarTimer = 0;
	
	
	public Creature() {
	}
	
	// Deliver size and position of the creature in pixels.
	public Creature(Vector2 size, HashMap<SpriteComponent.AnimationState, Animation> animations, float rounding,
			boolean circle) {
		super(size, animations,
				false, rounding, circle, SlopeShape.Even);	
	}
	
	public static Creature createFromPrototype(Creature prototype) {
		
		Creature creature = new Creature(new Vector2(prototype.hitboxSize[0], prototype.hitboxSize[1]), prototype.spriteComponent.animations, 0.8f, false);
		creature.creatureType = prototype.creatureType;
		creature.dexterity = prototype.dexterity;
		creature.strength = prototype.strength;
		creature.wisdom = prototype.wisdom;
		creature.speed = creature.deriveSpeed();
		//hitpoints are set when adding to world
		creature.jumpHeight = creature.deriveJumpHeight();
		creature.manaMax = prototype.deriveMana();
		creature.staminaMax = prototype.deriveStamina();
		creature.mana = creature.manaMax;
		creature.stamina = creature.staminaMax;
		creature.activeBuffs = new ArrayList<Buff>();
		
		
		creature.body.setMaxVelocity(creature.speed);
		//creature.body.setMaxVelocity(9001);
		
		// Set the attack component
		if(prototype.getComponent(AttackComponent.class) != null) {
			AttackComponent protoAttackComponent = prototype.getComponent(AttackComponent.class);
			
			AttackComponent newAttackComponent = AttackComponent.attackComponentFromPrototype(protoAttackComponent, creature);
		
			
			creature.addComponent(newAttackComponent);
			creature.getComponent(AttackComponent.class).setParent(creature);
		}
		
		if (prototype.getComponent(AudioComponent.class) != null) {
			creature.addComponent(prototype.getComponent(AudioComponent.class));
		}
		
		
		
		

		
		
		return creature;
	}	
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
				
		if (!body.finalDeath) {
			
			regenerateStamina(deltaTime);
			regenerateMana(deltaTime);			
			regenerateHealth(deltaTime);
			
			ArrayList<Buff> buffsToRemove = new ArrayList<Buff>();
			
			/*
			 * Run through all buffs
			 */
			if (activeBuffs.size() > 0) {
				for (Buff buff : activeBuffs) {
					// quickndirty
					if (buff.getStat().equals(Stats.Stealth)) {
						//System.out.println("stealthing");
						getComponent(SpriteComponent.class).setHue(new Color(0.7f,0.7f,0.7f,0.3f));
					}
					if (buff.getStat().equals(Stats.Freeze)) {
						//System.out.println("stealthing");
						getComponent(SpriteComponent.class).setHue(new Color(0.2f,0.2f,1.50f,0.9f));						
					}
					
					// If ability is not toggled manually we reduce the timer
					// and remove the buff if duration has ran out
					if (!activatedAbility) {												
						
						if (buff.getDuration() != -1 && buff.reduceDuration(deltaTime) ){
							buffsToRemove.add(buff);
						}
					}
				}
				if (buffsToRemove.size() > 0) {
					for (Buff buff : buffsToRemove) {
						applyDebuff(buff);
					}
				}
				
				if (barbarianSpecialBarTimer > 0) {
					barbarianSpecialBar.bar.setCurrentValue(barbarianSpecialBarTimer);
					barbarianSpecialBarTimer -= deltaTime;
					if (barbarianSpecialBarTimer <= 0) {
						GameConfiguration.hud.removeElement(barbarianSpecialBar);
					}
				}
			}
			if (specialActive) {
				specialTimer -= deltaTime;
			}
			if (specialTimer <= 0) {
				specialActive = false;
			}
			if (chargeTimer > 0) {
				chargeTimer -= deltaTime;				
			}
			else if (specialHasCharged) {
				if (getComponent(MovementComponent.class).onGround() ) {
					specialHasCharged = false;
				}
					
			}
			
			
		}
		
	}
	
	private void regenerateMana(float deltaTime) {
		
		float manaregen;
		
		if (GameConfiguration.manaRegenAsPercentage) {
			manaregen = deltaTime * GameConfiguration.manaRegenRatePercentage * manaMax;
			if (manaRegenBonus < 0) {
				manaregen += manaRegenBonus*deltaTime;
			}
			else {
				manaregen = manaregen * (1+manaRegenBonus);
			}
		}
		else
			manaregen = deltaTime * GameConfiguration.manaRegenRateStatic;			
			
		
		if (!modifyMana(manaregen) && activatedAbility == true) {
			activateSpecialAbility();
		}
		
	}

	private void regenerateStamina(float deltaTime) {
		float staminaregen;
		
		if (GameConfiguration.staminaRegenAsPercentage) {
			staminaregen = deltaTime * GameConfiguration.staminaRegenRatePercentage * staminaMax;
			if (staminaRegenBonus < 0) {
				staminaregen += staminaRegenBonus*deltaTime;
			}
			else {
				staminaregen = staminaregen * (1+staminaRegenBonus);
			}
		}
		else
			staminaregen = deltaTime * GameConfiguration.staminaRegenRateStatic;
		
		modifyStamina(staminaregen);
		
	}
	
	private void regenerateHealth(float deltaTime) {
		float healthregen;		
		if (healthRegenBonus > 0) {
			BodyComponent bodycomp = getComponent(BodyComponent.class);
			healthregen = deltaTime * healthRegenBonus * bodycomp.getHitPointsMax();
			bodycomp.heal(healthregen);			
		}
	}

	public void removeFromWorld(){
		getComponent(BodyComponent.class).removeFromWorld();
		getComponent(BodyComponent.class).body.destroyFixture(sensorFixture);
	}
	
	public void addToWorld(World world, Vector2 position) {
		//set hitpoints		 
		float hp = deriveHitpoints();
		getComponent(BodyComponent.class).setHitPoints(hp);
		getComponent(BodyComponent.class).heal(hp);
		getComponent(BodyComponent.class).addToWorld(world, position);
		//super.addToWorld(world, position);
		float PIXELS_PER_METER = GameConfiguration.PIXELS_PER_METER;
		
		// Create a sensor at the level of the feet for detecting if a creature is touching the ground.
		// TODO: Transfer creating the body, shape and fixture to a static helper function.
		PolygonShape sensorShape = new PolygonShape();
		sensorShape.setAsBox(body.getSize().x / (3 * PIXELS_PER_METER), body.getSize().y / (12 * PIXELS_PER_METER),
				new Vector2(0, -body.getSize().y/PIXELS_PER_METER/2.23f), 0);
		FixtureDef sensorFixtureDef = new FixtureDef();
		sensorFixtureDef.shape = sensorShape;
		sensorFixtureDef.isSensor = true;
		sensorFixtureDef.density = 0;
		
		
		
		
		// Attach the foot-sensor on the body.
		sensorFixture = getComponent(BodyComponent.class).body.createFixture(sensorFixtureDef);
		//System.out.println(this);
		sensorFixture.setUserData(new ContactEvent(this, "sensor"));
		
		
		// Creatures shall not rotate according to physics!
		getComponent(BodyComponent.class).body.setFixedRotation(true);
		
		
	}
	
	public float getStealth() {		
		return stealth;
	}
	public float getDexterity() {
		System.out.println("Dexterity "+dexterity);
		return dexterity; 
	}
	public float getStrength() {
		System.out.println("Strength "+strength);
		return strength;
	}
	public float getWisdom() {
		System.out.println("Wisdom "+wisdom);
		return wisdom;				
	}
	public float getSpeed(){
		return speed;
	}
	public float getJumpHeight(){
		return jumpHeight;				
	}
	public float getMana() {
		return mana;
	}
	public float getStamina() {
		return stamina;
	}
	public float deriveSpeed() {
		return this.dexterity * GameConfiguration.dexSpeedMultiplier + GameConfiguration.speedBaseModifier;		
	}
	
	public float deriveJumpHeight() {
		return GameConfiguration.jumpHeightBaseModifier;
	}
	public float deriveDoubleJumpMultiplier() {
		return GameConfiguration.dexJumpHeightMultiplier*dexterity;
	}
	public float deriveMana(){
		float value = wisdom * GameConfiguration.wisManaMultiplier + GameConfiguration.manaBaseModifier;
		return value;
	}
	public float deriveStamina(){
		float value = dexterity * GameConfiguration.dexStaminaMultiplier + GameConfiguration.staminaBaseModifier;
		return value;				
	}
	private float deriveHitpoints() {
		float value = strength * GameConfiguration.strHealthMultiplier + GameConfiguration.healthBaseModifier; 
		return value;
	}
	// return true if legal operation
	public boolean modifyMana(float value){
		
		float newValue = mana+value;
		if (newValue < 0) {
			System.out.println("Out of Mana!");
			return false;
		}
		else if (newValue > manaMax)
			mana = manaMax;
		else mana = newValue;
		
		//System.out.println("Mana("+mana+"/"+manaMax+"): "+value);
		return true;
	}
	
	public boolean modifyStamina(float value){
		
		float newValue = stamina+value;
		if (newValue < 0)
			return false;
		else if (newValue > staminaMax)
			stamina = staminaMax;
		else stamina = newValue;
		
		//System.out.println("Stamina("+stamina+"/"+staminaMax+"): "+value);
		
		return true;	
		
	}

	public void modifyStrength(float strengthModifier) {
		strength += strengthModifier;
		float newHp = deriveHitpoints();
		
		BodyComponent body = getComponent(BodyComponent.class);
		float hpPercentage = body.hitPoints / body.hitPointsMax;
		body.setHitPoints(newHp);
		body.hitPoints = newHp*hpPercentage;
		
		statsUpdated = true;
		
	}

	public void modifyDexterity(float dexterityModifier) {
		dexterity += dexterityModifier;
		speed = deriveSpeed();	
		if (deriveStamina()<10)
			staminaMax = 10;
		staminaMax = deriveStamina();
		MovementComponent movement = getComponent(MovementComponent.class);
		movement.setDoubleJumpMultiplier(deriveDoubleJumpMultiplier());
		movement.setSpeed(speed);
		body.setMaxVelocity(movement.getSpeed());
		System.out.println("Dex modified to: "+dexterity);
		
		statsUpdated = true;
	}	
	
	public void modifyWisdom (float wisdomModifier) {
		wisdom += wisdomModifier;
		manaMax = deriveMana();
		mana = manaMax;		
		
		statsUpdated = true;
	}

	public float getStaminaMax() {
		return staminaMax;
	}

	public float getManaMax() {

		return manaMax;
	}
	public String getStatsAsString() {
		String str = ("STR: " + strength);
		String dex = ("DEX: " + dexterity);
		String wis = ("WIS: " + wisdom);
		String hp = ("Health: "+ getComponent(BodyComponent.class).getHitPoints()+"/"+getComponent(BodyComponent.class).getHitPointsMax());
		String stamina = ("Stamina: "+Math.round(this.stamina)+"/"+staminaMax);
		String mana = ("Mana: "+Math.round(this.mana)+"/"+manaMax);
		String jump = ("Jump: "+getComponent(MovementComponent.class).getJumpHeight()+"/"+deriveJumpHeight());
		String speed = ("Speed: "+getComponent(MovementComponent.class).getSpeed()+"/"+deriveSpeed());
		
		return (str+" "+dex+" "+wis+" "+hp+" "+stamina+" "+mana+" "+jump+" "+speed);
	}
	public boolean statsUpdated() {
		boolean wasUpdated = statsUpdated;
		statsUpdated = false;
		return wasUpdated; 
	}
	
	public float getCreatureDangerLevel() {
		return (float)Math.sqrt((double)(strength + wisdom));
	}
	
	public void applyBuff(Buff buff) {
		switch (buff.getStat()) {
			case Strength:
				modifyStrength(buff.getBuffValue());
				break;
			case Dexterity:
				modifyDexterity(buff.getBuffValue());
				break;
			case Wisdom:
				modifyWisdom(buff.getBuffValue());
				break;
			case HealthRegen:
				this.healthRegenBonus += buff.getBuffValue();
				break;
			case StaminaRegen:
				this.staminaRegenBonus += buff.getBuffValue();
				break;
			case ManaRegen:
				this.manaRegenBonus += buff.getBuffValue();
			case Stealth:
				this.stealth += buff.getBuffValue();
				break;
		}
		activeBuffs.add(buff);			
	}
	private void applyDebuff(Buff buff) {
		switch (buff.getStat()) {
		case Strength:
			modifyStrength(-buff.getBuffValue());
			break;
		case Dexterity:
			modifyDexterity(-buff.getBuffValue());
			break;
		case Wisdom:
			modifyWisdom(-buff.getBuffValue());
			break;
		case HealthRegen:
			this.healthRegenBonus -= buff.getBuffValue();
			break;
		case StaminaRegen:
			this.staminaRegenBonus -= buff.getBuffValue();
			break;
		case ManaRegen:
			this.manaRegenBonus -= buff.getBuffValue();
		case Stealth:
			this.stealth -= buff.getBuffValue();
			break;
	}
	System.out.println("Removing buff: "+buff.getStat()+" +"+buff.getBuffValue()+" "+buff.getDuration()+"s ");
	activeBuffs.remove(buff);
		
	}

	public void activateSpecialAbility() {
		if (!specialActive || activatedAbility) {
			System.out.println("Activating "+creatureType+" special ability...");
			switch (creatureType) {
				case Barbarian:
					PlayerInputComponent.keyPressed();
					specialBarbarian();
					break;
				case Ninja:					
					PlayerInputComponent.keyPressed();
					if (!activatedAbility) {
						specialNinja();
						activatedAbility = true;
						applyCriticalDamage = true;
					}
					else {
						activatedAbility = false;
						applyCriticalDamage = false;
					}					
					
					break;
				case Sorceress:					
						specialSorceress();						
					break;
			}
		}
		
	}

	private void specialSorceress() {
		// System.out.println("Activating "+creatureType+" special ability...phase2");
		float manaCost = -0.75f;
		float timerExtra = 0.01f;
		//MovementComponent move = getComponent(MovementComponent.class);
		//if (move.onGround() && chargeTimer <= 0) {
		if (!specialHasCharged && chargeTimer <= 0) {
			Animation.play(this, AnimationState.LevitateCharge);	
			chargeTimer = getComponent(SpriteComponent.class).getAnimation(AnimationState.LevitateCharge).getLength()+timerExtra;
			specialHasCharged = true;
		}
		if (chargeTimer <= timerExtra) {			
			if (modifyMana(manaCost)) {			
			SpriteComponent spriteComp = getComponent(SpriteComponent.class);
			Animation.play(this, AnimationState.LevitateLoop);			
			getComponent(BodyComponent.class).applyLinearImpulse(new Vector2(0,1));			
			}
		}		
	}

	private void specialNinja() {
		System.out.println("Activating "+creatureType+" special ability...phase2");
		float manaCost = -10;		
		if (modifyMana(manaCost)) {
			//manaDrain  = -2.5f;
			int level = getComponent(ExperienceComponent.class).getLevel();
			float duration = -1;
			applyBuff( new Buff(Stats.Stealth, 100.0f, duration ) );
			applyBuff( new Buff(Stats.ManaRegen, -10.0f, duration ) );
			applyBuff( new Buff(Stats.Dexterity, -dexterity*0.5f, duration) );			
			
		}
	}

	private void specialBarbarian() {		
		System.out.println("Activating "+creatureType+" special ability...phase2");
		float manaCost = -50;
	
		if (modifyMana(manaCost)) {		
		
			int level = getComponent(ExperienceComponent.class).getLevel();
			float duration = 6+0.5f*level;
			barbarianSpecialBarTimer = duration;
			barbarianSpecialBar = new HudBarElement(new Vector2(224,55), new Texture(1, 1, Format.RGB565), 
													new Vector2(0,0), new Vector2(64,16), duration, new Color(0.7f,0.1f,0.1f,1f), false);
			
			GameConfiguration.hud.addElement(barbarianSpecialBar);
			
			applyBuff( new Buff(Stats.Strength, (9f+level), duration ) );
			applyBuff( new Buff(Stats.HealthRegen, 0.02f, duration) );
			applyBuff( new Buff(Stats.StaminaRegen, 1.0f, duration) );
			
			Animation.play(this, AnimationState.Rage);
			
			specialActive = true;
			specialTimer = duration;
		}			
	}
	
	
}
