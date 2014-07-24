package com.cowthegreat.hexwars;

import java.util.HashMap;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.utils.Pool;

public class ModelInstancePool {
	AssetManager manager;

	HashMap<String, Pool<ModelInstance>> instancePool;

	public ModelInstancePool(AssetManager loadedManager) {
		manager = loadedManager;
		instancePool = new HashMap<String, Pool<ModelInstance>>();
	}

	public ModelInstance obtainInstance(String modelname) {
		if(generatePool(modelname)){
			return instancePool.get(modelname).obtain();
		}
		return null;
	}
	
	public ModelInstance freeInstance(String modelName, ModelInstance instance){
		if(instancePool.containsKey(modelName)){
			instancePool.get(modelName).free(instance);
			return null;
		}
		return instance;
	}

	public Pool<ModelInstance> getPool(String poolName) {
		if (generatePool(poolName)) {
			return instancePool.get(poolName);
		}
		return null;
	}

	private boolean generatePool(String poolname) {
		if(instancePool.containsKey(poolname))
			return true;
		final Model m = manager.get(poolname, Model.class);
		if (m == null)
			return false;
		instancePool.put(poolname, new Pool<ModelInstance>() {
			@Override
			protected ModelInstance newObject() {
				return new ModelInstance(m);
			}
		});
		return true;
	}
	
	public void dispose(){
		for(Pool<ModelInstance> pool : instancePool.values()){
			pool.clear();
		}
	}
}
