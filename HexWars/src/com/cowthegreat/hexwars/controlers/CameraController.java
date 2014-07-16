package com.cowthegreat.hexwars.controlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class CameraController extends GestureDetector {
	private static class CameraListener extends GestureAdapter {
		RawCameraController raw;
		CameraController cc;
		
		boolean rotate;
		
		@Override
		public boolean touchDown(float x, float y, int pointer, int button) {
			return super.touchDown(x, y, pointer, button);
		}
		
		@Override
		public boolean longPress(float x, float y) {
			rotate = true;
			return super.longPress(x, y);
		}
		
		@Override
		public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
				Vector2 pointer1, Vector2 pointer2) {
			rotate = false;
			return super.pinch(initialPointer1, initialPointer2, pointer1, pointer2);
		}	
		
		Vector2 rotVec = new Vector2();
		Vector2 v = new Vector2();
		@Override
		public boolean pan(float x, float y, float deltaX, float deltaY) {
			if(rotate){
				rotVec.set(-(y - Gdx.graphics.getHeight() / 2), x - Gdx.graphics.getWidth() / 2);
				v.set(deltaX, deltaY);
				float r = rotVec.dot(v) / 100;
				raw.rotate(r);
			} else {
				float xv = deltaX / cc.camera.viewportWidth * -raw.zoom;
				float yv = deltaY / cc.camera.viewportHeight * -raw.zoom;
				
				raw.move(xv, 0, yv);
			}
			return true;
		}
		
		@Override
		public boolean tap(float x, float y, int count, int button) {
			cc.planeSelect.set(raw.pick(x, y));
			return super.tap(x, y, count, button);
		}
		
		@Override
		public boolean panStop(float x, float y, int pointer, int button) {
			rotate = false;
			return super.panStop(x, y, pointer, button);
		}

		@Override
		public boolean zoom(float initialDistance, float distance) {
			raw.zoom(distance);
			return true;
		}
	}

	public RawCameraController raw;
	public Camera camera;
	
	public Vector3 planeSelect;
	
	Rectangle dims;
	
	Rectangle scrollBounds;

	public CameraController(Camera cam) {
		this(new CameraListener(), cam);
	}

	protected CameraController(CameraListener listener, Camera cam) {
		super(listener);
		listener.cc = this;
		setLongPressSeconds(0.5f);

		camera = cam;
		raw = new RawCameraController(camera);
		listener.raw = raw;
		

		dims = new Rectangle();
		scrollBounds = new Rectangle(0, 0, 100, 100);
		planeSelect = new Vector3();
	}
	
	@Override
	public boolean scrolled(int amount) {
		raw.zoom(amount);
		return true;
	}

	public void update() {
		if(Gdx.input.isKeyPressed(Input.Keys.Q)){
			raw.rotate(5);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.E)){
			raw.rotate(-5);
		}
		
		raw.applyBounds(scrollBounds);
		
		raw.update();
	}
	
	Vector3 corners[] = new Vector3[]{
		new Vector3(), new Vector3(), new Vector3(), new Vector3()	
	};
	public Rectangle getDimensions(){
		corners[0].set(raw.pick(0, 0));
		corners[1].set(raw.pick(0, camera.viewportHeight));
		corners[2].set(raw.pick(camera.viewportWidth, camera.viewportHeight));
		corners[3].set(raw.pick(camera.viewportWidth, 0));
		
		dims.setX(Math.min(corners[0].x, Math.min(corners[1].x, Math.min(corners[2].x, corners[3].x))));
		dims.setY(Math.min(corners[0].z, Math.min(corners[1].z, Math.min(corners[2].z, corners[3].z))));
		
		dims.setWidth (Math.max(corners[0].x, Math.max(corners[1].x, Math.max(corners[2].x, corners[3].x))) - dims.x);
		dims.setHeight(Math.max(corners[0].z, Math.max(corners[1].z, Math.max(corners[2].z, corners[3].z))) - dims.y);
		
		return dims;
	}
	
	public void setScrollBounds(float x, float y, float width, float height){
		scrollBounds.set(x, y, width, height);
	}

	public Rectangle getScrollBounds(){
		return scrollBounds;
	}
	
}
