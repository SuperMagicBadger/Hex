package com.cowthegreat.hexwars;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

public class CameraController extends GestureDetector {
	private static class CameraListener extends GestureAdapter {
		CameraController cc;

		boolean rotate;

		@Override
		public boolean longPress(float x, float y) {
			rotate = true;
			return super.longPress(x, y);
		}

		@Override
		public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
				Vector2 pointer1, Vector2 pointer2) {
			rotate = false;
			return super.pinch(initialPointer1, initialPointer2, pointer1,
					pointer2);
		}

		Vector2 rotVec = new Vector2();
		Vector2 v = new Vector2();

		@Override
		public boolean pan(float x, float y, float deltaX, float deltaY) {
			if (rotate) {
				rotVec.set(-(y - Gdx.graphics.getHeight() / 2), x
						- Gdx.graphics.getWidth() / 2);
				v.set(deltaX, deltaY);
				float r = rotVec.dot(v) / 100;
				cc.rotate(r);
			} else {
				float xv = deltaX / cc.camera.viewportWidth * -cc.zoom;
				float yv = deltaY / cc.camera.viewportHeight * -cc.zoom;

				cc.move(xv, 0, yv);
			}
			return true;
		}

		@Override
		public boolean panStop(float x, float y, int pointer, int button) {
			rotate = false;
			return super.panStop(x, y, pointer, button);
		}

		@Override
		public boolean zoom(float initialDistance, float distance) {
			cc.zoom(distance);
			return true;
		}
	}

	private static Vector3 mv = new Vector3();
	private static Vector3 updateV3 = new Vector3();
	
	public Camera camera;

	Rectangle dims;
	Rectangle scrollBounds;

	Vector3 center;
	float zoom;
	Vector3 rotation;
	Quaternion rotationQuat;

	Plane levelPlane;

	public CameraController(Camera cam) {
		this(new CameraListener(), cam);
	}

	protected CameraController(CameraListener listener, Camera cam) {
		super(listener);
		listener.cc = this;
		setLongPressSeconds(0.5f);

		camera = cam;

		center = new Vector3(0, 0, 0);
		rotation = new Vector3(0, -45, 0);
		zoom = 15;

		camera.up.set(Vector3.Y);
		rotationQuat = new Quaternion();

		levelPlane = new Plane(Vector3.Y, Vector3.Zero);

		dims = new Rectangle();
		scrollBounds = new Rectangle(0, 0, 100, 100);
	}

	@Override
	public boolean scrolled(int amount) {
		zoom(amount);
		return true;
	}

	public void update() {
		if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
			rotate(5);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.E)) {
			rotate(-5);
		}

		applyBounds(scrollBounds);

		updateV3.set(0, 0, zoom);
		rotationQuat.setEulerAngles(rotation.x, rotation.y, rotation.z);
		updateV3.mul(rotationQuat);

		camera.position.set(center).add(updateV3);
		camera.up.set(Vector3.Y);
		camera.lookAt(center);

		camera.update();
	}

	Vector3 corners[] = new Vector3[] { new Vector3(), new Vector3(),
			new Vector3(), new Vector3() };

	public Rectangle getDimensions() {
		pick(0, 0, corners[0]);
		pick(0, camera.viewportHeight, corners[1]);
		pick(camera.viewportWidth, camera.viewportHeight, corners[2]);
		pick(camera.viewportWidth, 0, corners[3]);

		dims.setX(Math.min(corners[0].x,
				Math.min(corners[1].x, Math.min(corners[2].x, corners[3].x))));
		dims.setY(Math.min(corners[0].z,
				Math.min(corners[1].z, Math.min(corners[2].z, corners[3].z))));

		dims.setWidth(Math.max(corners[0].x,
				Math.max(corners[1].x, Math.max(corners[2].x, corners[3].x)))
				- dims.x);
		dims.setHeight(Math.max(corners[0].z,
				Math.max(corners[1].z, Math.max(corners[2].z, corners[3].z)))
				- dims.y);

		return dims;
	}

	public void setScrollBounds(float x, float y, float width, float height) {
		scrollBounds.set(x, y, width, height);
	}

	public Rectangle getScrollBounds() {
		return scrollBounds;
	}

	public void setPosition(Vector3 pos) {
		center.set(pos);
	}

	public void setPosition(float x, float y, float z) {
		center.set(x, y, z);
	}

	public void move(float x, float y, float z) {
		mv.set(x, y, z);
		mv.mul(rotationQuat.setEulerAngles(rotation.x, 0, 0));
		center.add(mv);
	}

	public void zoom(float dist) {
		zoom += dist;
		rotation.y -= dist;
		if (zoom > 50) {
			zoom = 50;
			rotation.y = -45 - (50 - 15);
		} else if (zoom < 15) {
			zoom = 15;
			rotation.y = -45;
		}
	}

	public void rotate(float angle) {
		rotation.x = (rotation.x + angle) % 360;
	}

	public Vector3 pick(float x, float y, Vector3 v3) {
		Ray r = camera.getPickRay(x, y);
		Intersector.intersectRayPlane(r, levelPlane, v3);
		return v3;
	}

	public void applyBounds(Rectangle scrollBounds) {
		// bind camera x
		if (center.x < scrollBounds.x) {
			center.x = scrollBounds.x;
		} else if (center.x > scrollBounds.x + scrollBounds.width) {
			center.x = scrollBounds.x + scrollBounds.width;
		}

		// bind camera y
		if (center.z < scrollBounds.y) {
			center.z = scrollBounds.y;
		} else if (center.z > scrollBounds.y + scrollBounds.height) {
			center.z = scrollBounds.y + scrollBounds.height;
		}
	}
}
