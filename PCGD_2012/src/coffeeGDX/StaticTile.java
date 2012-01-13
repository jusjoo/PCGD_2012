package coffeeGDX;

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
	
	protected static final int PIXELS_PER_METER = 32;
	
	public StaticTile(World world, float x, float y) {
		bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.StaticBody;
		bodyDef.position.set(x/2, y/2);
		
		body = world.createBody(bodyDef);
		
		PolygonShape bodyShape = new PolygonShape();

		bodyShape.setAsBox(16f / (2 * PIXELS_PER_METER), 16f / (2 * PIXELS_PER_METER));
		
		FixtureDef bodyFixtureDef = new FixtureDef();
		bodyFixtureDef.shape = bodyShape;
		bodyFixtureDef.friction = 0.5f;		

		body.createFixture(bodyFixtureDef);
		
		bodyShape.dispose();
 
		//body.setLinearVelocity(new Vector2(0.0f, 0.0f));
	}
	
	public Vector2 getPosition() {
		return body.getPosition();
	}
	
}
