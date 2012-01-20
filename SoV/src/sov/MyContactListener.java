package sov;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class MyContactListener implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		
		Object fixtureUserData = contact.getFixtureA().getBody().getUserData();
		//Object fixtureUserData2 = contact.getFixtureB().getBody().getUserData();
		
		
		if(fixtureUserData != null) {
			if(fixtureUserData.getClass() == Player.class ) {
				// Test to make sure the collision is actually coming from the bottom
				// TODO: Support different angles (ninja might be able to jump from walls)
				if(contact.getWorldManifold().getNormal().y > 0) {
					((Player)fixtureUserData).setAllowJumping(true);
				}
				
			}
			
			if(fixtureUserData.getClass() == Player.class) {
				
			}
		}
		
		
		fixtureUserData = contact.getFixtureB().getBody().getUserData();
		
		if(fixtureUserData != null && fixtureUserData.getClass() == Player.class) {
			// Test to make sure the collision is actually coming from the bottom
			// TODO: Support different angles (ninja might be able to jump from walls)
			if(contact.getWorldManifold().getNormal().y > 0) {
				((Player)fixtureUserData).setAllowJumping(true);
			}
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
