package mvgen.utils;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;

public class VideoMaker {
	
	protected int frameRate;
	protected int frameIndex = 0;
	
	protected IMediaWriter writer;

	public void createVideoWriter(int frameRate, Dimension imagesSize)
	{
		writer = ToolFactory.makeWriter("Generated/video.mp4");
		
		writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_MPEG4, imagesSize.width, imagesSize.height);
		
		this.frameRate = frameRate;
	}
	
	public void addFrame(BufferedImage frame)
	{
		BufferedImage bi = convertToType(frame, BufferedImage.TYPE_3BYTE_BGR);
		writer.encodeVideo(0, bi, frameIndex++ * 1000 / frameRate, TimeUnit.MILLISECONDS);
	}
	
	public void closeVideoWriter()
	{
		writer.close();
	}
	

	protected static BufferedImage convertToType(BufferedImage sourceImage, int targetType)
	{
		BufferedImage image;
		if (sourceImage.getType() == targetType) {
			image = sourceImage;
		}
		else {
			image = new BufferedImage(sourceImage.getWidth(),
					sourceImage.getHeight(), targetType);
			image.getGraphics().drawImage(sourceImage, 0, 0, null);
		}
		return image;
	}

}
