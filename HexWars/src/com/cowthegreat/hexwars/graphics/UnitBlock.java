package com.cowthegreat.hexwars.graphics;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Pool;

public class UnitBlock{
	Pool<ModelInstance> pooledInstances;
	ArrayList<ModelInstance> instances;
	BoundingBox unitBox;
	Vector3 position;
	Vector3 dimensions;
	int count;
	int heightCount;
	int widthCount;
	int depthCount;
	
	public UnitBlock(Pool<ModelInstance> p, Vector3 dim){
		pooledInstances = p;
		position = new Vector3();
		dimensions = dim;
		widthCount = 4;
		heightCount = 2;
		depthCount = 2;
		
		instances = new ArrayList<ModelInstance>();
		count = 0;
		
		ModelInstance mi = pooledInstances.obtain();
		unitBox = mi.calculateBoundingBox(new BoundingBox());
		pooledInstances.free(mi);
	}
	
	public void setSlotCounts(int width, int depth, int height){
		heightCount = height;
		widthCount = width;
		depthCount = depth;
	}
	
	public void setCount(int num){
		count = maxCount() < num ? maxCount() : num;
		System.out.println("set to: " + count);
		if(instances.size() > count){
			System.out.println("shrink");
			for(int i = instances.size() - 1; i > count; i++){
				pooledInstances.free(instances.get(i));
				instances.remove(i);
			}
			layout();
		} else if (instances.size() < count){
			System.out.println("grow");
			for(int i = Math.max(0, instances.size() - 1); i < count; i++){
				ModelInstance inst = pooledInstances.obtain();
				instances.add(inst);
			}
			layout();
		}
		System.out.println(instances.size() + " " + count);
	}
	
	public int maxCount(){
		return widthCount * heightCount * depthCount;
	}
	public void setPosition(float x, float y, float z) {
		if(position.x == x && position.y == y && position.z == z){
			return;
		}
		position.set(x, y, z);
		layout();
	}
	
	public void setPosition(Vector3 pos){
		setPosition(pos.x, pos.y, pos.z);
	}
	
	public Vector3 getPosition() {
		return position;
	}

	Vector3 trans = new Vector3();
	public void layout(){
		for(int i = 0; i < instances.size(); i++){
			ModelInstance mi = instances.get(i);
			trans.x = px(i);
			trans.y = py(i);
			trans.z = pz(i);
			trans.add(position).sub(dimensions.x / 2, dimensions.y / 2, dimensions.z / 2);
			mi.transform.setToTranslation(trans);
		}
	}
	
	public void draw(ModelBatch batch, Environment environ){
		batch.render(instances, environ);
	}
	
	// horizontal 
	private float px(int x){
		return (dimensions.x / (widthCount + 1)) * ((x % widthCount) + 1);
	}
	
	// depth
	private float pz(int x){
		return (dimensions.z / (depthCount + 1)) * (((x % (widthCount * depthCount)) / (widthCount)) + 1);
	}
	
	// height
	private float py(int x){
		return (dimensions.y / (heightCount + 1)) * (((x % (widthCount * depthCount * heightCount)) / (widthCount * depthCount)) + 1);
	}


}
