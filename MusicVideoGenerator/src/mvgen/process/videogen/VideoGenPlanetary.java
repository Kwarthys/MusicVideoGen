package mvgen.process.videogen;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import mvgen.model.AudioSample;
import mvgen.utils.VideoMaker;

public class VideoGenPlanetary extends VideoGenerator {
	
	protected Dimension imageSize;
	protected int MAX_AMPLITUDE; 

	@Override
	public void generateVideo(List<AudioSample> samples) {
		imageSize = new Dimension(1920,1080);

		MAX_AMPLITUDE = Math.min(imageSize.width, imageSize.height) / 3 * 2;

		VideoMaker vm = new VideoMaker();
		vm.createVideoWriter(50, imageSize);

		double[] starts = new double[3];		
		starts[0] = 0;
		starts[1] = 0;
		starts[2] = 0;
		
		double sidesIndex = 0;
		//boolean sidesUp = true;

		//Random generator = new Random();

		int printStep = 5;
		int threshold = printStep;
		
		int arcNB = 4;

		for(int i = 0; i < samples.size(); i++)
		{
			if( i * 100 / samples.size()  > threshold)
			{
				System.out.println("Video Generation " + threshold + "%.");
				threshold+=printStep;
			}

			BufferedImage frame = new BufferedImage(imageSize.width, imageSize.height, BufferedImage.TYPE_INT_RGB);

			Graphics2D g = frame.createGraphics();
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, imageSize.width, imageSize.height);


			double[] amplitude = new double[3];


			amplitude[0] = samples.get(i).getBass();

			amplitude[1] = samples.get(i).getMid();

			amplitude[2] = samples.get(i).getHigh();
			
			double globalAmp = (amplitude[0] + amplitude[1] + amplitude[2]) / 3;
			
			/*** Sides ***/
			
			g.setColor(new Color(0x8a0000));
			
			int angle = 15;
			
			int sidesRadius = (int)((imageSize.width/2 - MAX_AMPLITUDE) /4 + MAX_AMPLITUDE);

			g.setStroke(new BasicStroke((float)(amplitude[2] * 70) + 10));			
			
			for(int arci = 0; arci < arcNB; arci++)
			{
				int start = 360 * arci / arcNB;
				g.drawArc(imageSize.width/2 - sidesRadius, imageSize.height/2 - sidesRadius, sidesRadius * 2 , sidesRadius * 2, start + (int)sidesIndex - angle, angle + angle);
			}
			
			sidesIndex+=0.5;
			/*
			if(sidesIndex > 15)
				sidesUp = false;
			else if(sidesIndex < -15)
				sidesUp = true;
			
			if(sidesUp)
			{
				sidesIndex+=0.5;
			}			
			else
			{
				sidesIndex-=0.5;
			}
			
			*/
			
			/*** CENTER ***/
			
			g.setColor(new Color((int)(globalAmp * 255), (int)(globalAmp * 255), (int)(globalAmp * 255)));

			int sunRadius = (int)(MAX_AMPLITUDE * 3 / 4 * (globalAmp) + MAX_AMPLITUDE / 4);
			
			g.fillOval(imageSize.width/2 - sunRadius, imageSize.height/2 - sunRadius, sunRadius * 2 , sunRadius * 2);
			
			
			/*** INNER ARC ***/			
			
			g.setColor(getTeintedColor(amplitude[0]));
			
			int earthRadius = 100;
			double earthSpeed = (amplitude[0] * 10 + 0.5);
			starts[0] += earthSpeed;
			
			g.setStroke(new BasicStroke((float) (amplitude[0] * 30f) + 10));
			
			g.drawArc(imageSize.width/2 - earthRadius, imageSize.height/2 - earthRadius, earthRadius * 2 , earthRadius * 2 , (int) starts[0], 90);
			
			/*** MID ARC ***/
			
			g.setColor(getTeintedColor(amplitude[1]));
			
			int marsRadius = 200;
			double marsSpeed = ( - amplitude[1] * 9 - 0.5);
			starts[1] += marsSpeed;
			
			g.setStroke(new BasicStroke((float) (amplitude[1] * 30f) + 10));
			
			g.drawArc(imageSize.width/2 - marsRadius, imageSize.height/2 - marsRadius, marsRadius * 2 , marsRadius * 2 ,  (int) starts[1], 90);
			
			/*** OUTER ARC ***/
			
			g.setColor(getTeintedColor(amplitude[2]));
			
			int jupiterRadius = 300;
			double juiterSpeed = (amplitude[2] * 8 + 0.5);
			starts[2] += juiterSpeed;
			
			g.setStroke(new BasicStroke((float) (amplitude[2] * 30f) + 20));
			
			g.drawArc(imageSize.width/2 - jupiterRadius, imageSize.height/2 - jupiterRadius, jupiterRadius * 2 , jupiterRadius * 2 ,  (int) starts[2], 90);

			
			vm.addFrame(frame);
		}

		vm.closeVideoWriter();
		System.out.println("Video Generation done.");

	}
	
	protected Color getTeintedColor(double a)
	{
		a = sigmoid(a);
		return new Color(255, 255  - (int)(a * 255), 255 - (int)(a * 255));
	}
	
	public static double sigmoid(double x)
	{
		double lambda = 4;
		return (Math.exp(lambda*x) - 1 )/ (Math.exp(lambda*x) + 1);
	}

}
