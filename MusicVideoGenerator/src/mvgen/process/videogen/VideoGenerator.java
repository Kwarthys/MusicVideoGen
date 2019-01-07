package mvgen.process.videogen;

import java.awt.Dimension;
import java.util.List;

import mvgen.model.AudioSample;
import mvgen.utils.VideoMaker;

public abstract class VideoGenerator {	

	protected Dimension imageSize = new Dimension(1920,1080);

	protected int MAX_AMPLITUDE = Math.min(imageSize.width, imageSize.height) / 2;
	protected VideoMaker vm = new VideoMaker();

	protected int printStep = 5;
	protected int threshold = printStep;
	
	protected void createVideoMaker()
	{
		vm.createVideoWriter(50, this.imageSize);
	}
	
	protected void printProgress(int step, int samplesListSize)
	{
		if( step * 100 / samplesListSize  > threshold)
		{
			System.out.println("Video Generation " + threshold + "%.");
			threshold+=printStep;
		}
	}
	
	public abstract void generateVideo(List<AudioSample> samples);
}
