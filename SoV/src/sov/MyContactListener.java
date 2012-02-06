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
						((Entity)fixtureUserData).getComponent(MovementComponent.class).setAllowJumping(true);
					}
					
				}
				
				if(fixtureUserData2 != null && Entity.class.isAssignableFrom(fixtureUserData2.getClass())) {
					
					//if(Creature.class.isAssignableFrom(fixtureUserData.getClass()) && Creature.class.isAssignableFrom(fixtureUserData2.getClass())) {
					if(AttackComponent.class.isAssignableFrom(fixtureUserData.getClass()) && ((Entity)fixtureUserData2).getComponent(BodyComponent.class) != null) {	
									
						//fixtureUserData.getDamage()	
						((Entity)fixtureUserData2).getComponent(BodyComponent.class).setToTakeDamage(1);
						
						/*
						if(contact.getFixtureB() == ((Creature)fixtureUserData2).getComponent(AttackComponent.class).getAttackFixture()) {
							((Creature)fixtureUserData).getComponent(BodyComponent.class).setToTakeDamage(1);
							//((Creature)fixtureUserData2).getComponent(AttackComponent.class);
						}*/
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
