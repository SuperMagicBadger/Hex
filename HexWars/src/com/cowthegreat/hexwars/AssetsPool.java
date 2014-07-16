package com.cowthegreat.hexwars;

import java.util.HashMap;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.utils.Pool;

public class AssetsPool {
	AssetManager manager;

	HashMap<String, Pool<ModelInstance>> instancePool;

	public AssetsPool(AssetManager loadedManager) {
		manager = loadedManager;
		instancePool = new HashMap<String, Pool<ModelInstance>>();
	}

	public ModelInstance obtainInstance(String modelname) {
		if(generatePool(modelname)){
			return instancePool.get(modelname).obtain();
		}
		return null;
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
	
}
