package coffeeGDX;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class MovingSprite {
	Sprite sprite;
	Body body;
	
	Body bottomSensor;
	boolean allowJumping = true;
	
	//ArrayList<Animation> animations = new ArrayList<Animation>();
	
	enum AnimationState { IDLE, RUN, JUMP, HURT } 
	float stateTime = 0;
	AnimationState currentAnimationState = AnimationState.IDLE;
	
	
	HashMap<AnimationState, Animation> animations = new HashMap<AnimationState, Animation>();
	
	protected static final int PIXELS_PER_METER = 32;
	
	protected final float maxVelocity = 3.0f;
	
	public MovingSprite(TextureRegion textureRegion, World world, Vector2 position) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(position.x/2, position.y/2);
		//bodyDef.position.set(Gdx.graphics.getWidth() / PIXELS_PER_METER /2, Gdx.graphics.getHeight() / PIXELS_PER_METER /2);
		
		body = world.createBody(bodyDef);
		
		//PolygonShape bodyShape = new PolygonShape();
		
		CircleShape bodyShape = new CircleShape();
		bodyShape.setRadius(0.2f);

		Vector2 size = new Vector2(9, 14);
		
		/*Vector2[] shape = new Vector2[4];
		float shapeSize = 16f;
		shape[0] = new Vector2(0,0);
		shape[1] = new Vector2(shapeSize * PIXELS_PER_METER,0);
		shape[2] = new Vector2(shapeSize * PIXELS_PER_METER,shapeSize * PIXELS_PER_METER);
		shape[3] = new Vector2(0,shapeSize * PIXELS_PER_METER);*/
		
		//bodyShape.set(shape);
		
		//bodyShape.setAsBox(size.x / (2 * PIXELS_PER_METER), size.y / (2 * PIXELS_PER_METER));
		
		FixtureDef bodyFixtureDef = new FixtureDef();
		bodyFixtureDef.shape = bodyShape;
		bodyFixtureDef.density = 2.0f;
		bodyFixtureDef.friction = 0.05f;
		
		
		
		body.setFixedRotation(true);

		body.createFixture(bodyFixtureDef);
		
		//body.createFixture(bodyShape, 70);
		bodyShape.dispose();
 
		body.setLinearVelocity(new Vector2(0.0f, 0.0f));
		//body.setLinearDamping(5.0f);
		body.setLinearDamping(2.0f);
		
		
		PolygonShape sensorShape = new PolygonShape();
		//sensorShape.setAsBox(size.x / (4 * PIXELS_PER_METER), size.y / (6 * PIXELS_PER_METER));
		sensorShape.setAsBox(size.x / (4 * PIXELS_PER_METER), size.y / (6 * PIXELS_PER_METER),
				new Vector2(0, -size.y/PIXELS_PER_METER/2), 0);
		FixtureDef sensorFixture = new FixtureDef();
		sensorFixture.shape = sensorShape;
		sensorFixture.isSensor = true;
		sensorFixture.density = 0;
		
		body.createFixture(sensorFixture);
		
		body.setUserData(this);
		
		TextureRegion frames[][] = textureRegion.split(16, 16);
		
		ArrayList<TextureRegion> idle = new ArrayList<TextureRegion>();
		idle.add(frames[0][0]);
		
		ArrayList<TextureRegion> run = new ArrayList<TextureRegion>();
		for(int i=1; i<4; i++) {
			run.add(frames[0][i]);
		}
		
		ArrayList<TextureRegion> jump = new ArrayList<TextureRegion>();
		jump.add(frames[0][4]);
		
		animations.put(AnimationState.IDLE, new Animation(1f, idle));
		animations.put(AnimationState.RUN, new Animation(0.1f, run));
		animations.put(AnimationState.JUMP, new Animation(1f, jump));
		
		sprite = new Sprite(animations.get(currentAnimationState).getKeyFrame(0, true));
		
	}
	
	public void render(SpriteBatch spriteBatch) {
		/*sprite.setPosition(	PIXELS_PER_METER * body.getPosition().x - sprite.getWidth() / 2,
							PIXELS_PER_METER * body.getPosition().y - sprite.getHeight() / 2);*/
		
		//sprite = animations.get(0).
		
		sprite.setPosition(body.getPosition().x * PIXELS_PER_METER,
				body.getPosition().y * PIXELS_PER_METER - sprite.getHeight());
		
		sprite.draw(spriteBatch);
	}
	
	public Vector2 getPosition() {
		//return body.getPosition();
		//return new Vector2(sprite.getX()+sprite.getWidth(), sprite.getY()+sprite.getHeight()/2);
		return new Vector2(sprite.getX(), sprite.getY());
		
	}
	
	public void update(float deltaTime) {
		/*if(body.getPosition().y < 0) {
			body.setLinearVelocity(body.getLinearVelocity().x,0);
			body.setTransform(body.getPosition().x, 0, 0);
		}*/
		
		//body.getFixtureList().get(1).
		
		stateTime += deltaTime;
		
		sprite.scroll(5, 0);
		
		sprite.setRegion(animations.get(currentAnimationState).getKeyFrame(stateTime, true));
		
		Vector2 currentVelocity = body.getLinearVelocity();
		
		if(Math.abs(currentVelocity.x) > 0.1f && Math.abs(currentVelocity.y) < 0.5f) {
			currentAnimationState = AnimationState.RUN;
		} else if (Math.abs(currentVelocity.y) >= 0.5f) {
			currentAnimationState = AnimationState.JUMP;
		} else {
			currentAnimationState = AnimationState.IDLE;
		}
		
		if(currentVelocity.x >= 0) {
			sprite.flip(false, false);
		} else {
			sprite.flip(true, false);
		}
			
		
		if(currentVelocity.x > maxVelocity) {
			body.setLinearVelocity(maxVelocity, body.getLinearVelocity().y);
		}
		else if(currentVelocity.x < -maxVelocity) {
			body.setLinearVelocity(-maxVelocity, body.getLinearVelocity().y);
		}
		
	}
	
	public void setAllowJumping(boolean allowJumping) {
		this.allowJumping = allowJumping;
	}
}
