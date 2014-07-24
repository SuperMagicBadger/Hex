package com.cowthegreat.hexwars.framework;

import java.util.HashMap;

import com.cowthegreat.hexwars.component.Component;
import com.cowthegreat.hexwars.component.Component.componentType;

public class Entity {
	public HashMap<componentType, Component> componentMap;
	
	public Entity() {
		componentMap = new HashMap<componentType, Component>();
	}
	
	public boolean hasComponent(componentType type){
		return componentMap.containsKey(type);
	}
	
	public boolean addComponent(Component c){
		if(hasComponent(c.getComponentType())){
			return false;
		}
		componentMap.put(c.getComponentType(), c);
		return true;
	}
	
	public Component getComponent(componentType type){
		if(hasComponent(type)){
			return componentMap.get(type);
		}
		return null;
	}
}
