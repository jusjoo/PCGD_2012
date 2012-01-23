package sov;

import java.util.HashMap;

import sov.AnimatedSprite.AnimationState;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Monster extends Creature {
	
	BodyEntity entityToFollow = null;

	public Monster(Vector2 size,
			HashMap<AnimationState, Animation> animations, float rounding,
			boolean circle) {
		super(size, animations, rounding, circle);
		// TODO Auto-generated constructor stub
	}
	
	public void setToFollow(BodyEntity entity) {
		//float PPM = GameConfiguration.PIXELS_PER_METER;
		entityToFollow = entity;
	}
	
	@Override
	public void update(float deltaTime) {
		
		if(alive) {
			if(entityToFollow != null) {
				if(body.getPosition().x < entityToFollow.body.getPosition().x) {
					body.applyLinearImpulse(new Vector2(speed, 0), body.getWorldCenter());
					facingRight = true;
				} else {
					body.applyLinearImpulse(new Vector2(-speed, 0), body.getWorldCenter());
					facingRight = false;
				}
			}
		}
		
		
		super.update(deltaTime);
		
		
	}

}
