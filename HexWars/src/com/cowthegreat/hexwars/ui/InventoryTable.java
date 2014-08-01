package com.cowthegreat.hexwars.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.cowthegreat.hexwars.HexWars;
import com.cowthegreat.hexwars.component.InventoryComponent;
import com.cowthegreat.hexwars.component.Component.componentType;
import com.cowthegreat.hexwars.framework.Entity;

public class InventoryTable extends Table{
	InventoryComponent comp;
	
	Label supplies, sValue;
	Label materials, mValue;
	Label chems, cValue;
	Label ammo, aValue;
	Label fuel, fValue;
	
	public InventoryTable(HexWars game){
		LabelStyle ls = new LabelStyle();
		ls.font = game.assets.get("fonts/small_font_25.fnt", BitmapFont.class);
		ls.fontColor = Color.RED;

		supplies = new Label("Supplies: ", ls);
		materials = new Label("Materials: " , ls);
		chems = new Label("Chems: ", ls);
		ammo = new Label("Ammo: ", ls);
		fuel = new Label("Fuel: ", ls);
		
		sValue = new Label("0/0", ls);
		mValue = new Label("0/0", ls);
		cValue = new Label("0/0", ls);
		aValue = new Label("0/0", ls);
		fValue = new Label("0/0", ls);

		add(supplies).left();
		add(sValue).right().row();
		add(materials).left();
		add(mValue).right().row();
		add(chems).left();
		add(cValue).right().row();
		add(ammo).left();
		add(aValue).right().row();
		add(fuel).left();
		add(fValue).right();
		pack();
		
		setVisible(false);
	}
	
	public boolean display(Entity ent){
		if(ent != null){
			comp = (InventoryComponent) ent.getComponent(componentType.INVENTORY);
		} else {
			comp = null;
		}
		setVisible(comp != null);
		
		if(comp != null){
			sValue.setText(comp.supplies + (comp.maxSupplies > 0 ? ("/" + comp.maxSupplies) : "" ));
			mValue.setText(comp.materials + (comp.maxMaterials > 0 ? ("/" + comp.maxMaterials) : "" ));
			cValue.setText(comp.chem + (comp.maxChem > 0 ? ("/" + comp.maxChem) : "" ));
			aValue.setText(comp.ammo + (comp.maxAmmo > 0 ? ("/" + comp.maxAmmo) : "" ));
			fValue.setText(comp.fuel + (comp.maxFuel > 0 ? ("/" + comp.maxFuel) : "" ));
		}
		
		pack();
		return comp != null;
	}
}
