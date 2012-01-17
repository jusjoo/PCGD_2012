package sov;

import java.util.ArrayList;

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
	public float interpolationAmount = 0.012f;
		
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
