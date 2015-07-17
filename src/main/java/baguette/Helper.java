package baguette;

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
    
    public static double firePower(double distance, double energy){
    	double power = Math.min(400/distance, 0.1*energy);
    	return power;
    }
}
