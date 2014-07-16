package com.cowthegreat.hexwars.units;

public class ResourceNode {
	
	public static final int SUPPLY = 0;
	public static final int SALV0 = 1;
	public static final int FUEL = 2;
	public static final int MATERIALS = 3;
	public static final int CHEMICALS = 4;
	private static final int _MAX = 5;
	
	int[] res = new int[_MAX];
	int[] maxValue = new int[_MAX];

	public void set(int type, int amount) {
		res[type] = amount;
	}
	
	public void setMax(int type, int amount){
		
	}
	
	public int get(int type, int amount) {
		return res[type];
	}
	
	public int request(int type, int amnt){
		// sufficient stores
		if(res[type] >= amnt){
			res[type] -= amnt;
			return amnt;
		}
		// insufficient stores
		int ret = res[type];
		res[type] = 0;
		return ret;
	}
	
	public int place(int type, int amnt){
		// sufficient stores
		if(maxValue[type] - res[type] >= amnt || maxValue[type] < 0){
			res[type] += amnt;
			return amnt;
		}
		// insufficient stores
		int ret = maxValue[type] - res[type];
		res[type] = maxValue[type];
		return ret;
	}
}
