package com.cowthegreat.hexwars.framework;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.cowthegreat.hexwars.component.Component.componentType;
import com.cowthegreat.hexwars.component.HexPositionComponent;
import com.cowthegreat.hexwars.component.MovementComponent;
import com.cowthegreat.hexwars.component.PositionComponent;
import com.cowthegreat.hexwars.component.RotationComponent;
import com.cowthegreat.hexwars.hex.HexKey;
import com.cowthegreat.hexwars.hex.HexMath;

public class MovementSystem {
	
	public static void move(Entity ent, Vector3 destination, float duration){
		MovementComponent movement = (MovementComponent) ent.getComponent(componentType.MOVEMENT);
		PositionComponent position = (PositionComponent) ent.getComponent(componentType.POSITION);
		if(movement == null || position == null){
			return;
		}
		movement.destination.set(destination);
		movement.source.set(position.position);
		movement.duration = duration;
		movement.reset();
	}
	
	public static boolean isMoving(Entity ent){
		MovementComponent mover = (MovementComponent) ent.getComponent(componentType.MOVEMENT);
		PositionComponent pos = (PositionComponent) ent.getComponent(componentType.POSITION);
		if(mover != null && pos != null && mover.duration > 0){
			return mover.duration > mover.elapsed;
		}
		return false;
	}
	
	public static void update(Entity ent, float delta){
		MovementComponent mover = (MovementComponent) ent.getComponent(componentType.MOVEMENT);
		PositionComponent pos = (PositionComponent) ent.getComponent(componentType.POSITION);
		HexPositionComponent hex = (HexPositionComponent) ent.getComponent(componentType.HEX_POSITION);
		RotationComponent rot = (RotationComponent) ent.getComponent(componentType.ROTATION);
		
		if(pos != null && mover != null){
			pos.position = mover.getPosition(pos.position, delta);
			
			if(mover.faceMovement){
				pos.face(mover.destination);
			}
			
			if(hex != null){
				HexKey hk = HexMath.get().pixelToHex(pos.position.x, pos.position.z);
				hex.position.set(hk);
				hk.release();
			}
		}		
		
		if(rot != null && (rot.duration == -1 || rot.elapsed < rot.duration)){
			pos.rotation += rot.rotationRate * delta;
			pos.rotation = pos.rotation % 360;
			rot.elapsed += delta;
		}
	}
	
	public static void place(Entity ent, Vector3 newpos){
		PositionComponent pos = (PositionComponent) ent.getComponent(componentType.POSITION);
		HexPositionComponent hex = (HexPositionComponent) ent.getComponent(componentType.HEX_POSITION);
		
		if(pos != null){
			pos.position.set(newpos);
			
			if(hex != null){
				HexKey hk = HexMath.get().pixelToHex(pos.position.x, pos.position.z);
				hex.position.set(hk);
				hk.release();
			}
		}
	}

	public static void place(Entity ent, HexKey key){
		PositionComponent pos = (PositionComponent) ent.getComponent(componentType.POSITION);
		HexPositionComponent hex = (HexPositionComponent) ent.getComponent(componentType.HEX_POSITION);
		
		if(hex != null){
			hex.position.set(key);
			
			if(pos != null){
				Vector2 v2d = HexMath.get().hexToPixel(key);
				pos.position.set(v2d.x, 0, v2d.y);
			}
		}
	}
}
