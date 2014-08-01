package com.cowthegreat.hexwars.framework;

import java.util.ArrayDeque;
import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool;
import com.cowthegreat.hexwars.component.Component.componentType;
import com.cowthegreat.hexwars.component.HexPositionComponent;
import com.cowthegreat.hexwars.component.MovementComponent;
import com.cowthegreat.hexwars.component.PositionComponent;
import com.cowthegreat.hexwars.component.RotationComponent;
import com.cowthegreat.hexwars.component.UnitComponent;
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
	

	private static ArrayDeque<GraphNode> expansionList = new ArrayDeque<GraphNode>();
	private static HashMap<HexKey, GraphNode> results = new HashMap<HexKey, GraphNode>();
	private static class GraphNode implements Comparable<GraphNode>{
		private static Pool<GraphNode> pool = new Pool<MovementSystem.GraphNode>(){
			@Override
			protected GraphNode newObject() {
				return new GraphNode();
			}
		};
		public HexKey key;
		public int value;
		
		public static GraphNode obtain(HexKey hk, GraphNode parent, int cost){
			GraphNode gn = pool.obtain();
			gn.key = hk;
			gn.value = cost;
			if(parent != null){
				gn.value += parent.value;
			}
			return gn;
		}
		public GraphNode release(){
			pool.free(this);
			return null;
		}
		@Override
		public int compareTo(GraphNode o) {
			return value - o.value;
		}
		@Override
		public int hashCode() {
			return key.hashCode();
		}
	}
	public static HexKey[] generateMovementRange(Entity ent, HexPositionMap map){
		UnitComponent unit = (UnitComponent) ent.getComponent(componentType.UNIT_DATA);
		HexPositionComponent pos = (HexPositionComponent) ent.getComponent(componentType.HEX_POSITION);
		
		if(unit == null || pos == null){
			return null;
		}

		// set up
		expansionList.push(GraphNode.obtain(pos.position, null, 0));
		expansionList.clear();
		results.clear();
		
		// breadth first search for positions
		while(!expansionList.isEmpty()){
			GraphNode gn = expansionList.poll();
			if(results.containsKey(gn.key)){
				if(gn.compareTo(results.get(gn.key)) > 0){
					results.get(gn.key).release();
					results.put(gn.key, gn);
				} else {
					gn.release();
				}
			} else {
				HexKey[] neighbors = gn.key.getNeighbors();
				for(HexKey n : neighbors){
					GraphNode newNode = GraphNode.obtain(n, gn, map.getCost(n, unit));
					if(newNode.value > unit.AP){
						newNode = newNode.release();
					} else {
						expansionList.add(newNode);
					}
				}
			}
		}
		
		// compose results
		results.remove(pos.position).release();
		HexKey[] res = new HexKey[results.size()];
		int i = 0;
		for(HexKey hk : results.keySet()){
			res[i] = hk;
			results.remove(hk).release();
			i++;
		}
		
		return res;
	}
}
