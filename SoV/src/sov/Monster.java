package sov;

import java.util.HashMap;

import sov.AnimatedSprite.AnimationState;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Monster extends Creature {

	public Monster(World world, Vector2 position, Vector2 size,
			HashMap<AnimationState, Animation> animations, float rounding,
			boolean circle) {
		super(world, position, size, animations, rounding, circle);
		// TODO Auto-generated constructor stub
	}

}
