package com.aks.matchnmove;

public class TaxiForSureCostCalculator {

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
		final double T4S_SEDAN_START = 4, T4S_SEDAN_START_COST = 49, T4S_SEDAN_RATE = 16;
		final double T4S_MINI_START = 4, T4S_MINI_START_COST = 49, T4S_MINI_RATE = 14;
		double t4s_sedan_cost, t4s_mini_cost;
		double[] cost = new double[2];
		if(distanceValue > T4S_SEDAN_START) {
			t4s_sedan_cost = ((distanceValue - T4S_SEDAN_START) * T4S_SEDAN_RATE + T4S_SEDAN_START_COST);
			t4s_mini_cost = ((distanceValue - T4S_MINI_START) * T4S_MINI_RATE + T4S_MINI_START_COST);
		} else {
			t4s_sedan_cost = T4S_SEDAN_START_COST;
			t4s_mini_cost = T4S_SEDAN_START_COST;
		}
		cost[0] = t4s_sedan_cost;
		cost[1] = t4s_mini_cost;
		return cost;
	}
	
	private double[] bangaloreCost(float distanceValue){
		return delhiCost(distanceValue);
	}
	private double[] puneCost(float distanceValue){
		return delhiCost(distanceValue);
	}
	
	private double[] hyderabadCost(float distanceValue){
		final double T4S_SEDAN_START = 4, T4S_SEDAN_START_COST = 49, T4S_SEDAN_RATE = 14;
		final double T4S_MINI_START = 4, T4S_MINI_START_COST = 49, T4S_MINI_RATE = 12;
		double t4s_sedan_cost, t4s_mini_cost;
		double[] cost = new double[2];
		if(distanceValue > T4S_SEDAN_START) {
			t4s_sedan_cost = ((distanceValue - T4S_SEDAN_START) * T4S_SEDAN_RATE + T4S_SEDAN_START_COST);
			t4s_mini_cost = ((distanceValue - T4S_MINI_START) * T4S_MINI_RATE + T4S_MINI_START_COST);
		} else {
			t4s_sedan_cost = T4S_SEDAN_START_COST;
			t4s_mini_cost = T4S_SEDAN_START_COST;
		}
		cost[0] = t4s_sedan_cost;
		cost[1] = t4s_mini_cost;
		return cost;
	}
	
	private double[] mumbaiCost(float distanceValue){
		final double T4S_SEDAN_START = 6, T4S_SEDAN_START_COST = 150, T4S_SEDAN_RATE = 15;
		final double T4S_MINI_START = 6, T4S_MINI_START_COST = 150, T4S_MINI_RATE = 12;
		double t4s_sedan_cost, t4s_mini_cost;
		double[] cost = new double[2];
		if(distanceValue > T4S_SEDAN_START) {
			t4s_sedan_cost = ((distanceValue - T4S_SEDAN_START) * T4S_SEDAN_RATE + T4S_SEDAN_START_COST);
			t4s_mini_cost = ((distanceValue - T4S_MINI_START) * T4S_MINI_RATE + T4S_MINI_START_COST);
		} else {
			t4s_sedan_cost = T4S_SEDAN_START_COST;
			t4s_mini_cost = T4S_SEDAN_START_COST;
		}
		cost[0] = t4s_sedan_cost;
		cost[1] = t4s_mini_cost;
		return cost;
	}
	
}
