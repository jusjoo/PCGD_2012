package sov;

import java.util.HashMap;

import sov.BodyComponent.SlopeShape;
import sov.DynamicObjectFactory.AnimationType;
import sov.SpriteComponent.AnimationState;



import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.IdentityMap.Entry;
import com.google.gson.annotations.Expose;

public class Collectible extends Entity {

	@Expose CollectibleType type;
	@Expose AnimationType animations;
	@Expose String soundFile;
	@Expose	HashMap<CollectibleEffect, Float> effects;
	@Expose Float size;

	Sound sound;
	BodyComponent body;
	SpriteComponent sprites;
	
	boolean setToDestroy = false;
	
	public enum CollectibleType {RejuvenationPotion, HealthPotion, ManaPotion, ExpBall, GoldCoin, BigDiamond}
	public enum CollectibleEffect {heal, giveMana, giveExp, giveScore}
	
	public Collectible(CollectibleType type, AnimationType animations, Sound sound, HashMap<CollectibleEffect, Float> effects, float size) {
		this.type = type;
		this.animations = animations;
		this.sound = sound;
		this.effects = effects;
		this.size = size;
	}
	
	public Collectible() {
	}
	
	public static Collectible fromPrototype(Collectible prototype) {
		
		System.out.println(prototype.type);
		
		Collectible collectible = new Collectible(
				prototype.type, 
				prototype.animations,
				prototype.sound, 
				prototype.effects, 
				prototype.size);
				
		return collectible;

	}
	
	public void createComponents(HashMap<AnimationState, Animation> animations) {
		
		body = new BodyComponent(this, new Vector2(size,size), false, 1.0f, true, SlopeShape.Even, false);
		this.addComponent(body);
		
		sprites = new SpriteComponent(this, animations);
		this.addComponent(sprites);
		
		AudioComponent audio = new AudioComponent(this);
		audio.addSound(AnimationState.Die, sound);
		this.addComponent(audio);
		
		
		
		
	}
	
	public void createAudioComponent(Sound sound) {
		this.sound = sound;
		
		
	}

	public void addToWorld(World world, Vector2 coordinates) {
		
		body.addToWorld(world, coordinates);
		
		body.setUserData(new ContactEvent(this, "collectible"));
		// TODO Auto-generated method stub
	}
	
	public void consumeBy(Creature creature) {
		for(java.util.Map.Entry<CollectibleEffect, Float> entry : effects.entrySet()) {
			switch (entry.getKey()) {
			case heal: 		creature.body.heal(entry.getValue());
							System.out.println("Heal " + entry.getValue());
							break;
			case giveMana: 	creature.modifyMana(entry.getValue());
							System.out.println("Mana " + entry.getValue());
							break;
			case giveExp: 	creature.getComponent(ExperienceComponent.class).giveExperience((int)Math.floor(entry.getValue()));
							System.out.println("Exp" + entry.getValue());
							break;
			case giveScore: creature.getComponent(ExperienceComponent.class).giveScore((int)Math.floor(entry.getValue()));
							System.out.println("Score" + entry.getValue());
			}
		}
		
		body.setToDie = true;
	}

	/*
	 * FIXME: Render ignores angle for some reason.
	 */
	public void render(SpriteBatch spriteBatch) {
		sprites.render(spriteBatch, true, 
				body.getPosition().x, body.getPosition().y,
				body.getAngle(), body.getSize());
		
	}

	public void update(float deltaTime) {
		super.update(deltaTime);
		
		if (body.setToDie) {
			//body.removeFromWorld();
			if (sprites.isAtLastFrame()) {
				this.setToDestroy = true;
			}
		}
		
		
	}
	
}
