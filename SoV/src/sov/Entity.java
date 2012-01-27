package sov;

import java.util.ArrayList;

public class Entity {
	protected ArrayList<Component> components = new ArrayList<Component>();
	
	public void addComponent(Component component) {
		components.add(component);
	}
	
	public void removeComponent(Component component) {
		components.remove(component);
	}
	
	public void update(float deltaTime) {
		for(Component component : components) {
			component.update(deltaTime);
		}
	}
}
