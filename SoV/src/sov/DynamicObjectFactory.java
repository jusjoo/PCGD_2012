package sov;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.JsonReader;

public class DynamicObjectFactory {
	
	public enum Creatures { Barbarian, Ninja, Sorceress, Goblin };
	HashMap<Creatures, Creature> creatures;
	
	public DynamicObjectFactory(String directory) {
		
		//Gson
		
		//reader.parse(new FileHandle(directory + "/creatures.json"));
		//reader.parse(new FileHandle("assets/creatures/creatures.json"));
		
		
	}
	
	public Creature spawnCreature(World world, Creatures type, Vector2 coordinates) {
		Creature creature = (Creature) creatures.get(type).clone();
		creature.addToWorld(world, coordinates);
		return creature;
	}
}
