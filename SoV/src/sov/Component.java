package sov;

/*
 * A component is some sort of data or functionality which can be added to any Entity
 * Some examples include: Sprite rendering, AI, Keyboard input, a Physical body, etc.
 */
public abstract class Component {
	// The parent of the Component
	protected Entity parent;
	
	public Component(Entity parent) {
		this.parent = parent;
	}
	
	// Update component
	// TODO: What to do with a component which handles rendering for example?
	public abstract void update(float deltaTime);
}
