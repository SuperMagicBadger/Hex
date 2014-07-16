package com.cowthegreat.hexwars.hex;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class HexMath {
	private static final HexMath single = new HexMath();
	public float side;
	public Orientation o;

	public enum Orientation {
		POINTY, FLAT
	}

	public static HexMath get(){
		return single;
	}
	
	// ------------------------------------------
	// Pixel Data -------------------------------
	// ------------------------------------------
	
	public float getWidth() {
		float result = 0;

		switch (o) {
		case POINTY:
			result = (float) (Math.sqrt(3) / 2f * side * 2);
			break;
		case FLAT:
			result = side * 2;
			break;
		}

		return result;
	}

	public float getHeight() {
		float result = 0;

		switch (o) {
		case FLAT:
			result = side * 2;
			break;
		case POINTY:
			result = (float) (Math.sqrt(3) / 2f * side * 2);
			break;
		}

		return result;
	}

	public float setSideByWidth(float width) {

		switch (o) {
		case POINTY:
			side = (float) (width / Math.sqrt(3));
			break;
		case FLAT:
			side = 0;
			break;
		}

		return side;
	}

	public float horizonalSpaceing() {
		float result = 0;

		switch (o) {
		case FLAT:
			result = 3f * getWidth() / 4f;
			break;
		case POINTY:
			result = getWidth();
			break;
		}

		return result;
	}

	public float verticalSpaceing() {
		float result = 0;

		switch (o) {
		case FLAT:
			result = getHeight();
			break;
		case POINTY:
			result = 3f * getHeight() / 4f;
			break;
		}

		return result;
	}

	private float magicSin(float size, float i) {
		return size * MathUtils.sinDeg((float) (60 * i));
	}

	private float magicCos(float size, float i) {
		return size * MathUtils.cosDeg((float) (60 * i));
	}

	public Vector2[] getPoints(float x, float y) {
		Vector2[] result = new Vector2[6];

		switch (o) {
		case POINTY:
			result[0] = new Vector2(x + magicCos(side, 0.5f), (y
					+ magicSin(side, 0.5f)));
			result[1] = new Vector2(x + magicCos(side, 1.5f), (y
					+ magicSin(side, 1.5f)));
			result[2] = new Vector2(x + magicCos(side, 2.5f), (y
					+ magicSin(side, 2.5f)));
			result[3] = new Vector2(x + magicCos(side, 3.5f), (y
					+ magicSin(side, 3.5f)));
			result[4] = new Vector2(x + magicCos(side, 4.5f), (y
					+ magicSin(side, 4.5f)));
			result[5] = new Vector2(x + magicCos(side, 5.5f), (y
					+ magicSin(side, 5.5f)));
			break;
		case FLAT:
			result[0] = new Vector2(x + magicCos(side, 0f), (y
					+ magicSin(side, 0f)));
			result[1] = new Vector2(x + magicCos(side, 1f), (y
					+ magicSin(side, 1f)));
			result[2] = new Vector2(x + magicCos(side, 2f), (y
					+ magicSin(side, 2f)));
			result[3] = new Vector2(x + magicCos(side, 3f), (y
					+ magicSin(side, 3f)));
			result[4] = new Vector2(x + magicCos(side, 4f), (y
					+ magicSin(side, 4f)));
			result[5] = new Vector2(x + magicCos(side, 5f), (y
					+ magicSin(side, 5f)));
			break;
		}

		return result;
	}
	
	// ------------------------------------------
	// Hex Key Conversion -----------------------
	// ------------------------------------------
	
	public Vector2 hexToPixel(int q, int r, Vector2 pos) {
		switch (o) {
		case POINTY:
			pos.x = (float) (side * Math.sqrt(3) * (q + r / 2f));
			pos.y = (side * 3f / 2f * r);
			break;
		case FLAT:
			pos.x = side * 3f / 2f * q;
			pos.y = ((float) (side * Math.sqrt(3) * (r + q / 2f)));
			break;
		}
		return pos;
	}

	public Vector2 hexToPixel(HexKey key) {
		return hexToPixel(key.q, key.r, new Vector2());
	}
	
	public Vector2 hexToPixel(HexKey hk, Vector2 pos){
		return hexToPixel(hk.q, hk.r, pos);
	}

	public HexKey pixelToHex(float x, float y) {
		HexKey result = HexKey.obtainKey();
		
		// fast translation
		switch (o) {
		case POINTY:
			result.q = (int) ((1f / 3f * Math.sqrt(3f) * x - 1f / 3f * y) / side);
			result.r = (int) (2f / 3f * y / side);
			break;
		case FLAT:
			result.q = (int) (2f / 3f * x / side);
			result.r = (int) ((1f / 3f * Math.sqrt(3f) * y - 1f / 3f * x) / side);
			break;
		}

		// error check with distance
		for (int j = 0; j < 2; j++) {
			HexKey[] neighbors = result.getNeighbors();
			Vector2 temp = hexToPixel(result);
			float min = temp.dst(x, y);
			for (int i = 0; i < neighbors.length; i++) {
				temp = hexToPixel(neighbors[i]);
				float d = temp.dst(x, y);
				if (d < min) {
					min = d;
					result.release();
					result = neighbors[i];
				} else {
					neighbors[i].release();
				}
			}
		}
		return result;
	}

	// ------------------------------------------
	// Vector Conversion ------------------------
	// ------------------------------------------
	
	public Vector2 axialToOddR(Vector2 pos) {
		pos.x = pos.x + (pos.y - ((int)(pos.y)&1)) / 2;
		pos.y = pos.y;
		return pos;
	}
	
	public Vector2 oddRToAxial(Vector2 pos){
		pos.x = pos.x - (pos.y - ((int)(pos.y)&1)) / 2;
		pos.y = pos.y;
		return pos;
	}

	private Vector2 ptaTemp = new Vector2();
	public Vector2 pixelToAxial(Vector2 pos){
		
		
		// fast translation
		switch (o) {
		case POINTY:
			ptaTemp.x = (int) ((1f / 3f * Math.sqrt(3f) * pos.x - 1f / 3f * pos.y) / side);
			ptaTemp.y = (int) (2f / 3f * pos.y / side);
			break;
		case FLAT:
			ptaTemp.x = (int) (2f / 3f * pos.x / side);
			ptaTemp.y = (int) ((1f / 3f * Math.sqrt(3f) * pos.y - 1f / 3f * pos.x) / side);
			break;
		}
		
		// check neighbors
		
		
		return pos;
	}
	
	public Vector2 pixelToOddR(Vector2 pos){
		return axialToOddR(pixelToAxial(pos));
	}
	
	public Vector2 axialToPixel(Vector2 pos){
		switch (o) {
		case POINTY:
			pos.x = (float) (side * Math.sqrt(3) * (pos.x + pos.y / 2f));
			pos.y = (side * 3f / 2f * pos.y);
			break;
		case FLAT:
			pos.y = ((float) (side * Math.sqrt(3) * (pos.y + pos.x / 2f)));
			pos.x = side * 3f / 2f * pos.x;
			break;
		}
		return pos;
	}
	
	public Vector2 oddRToPixel(Vector2 pos){
		return axialToPixel(axialToOddR(pos));
	}
}
