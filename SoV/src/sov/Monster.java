package sov;

import java.util.HashMap;

import sov.SpriteComponent.AnimationState;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Monster extends Creature {
	
	BodyComponent entityToFollow = null;

	public Monster(Vector2 size,
			HashMap<AnimationState, Animation> animations, float rounding,
			boolean circle) {
		super(size, animations, rounding, circle);
		// TODO Auto-generated constructor stub
	}
	
	public void setToFollow(BodyComponent entity) {
		//float PPM = GameConfiguration.PIXELS_PER_METER;
		entityToFollow = entity;
	}
	
	@Override
	public void update(float deltaTime) {
		
		if(alive) {
			if(entityToFollow != null) {
				if(getBodyComponent().getPosition().x < entityToFollow.getPosition().x) {
					getBodyComponent().body.applyLinearImpulse(new Vector2(speed, 0), getBodyComponent().body.getWorldCenter());
					facingRight = true;
				} else {
					getBodyComponent().body.applyLinearImpulse(new Vector2(-speed, 0), getBodyComponent().body.getWorldCenter());
					facingRight = false;
				}
			}
		}
		
		
		super.update(deltaTime);
		
		
	}

}
