package mvgen.process.videogen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import mvgen.utils.VideoMaker;

public class VideoAmplitudeGenerator {
	
	public static int MAX_AMPLITUDE = 200;
	
	public static void generateVideo(List<Double> samples)
	{

		Dimension imageSize = new Dimension(1080,720);

		VideoMaker vm = new VideoMaker();
		vm.createVideoWriter(50, imageSize);

		//Random generator = new Random();

		int x = imageSize.width / 2;
		int y = imageSize.height / 2;

		for(int i = 0; i < samples.size(); i++)
		{
			BufferedImage frame = new BufferedImage(imageSize.width, imageSize.height, BufferedImage.TYPE_INT_RGB);
			
			int radius = (int)(samples.get(i) * MAX_AMPLITUDE);

			Graphics2D g = frame.createGraphics();
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, imageSize.width, imageSize.height);

			g.setColor(Color.RED);
			g.fillOval(x - radius, y - radius, radius*2, radius*2);

			vm.addFrame(frame);
		}
		
		vm.closeVideoWriter();
	}
}
