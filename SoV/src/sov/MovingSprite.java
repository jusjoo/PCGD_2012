package sov;

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

public class MovingSprite extends AnimatedSprite {
	Sprite sprite; // Picture for the creature
	Body body; // A Box2D body for collision
	
	Body bottomSensor; // A sensor for the "feet" of the creature
	boolean allowJumping = true;
	
	
	float stateTime = 0;
	
	
	
	
	
	protected static final int PIXELS_PER_METER = 32;
	
	protected final float maxVelocity = 3.0f;
	
	public MovingSprite(TextureRegion textureRegion, World world, Vector2 position, Vector2 size) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(position.x/2, position.y/2);
		//bodyDef.position.set(Gdx.graphics.getWidth() / PIXELS_PER_METER /2, Gdx.graphics.getHeight() / PIXELS_PER_METER /2);
		
		body = world.createBody(bodyDef);
		
		
		
		//CircleShape bodyShape = new CircleShape();
		//bodyShape.setRadius(0.2f);
		
		//new Vector2(-shapeSize / PIXELS_PER_METER/2,-shapeSize / PIXELS_PER_METER/2);
		//new Vector2(shapeSize / PIXELS_PER_METER/2,-shapeSize / PIXELS_PER_METER/2);
		//new Vector2(shapeSize / PIXELS_PER_METER/2,shapeSize / PIXELS_PER_METER/2);
		
		/*
		// Working Quad
		shape[0] = new Vector2(-shapeSize / PIXELS_PER_METER/2,	-shapeSize / PIXELS_PER_METER/2);
		shape[1] = new Vector2(shapeSize / PIXELS_PER_METER/2,	-shapeSize / PIXELS_PER_METER/2);
		shape[2] = new Vector2(shapeSize / PIXELS_PER_METER/2,	shapeSize / PIXELS_PER_METER/2);
		shape[3] = new Vector2(-shapeSize / PIXELS_PER_METER/2,	shapeSize / PIXELS_PER_METER/2);
		*/

		PolygonShape bodyShape = new PolygonShape();
		
		Vector2[] shape = new Vector2[8];
		//float shapeSize = 13f;
		float rounding = 0.8f;
		shape[0] = new Vector2(-size.x / PIXELS_PER_METER/2,	-size.y*rounding / PIXELS_PER_METER/2);
		shape[1] = new Vector2(-size.x*rounding / PIXELS_PER_METER/2,	-size.y / PIXELS_PER_METER/2);
		shape[2] = new Vector2(size.x*rounding / PIXELS_PER_METER/2,	-size.y / PIXELS_PER_METER/2);
		shape[3] = new Vector2(size.x / PIXELS_PER_METER/2,	-size.y*rounding / PIXELS_PER_METER/2);
		shape[4] = new Vector2(size.x / PIXELS_PER_METER/2,	size.y*rounding / PIXELS_PER_METER/2);
		shape[5] = new Vector2(size.x*rounding / PIXELS_PER_METER/2,	size.y / PIXELS_PER_METER/2);
		shape[6] = new Vector2(-size.x*rounding / PIXELS_PER_METER/2,	size.y / PIXELS_PER_METER/2);
		shape[7] = new Vector2(-size.x / PIXELS_PER_METER/2,	size.y*rounding / PIXELS_PER_METER/2);
		
		bodyShape.set(shape);
		
		//bodyShape.setAsBox(size.x / (2 * PIXELS_PER_METER), size.y / (2 * PIXELS_PER_METER));
		
		FixtureDef bodyFixtureDef = new FixtureDef();
		bodyFixtureDef.shape = bodyShape;
		bodyFixtureDef.density = 3.5f;
		bodyFixtureDef.friction = 0.02f;
		
		
		
		//body.setFixedRotation(true);

		body.createFixture(bodyFixtureDef);
		
		//body.createFixture(bodyShape, 70);
		bodyShape.dispose();
 
		body.setLinearVelocity(new Vector2(0.0f, 0.0f));
		//body.setLinearDamping(5.0f);
		body.setLinearDamping(2.6f);
		
		
		
		
		TextureRegion frames[][] = textureRegion.split(16, 16);
		
		/*ArrayList<TextureRegion> idle = new ArrayList<TextureRegion>();
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
		*/
		
		ArrayList<TextureRegion> idle = new ArrayList<TextureRegion>();
		idle.add(frames[0][0]);
		animations.put(AnimationState.IDLE, new Animation(1f, idle));
		animations.put(AnimationState.RUN, new Animation(0.1f, idle));
		animations.put(AnimationState.JUMP, new Animation(1f, idle));
		
		
		
		sprite = new Sprite(animations.get(currentAnimationState).getKeyFrame(0, true));
		
	}
	
	public void render(SpriteBatch spriteBatch) {
		/*sprite.setPosition(	PIXELS_PER_METER * body.getPosition().x - sprite.getWidth() / 2,
							PIXELS_PER_METER * body.getPosition().y - sprite.getHeight() / 2);*/
		
		//sprite = animations.get(0).
		
		sprite.setPosition(body.getPosition().x * PIXELS_PER_METER,
				body.getPosition().y * PIXELS_PER_METER - sprite.getHeight());
		
		sprite.setRotation((float) (body.getAngle()*180/Math.PI));
		
		sprite.draw(spriteBatch);
	}
	
	public Vector2 getPosition() {
		//return body.getPosition();
		//return new Vector2(sprite.getX()+sprite.getWidth(), sprite.getY()+sprite.getHeight()/2);
		return new Vector2(sprite.getX(), sprite.getY());
		//return new Vector2(body.getPosition().x, body.getPosition().y);
		
	}
	
	public void update(float deltaTime) {
		/*if(body.getPosition().y < 0) {
			body.setLinearVelocity(body.getLinearVelocity().x,0);
			body.setTransform(body.getPosition().x, 0, 0);
		}*/
		
		//body.getFixtureList().get(1).
		
		stateTime += deltaTime;
		
		sprite.setRegion(animations.get(currentAnimationState).getKeyFrame(stateTime, true));
		
		Vector2 currentVelocity = body.getLinearVelocity();
		
		if(Math.abs(currentVelocity.x) > 0.1f && allowJumping) {
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
