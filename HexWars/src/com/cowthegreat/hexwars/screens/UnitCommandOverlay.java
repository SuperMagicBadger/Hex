package com.cowthegreat.hexwars.screens;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.cowthegreat.hexwars.HexWars;

public class UnitCommandOverlay extends Table {
	
	HexWars game;
	
	ArrayList<Button> buttonList;
	
	public static interface UCOListener{
		public void onSelect(String buttonTag);
	}
	
	public UnitCommandOverlay(HexWars game){
		this.game = game;
		
		TextButtonStyle tbs = new TextButtonStyle();
		tbs.font = game.assets.get("fonts/small_font_25.fnt", BitmapFont.class);
		
		TextButton move = new TextButton("move", tbs);
		TextButton attack = new TextButton("attack", tbs);
		
		buttonList = new ArrayList<Button>();
		buttonList.add(move);
		buttonList.add(attack);
		
		add(move).row();
		add(attack);
		
		pack();
	}
	
}
