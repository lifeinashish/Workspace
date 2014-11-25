package com.aks.matchnmove;

public class MeruCostCalculator {
	
	public double[] costCalulator(float distanceValue, int location_code) {
		switch(location_code) {
		case 1:
			return delhiCost(distanceValue);
		case 2:
			return mumbaiCost(distanceValue);
		case 3:
			return hyderabadCost(distanceValue);
		case 4:
			return bangaloreCost(distanceValue);
		case 5:
			return puneCost(distanceValue);
		}
		return null;
	}

	private double[] delhiCost(float distanceValue){
		final double MERU_START = 3, MERU_DAY_START_COST = 69, MERU_DAY_RATE = 23;
		final double MERU_NIGHT_START_COST = 86.25, MERU_NIGHT_RATE = 28.75;
		double meru_day_cost, meru_night_cost;
		double[] cost = new double[2];
		if(distanceValue > MERU_START) {
			meru_day_cost = ((distanceValue - MERU_START) * MERU_DAY_RATE + MERU_DAY_START_COST);
			meru_night_cost = ((distanceValue - MERU_START) * MERU_NIGHT_RATE + MERU_NIGHT_START_COST);
		} else {
			meru_day_cost = MERU_DAY_START_COST;
			meru_night_cost = MERU_NIGHT_START_COST;
		}
		cost[0] = meru_day_cost;
		cost[1] = meru_night_cost;
		return cost;
	}
	
	private double[] mumbaiCost(float distanceValue){
		final double MERU_START = 1, MERU_DAY_START_COST = 27, MERU_DAY_RATE = 20;
		final double MERU_NIGHT_START_COST = 33.75, MERU_NIGHT_RATE = 25;
		double meru_day_cost, meru_night_cost;
		double[] cost = new double[2];
		if(distanceValue > MERU_START) {
			meru_day_cost = ((distanceValue - MERU_START) * MERU_DAY_RATE + MERU_DAY_START_COST);
			meru_night_cost = ((distanceValue - MERU_START) * MERU_NIGHT_RATE + MERU_NIGHT_START_COST);
		} else {
			meru_day_cost = MERU_DAY_START_COST;
			meru_night_cost = MERU_NIGHT_START_COST;
		}
		cost[0] = meru_day_cost;
		cost[1] = meru_night_cost;
		return cost;
	}
	
	private double[] bangaloreCost(float distanceValue){
		final double MERU_START = 4, MERU_DAY_START_COST = 80, MERU_DAY_RATE = 19.5;
		final double MERU_NIGHT_START_COST = 88, MERU_NIGHT_RATE = 21.45;
		double meru_day_cost, meru_night_cost;
		double[] cost = new double[2];
		if(distanceValue > MERU_START) {
			meru_day_cost = ((distanceValue - MERU_START) * MERU_DAY_RATE + MERU_DAY_START_COST);
			meru_night_cost = ((distanceValue - MERU_START) * MERU_NIGHT_RATE + MERU_NIGHT_START_COST);
		} else {
			meru_day_cost = MERU_DAY_START_COST;
			meru_night_cost = MERU_NIGHT_START_COST;
		}
		cost[0] = meru_day_cost;
		cost[1] = meru_night_cost;
		return cost;
	}
	
	private double[] hyderabadCost(float distanceValue){
		final double MERU_START = 2, MERU_DAY_START_COST = 40, MERU_DAY_RATE = 21;
		final double MERU_NIGHT_START_COST = 50	, MERU_NIGHT_RATE = 26.25;
		double meru_day_cost, meru_night_cost;
		double[] cost = new double[2];
		if(distanceValue > MERU_START) {
			meru_day_cost = ((distanceValue - MERU_START) * MERU_DAY_RATE + MERU_DAY_START_COST);
			meru_night_cost = ((distanceValue - MERU_START) * MERU_NIGHT_RATE + MERU_NIGHT_START_COST);
		} else {
			meru_day_cost = MERU_DAY_START_COST;
			meru_night_cost = MERU_NIGHT_START_COST;
		}
		cost[0] = meru_day_cost;
		cost[1] = meru_night_cost;
		return cost;
	}
	
	private double[] puneCost(float distanceValue){
		final double MERU_START = 8, MERU_DAY_START_COST = 200, MERU_DAY_RATE = 20;
		final double MERU_NIGHT_START_COST = 250	, MERU_NIGHT_RATE = 25;
		double meru_day_cost, meru_night_cost;
		double[] cost = new double[2];
		if(distanceValue > MERU_START) {
			meru_day_cost = ((distanceValue - MERU_START) * MERU_DAY_RATE + MERU_DAY_START_COST);
			meru_night_cost = ((distanceValue - MERU_START) * MERU_NIGHT_RATE + MERU_NIGHT_START_COST);
		} else {
			meru_day_cost = MERU_DAY_START_COST;
			meru_night_cost = MERU_NIGHT_START_COST;
		}
		cost[0] = meru_day_cost;
		cost[1] = meru_night_cost;
		return cost;
	}
}
