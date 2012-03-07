package sov;

public class ExperienceComponent extends Component {
	//private Creature creature;	
	
	private int level;
	private int experience;					//current experience
	private int levelThreshold;				// experience threshold required to reach next level	
	private int thresholdModifier;		// how much levelthreshold increases after each levelup
	private int nextLevelExpTotal;
	private int score = 0;
	
	/*
	 * Modifiers determine how much each stat goes up on levelup
	 */
	private float levelUpStrengthModifier;	
	private float levelUpDexterityModifier;
	private float levelUpWisdomModifier;
	
	public ExperienceComponent(Entity parent) {
		super(parent);
		experience = 0;
		levelThreshold = 100;
		thresholdModifier = 50;
		nextLevelExpTotal = levelThreshold;
		levelUpStrengthModifier = 1;
		levelUpDexterityModifier = 1;
		levelUpWisdomModifier = 1;
		level = 1;
	}
	@Override
	public void update(float deltaTime) {
		if (experience >= nextLevelExpTotal) {
			levelUp();
		}		
	}
	public void setStatBonuses(float strMod, float dexMod, float wisMod) {
		levelUpStrengthModifier = strMod;
		levelUpDexterityModifier = dexMod;
		levelUpWisdomModifier = wisMod;
	}
	public void giveExperience(int exp) {
		this.experience += exp;
		System.out.println("+"+exp+" exp");
	}
	public void levelUp() {				
		Creature creature = (Creature) parent;
		
		level++;
		levelThreshold += thresholdModifier;
		nextLevelExpTotal += levelThreshold;
		creature.modifyStrength(levelUpStrengthModifier);
		creature.modifyDexterity(levelUpDexterityModifier);
		creature.modifyWisdom(levelUpWisdomModifier);
		
		System.out.println("levelUp! ("+getLevel()+") Exp: "+experience+" Next:"+nextLevelExpTotal);
		System.out.println("Stats: Str "+creature.strength+" Dex "+creature.dexterity+" Wis "+creature.wisdom);
	}
	
	public int getLevel() {
		return level;
	}
	public int getExperience() {
		return this.experience;
	}
	public int getNextLevelExp() {
		return this.nextLevelExpTotal;
	}
	
	public int getScore() {
		return this.score + (level-1)*75;
	}
	
	public void giveScore(int sc) {
		this.score += sc;
		System.out.println("+"+sc+" Score now: " + score);
	} 

}
