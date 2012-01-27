package sov;

import java.util.HashMap;

import sov.SpriteComponent.AnimationState;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Player extends Creature {

	//protected enum CharacterClass { Barbarian, Sorceress, Ninja };
	
	protected GameConfiguration config = new GameConfiguration();
	
	protected float attackImpulse;
	
	//protected CharacterClass characterClass;

	// Deliver size and position of the player in pixels.
	public Player(Vector2 size, HashMap<AnimationState, Animation> animations, float rounding,
			boolean circle) {
		super(size, animations, rounding,
				circle);
		this.attackImpulse = config.attackImpulse;
	}
	
	//@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		takeInput();
	}
	
	protected void takeInput() {
		
		

	}

}
