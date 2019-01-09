package mvgen.omi;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Drawer extends JPanel{
	
	private BufferedImage img = null;
	
	public void repaintWith(BufferedImage img)
	{
		this.img = img;
		repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(Color.BLACK);
		if(img != null)
		{
			g2d.fillRect(0, 0, img.getWidth(), img.getHeight());
			g2d.drawImage(img, null, 0, 0);
		}
	}
}
