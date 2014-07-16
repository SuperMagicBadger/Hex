package com.cowthegreat.hexwars;

import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

public class DiceSet {
	public static void main(String[] args){
		DiceSet dice = DiceSet.get();
		Vector<Integer> vec = new Vector<Integer>();
		
		int n = 1000;
		int d = 3;
		int s = 4;
		
		// normal roll
		for(int i = 0; i < n; i++){
			int r = dice.roll(d, s);
			vec.add(r);
		}
		

		int[] arr = new int[d * s];
		
		for(int i = 0; i < arr.length; i++){
			arr[i] = 0;
		}

		for(int i = 0; i < vec.size(); i++){
			arr[vec.get(i) - 1]++;
		}
		
		for(int i = 0; i < arr.length; i++){
			System.out.print((i + 1) + ":" + arr[i] + " ");
		}

		System.out.println();
		
		// rerroll
		vec.clear();
		for(int i = 0; i < n; i++){
			int r = dice.rerollRoll(d, s, false, 1);
			vec.add(r);
		}
		
		arr = new int[d * s];
		
		for(int i = 0; i < arr.length; i++){
			arr[i] = 0;
		}

		for(int i = 0; i < vec.size(); i++){
			arr[vec.get(i) - 1]++;
		}
		
		for(int i = 0; i < arr.length; i++){
			System.out.print((i + 1) + ":" + arr[i] + " ");
		}
		
		System.out.println();
		vec.clear();
		for(int i = 0; i < n; i++){
			int r = dice.rerollRoll(d, s, true, 1);
			vec.add(r);
		}
		
		arr = new int[d * s];
		
		for(int i = 0; i < arr.length; i++){
			arr[i] = 0;
		}

		for(int i = 0; i < vec.size(); i++){
			arr[vec.get(i) - 1]++;
		}
		
		for(int i = 0; i < arr.length; i++){
			System.out.print((i + 1) + ":" + arr[i] + " ");
		}

		System.out.println();
	
		// drop
		vec.clear();
		for(int i = 0; i < n; i++){
			int r = dice.dropRoll(d, s, false, 1);
			vec.add(r);
		}
		
		arr = new int[d * s];
		
		for(int i = 0; i < arr.length; i++){
			arr[i] = 0;
		}

		for(int i = 0; i < vec.size(); i++){
			arr[vec.get(i) - 1]++;
		}
		
		for(int i = 0; i < arr.length; i++){
			System.out.print((i + 1) + ":" + arr[i] + " ");
		}
		
		System.out.println();
		vec.clear();
		for(int i = 0; i < n; i++){
			int r = dice.dropRoll(d, s, true, 1);
			vec.add(r);
		}
		
		arr = new int[d * s];
		
		for(int i = 0; i < arr.length; i++){
			arr[i] = 0;
		}

		for(int i = 0; i < vec.size(); i++){
			arr[vec.get(i) - 1]++;
		}
		
		for(int i = 0; i < arr.length; i++){
			System.out.print((i + 1) + ":" + arr[i] + " ");
		}

	}
	
	
	private static final DiceSet single = new DiceSet();
	private Random rng;
	
	private DiceSet(){
		rng = new Random();
	}
	
	public static DiceSet get(){
		return single;
	}
	
	public int roll(int num, int dice){
		int total = 0;
		for(int i = 0; i < num; i++){
			total += rng.nextInt(dice) + 1;
		}
		return total;
	}
	
	public int roll(int dice){
		return rng.nextInt(dice) + 1;
	}
	
	/**
	 * drop either the lowest or the highest roll
	 * @param num dice count
	 * @param dice number of sides
	 * @param lowest drop the lowest?
	 * @return result for the roll
	 */
	public int dropRoll(int num, int dice, boolean lowest, int dropCount){
		int[] arr = new int[num];
		int sum = 0;
		for(int i = 0; i < arr.length; i++){
			arr[i] = roll(dice);
			sum += arr[i];
		}
		
		Arrays.sort(arr);
		
		if(lowest){
			for(int i = 0; i < dropCount; i++){
				sum -= arr[i];
			}
		} else {
			int i = arr.length - 1;
			int c = 0;
			while(c < dropCount){
				sum -= arr[i];
				i--;
				c++;
			}
		}
		
		
		
		return sum;
	}
	
	/**
	 * reroll either the lowest or highest, depending on the "lowest" parameter
	 * @param num dice count
	 * @param dice number of sides
	 * @param lowest set which value to reroll
	 * @return result of te roll
	 */
	public int rerollRoll(int num, int dice, boolean lowest, int rerollCount){
		int[] arr = new int[num];
		int rerollIndex = 0;
		int sum = 0;
		
		for(int i = 0; i < num; i++){
			arr[i] = roll(dice);
			if(lowest) {
				if(arr[rerollIndex] > arr[i]){
					rerollIndex = i;
				}
			} else {
				if(arr[rerollIndex] < arr[i]){
					rerollIndex = i;
				}
			}
		}
		
		for(int i = 0; i < num; i++){
			if(i != rerollIndex){
				sum += arr[i];
			} else {
				sum += roll(dice);
			}
		}
		
		return sum;
	}
	
	public float uniformNoise(){
		return rng.nextFloat();
	}
	
}
