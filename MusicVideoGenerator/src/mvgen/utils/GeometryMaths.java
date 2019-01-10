package mvgen.utils;

import java.awt.Point;

public class GeometryMaths {


	
	public static Point getARandomPointAround(Point start, double maxDistance)
	{
		int randDist = (int)(Math.random() * 2 / 3 * maxDistance + maxDistance / 3);
		int angle = (int)(Math.random() * 360.0);
		
		return getThePointThere(start, randDist, angle);
	}
	
	public static Point getARandomPointAt(Point start, double distance)
	{
		int angle = (int)(Math.random() * 360.0);
		
		return getThePointThere(start, (int)distance, angle);
	}
	
	public static Point getThePointThere(Point start, int distance, int angleInDegrees)
	{
		int newX = (int)(start.getX() + distance * Math.cos(angleInDegrees * Math.PI / 180));
		int newY = (int)(start.getY() + distance * Math.sin(angleInDegrees * Math.PI / 180));
		
		return new Point(newX, newY);
	}
}
