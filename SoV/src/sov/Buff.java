package sov;

import sov.Creature.Stats;

public class Buff {
	
	private Stats stat;
	private float buffValue;
	private float duration;	// -1 means toggleable
	
	public Buff(Stats stat, float buffValue, float duration) {
		this.stat = stat;
		this.buffValue = buffValue;
		this.duration = duration;
		System.out.println("New buff: "+stat+" +"+buffValue+" "+duration+"s ");
	}
	public Stats getStat() {
		return stat;
	}
	public float getBuffValue() {
		return buffValue;
	}
	public float getDuration() {
		return buffValue;
	}
	// return true if time ran out!
	public boolean reduceDuration(float time) {
		boolean timeout;
		duration -= time;
		if (duration <= 0)
			timeout = true;
		else timeout = false;
		
		
		
		return timeout;
	}

}
