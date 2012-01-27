package sov;

public abstract class Component {
	protected Object parent;
	
	public Component(Object parent) {
		this.parent = parent;
	}
	
	public abstract void update(float deltaTime);
}
