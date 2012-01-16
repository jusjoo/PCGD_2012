package sov;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Creature extends MovingSprite {
	
	public Creature(TextureRegion textureRegion, World world, Vector2 position, Vector2 size) {
		super(textureRegion, world, position, size);
		
		//Vector2 sensorsize = new Vector2(9, 14);
		
		PolygonShape sensorShape = new PolygonShape();
		//sensorShape.setAsBox(size.x / (4 * PIXELS_PER_METER), size.y / (6 * PIXELS_PER_METER));
		sensorShape.setAsBox(size.x / (3 * PIXELS_PER_METER), size.y / (12 * PIXELS_PER_METER),
				new Vector2(0, -size.y/PIXELS_PER_METER/2), 0);
		FixtureDef sensorFixture = new FixtureDef();
		sensorFixture.shape = sensorShape;
		sensorFixture.isSensor = true;
		sensorFixture.density = 0;
		
		body.createFixture(sensorFixture);
		
		body.setUserData(this);
		
	}

}
