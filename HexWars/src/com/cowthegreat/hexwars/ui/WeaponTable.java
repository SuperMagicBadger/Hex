package com.cowthegreat.hexwars.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.cowthegreat.hexwars.HexWars;
import com.cowthegreat.hexwars.component.WeaponComponent;
import com.cowthegreat.hexwars.component.Component.componentType;
import com.cowthegreat.hexwars.framework.Entity;

public class WeaponTable extends Table{
	WeaponComponent comp;
	
	Label accuracy, aValue;
	Label range, rValue;
	Label damage, dValue;
	Label ammoCost, amValue;
	
	
	
	public WeaponTable(HexWars game){
		comp = null;
		
		LabelStyle ls = new LabelStyle();
		ls.font = game.assets.get("fonts/small_font_25.fnt", BitmapFont.class);
		ls.fontColor = Color.RED;
		
		damage = new Label("Damage: ", ls);
		accuracy = new Label("Acc: ", ls);
		range = new Label("Range: ", ls);
		ammoCost = new Label("Ammo: ", ls);
		
		aValue = new Label("", ls);
		rValue = new Label("", ls);
		dValue = new Label("", ls);
		amValue = new Label("", ls);
		
		add(damage).left();
		add(dValue).right().row();
		add(accuracy).left();
		add(aValue).right().row();
		add(range).left();
		add(rValue).right().row();
		add(ammoCost).left();
		add(amValue).right().row();
		
		pack();
		
		setVisible(false);
	}
	
	public boolean display(Entity ent){
		if(ent == null){
			comp = null;
		} else {
			comp = (WeaponComponent) ent.getComponent(componentType.WEAPONS);
		}
		
		if(comp == null){
			setVisible(false);
			return false;
		}

		setVisible(true);
		
		dValue.setText("" + comp.accuracy);
		aValue.setText("" + comp.damagePerPoint);
		rValue.setText("" + comp.range);
		amValue.setText("" + comp.ammoCost);
		
		pack();
		
		return true;
	}
}
