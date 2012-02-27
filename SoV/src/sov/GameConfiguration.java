package sov;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

/*
 * Keeps all the configuration data neatly in one place.
 */
public class GameConfiguration {
	
	// public reference to the application instance
	public static LwjglApplication instance;
	public static DynamicObjectFactory factory;
	public static GameMap map;
	// debug mode
	public static boolean debugMode = false;
	// light rendering
	public static boolean lightRendering = false;
	// activates the developer controls
	public static boolean devMode = true;
	
	// game window dimensions
	public static int windowSizeX = 1024;
	public static int windowSizeY = 768;
	
	// world
	public static String firstMap = "emptymap.tmx";
	public static float physicsLinearDamping = 3.0f;
	public static float physicsFrictionDef = 0.02f;
	public static float physicsWorldGravity = -30;
	// How quickly does the camera follow the player?
	public static float interpolationAmount = 1.00f;
	
	// Pixels per meter is used to translate Box2d coordinates into pixel coordinates.
	public static final float PIXELS_PER_METER = 32;
	public static final float audioComponentTimer = 0.3f;

	public static final float musicVolume = 0.7f;

	public static final String menuMoveSoundFile = "assets/sound/menu_4.ogg";
	public static final String menuConfirmSoundFile = "assets/sound/menu_2.ogg";
	public static final String menuBackSoundFile = "assets/sound/menu_3.ogg";
	public static final String startGameSoundFile = "assets/sound/start_game.ogg";
	
		
	// player
	public static float speed = 0.8f;
	public static float jumpHeight = 9.2f;
	public static float attackImpulse = 0.8f;
	
	// creature
	public static boolean randomizedDamage = true;
	
	public static float strHealthMultiplier = 15f;
	public static float healthBaseModifier = 25f;	
	public static float strDamageMultiplier = 1.0f;	
	public static float dexSpeedMultiplier = 0.45f;
	public static float speedBaseModifier = 3.0f;
	public static float dexStaminaMultiplier = 10;
	public static float staminaBaseModifier = 50;
	public static float wisManaMultiplier = 10;
	public static float manaBaseModifier = 0;	
	
	public static float dexJumpHeightMultiplier = 4.0f;
	public static float jumpHeightBaseModifier = 80.0f;
	public static float creatureMaxVelocityMultiplier = 1.15f;//redundant
		
	public static boolean staminaRegenAsPercentage = true;	
	public static float staminaRegenRatePercentage = 0.05f;		//regen per second
	public static float staminaRegenRateStatic = 7;
	public static float staminaCostJump = 7.0f;
	public static float staminaCostDoubleJump = 21.0f;
	public static float staminaCostAttackMultiplier = 1.75f;
	
	public static boolean manaRegenAsPercentage = true;
	public static float manaRegenRatePercentage = 0.02f;			//regen per second
	public static float manaRegenRateStatic = 1;	
	public static float manaCostAttackMultiplier = 1.0f;
	
	
	
	
	// immunity time in seconds after taking damage
	public static float immuneTime = 0.5f;
	
	//key mappings
	// naming
	public static int moveLeft = Keys.LEFT;
	public static int moveRight = Keys.RIGHT;
	public static int moveUp = Keys.UP;
	public static int moveDown = Keys.DOWN;
	public static int actionJump = Keys.UP;
	public static int actionCrouch = Keys.DOWN;
	public static int actionAttack = Keys.SPACE;
	public static int actionAltAttack = Keys.CONTROL_LEFT;
	public static int escapeKey = Keys.ESCAPE;
	public static int activateMenu = Keys.ENTER;
	
	// developer key mappings
	public static int debugRenderKey = Keys.NUM_1;
	public static int lightRenderKey = Keys.NUM_2;
	public static int showStats = Keys.NUM_4;
	public static int giveExp10 = Keys.NUM_5;
	public static int giveExp50 = Keys.NUM_6;	
	public static int selectBarbarianKey = Keys.NUM_0;
	public static int selectNinjaKey = Keys.NUM_9;
	public static int selectSorceressKey = Keys.NUM_8; 
	public static int mapSelectKey = Keys.M;
	
	
	public static DynamicObjectFactory dynamicObjectFactory;
	public static float trapDamage = 50f;
	
	
	/*
	 * 		Leveling variables for stats
	 */
	
	public static float BarbarianLevelUpStr = 1.0f;
	public static float NinjaLevelUpStr = 0.6f;
	public static float SorceressLevelUpStr = 0.4f;
	
	public static float BarbarianLevelUpDex = 0.5f;
	public static float NinjaLevelUpDex = 0.7f;
	public static float SorceressLevelUpDex = 0.33f;
	
	public static float BarbarianLevelUpWis= 0.3f;
	public static float NinjaLevelUpWis = 0.5f;
	public static float SorceressLevelUpWis = 1.2f;

	
	
	
	
	
	/*
	 * Loads the default configuration
	 * 
	 * TODO: load a configuration from a file
	 */
	public GameConfiguration() {
		
		
	}
}
