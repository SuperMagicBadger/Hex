package com.cowthegreat.hexwars;

import java.io.File;

import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2.Settings;

public class AtlasBuilder {
	
	public static String[] sources = new String[]{
		"../images/tiles"
	};
	
	public static String[] names = new String[]{
		"default_tiles"
	};
	
	public static String appDest = "../HexWars-android/assets";
	public static String internalDest = "./";
	
	public static void traverse(String directoryName){
		// validate directory
		File dir = new File(directoryName);
		if(!dir.isDirectory()) {
			System.err.println("file is not a directory");
			return;
		}

		// settings
		Settings settings = new Settings();
		settings.minHeight = settings.minWidth = 2;
		settings.maxHeight = settings.maxWidth = 1024;
		settings.filterMag = TextureFilter.Nearest;
		settings.filterMin = TextureFilter.Nearest;
		settings.pot = false;
		
		// build atlas
		for(File candidate : dir.listFiles()){
			System.out.println("packing candidate: " + candidate.getName());
			if(candidate.isDirectory()){
				build(settings, candidate.getAbsolutePath(), candidate.getName());
			}
		}
	}
	
	public static void build(Settings packerSettings, String source, String assetName){
		TexturePacker2.process(packerSettings, source, appDest, assetName);
		TexturePacker2.process(packerSettings, source, internalDest, assetName);
	}
}
