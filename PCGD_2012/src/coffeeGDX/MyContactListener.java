package coffeeGDX;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class MyContactListener implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		
		Object fixtureUserData = contact.getFixtureA().getBody().getUserData();
		
		if(fixtureUserData != null && fixtureUserData.getClass() == Player.class) {
			((Player)fixtureUserData).setAllowJumping(true);
			
		}
		
		fixtureUserData = contact.getFixtureB().getBody().getUserData();
		
		if(fixtureUserData != null && fixtureUserData.getClass() == Player.class) {
			((Player)fixtureUserData).setAllowJumping(true);
		}

	}

	@Override
	public void endContact(Contact contact) {
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {

	}

}
