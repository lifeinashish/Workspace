package com.aks.matchnmove;

public class OlaCostCalculator {
	
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
		final double OLA_SEDAN_START = 8, OLA_SEDAN_START_COST = 200, OLA_SEDAN_RATE = 18;
		final double OLA_MINI_START = 4, OLA_MINI_START_COST = 100, OLA_MINI_RATE = 10;
		double ola_sedan_cost, ola_mini_cost;
		double[] cost = new double[2];
		if(distanceValue > OLA_SEDAN_START) {
			ola_sedan_cost = ((distanceValue - OLA_SEDAN_START) * OLA_SEDAN_RATE + OLA_SEDAN_START_COST);
		} else {
			ola_sedan_cost = OLA_SEDAN_START_COST;
		}
		if(distanceValue > OLA_MINI_START) {
			ola_mini_cost = ((distanceValue - OLA_MINI_START) * OLA_MINI_RATE + OLA_MINI_START_COST);
		} else {
			ola_mini_cost = OLA_MINI_START_COST;
		}
		cost[0] = ola_sedan_cost;
		cost[1] = ola_mini_cost;
		return cost;
	}
	
	private double[] bangaloreCost(float distanceValue){
		final double OLA_SEDAN_START = 8, OLA_SEDAN_START_COST = 150, OLA_SEDAN_RATE = 13;
		final double OLA_MINI_START = 6, OLA_MINI_START_COST = 100, OLA_MINI_RATE = 10;
		double ola_sedan_cost, ola_mini_cost;
		double[] cost = new double[2];
		if(distanceValue > OLA_SEDAN_START) {
			ola_sedan_cost = ((distanceValue - OLA_SEDAN_START) * OLA_SEDAN_RATE + OLA_SEDAN_START_COST);
		} else {
			ola_sedan_cost = OLA_SEDAN_START_COST;
		}
		if(distanceValue > OLA_MINI_START) {
			ola_mini_cost = ((distanceValue - OLA_MINI_START) * OLA_MINI_RATE + OLA_MINI_START_COST);
		} else {
			ola_mini_cost = OLA_MINI_START_COST;
		}
		cost[0] = ola_sedan_cost;
		cost[1] = ola_mini_cost;
		return cost;
	}
	private double[] mumbaiCost(float distanceValue){
		final double OLA_SEDAN_START = 4, OLA_SEDAN_START_COST = 150, OLA_SEDAN_RATE = 21;
		final double OLA_MINI_START = 4, OLA_MINI_START_COST = 100, OLA_MINI_RATE = 15;
		double ola_sedan_cost, ola_mini_cost;
		double[] cost = new double[2];
		if(distanceValue > OLA_SEDAN_START) {
			ola_sedan_cost = ((distanceValue - OLA_SEDAN_START) * OLA_SEDAN_RATE + OLA_SEDAN_START_COST);
		} else {
			ola_sedan_cost = OLA_SEDAN_START_COST;
		}
		if(distanceValue > OLA_MINI_START) {
			ola_mini_cost = ((distanceValue - OLA_MINI_START) * OLA_MINI_RATE + OLA_MINI_START_COST);
		} else {
			ola_mini_cost = OLA_MINI_START_COST;
		}
		cost[0] = ola_sedan_cost;
		cost[1] = ola_mini_cost;
		return cost;
	}
	
	private double[] hyderabadCost(float distanceValue){
		final double OLA_SEDAN_START = 6, OLA_SEDAN_START_COST = 150, OLA_SEDAN_RATE = 14;
		final double OLA_MINI_START = 4, OLA_MINI_START_COST = 100, OLA_MINI_RATE = 10;
		double ola_sedan_cost, ola_mini_cost;
		double[] cost = new double[2];
		if(distanceValue > OLA_SEDAN_START) {
			ola_sedan_cost = ((distanceValue - OLA_SEDAN_START) * OLA_SEDAN_RATE + OLA_SEDAN_START_COST);
		} else {
			ola_sedan_cost = OLA_SEDAN_START_COST;
		}
		if(distanceValue > OLA_MINI_START) {
			ola_mini_cost = ((distanceValue - OLA_MINI_START) * OLA_MINI_RATE + OLA_MINI_START_COST);
		} else {
			ola_mini_cost = OLA_MINI_START_COST;
		}
		cost[0] = ola_sedan_cost;
		cost[1] = ola_mini_cost;
		return cost;
	}
	
	private double[] puneCost(float distanceValue){
		final double OLA_SEDAN_START = 6, OLA_SEDAN_START_COST = 100, OLA_SEDAN_RATE = 18;
		final double OLA_MINI_START = 5, OLA_MINI_START_COST = 100, OLA_MINI_RATE = 12;
		double ola_sedan_cost, ola_mini_cost;
		double[] cost = new double[2];
		if(distanceValue > OLA_SEDAN_START) {
			ola_sedan_cost = ((distanceValue - OLA_SEDAN_START) * OLA_SEDAN_RATE + OLA_SEDAN_START_COST);
		} else {
			ola_sedan_cost = OLA_SEDAN_START_COST;
		}
		if(distanceValue > OLA_MINI_START) {
			ola_mini_cost = ((distanceValue - OLA_MINI_START) * OLA_MINI_RATE + OLA_MINI_START_COST);
		} else {
			ola_mini_cost = OLA_MINI_START_COST;
		}
		cost[0] = ola_sedan_cost;
		cost[1] = ola_mini_cost;
		return cost;
	}
}
