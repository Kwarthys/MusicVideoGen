package mvgen.process.videogen.graphical;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class LightningGenerator {

	private ArrayList<Line> lines = new ArrayList<>();
	
	private int maxIterations;
	private int iterations = 0;
	
	private ArrayList<Point[]> movePoints = new ArrayList<>();
	
	public void drawMeALightning(BufferedImage toAlter, Point start, Point end, int iterations)
	{
		this.maxIterations = iterations;
		buildModel(start, end);
		Graphics2D g = (Graphics2D) toAlter.getGraphics();
		g.setColor(Color.RED);
		g.fillOval(start.x-10, start.y-10, 20, 20);
		g.fillOval(end.x-10, end.y-10, 20, 20);
		g.setColor(Color.WHITE);
		for(Line l : lines)
		{
			g.setStroke(new BasicStroke((maxIterations - l.index) * 0.8f));
			g.drawLine(l.start.x, l.start.y, l.end.x, l.end.y);
		}
		
		/*
		for(int i = 0; i < movePoints.size(); i++)
		{
			Point[] move = movePoints.get(i);
			g.setColor(Color.BLUE);
			g.drawLine(move[0].x, move[0].y, move[1].x, move[1].y);
			g.fillOval(move[0].x-10, move[0].y-10, 20, 20);
			g.setColor(Color.WHITE);
			g.drawString(String.valueOf(i), move[0].x, move[0].y);
		}
		*/
	}
	
	private void buildModel(Point start, Point end)
	{
		lines = new ArrayList<>();
		lines.add(new Line(start, end, 0));
		iterateModel();
	}
	
	private void iterateModel()
	{
		if(iterations >= maxIterations)
		{
			System.out.println("Lightning done");
			return;
		}
		
		int added = 0;
		int i = 0;
		while(i < this.lines.size() - added)
		{
			/*** Break the line ***/
			Line lineToBreak = this.lines.get(i);
			Point middle = new Point((lineToBreak.end.x*2 + lineToBreak.start.x)/3, (lineToBreak.end.y*2 + lineToBreak.start.y)/3);
			
			Point movedPoint = getARandomPointAround(middle, lineToBreak.getLenght()/4);
			
			this.lines.add(i+1, new Line(movedPoint, lineToBreak.end, lineToBreak.index));
			
			lineToBreak.setEndPoint(movedPoint);
			
			Point[] tmp = {new Point(middle), new Point(movedPoint)};
			
			movePoints.add(tmp);
			
			i+=2;
			
			/*** Create a new line ***/
			this.lines.add(new Line(movedPoint, getARandomPointAround(movedPoint, lineToBreak.getLenght()/2), lineToBreak.index+1));
			++added;
		}
		
		++iterations;
		iterateModel();
		
	}
	
	public Point getARandomPointAround(Point start, double maxDistance)
	{
		int randDist = (int)(Math.random() * 2 / 3 * maxDistance + maxDistance / 3);
		int angle = (int)(Math.random() * 360.0);
		
		System.out.println(randDist + " " + angle + " " + start.x + ":" + start.y);
		
		return getThePointThere(start, randDist, angle);
	}
	
	public Point getThePointThere(Point start, int distance, int angleInDegrees)
	{
		int newX = (int)(start.getX() + distance * Math.cos(angleInDegrees * Math.PI / 180));
		int newY = (int)(start.getY() + distance * Math.sin(angleInDegrees * Math.PI / 180));
		
		return new Point(newX, newY);
	}
	
	
	
	public class Line
	{
		public Point start;
		public Point end;
		public int index;
		
		public Line(Point start, Point end, int index)
		{
			this.start = new Point(start);
			this.end = new Point(end);
			this.index = index;
		}
		
		public double getLenght()
		{
			return Math.sqrt(Math.pow(start.x - end.x, 2) + Math.pow(start.y - end.y, 2));
		}
		
		public void setEndPoint(Point end)
		{
			this.end = new Point(end);
		}
	}
}
