package mvgen.process.videogen;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.util.List;

import mvgen.model.AudioSample;

public class VideoGenWavesAndCircle extends VideoGenerator{
	
	@Override
	public void generateVideo(List<AudioSample> samples) {
		
		MAX_AMPLITUDE = Math.min(imageSize.width, imageSize.height) / 4;

		createVideoMaker();

		for(int i = 0; i < samples.size(); i++)
		{
			printProgress(i, samples.size());
			
			BufferedImage frame = new BufferedImage(imageSize.width, imageSize.height, BufferedImage.TYPE_INT_RGB);

			Graphics2D g = frame.createGraphics();
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, imageSize.width, imageSize.height);
			

			double[] amplitude = new double[3];


			amplitude[0] = samples.get(i).getBass();

			amplitude[1] = samples.get(i).getMid();

			amplitude[2] = samples.get(i).getHigh();

			
			g.setColor(Color.RED);
			g.setStroke(new BasicStroke(20));			
			Polygon p = getPolyline(amplitude[0], 40, imageSize.width/2, i);						
			g.drawPolyline(p.xpoints, p.ypoints, p.npoints);
			
			g.setColor(Color.GREEN);
			g.setStroke(new BasicStroke(15));			
			p = getPolyline(amplitude[1], 30, imageSize.width/2 + imageSize.width/6, i);						
			g.drawPolyline(p.xpoints, p.ypoints, p.npoints);
			
			g.setColor(Color.BLUE);
			g.setStroke(new BasicStroke(10));			
			p = getPolyline(amplitude[2], 20, imageSize.width/2 + imageSize.width/3, i);						
			g.drawPolyline(p.xpoints, p.ypoints, p.npoints);
			
			int radius = (int)( MAX_AMPLITUDE * ((amplitude[0] + amplitude[1] + amplitude[2]) / 3));
			g.setStroke(new BasicStroke(radius * 0.5f));
			g.setColor(Color.YELLOW);
			g.drawOval(imageSize.width/4 - radius, imageSize.height / 2 - radius, radius * 2 , radius * 2);
			//g.drawArc(x, y, width, height, startAngle, arcAngle);
			
			
			
			vm.addFrame(frame);
		}
		
		vm.closeVideoWriter();
		System.out.println("Video Generation done.");
	}
	
	protected Polygon getPolyline(double a, double p, double xOffset, double yOffset)
	{
		Polygon polygon = new Polygon();
		for(int yi = - 10 ; yi < imageSize.height + 10; yi++)
		{
			polygon.addPoint((int)(xOffset + a * imageSize.width / 12 * Math.sin((double)(yi + yOffset) / p)), yi);
		}
		
		return polygon;
	}
}
