package mvgen.process.videogen;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import mvgen.model.AudioSample;
import mvgen.model.Simple3VoicesSample;

public class Video3VoicesGenerator extends VideoGenerator{

	@Override
	public void generateVideo(List<AudioSample> samples) {

		this.createVideoMaker();

		int x = imageSize.width / 2;
		int y = imageSize.height / 2;
		
		ArrayList<Simple3VoicesSample> dataSamples = Simple3VoicesSample.getSamples(samples);
		
		Simple3VoicesSample.normalizeData(dataSamples);
		Simple3VoicesSample.liftData(dataSamples);

		for(int i = 0; i < samples.size(); i++)
		{
			printProgress(i, samples.size());

			BufferedImage frame = new BufferedImage(imageSize.width, imageSize.height, BufferedImage.TYPE_INT_RGB);

			Graphics2D g = frame.createGraphics();
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, imageSize.width, imageSize.height);


			double[] amplitude = new double[3];

			amplitude[0] = dataSamples.get(i).getData()[0];
			amplitude[1] = dataSamples.get(i).getData()[1];
			amplitude[2] = dataSamples.get(i).getData()[2];

			int[] radiuses = new int[3];
			radiuses[0] = (int)(amplitude[0] * MAX_AMPLITUDE);
			radiuses[1] = (int)(amplitude[1] * MAX_AMPLITUDE);
			radiuses[2] = (int)(amplitude[2] * MAX_AMPLITUDE);

			x = imageSize.width / 4;
			g.setColor(Color.red);
			g.fillOval(x - radiuses[0], y - radiuses[0], radiuses[0]*2, radiuses[0]*2);

			x = imageSize.width / 2;
			g.setColor(Color.green);
			g.fillOval(x - radiuses[1], y - radiuses[1], radiuses[1]*2, radiuses[1]*2);

			g.setColor(Color.yellow);
			x =  3 * imageSize.width / 4;
			g.fillOval(x - radiuses[2], y - radiuses[2], radiuses[2]*2, radiuses[2]*2);

			vm.addFrame(frame);

		}

		vm.closeVideoWriter();
		System.out.println("Video Generation done.");
	}
	
	

	

}
