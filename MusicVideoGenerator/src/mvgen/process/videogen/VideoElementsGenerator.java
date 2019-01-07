package mvgen.process.videogen;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import mvgen.model.AudioSample;
import mvgen.model.Simple3VoicesSample;

public class VideoElementsGenerator extends VideoGenerator {
	
	private BufferedImage ship;
	private BufferedImage frame;

	@Override
	public void generateVideo(List<AudioSample> samples) {

		createVideoMaker();
		
		frame = new BufferedImage(imageSize.width, imageSize.height, BufferedImage.TYPE_INT_RGB);

		ship = null;

		try {
			ship = ImageIO.read(new File("bateau-pirate.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		if(ship == null)
		{
			System.err.println("No ship, returning to port.");
			return;
		}

		ArrayList<Simple3VoicesSample> dataSamples = Simple3VoicesSample.getSamples(samples);		
		Simple3VoicesSample.normalizeData(dataSamples);
		Simple3VoicesSample.liftData(dataSamples);

		for(int i = 0; i < dataSamples.size(); i++)
		{
			printProgress(i, dataSamples.size());
			
			vm.addFrame(getAFrame(dataSamples.get(i)));
		}

		vm.closeVideoWriter();
		System.out.println("Video Done");
	}
	
	private BufferedImage getAFrame(Simple3VoicesSample sample)
	{		
		Graphics2D g = frame.createGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, imageSize.width, imageSize.height);

		double[] amplitude = new double[3];

		amplitude[0] = sample.getData()[0];
		amplitude[1] = sample.getData()[1];
		amplitude[2] = sample.getData()[2];

		float shipModifier = (float) (0.3f + sample.getGlobalAmp()/3);
		
		/*** Drawing light effect around ship ***/
		float[] dist = {0.75f, 1.0f};
		Color[] colors = {Color.WHITE, Color.BLACK};
		int gradientCircleRadius = (int) (imageSize.height);
		
		g.setPaint(new RadialGradientPaint(new Point(imageSize.width/2, imageSize.height/2), ship.getHeight() * shipModifier * 1.1f, dist, colors));
		g.fillOval(imageSize.width/2 - gradientCircleRadius/2, imageSize.height/2 - gradientCircleRadius/2, gradientCircleRadius, gradientCircleRadius);

		/*** Drawing the ship ***/
		g.drawImage(ship, new AffineTransformOp(new AffineTransform(shipModifier,0f,0f,shipModifier,0,0),null), (int)(imageSize.width - ship.getWidth()*shipModifier)/2, (int)(imageSize.height - ship.getHeight()*shipModifier)/2);

		
		return frame;
	}

}
