package mvgen.launcher;

import java.awt.Point;
import java.awt.image.BufferedImage;

import mvgen.omi.Drawer;
import mvgen.omi.Visualizer;
import mvgen.process.videogen.graphical.LightningGenerator;

public class QuickTester {
	
	private Drawer d;

	public QuickTester() {

		
		d = new Drawer();
		new Visualizer(d);
		
		tryOneLightninh();
	}

	
	public void tryOneLightninh()
	{
		BufferedImage img = new BufferedImage(1000, 1000, BufferedImage.TYPE_3BYTE_BGR);
		LightningGenerator.drawMeALightning(img, new Point(0,0), new Point(500,500));
		d.repaintWith(img);
	}
}
