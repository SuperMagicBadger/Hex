package com.cowthegreat.hexwars.framework;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.cowthegreat.hexwars.ModelInstancePool;
import com.cowthegreat.hexwars.component.HexPositionComponent;
import com.cowthegreat.hexwars.component.InventoryComponent;
import com.cowthegreat.hexwars.component.MovementComponent;
import com.cowthegreat.hexwars.component.PositionComponent;
import com.cowthegreat.hexwars.component.RenderComponent;
import com.cowthegreat.hexwars.component.RotationComponent;
import com.cowthegreat.hexwars.component.WeaponComponent;

public class EntityFactory {

	ModelInstancePool miPool;

	public EntityFactory(AssetManager am) {
		miPool = new ModelInstancePool(am);
	}

	public Entity createPlanet() {
		Entity e = new Entity();

		HexPositionComponent hexPos = new HexPositionComponent();
		PositionComponent pos = new PositionComponent();
		RenderComponent renCom = new RenderComponent();
		RotationComponent roCom = new RotationComponent();
		InventoryComponent invCom = new InventoryComponent();

		hexPos.position.setOddR(0, 0);
		hexPos.descritor.inner = Color.GRAY;
		hexPos.descritor.outer = Color.GREEN;

		renCom.model = miPool.obtainInstance("planet.g3db");

		roCom.rotationRate = 50;
		roCom.duration = -1;

		invCom.ammo = 0;
		invCom.supplies = 10;
		invCom.materials = 10;
		invCom.fuel = 0;

		invCom.maxAmmo = invCom.maxSupplies = invCom.maxChem = invCom.maxFuel = invCom.maxMaterials = -1;

		e.addComponent(pos);
		e.addComponent(hexPos);
		e.addComponent(renCom);
		e.addComponent(roCom);
		e.addComponent(invCom);

		return e;
	}

	public Entity createFleet() {
		Entity e = new Entity();

		HexPositionComponent hexPos = new HexPositionComponent();
		PositionComponent pos = new PositionComponent();
		RenderComponent renCom = new RenderComponent();
		InventoryComponent invCom = new InventoryComponent();
		WeaponComponent weapCom = new WeaponComponent();
		MovementComponent moveCom = new MovementComponent();

		hexPos.position.setOddR(0, 0);
		hexPos.descritor.inner = Color.GRAY;
		hexPos.descritor.outer = Color.GREEN;

		renCom.model = miPool.obtainInstance("untitled.g3db");

		invCom.ammo = 0;
		invCom.supplies = 0;
		invCom.materials = 0;
		invCom.fuel = 0;

		invCom.maxAmmo = invCom.maxSupplies = invCom.maxChem = invCom.maxFuel = invCom.maxMaterials = -1;

		moveCom.duration = 1;
		moveCom.elapsed = 1;

		weapCom.accuracy = 100;
		weapCom.ammoCost = 1;
		weapCom.damagePerPoint = 1;
		weapCom.range = 2;

		e.addComponent(pos);
		e.addComponent(hexPos);
		e.addComponent(renCom);
		e.addComponent(invCom);
		e.addComponent(weapCom);
		e.addComponent(moveCom);

		return e;
	}
	
	public Entity createTile(){
		Entity e = new Entity();
		
		
		
		return e;
	}
	
	public Entity destroyEntity(Entity ent){
		return null;
	}
}
