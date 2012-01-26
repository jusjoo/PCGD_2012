package sov;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;

import sov.SpriteComponent.AnimationState;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.JsonReader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class DynamicObjectFactory {
	
	HashMap<Creature.CreatureType, Creature> creatures = new HashMap<Creature.CreatureType, Creature>();
	
	public DynamicObjectFactory(String directory) {
		
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		
		//Type protoType = new 
		
		String filename = "creatures";
		Creature[] creaturePrototypes = null;
		try {
			creaturePrototypes = gson.fromJson(new FileReader("assets/creatures/"+filename+".json"), Creature[].class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		for(Creature creaturePrototype : creaturePrototypes) {
			//spritePrototypes.put(spritePrototype.getId(), spritePrototype);
			//prototype.init();
			//prototypeObjects.put(filename + String.valueOf(prototype.getID()), prototype);
			
			creatures.put(creaturePrototype.creatureType, creaturePrototype);
			System.out.println(creaturePrototype.frames.get(AnimationState.Idle));
			
			//System.out.println(prototype.getClass() + " id: " + prototype.getID());
			
			//System.out.println(prototype.getID());
		}
		
		//gson.fromJson(new FileHandler, classOfT)
		
		//reader.parse(new FileHandle(directory + "/creatures.json"));
		//reader.parse(new FileHandle("assets/creatures/creatures.json"));
		
		
	}
	
	public Creature spawnCreature(World world, Creature.CreatureType type, Vector2 coordinates) {
		Creature creature = (Creature) creatures.get(type).clone();
		creature.addToWorld(world, coordinates);
		return creature;
	}
}
