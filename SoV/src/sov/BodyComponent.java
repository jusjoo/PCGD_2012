package sov;

import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class BodyComponent extends Component {
	// A Box2D body for collision
	Body body;
	BodyDef bodyDef;
	Fixture bodyFixture;
	FixtureDef bodyFixtureDef;
	
	// SlopeShape determines which direction the slope comes from. (If there is a slope)
	// TODO: Change the coordinate system to NorthWest, NorthEast, SouthEast, SouthWest,
	// so that we can have slopes under the tiles as well. (sloped ceilings, etc)
	public static enum SlopeShape { Even, Left, Right }
	
	// The maximum velocity the entity can have, before it speed is limited
	protected float maxVelocity;
	
	// Size of the entity in pixels
	protected Vector2 size;
	
	// Use staticBody for everything that doesn't move.
	public BodyComponent(Object parent, Vector2 size,
			boolean staticBody, float rounding, boolean circle, SlopeShape slopeShape, boolean sensorEntity) {	
		super(parent);
		
		float PIXELS_PER_METER = GameConfiguration.PIXELS_PER_METER;
		
		bodyDef = new BodyDef();
		if(staticBody) {
			bodyDef.type = BodyDef.BodyType.StaticBody;
		} else {
			bodyDef.type = BodyDef.BodyType.DynamicBody;
		}


		//bodyDef.position.set( position.x/PIXELS_PER_METER , position.y/PIXELS_PER_METER );

		
		this.size = size;
		
		
		
		bodyFixtureDef = new FixtureDef();
		
		if(circle) {
			CircleShape circleShape = new CircleShape();
			circleShape.setRadius(size.x / PIXELS_PER_METER/2);
			bodyFixtureDef.shape = circleShape;
		} else {
			PolygonShape polygonShape = new PolygonShape();
			if(slopeShape == SlopeShape.Even) {
				if(rounding < 1) {
					
					
					Vector2[] shape = new Vector2[8];
					
					shape[0] = new Vector2(-size.x / PIXELS_PER_METER/2,	-size.y*rounding / PIXELS_PER_METER/2);
					shape[1] = new Vector2(-size.x*rounding / PIXELS_PER_METER/2,	-size.y / PIXELS_PER_METER/2);
					shape[2] = new Vector2(size.x*rounding / PIXELS_PER_METER/2,	-size.y / PIXELS_PER_METER/2);
					shape[3] = new Vector2(size.x / PIXELS_PER_METER/2,	-size.y*rounding / PIXELS_PER_METER/2);
					shape[4] = new Vector2(size.x / PIXELS_PER_METER/2,	size.y*rounding / PIXELS_PER_METER/2);
					shape[5] = new Vector2(size.x*rounding / PIXELS_PER_METER/2,	size.y / PIXELS_PER_METER/2);
					shape[6] = new Vector2(-size.x*rounding / PIXELS_PER_METER/2,	size.y / PIXELS_PER_METER/2);
					shape[7] = new Vector2(-size.x / PIXELS_PER_METER/2,	size.y*rounding / PIXELS_PER_METER/2);
					
					polygonShape.set(shape);
				} else {
					polygonShape.setAsBox(size.x / (2 * PIXELS_PER_METER), size.y / (2 * PIXELS_PER_METER));
				}
			} else if(slopeShape==SlopeShape.Left) {
				Vector2[] shapePolygon = new Vector2[3];
				float shapeSize = 16f;
				shapePolygon[0] = new Vector2(-shapeSize / PIXELS_PER_METER/2,-shapeSize / PIXELS_PER_METER/2);
				shapePolygon[1] = new Vector2(shapeSize / PIXELS_PER_METER/2,-shapeSize / PIXELS_PER_METER/2);
				shapePolygon[2] = new Vector2(shapeSize / PIXELS_PER_METER/2,shapeSize / PIXELS_PER_METER/2);
				
				polygonShape.set(shapePolygon);
			} else if(slopeShape==SlopeShape.Right) {
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
				
				polygonShape.set(shapePolygon);	
			} else {
				
			}
			
			bodyFixtureDef.shape = polygonShape;
			
		}
		
		
		
	}
	
	// Return position in pixels
	public Vector2 getPosition() {
		return new Vector2(body.getPosition().x * GameConfiguration.PIXELS_PER_METER,
				body.getPosition().y * GameConfiguration.PIXELS_PER_METER);				
	}
	
	public float getMaxVelocity() {
		return maxVelocity;
	}
	
	public void setMaxVelocity(float maxVelocity) {
		this.maxVelocity = maxVelocity;
	}
	
	public Vector2 getLinearVelocity() {
		return body.getLinearVelocity();
	}
	
	public void setLinearVelocity(float x, float y) {
		body.setLinearVelocity(x, y);
	}
	
	public float getAngle() {
		return body.getAngle();
	}
	
	public Vector2 getSize() {
		return size;
	}
	
	public Object getParent() {
		return parent;
	}
	
	public void die() {
		body.destroyFixture(bodyFixture);
		body.getFixtureList().clear();
		body.setGravityScale(0);
		body.setLinearVelocity(0f, 0f);
	}
	
	public void setPosition(Vector2 coordinates) {
		body.setTransform(coordinates.mul(1/GameConfiguration.PIXELS_PER_METER), 0);
	}
	
	public void addToWorld(World world, Vector2 position) {
		float PIXELS_PER_METER = GameConfiguration.PIXELS_PER_METER;
		bodyDef.position.set( position.x/PIXELS_PER_METER , position.y/PIXELS_PER_METER );
		
		body = world.createBody(bodyDef);
		
		bodyFixtureDef.density = 3.5f;
		bodyFixtureDef.friction = 0.02f;
		bodyFixture = body.createFixture(bodyFixtureDef);
		
		
		
		// Linear damping dictates along with friction how quickly the entity is slowed down.
		body.setLinearDamping(2.6f);
		
		// Set userdata for body, used to find out which object is touching the ground in MyContactListener
		body.setUserData(this.parent);
	}
	
	public void applyLinearImpulse(Vector2 impulse) {
		body.applyLinearImpulse(impulse, body.getWorldCenter());
	}
	
	public void createFixture(FixtureDef fixtureDef) {
		body.createFixture(fixtureDef);
	}

	@Override
	public void update(float deltaTime) {		
	}
}
