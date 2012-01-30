package sov;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class MyContactListener implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		
		Object fixtureUserData;
		Object fixtureUserData2;
		
		for(int i=0; i<2; i++) {
			if(i==0) {
				fixtureUserData = contact.getFixtureA().getBody().getUserData();
				fixtureUserData2 = contact.getFixtureB().getBody().getUserData();
			} else {
				fixtureUserData = contact.getFixtureB().getBody().getUserData();
				fixtureUserData2 = contact.getFixtureA().getBody().getUserData();
			}
			
			
			
			if(fixtureUserData != null) {
				
				// Check for jumping
				if(Creature.class.isAssignableFrom(fixtureUserData.getClass())) {
					// Test to make sure the collision is actually coming from the bottom
					// TODO: Support different angles (ninja might be able to jump from walls)
					if(contact.getWorldManifold().getNormal().y > 0) {
						((Creature)fixtureUserData).setAllowJumping(true);
					}
					
				}
				
				if(fixtureUserData2 != null) {
				
					if(Creature.class.isAssignableFrom(fixtureUserData.getClass()) && Creature.class.isAssignableFrom(fixtureUserData2.getClass())) {
						
						// Skip entities that don't have an attack component attached
						// TODO: maybe optimize with a simple boolean value in Creature class?
						if ( ((Creature)fixtureUserData2).components.containsKey(AttackComponent.class)) {
							
							// If contact is with an attack fixture
							if(contact.getFixtureB() == ((Creature)fixtureUserData2).getComponent(AttackComponent.class).getAttackFixture()) {
								((SpriteBody)fixtureUserData).setToTakeDamage(1);
								((Creature)fixtureUserData2).getComponent(AttackComponent.class).setToStopDamage();
							}
						}
					}
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
