package sov;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;

/*
 * Keeps all the configuration data neatly in one place.
 */
public class GameConfiguration {

	// debug mode
	public boolean debugMode = false;
	// TODO: developer cheats
	public boolean devMode = false;
	
	// world
	public String firstMap = "desert_testbig.tmx";
	// How quickly does the camera follow the player?
	public float interpolationAmount = 0.022f;
	
	// Pixels per meter is used to translate Box2d coordinates into pixel coordinates.
	public static final float PIXELS_PER_METER = 32;
		
	// player
	public float speed = 0.8f;
	public float jumpHeight = 9.2f;
	public float attackImpulse = 0.8f;
	
	//key mappings
	// naming
	public int moveLeft = Keys.LEFT;
	public int moveRight = Keys.RIGHT;
	public int moveUp = Keys.UP;
	public int moveDown = Keys.DOWN;
	public int actionJump = Keys.UP;
	public int actionCrouch = Keys.DOWN;
	public int actionAttack = Keys.SPACE;
	
	
	
	
	/*
	 * Loads the default configuration
	 * 
	 * TODO: load a configuration from a file
	 */
	public GameConfiguration() {
		
		
	}
}
