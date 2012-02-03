package sov;

import com.badlogic.gdx.Input.Keys;

/*
 * Keeps all the configuration data neatly in one place.
 */
public class GameConfiguration {

	// debug mode
	public static boolean debugMode = true;
	// light rendering
	public static boolean lightRendering = false;
	// activates the developer controls
	public static boolean devMode = true;
	
	// world
	public static String firstMap = "barbarian_cave.tmx";
	// How quickly does the camera follow the player?
	public static float interpolationAmount = 1.00f;
	
	// Pixels per meter is used to translate Box2d coordinates into pixel coordinates.
	public static final float PIXELS_PER_METER = 32;
		
	// player
	public static float speed = 0.8f;
	public static float jumpHeight = 9.2f;
	public static float attackImpulse = 0.8f;
	
	// creature
	public static float strHealthMultiplier = 1.0f;
	public static float strDamageMultiplier = 1.0f;
	public static float healthBaseModifier = 1.0f;	
	public static float dexSpeedMultiplier = 0.4f;
	public static float speedBaseModifier = 1.0f;
	public static float dexJumpHeightMultiplier = 1.15f;
	public static float jumpHeightBaseModifier = 1.0f;
	public static float creatureMaxVelocityMultiplier = 1.15f;
	
	
	
	
	// immunity time in seconds after taking damage
	public static float immuneTime = 0.2f;
	
	//key mappings
	// naming
	public static int moveLeft = Keys.LEFT;
	public static int moveRight = Keys.RIGHT;
	public static int moveUp = Keys.UP;
	public static int moveDown = Keys.DOWN;
	public static int actionJump = Keys.UP;
	public static int actionCrouch = Keys.DOWN;
	public static int actionAttack = Keys.SPACE;
	
	// developer key mappings
	public static int debugRenderKey = Keys.NUM_1;
	public static int lightRenderKey = Keys.NUM_2;
	public static int selectBarbarianKey = Keys.NUM_0;
	public static int selectNinjaKey = Keys.NUM_9;
	public static int selectSorceressKey = Keys.NUM_8; 
	
	public static DynamicObjectFactory dynamicObjectFactory;
	
	
	
	/*
	 * Loads the default configuration
	 * 
	 * TODO: load a configuration from a file
	 */
	public GameConfiguration() {
		
		
	}
}
