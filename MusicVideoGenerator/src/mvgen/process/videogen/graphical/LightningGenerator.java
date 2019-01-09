package mvgen.process.videogen.graphical;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class LightningGenerator {

	public static void drawMeALightning(BufferedImage toAlter, Point start, Point end)
	{
		Graphics2D g = (Graphics2D) toAlter.getGraphics();
		g.setColor(Color.RED);
		g.fillRect(start.x, start.y, 500, 500);
	}
}
