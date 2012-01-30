package sov;

import java.util.HashMap;

/*
 * Entity is any sort of an object which exists in a map. It doesn't have to have a location.
 * An entity is defined by the components it has. If you want a physics body in the map, add
 * a component for that. If you want it to be drawn, add a component which handles the drawing.
 * 
 * If you override the update method, remember to call super.update as well.
 */
public class Entity {
	
	// A map which contains all the components in this Entity.
	// There can only be one component of a particular class in here at once.
	protected HashMap<Class<? extends Component>, Component> components = new HashMap<Class<? extends Component>, Component>();
	
	// Add a component
	public Entity addComponent(Component component) {
		components.put(component.getClass(), component);
		return this;
	}
	
	// Remove a component based on the component class.
	public void removeComponent(Class<? extends Component> className) {
		components.remove(className);
	}
	
	// Return a component based on class name.
	public <T extends Component> T getComponent(Class<T> className) {
		return className.cast(components.get(className));
	}
	
	// Update all components
	public void update(float deltaTime) {
		for(Component component : components.values()) {
			component.update(deltaTime);
		}
	}
}
