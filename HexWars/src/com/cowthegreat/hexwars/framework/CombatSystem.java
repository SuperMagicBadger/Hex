package com.cowthegreat.hexwars.framework;

import com.cowthegreat.hexwars.component.Component.componentType;
import com.cowthegreat.hexwars.component.HealthComponent;
import com.cowthegreat.hexwars.component.WeaponComponent;

public class CombatSystem {
	public static void main(String[] args){
		Entity a, b;
		
		a = new Entity();
		
		WeaponComponent wep = new WeaponComponent();
		wep.accuracy = 100;
		wep.ammoCost = 1;
		wep.damagePerPoint = 100;
		wep.range = 2;
		a.addComponent(wep);
		
		HealthComponent health = new HealthComponent();
		health.armor = 2;
		health.health = 10;
		health.maxHealth = 100;
		a.addComponent(health);
		
		b = new Entity();
		
		System.out.println(estimateDamage(a, b));
		
		HealthComponent health2 = new HealthComponent();
		health2.armor = 2;
		health2.health = 100;
		health2.maxHealth = 100;
		b.addComponent(health2);
		
		System.out.println(estimateDamage(a, b));
	}
	
	public static boolean isDead(Entity entity){
		HealthComponent health = (HealthComponent) entity.getComponent(componentType.HEALTH);
		if(health == null || health.health > 0){
			return false;
		}
		return true;
	}
	
	public static void doCombat(Entity attacker, Entity defender){
		HealthComponent target = (HealthComponent) defender.getComponent(componentType.HEALTH);
		
		int damage = estimateDamage(attacker, defender);
		
		target.health -= damage;
	}
	
	public static int estimateDamage(Entity attacker, Entity defender){
		WeaponComponent weapon = (WeaponComponent) attacker.getComponent(componentType.WEAPONS);
		HealthComponent source = (HealthComponent) attacker.getComponent(componentType.HEALTH);
		HealthComponent target = (HealthComponent) defender.getComponent(componentType.HEALTH);
		
		if(weapon == null || target == null){
			return 0;
		}
		
		int damage = weapon.damagePerPoint;
		
		if(source != null){
			damage *= (float) source.health / (float) source.maxHealth;
		}
		
		damage /= target.armor;
		damage = damage <= 0 ? 1 : damage;
		
		return damage;
	}
}
