package baguette;

import robocode.ScannedRobotEvent;
import robocode.Robot;

public class AdvancedEnemyState extends EnemyState {
	private double x;
	private double y;
	
	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}
	
	public void setX(double x){
		this.x = x;
	}
	
	public void setY(double y){
		this.y = y;
	}
	
	public AdvancedEnemyState(){
		reset();
	}
	
	public void reset(){
		super.reset();
		this.x = 0;
		this.y = 0;
	}
	
	public void update(ScannedRobotEvent e, Robot r){
		super.update(e);
		double absBearingDeg = (r.getHeading() + e.getBearing());
		if (absBearingDeg < 0) absBearingDeg += 360;
		// 0 deg is north, so we use sin to get the x
		this.x = r.getX() + Math.sin(Math.toRadians(absBearingDeg)) * e.getDistance();
		// idem => we use cos
		this.y = r.getY() + Math.cos(Math.toRadians(absBearingDeg)) * e.getDistance();
	}
	
	public double getFutureX(long when){
		return x + Math.sin(Math.toRadians(getHeading())) * getVelocity() * when;
	}
	
	public double getFutureY(long when){
		return y + Math.cos(Math.toRadians(getHeading())) * getVelocity() * when;
	}
}
