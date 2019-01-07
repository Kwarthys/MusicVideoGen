package mvgen.omi;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Grapher extends JPanel{
	
	ArrayList<Double> data = new ArrayList<>();
	
	int yStart = 500;
	
	int signalMaxSize = 400;
	
	

	@Override
	protected void paintComponent(Graphics g)
	{
		g.setColor(Color.BLUE);
		
		g.drawLine(0, yStart, 2000, yStart);
		
		g.setColor(Color.RED);
		
		Point lastPoint = null;
		
		int c = 0;
		
		for(int i = 0; i < this.data.size(); i++)
		{
			int current = (int)(this.data.get(i) * signalMaxSize);
			
			int currentY = yStart - current;
			
			if(lastPoint == null)
				lastPoint = new Point(i, currentY);
			else
			{
				//g.drawLine(10 + i, yStart, 10 + 1, yStart - (current/max*400));
				g.drawLine(lastPoint.x, lastPoint.y, i, currentY);
				
				lastPoint = new Point(i, currentY);
			}
			
		}
	}
}
