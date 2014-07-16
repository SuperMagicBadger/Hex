package com.cowthegreat.hexwars.controlers;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

public class RawCameraController {
	public Camera cam;
	
	public Vector3 center;
	public float zoom;
	private Vector3 rotation;
	private Quaternion quat;
	
	private Plane levelPlane;
	
	public RawCameraController(Camera camera){
		cam = camera;
		center = new Vector3(0, 0, 0);
		rotation = new Vector3(0, -45, 0);
		
		zoom = 15;

		cam.up.set(Vector3.Y);
		quat = new Quaternion();
		
		levelPlane = new Plane(Vector3.Y, Vector3.Zero);
	}
	
	public void update(){
		Vector3 add = new Vector3(0, 0, zoom);

		quat.setEulerAngles(rotation.x, rotation.y, rotation.z);
		
		add.mul(quat);
		
		cam.position.set(center).add(add);
		cam.up.set(Vector3.Y);
		cam.lookAt(center);
		
		cam.update();
	}
	
	public void setPosition(Vector3 pos){
		center.set(pos);
	}
	
	public void setPosition(float x, float y, float z){
		center.set(x, y, z);
	}
	
	
	private static Vector3 mv = new Vector3();
	public void move(float x, float y, float z){
		mv.set(x, y, z);
		mv.mul(quat.setEulerAngles(rotation.x, 0, 0));
		center.add(mv);
		

		
	}
	
	public void zoom(float dist){
		zoom += dist;
		rotation.y -= dist;
		if(zoom > 50){
			zoom = 50;
			rotation.y = -45 - (50 - 15);
		} else if (zoom < 15){
			zoom = 15;
			rotation.y = -45;
		}
	}
	
	public void rotate(float angle){
		rotation.x = (rotation.x + angle) % 360;
	}
	
	Vector3 pickResult = new Vector3();
	public Vector3 pick(float x, float y){
		Ray r = cam.getPickRay(x, y);
		Intersector.intersectRayPlane(r, levelPlane, pickResult);
		return pickResult;
	}
	
	public void applyBounds(Rectangle scrollBounds){
		// bind camera x
		if(center.x < scrollBounds.x){
			center.x = scrollBounds.x;
		} else if (center.x > scrollBounds.x + scrollBounds.width){
			center.x = scrollBounds.x + scrollBounds.width;
		}
		
		// bind camera y
		if(center.z < scrollBounds.y){
			center.z = scrollBounds.y;
		} else if (center.z > scrollBounds.y + scrollBounds.height){
			center.z = scrollBounds.y + scrollBounds.height;
		}
	}
}
