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
import mvgen.model.NVoicesSample;

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

		ArrayList<NVoicesSample> dataSamples = NVoicesSample.getSamples(samples,20);		
		NVoicesSample.normalizeData(dataSamples);
		NVoicesSample.liftData(dataSamples);

		for(int i = 0; i < dataSamples.size(); i++)
		{
			printProgress(i, dataSamples.size());
			
			vm.addFrame(getAFrame(dataSamples.get(i)));
		}

		vm.closeVideoWriter();
		System.out.println("Video Done");
	}
	
	private BufferedImage getAFrame(NVoicesSample sample)
	{		
		Graphics2D g = frame.createGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, imageSize.width, imageSize.height);

		double[] amplitude = new double[sample.getSampleSize()];
		
		for(int i = 0; i < amplitude.length; i++)
		{
			amplitude[i] = sample.getData()[i];
		}

		float shipModifier = (float) (0.3f + sample.getGlobalAmp()/3);
		
		/*** Drawing light effect around ship ***/
		float[] dist = {0.75f, 1.0f};
		Color[] colors = {Color.WHITE, Color.BLACK};
		int gradientCircleRadius = (int) (imageSize.height);
		
		g.setPaint(new RadialGradientPaint(new Point(imageSize.width/2, imageSize.height/2), ship.getHeight() * shipModifier * 1.1f, dist, colors));
		g.fillOval(imageSize.width/2 - gradientCircleRadius/2, imageSize.height/2 - gradientCircleRadius/2, gradientCircleRadius, gradientCircleRadius);

		/*** Drawing the ship ***/
		g.drawImage(ship, new AffineTransformOp(new AffineTransform(shipModifier,0f,0f,shipModifier,0,0),null), (int)(imageSize.width - ship.getWidth()*shipModifier)/2, (int)(imageSize.height - ship.getHeight()*shipModifier)/2);

		g.setColor(Color.ORANGE);
		int bandLenght = (int)(imageSize.getWidth() / amplitude.length);
		for(int i = 0; i < amplitude.length; i++)
		{
			g.fillRect(bandLenght*i, 0, bandLenght, 50 + (int) (amplitude[i] * 800.0));
		}
		
		
		return frame;
	}

}
