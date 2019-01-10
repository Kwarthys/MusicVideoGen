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
import mvgen.process.videogen.graphical.LightningGenerator;
import mvgen.utils.GeometryMaths;

public class VideoElementsGenerator extends VideoGenerator {
	
	private BufferedImage ship;
	
	private BufferedImage lightning = null;
	
	private int counter = 0;

	@Override
	public void generateVideo(List<AudioSample> samples) {

		createVideoMaker();

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
		
		int askedBandNumber = 200;

		ArrayList<NVoicesSample> dataSamples = NVoicesSample.getSamples(samples,askedBandNumber);		
		NVoicesSample.normalizeData(dataSamples);
		NVoicesSample.liftData(dataSamples);
		
		System.out.println(askedBandNumber + " bands asked, " + dataSamples.get(0).getSampleSize() + " given.");

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
		BufferedImage frame = new BufferedImage(imageSize.width, imageSize.height, BufferedImage.TYPE_INT_RGB);	
		Graphics2D g = frame.createGraphics();
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, imageSize.width, imageSize.height);

		double[] amplitude = new double[sample.getSampleSize()];
		
		for(int i = 0; i < amplitude.length; i++)
		{
			amplitude[i] = sample.getData()[i];
		}

		float shipModifier = (float) (0.3f + sample.getGlobalAmp()/3);
		
		int roundedShipRadius = (int) (ship.getHeight() * shipModifier);
		
		/*** Drawing light effect around ship ***/
		float[] dist = {0.75f, 1.0f};
		Color[] colors = {Color.WHITE, Color.BLACK};
		int gradientCircleRadius = (int) (imageSize.height);
		
		g.setPaint(new RadialGradientPaint(new Point(imageSize.width/2, imageSize.height/2), roundedShipRadius * 1.1f, dist, colors));
		g.fillOval(imageSize.width/2 - gradientCircleRadius/2, imageSize.height/2 - gradientCircleRadius/2, gradientCircleRadius, gradientCircleRadius);

		/*** Drawing the ship ***/
		g.drawImage(ship, new AffineTransformOp(new AffineTransform(shipModifier,0f,0f,shipModifier,0,0),null), (int)(imageSize.width - ship.getWidth()*shipModifier)/2, (int)(imageSize.height - ship.getHeight()*shipModifier)/2);

		g.setColor(Color.ORANGE);
		int bandLenght = (int)(imageSize.getWidth() / amplitude.length);
		for(int i = 0; i < amplitude.length; i++)
		{
			g.fillRect(bandLenght*i, 0, bandLenght, 5 + (int) (amplitude[i] * 800.0));
		}
		
		int animStart = 5;
		int animEnd = animStart + 5;
		
		if(counter >= animStart)
		{			
			addTheLightning(g, counter - animStart, roundedShipRadius);
			
			if(counter > animEnd)
			{
				counter = 0;
				lightning = null;
			}
		}
		
		++counter;
		
		
		return frame;
	}
	
	private final static int[] tableAlpha = { 140, 160, 140, 120, 100};  
	private void addTheLightning(Graphics2D g, int animationIndex, int centerRadius)
	{		
		if(lightning == null)
		{
			Point start = GeometryMaths.getARandomPointAt(getFrameCenter(), imageSize.width/2);
			Point end = GeometryMaths.getARandomPointAt(getFrameCenter(), centerRadius);
			
			for(int xi = 0; xi < 100; xi++)
			{
				Point contender = GeometryMaths.getARandomPointAt(getFrameCenter(), centerRadius);
				
				if(contender.distance(start) < end.distance(start))
				{
					end = contender;
				}				
			}
			
			lightning = new LightningGenerator().drawMeALightning(imageSize, start, end, 4);
		}
		
		g.setColor(new Color(0,0,0,animationIndex < tableAlpha.length ? tableAlpha[animationIndex] : 100));
		g.fillRect(0, 0, imageSize.width, imageSize.height);
		
		g.drawImage(lightning, null, 0,0);
	}
	
	private Point getFrameCenter()
	{
		return new Point(imageSize.width/2, imageSize.height/2);
	}

}
