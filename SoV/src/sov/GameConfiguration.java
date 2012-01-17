package sov;

/*
 * Keeps all the configuration data neatly in one place.
 */
public class GameConfiguration {

	// TODO: developer cheats
	public boolean debugMode = false;
	public boolean devMode = false;
	
	// world
	public float interpolationAmount = 0.012f;
		
	// player
	public float speed = 0.1f;
	public float jumpHeight = 6.2f;
	
	
	
	/*
	 * Loads the default configuration
	 * 
	 * TODO: load a configuration from a file
	 */
	public GameConfiguration() {
		
		
	}
}
