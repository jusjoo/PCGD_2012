package sov;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;

/*
 * Keeps all the configuration data neatly in one place.
 */
public class GameConfiguration {

	// debug mode
	public static boolean debugMode = true;
	// light rendering
	public static boolean lightRendering = false;
	// TODO: developer cheats
	public static boolean devMode = false;
	
	// world
	public static String firstMap = "barbarian_village.tmx";
	// How quickly does the camera follow the player?
	public static float interpolationAmount = 1.00f;
	
	// Pixels per meter is used to translate Box2d coordinates into pixel coordinates.
	public static final float PIXELS_PER_METER = 32;
		
	// player
	public static float speed = 0.8f;
	public static float jumpHeight = 9.2f;
	public static float attackImpulse = 0.8f;
	
	//key mappings
	// naming
	public static int moveLeft = Keys.LEFT;
	public static int moveRight = Keys.RIGHT;
	public static int moveUp = Keys.UP;
	public static int moveDown = Keys.DOWN;
	public static int actionJump = Keys.UP;
	public static int actionCrouch = Keys.DOWN;
	public static int actionAttack = Keys.SPACE;
	
	// debug key mappings
	public static int debugRenderKey = Keys.F1;
	public static int lightRenderKey = Keys.F2;
	
	public static DynamicObjectFactory dynamicObjectFactory;
	
	
	
	/*
	 * Loads the default configuration
	 * 
	 * TODO: load a configuration from a file
	 */
	public GameConfiguration() {
		
		
	}
}
