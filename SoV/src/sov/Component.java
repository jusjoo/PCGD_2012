package sov;

public abstract class Component {
	protected Object parent;
	
	public enum ComponentType {AIComponent, KeyboardInputComponent, BodyComponent, SpriteComponent};
	
	public ComponentType componentType;
	
	public Component(Object parent) {
		this.parent = parent;
	}
	
	public abstract void update(float deltaTime);
}
