package sov;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class MyContactListener implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		
		Object fixtureUserData = contact.getFixtureA().getBody().getUserData();
		Object fixtureUserData2 = contact.getFixtureB().getBody().getUserData();
		
		if(fixtureUserData != null && fixtureUserData2 != null) {
			if(fixtureUserData.getClass() == Player.class) {
				// Test to make sure the collision is actually coming from the bottom
				// TODO: Support different angles (ninja might be able to jump from walls)
				if(contact.getWorldManifold().getNormal().y > 0) {
					((Player)fixtureUserData).setAllowJumping(true);
				}
				
			}
			
			if(Creature.class.isAssignableFrom(fixtureUserData.getClass()) && Creature.class.isAssignableFrom(fixtureUserData2.getClass())) {
				if(contact.getFixtureB() == ((Creature)fixtureUserData2).getAttackFixture()) {
					((AnimatedSpriteBody)fixtureUserData).takeDamage(1);
					
				}
			}
		}
		
		
		fixtureUserData = contact.getFixtureB().getBody().getUserData();
		fixtureUserData2 = contact.getFixtureA().getBody().getUserData();
		
		if(fixtureUserData != null && fixtureUserData2 != null) {
			if(fixtureUserData.getClass() == Player.class) {
				// Test to make sure the collision is actually coming from the bottom
				// TODO: Support different angles (ninja might be able to jump from walls)
				if(contact.getWorldManifold().getNormal().y > 0) {
					((Player)fixtureUserData).setAllowJumping(true);
				}
				
			}
			
			if(Creature.class.isAssignableFrom(fixtureUserData.getClass()) && Creature.class.isAssignableFrom(fixtureUserData2.getClass())) {
				if(contact.getFixtureB() == ((Creature)fixtureUserData2).getAttackFixture()) {
					((AnimatedSpriteBody)fixtureUserData).takeDamage(1);
					
				}
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
