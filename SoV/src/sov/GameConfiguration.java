package sov;

/*
 * Keeps all the configuration data neatly in one place.
 */
public class GameConfiguration {

	// debug mode
	public boolean debugMode = true;
	// TODO: developer cheats
	public boolean devMode = false;
	
	// world
	public String firstMap = "desert_test.tmx";
	// How quickly does the camera follow the player?
	public float interpolationAmount = 0.022f;
	
	// Pixels per meter is used to translate Box2d coordinates into pixel coordinates.
	public static final int PIXELS_PER_METER = 32;
		
	// player
	public float speed = 0.8f;
	public float jumpHeight = 9.2f;

	
	
	
	/*
	 * Loads the default configuration
	 * 
	 * TODO: load a configuration from a file
	 */
	public GameConfiguration() {
		
		
	}
}
