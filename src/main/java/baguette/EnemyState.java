package baguette;

import robocode.ScannedRobotEvent;

public class EnemyState {
// EnemyState class : used to keep informations on the enemy
	private String name;
	private double bearing;
	private double distance;
	private double energy;
	private double heading;
	private double velocity;
	
	public EnemyState() {
		reset();
	}
	public String getName() {
		return this.name;
	}
	public double getBearing() {
		return this.bearing;
	}
	public double getDistance() {
		return this.distance;
	}
	public double getEnergy() {
		return this.energy;
	}
	public double getHeading() {
		return this.heading;
	}
	public double getVelocity() {
		return this.velocity;
	}
	public void update(ScannedRobotEvent e) {
		this.name = e.getName();
		this.bearing = e.getBearing();
		this.distance = e.getDistance();
		this.energy = e.getEnergy();
		this.heading = e.getHeading();
		this.velocity = e.getVelocity();
	}
	public void reset() {
		this.name = "";
		this.bearing = 0;
		this.distance = 0;
		this.energy = 0;
		this.heading = 0;
		this.velocity = 0;
	}
	public boolean none() {
		if (this.name.length() == 0) {
			return true;
		} else {
			return false;
		}
	}
}