package mvgen.launcher;

import java.awt.Dimension;
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
		LightningGenerator lg = new LightningGenerator();
		
		for(int i = 0; i < 1000; i++)
		{
			BufferedImage img = lg.drawMeALightning(new Dimension(1000,1000), new Point(50,10), new Point(500,500), 4);
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
