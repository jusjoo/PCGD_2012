package sov;

import java.util.HashMap;

import sov.Component.ComponentType;

public class Entity {
	protected HashMap<Class<? extends Component>, Component> components = new HashMap<Class<? extends Component>, Component>();
	
	public void addComponent(Component component) {
		components.put(component.getClass(), component);
	}
	
	public void removeComponent(Class<? extends Component> className) {
		components.remove(className);
	}
	
	public <T extends Component> T getComponent(Class<T> className) {
		return className.cast(components.get(className));
	}
	
	public void update(float deltaTime) {
		for(Component component : components.values()) {
			component.update(deltaTime);
		}
	}
}
