package sov;

import sov.AIComponent.AIstate;

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
	World world;
	
	// SlopeShape determines which direction the slope comes from. (If there is a slope)
	// TODO: Change the coordinate system to NorthWest, NorthEast, SouthEast, SouthWest,
	// so that we can have slopes under the tiles as well. (sloped ceilings, etc)
	public static enum SlopeShape { Even, Left, Right }
	
	// The maximum velocity the entity can have, before it speed is limited
	protected float maxVelocity;
	
	// Size of the entity in pixels
	protected Vector2 size;
	
	boolean facingRight = true;
	float rounding;
	protected float hitPointsMax=1;
	protected float hitPoints = 1;		//default hitpoints
	protected boolean indestructible;
	boolean alive = true;
	boolean finalDeath = false;
	
	BarElement healthBar;
	float healthBarTimer;
	
	// tracks incoming damage
	protected float setToTakeDamage;
	// makes body immune to damage after being attacked
	protected float immuneTimer;
	
	boolean setToDie = false;
	
	// Use staticBody for everything that doesn't move.
	public BodyComponent(Entity parent, Vector2 size,
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
		this.rounding = rounding;
		
		
		bodyFixtureDef = new FixtureDef();
		bodyFixtureDef.isSensor = sensorEntity;
		
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
	
	private void takeDamage(float damage) {
		addHealthBar();
		if (immuneTimer <= 0) {
			hitPoints -= damage;
			if(hitPoints <= 0) {
				setToDie = true;
			}
			setToTakeDamage = 0;
			immuneTimer = GameConfiguration.immuneTime;
			 if (parent.hasComponent(AIComponent.class)) {
				 parent.getComponent(AIComponent.class).addAIstate(AIstate.Alarmed);
			 }
			System.out.println("Health "+hitPoints+"/"+hitPointsMax+"(-"+damage+")");
		}
		
	}
	
	protected BodyComponent setIndestructible(boolean indestructible) {
		this.indestructible = indestructible;
		return this;
	}
	
	protected boolean isIndestructible() {
		return indestructible;
	}
	
	protected void die() {
		if(alive) {
			//body.destroyFixture(body.getFixtureList().get(0));
			//body.getFixtureList().clear();
			
			parent.getComponent(SpriteComponent.class).setCurrentAnimationState(SpriteComponent.AnimationState.Die);
			alive = false;
			
			//TODO: siirrä nämä Entityyn!
			
			if(parent.getComponent(MovementComponent.class) != null) {
				//parent.removeComponent(InputComponent.class);
				parent.setComponentActive(InputComponent.class, false);
				parent.setComponentActive(MovementComponent.class, false);
				//parent.removeComponent(MovementComponent.class);
				if (parent.getComponent(AIComponent.class) != null) {
					parent.setComponentActive(AIComponent.class, false);
				}
				if (parent.getComponent(PlayerInputComponent.class) != null) {
					parent.setComponentActive(PlayerInputComponent.class, false);
				}
				
			}
		}
		
	}
	public void setToTakeDamage(float damage) {
		setToTakeDamage += damage;
		
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
	
	public void setFacingRight(boolean facingRight) {
		this.facingRight = facingRight;
	}
	
	public boolean getFacingRight() {
		return facingRight;
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
	
	
	
	public void setPosition(Vector2 coordinates) {
		body.setTransform(coordinates.mul(1/GameConfiguration.PIXELS_PER_METER), 0);
	}
	
	// FIXME: If there are any problems, it should really be world.destroyBody(body);
	public void removeFromWorld() {
		if(body != null && bodyFixture != null) {
			body.destroyFixture(bodyFixture);
			//world.destroyBody(body);
		}
	}
	
	public void addToWorld(World world, Vector2 position) {
		float PIXELS_PER_METER = GameConfiguration.PIXELS_PER_METER;
		bodyDef.position.set( position.x/PIXELS_PER_METER , position.y/PIXELS_PER_METER );
		
		this.world = world;
		
		body = world.createBody(bodyDef);
		
		bodyFixtureDef.density = 3.5f;
		bodyFixtureDef.friction = 0.02f;
		
		bodyFixture = body.createFixture(bodyFixtureDef);
		
		
		
		// Linear damping dictates along with friction how quickly the entity is slowed down.
		if (!bodyFixtureDef.isSensor) body.setLinearDamping(2.6f);
		// No linear damping for sensor bodies.
		else body.setLinearDamping(0.0f);
		
		
		// Set userdata for body, used to find out which object is touching the ground in MyContactListener
		setUserData(new ContactEvent(this, "body"));
	}
	
	public void applyLinearImpulse(Vector2 impulse) {
		body.applyLinearImpulse(impulse, body.getWorldCenter());
	}
	
	public void createFixture(FixtureDef fixtureDef) {
		body.createFixture(fixtureDef);
	}

	@Override
	public void update(float deltaTime) {		
		
		if (immuneTimer > 0) immuneTimer -= deltaTime;
		
		
		if (setToTakeDamage > 0 && immuneTimer <= 0) {
			takeDamage(setToTakeDamage);
		}
		if(setToDie) {
			die();
			if(!finalDeath && Math.abs(body.getLinearVelocity().x) < 0.5f && Math.abs(body.getLinearVelocity().y) < 0.5f) {
				body.destroyFixture(bodyFixture);
				body.getFixtureList().clear();
				body.setGravityScale(0);
				body.setLinearVelocity(0f, 0f);
				finalDeath = true;
			}
		}
		if(healthBar != null) {
			healthBar.setCurrentValue(getHitPoints());
			healthBarTimer -= deltaTime;
			if (healthBarTimer < 0) {
				removeHealthBar();
			}
		}
	}


	public void setUserData(ContactEvent contact) {
		body.getFixtureList().get(0).setUserData(contact);
	}
	public void setHitPoints(float hitPoints) {
		this.hitPointsMax = hitPoints;
	}
	public float getHitPoints() {
		return this.hitPoints;
	}
	public void heal(float healAmount) {
		
		if (hitPoints+healAmount <= hitPointsMax) {
			hitPoints+= healAmount;
		}
		else hitPoints = hitPointsMax;		
	}
	public void addHealthBar() {
		healthBar = new BarElement(new Vector2(0,-this.getSize().y/2), new Vector2(16,2), hitPointsMax);
		healthBarTimer = 3f;
	}
	
	public void removeHealthBar() {
		healthBar = null;
	}
}
