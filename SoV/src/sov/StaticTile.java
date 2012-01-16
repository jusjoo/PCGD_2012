package sov;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class StaticTile {
	Sprite sprite;
	Body body;
	BodyDef bodyDef;
	PolygonShape bodyShape;
	
	public static enum Shape { SQUARE, LEFT, RIGHT }
	
	protected static final int PIXELS_PER_METER = 32;
	
	public StaticTile(World world, float x, float y, Shape shape) {
		bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.StaticBody;
		bodyDef.position.set(x/2, y/2);
		
		body = world.createBody(bodyDef);
		
		PolygonShape bodyShape = new PolygonShape();

		if(shape==Shape.SQUARE) {
			bodyShape.setAsBox(16f / (2 * PIXELS_PER_METER), 16f / (2 * PIXELS_PER_METER));
		} else if(shape==Shape.LEFT) {
			Vector2[] shapePolygon = new Vector2[3];
			float shapeSize = 16f;
			shapePolygon[0] = new Vector2(-shapeSize / PIXELS_PER_METER/2,-shapeSize / PIXELS_PER_METER/2);
			shapePolygon[1] = new Vector2(shapeSize / PIXELS_PER_METER/2,-shapeSize / PIXELS_PER_METER/2);
			shapePolygon[2] = new Vector2(shapeSize / PIXELS_PER_METER/2,shapeSize / PIXELS_PER_METER/2);
			
			bodyShape.set(shapePolygon);
		} else if(shape==Shape.RIGHT) {
			Vector2[] shapePolygon = new Vector2[3];
			float shapeSize = 16f;
			/*
			// WORKS, JUST UPSIDE DOWN
			shapePolygon[0] = new Vector2(shapeSize / PIXELS_PER_METER/2,-shapeSize / PIXELS_PER_METER/2);
			shapePolygon[1] = new Vector2(shapeSize / PIXELS_PER_METER/2,shapeSize / PIXELS_PER_METER/2);
			shapePolygon[2] = new Vector2(-shapeSize / PIXELS_PER_METER/2,shapeSize / PIXELS_PER_METER/2);
			*/
			shapePolygon[0] = new Vector2(-shapeSize / PIXELS_PER_METER/2,-shapeSize / PIXELS_PER_METER/2);
			shapePolygon[1] = new Vector2(shapeSize / PIXELS_PER_METER/2,-shapeSize / PIXELS_PER_METER/2);
			shapePolygon[2] = new Vector2(-shapeSize / PIXELS_PER_METER/2,shapeSize / PIXELS_PER_METER/2);
			
			//shapePolygon[3] = new Vector2(0,0);
			
			bodyShape.set(shapePolygon);
		}
		
		FixtureDef bodyFixtureDef = new FixtureDef();
		bodyFixtureDef.shape = bodyShape;
		bodyFixtureDef.friction = 0.02f;		

		body.createFixture(bodyFixtureDef);
		
		bodyShape.dispose();
 
		//body.setLinearVelocity(new Vector2(0.0f, 0.0f));
	}
	
	public Vector2 getPosition() {
		return body.getPosition();
	}
	
}
