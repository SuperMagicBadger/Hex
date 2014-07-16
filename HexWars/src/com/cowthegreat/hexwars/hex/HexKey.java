package com.cowthegreat.hexwars.hex;

import com.badlogic.gdx.utils.Pool;

/**
 * the coordinate information for a hex tile. done in radial coordinates. can
 * convert to offset coords.
 * 
 * @author Cow
 * 
 */
public class HexKey implements Comparable<HexKey> {

	// pooling-----------------------------------

	private static Pool<HexKey> hexPool = new Pool<HexKey>() {
		@Override
		protected HexKey newObject() {
			// TODO Auto-generated method stub
			return new HexKey();
		}
	};

	public static HexKey obtainKey() {
		return hexPool.obtain();
	}
	
	public static HexKey obtainKey(HexKey toCopy) {
		return obtainKey(toCopy.q, toCopy.r);
	}

	public static HexKey obtainKey(int q, int r) {
		HexKey hk = obtainKey();
		hk.q = q;
		hk.r = r;
		return hk;
	}

	public HexKey release() {
		q = r = 0;
		hexPool.free(this);
		return null;
	}

	// pooling===================================

	// varblok--------------------
	public int q, r;
	// varblok====================
	
	// constructors---------------
	private HexKey() {
		q = r = 0;
	}

	private HexKey(int x, int y) {
		this.q = x;
		this.r = y;
	}
	// constructors===============

	@Override
	public int hashCode() {
		return q * 32 + r;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null){
			return false;
		}
		if (obj instanceof HexKey) {
			HexKey key = (HexKey) obj;
			return key.q == q && key.r == r;
		}
		return obj.equals(this) || super.equals(this);
	}

	@Override
	public int compareTo(HexKey o) {
		if (q == o.q) {
			return r - o.r;
		}
		return q - o.q;
	}
	
	public HexKey[] getNeighbors() {
		return new HexKey[] { new HexKey(q, r - 1), new HexKey(q - 1, r),
				new HexKey(q - 1, r + 1), new HexKey(q, r + 1),
				new HexKey(q + 1, r), new HexKey(q + 1, r - 1) };
	}
	
	public HexKey[] getNeighbors(HexKey[] arr){
		arr[0] = HexKey.obtainKey(this.q, this.r - 1);
		arr[1] = HexKey.obtainKey(this.q - 1, this.r);
		arr[2] = HexKey.obtainKey(this.q - 1, this.r + 1);
		arr[3] = HexKey.obtainKey(this.q, this.r + 1);
		arr[4] = HexKey.obtainKey(this.q + 1, this.r);
		arr[5] = HexKey.obtainKey(this.q + 1, this.r - 1);
		return arr;
	}

	@Override
	public String toString() {
		return "axial {" + q + ", " + r + "}, oddR {" + oddr_Q() + ", " + oddr_R() + "}";
	}

	public float distange(HexKey hk) {
		return (Math.abs(q - hk.q) + Math.abs(r - hk.r) + Math.abs(q + r - hk.q
				- hk.r)) / 2f;
	}

	// --------------------------------
	// Setters ------------------------
	// --------------------------------
	
	// Axial
	
	public void set(int q, int r) {
		this.q = q;
		this.r = r;
	}

	public void set(HexKey hk) {
		q = hk.q;
		r = hk.r;
	}
	
	// Offset
	
	public void setOddR(int or_q, int or_r){
		q = or_q - (or_r - (or_r&1)) / 2;
		r = or_r;
	}
	
	// --------------------------------
	// Getters ------------------------
	// --------------------------------
	
	public int Q(){
		return q;
	}
	
	public int R(){
		return r;
	}

	public int oddr_Q(){
		return q + (r - (r&1)) / 2;
	}
	
	public int oddr_R(){
		return r;
	}
	
}
