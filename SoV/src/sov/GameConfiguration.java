package sov;

import java.util.ArrayList;

/*
 * Keeps all the configuration data neatly in one place.
 */
public class GameConfiguration {

	// debug mode
	public boolean debugMode = false;
	// TODO: developer cheats
	public boolean devMode = false;
	
	// world
	public String firstMap = "desert_test.tmx";
	public float interpolationAmount = 0.022f;
		
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
