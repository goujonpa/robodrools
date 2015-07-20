package baguetteSOLO;
import java.awt.geom.Point2D;

public class Helper {

	// calculate absolute angle from absolute angle y based and relative angle
    public static double absoluteAngle(double baseAngle, double relativeAngle) {
        double angle = (baseAngle + relativeAngle) % 360;

        if (angle < 0) {
            angle += 360;
        }

        return angle;
    }

    // Transforms absolute angle into a relative angle, from an absolute base angle
    public static double relativeAngle(double baseAngle, double destAngle) {
        double angle = (destAngle - baseAngle) % 360;
        if (angle > 180) {
            angle -= 360;
        } else if (angle < -180) {
            angle += 360;
        }

        return angle;
    }

    // calculate a point's x from its base y, an angle and a distance
    public static double calculateX(double xBase, double absoluteAngle, double distance) {
        double offsetX = (Math.sin(Math.toRadians(absoluteAngle)) * distance);
        return xBase + offsetX;
    }

    // calculate a point's y from its base x , an angle and a distance
    public static double calculateY(double yBase, double absoluteAngle, double distance) {
        double offsetY = (Math.cos(Math.toRadians(absoluteAngle)) * distance);
        return yBase + offsetY;
    }

    // calculate the absolute angle between two points (origin and destination)
    public static double absoluteAngle(double xOrigin, double yOrigin, double xDestination, double yDestination) {
        double offsetX = xDestination - xOrigin;
        double offsetY = yDestination - yOrigin;

        return Math.toDegrees(Math.atan2(offsetX, offsetY));
    }

    // calculate the distance between two points
    public static double distance(double xOrigin, double yOrigin, double xDestination, double yDestination) {
        double offsetX = xDestination - xOrigin;
        double offsetY = yDestination - yOrigin;

        return Math.sqrt(offsetX*offsetX + offsetY*offsetY);
    }  
    
    // normalizes a bearing to between +180 and -180
    public static double normalizeBearing(double angle) {
    	while (angle >  180) angle -= 360;
    	while (angle < -180) angle += 360;
    	return angle;
    }
    
    // computes the absolute bearing between two points
    public static double absoluteBearing(double x1, double y1, double x2, double y2) {
    	double xo = x2-x1;
    	double yo = y2-y1;
    	double hyp = Point2D.distance(x1, y1, x2, y2);
    	double arcSin = Math.toDegrees(Math.asin(xo / hyp));
    	double bearing = 0;

    	if (xo > 0 && yo > 0) { // both pos: lower-Left
    		bearing = arcSin;
    	} else if (xo < 0 && yo > 0) { // x neg, y pos: lower-right
    		bearing = 360 + arcSin; // arcsin is negative here, actuall 360 - ang
    	} else if (xo > 0 && yo < 0) { // x pos, y neg: upper-left
    		bearing = 180 - arcSin;
    	} else if (xo < 0 && yo < 0) { // both neg: upper-right
    		bearing = 180 - arcSin; // arcsin is negative here, actually 180 + ang
    	}

    	return bearing;
    }    
    
    // Calculates the firePower in function of the enemy distance
    public static double firePower(double enemyDistance) {
    	return (Math.min(500 / enemyDistance, 3));
    }
    
    // Calculates the bulletSpeed in function of the firePower
    public static double bulletSpeed(double firePower){
    	return (20 - firePower * 3);
    }
    
    // time calculation (mainly for predictive aiming)
    // distance = rate * time, solved for time
    public static long time(double enemyDistance, double bulletSpeed) {
    	return (long)(enemyDistance / bulletSpeed);
    }
    
    // get the enemy's X
    public static double enemyX(double robotX, double robotHeading, double enemyBearing, double distance){
    	double absBearingDeg = robotHeading + enemyBearing;
    	if (absBearingDeg < 0) absBearingDeg += 360;
    	return (robotX + Math.sin(Math.toRadians(absBearingDeg)) * distance);
    	
    }
    
    // get the enemy's Y
    public static double enemyY(double robotY, double robotHeading, double enemyBearing, double distance){
    	double absBearingDeg = robotHeading + enemyBearing;
    	if (absBearingDeg < 0) absBearingDeg += 360;
    	return (robotY + Math.cos(Math.toRadians(absBearingDeg)) * distance);
    	
    }
    
    // predict enemy future X
    public static double enemyFutureX(double enemyX, double enemyHeading, double velocity, long when){
    	return (enemyX + Math.sin(Math.toRadians(enemyHeading)) * velocity * when);	
    }
    
    // predict enemy future Y
    public static double enemyFutureY(double enemyY, double enemyHeading, double velocity, long when){
    	return (enemyY + Math.cos(Math.toRadians(enemyHeading)) * velocity * when); 	
    }
}


