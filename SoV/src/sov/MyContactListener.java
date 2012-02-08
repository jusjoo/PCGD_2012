package sov;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class MyContactListener implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		
		ContactEvent fixtureUserData;
		ContactEvent fixtureUserData2;
		
		for(int i=0; i<2; i++) {
			if(i==0) {
				fixtureUserData = ContactEvent.class.cast(contact.getFixtureA().getUserData());
				fixtureUserData2 = ContactEvent.class.cast(contact.getFixtureB().getUserData());
			} else {
				fixtureUserData = ContactEvent.class.cast(contact.getFixtureB().getUserData());
				fixtureUserData2 = ContactEvent.class.cast(contact.getFixtureA().getUserData());
			}
			
			/*if(fixtureUserData2 != null && fixtureUserData2.name == "sensor") {
				System.out.println("sensor colliding");
			}*/
			
			if(fixtureUserData != null && fixtureUserData.parent != null) {
				
				// Check for jumping
				//if(Creature.class.isAssignableFrom(fixtureUserData.parent.getClass()) && fixtureUserData.name == "sensor") {
				if(fixtureUserData.name == "sensor") {
					System.out.println("sensor colliding");
					// Test to make sure the collision is actually coming from the bottom
					// TODO: Support different angles (ninja might be able to jump from walls)
					//if(contact.getWorldManifold().getNormal().y > 0) {
						
						((Entity)fixtureUserData.parent).getComponent(MovementComponent.class).setAllowJumping(true);
						System.out.println(((Entity)fixtureUserData.parent).);
					//}
					
				}
				
				if(fixtureUserData2 != null && Entity.class.isAssignableFrom(fixtureUserData2.parent.getClass())) {
					

					// If contact is coming from a MeleeAttack to a BodyComponent
					if(MeleeAttack.class.isAssignableFrom(fixtureUserData.getClass()) && ((Entity)fixtureUserData2).getComponent(BodyComponent.class) != null) {	
						// Deal some damage	
						((MeleeAttack)fixtureUserData).dealDamageTo( ((Entity)fixtureUserData2).getComponent(BodyComponent.class));

					}
					
					// If contact is coming from a Projectile to a BodyComponent
					if(Projectile.class.isAssignableFrom(fixtureUserData.getClass()) && ((Entity)fixtureUserData2).getComponent(BodyComponent.class) != null) {	
						// Deal some damage	
						((Projectile)fixtureUserData).dealDamageTo( ((Entity)fixtureUserData2).getComponent(BodyComponent.class));
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
