package sov;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class MyContactListener implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		
		ContactEvent fixtureUserData;
		ContactEvent fixtureUserData2;
		
		/*
		 * Swap fixtureUserDatas on second run, so we check all contacts both ways.
		 */
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
			
			//if (fixtureUserData == null) System.out.println("null lol "+i);
			
			if(fixtureUserData != null && fixtureUserData.parent != null) {
				//System.out.println(fixtureUserData.getClass());
				//System.out.println("parentti " + fixtureUserData.parent.getClass());
				
				
				// Check for jumping
				if(fixtureUserData.name == "sensor") {
					//System.out.println("sensor colliding");
					// Test to make sure the collision is actually coming from the bottom
					// TODO: Support different angles (ninja might be able to jump from walls)
					//if(contact.getWorldManifold().getNormal().y > 0) {
						
						((Entity)fixtureUserData.parent).getComponent(MovementComponent.class).setAllowJumping(true);
						//System.out.println(((Entity)fixtureUserData.parent));
					//}
					
				}
				
				// Check for traps
				if(fixtureUserData.name == "trap" && fixtureUserData2.parent.getClass() == BodyComponent.class) {
					((BodyComponent)fixtureUserData2.parent).setToTakeDamage(GameConfiguration.trapDamage);
				}
				
				if(fixtureUserData.name == "exit" && fixtureUserData2.parent.getClass() == BodyComponent.class && ((BodyComponent)fixtureUserData2.parent).parent.getComponent(PlayerInputComponent.class) != null ) {
					GameConfiguration.hud.activateWinScreen();
				}
				
				// Check for collectibles
				if(fixtureUserData.name == "collectible" 
						&& fixtureUserData2 != null
						&& fixtureUserData2.name == "body"
						&& ((Component)fixtureUserData2.parent).parent != null
						&& ((Component)fixtureUserData2.parent).parent.hasComponent(PlayerInputComponent.class) )  {
					((Collectible)fixtureUserData.parent).consumeBy( (Creature) ((Component)fixtureUserData2.parent).parent );
				}
				
				// Handle a melee attack if one fixture is named "melee" and the other is a bodyComponent
				if(fixtureUserData.name == "melee" && fixtureUserData2.parent.getClass() == BodyComponent.class) {
					((MeleeAttack)fixtureUserData.parent).dealDamageTo((BodyComponent)fixtureUserData2.parent);			
				}
				
				// Handle projectile contacts
				if(fixtureUserData.name == "projectile")  {
					
					// If projectile hits a bodyComponent
					if (fixtureUserData2.parent.getClass() == BodyComponent.class) {
						((Projectile)fixtureUserData.parent).dealDamageTo((BodyComponent)fixtureUserData2.parent);
						
					} else if (fixtureUserData2.name == "collectible") {
						Vector2 pos1 = ((Projectile)fixtureUserData.parent).body.getPosition();
						Vector2 pos2 = ((Collectible)fixtureUserData2.parent).body.getPosition();
						
						pos1.add(pos2);
						((Collectible)fixtureUserData2.parent).body.applyLinearImpulse(pos1.nor());
					
					} else {
					
						// it's hitting something else and should die
						((Projectile)fixtureUserData.parent).setToDie();
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
