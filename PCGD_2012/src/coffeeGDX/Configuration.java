package coffeeGDX;

public class Configuration {
	
	// debug mode on/off
	private boolean debugMode = false;
	
	// camera follow speed
	private float interpolationAmount = 0.012f;
	
	
	// player configuration
	protected final float speed = 0.05f;
	protected final float jumpHeight = 1.6f;
	
	
	
	
	
	
	// getters:
	public boolean getDebugMode() { return debugMode;}
	public float getInterpolationAmount() { return interpolationAmount; }
	public float getPlayerSpeed() {	return speed;	}
	public float getPlayerJumpHeight() { return jumpHeight;	}
	
	
	
	/*
	 * Creates a new configuration object
	 * 
	 * TODO: get configuration from a given file
	 */
	public Configuration() {
		
	}

	
}
