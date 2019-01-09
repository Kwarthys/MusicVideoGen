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
		for(int i = 0; i < 8; i--)
		{
			BufferedImage img = new BufferedImage(1000, 1000, BufferedImage.TYPE_3BYTE_BGR);
			new LightningGenerator().drawMeALightning(img, new Point(50,10), new Point(500,500), 4);
			d.repaintWith(img);
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
