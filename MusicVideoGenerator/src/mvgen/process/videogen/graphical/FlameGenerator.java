package mvgen.process.videogen.graphical;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class FlameGenerator {

	public FlameGenerator()
	{
		
	}
	
	public BufferedImage drawMeTheFlames(Dimension imageSize)
	{
		BufferedImage image = new BufferedImage(imageSize.width, imageSize.height, BufferedImage.TYPE_4BYTE_ABGR);

		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setColor(new Color(0, 0, 0, 0));
		g.fillRect(0, 0, image.getWidth(), image.getHeight());
		
		return image;
	}
}
