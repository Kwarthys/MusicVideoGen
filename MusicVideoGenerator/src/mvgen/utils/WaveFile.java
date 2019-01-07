package mvgen.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class WaveFile {
    public final int NOT_SPECIFIED = AudioSystem.NOT_SPECIFIED; // -1
    public final int INT_SIZE = 4;

    private int sampleSize = NOT_SPECIFIED;
    private long framesCount = NOT_SPECIFIED;
    private int sampleRate = NOT_SPECIFIED;
    private int channelsNum;
    private byte[] data;      // wav bytes
    private AudioInputStream audioInputStream;
    private AudioFormat format;
    
    private byte[] buffer;
    private final int CHUNKSIZE = 1024;

    private Clip clip;
    private boolean canPlay;
    
    private boolean readable = true;

    public WaveFile(File file) throws UnsupportedAudioFileException, IOException {
        if (!file.exists()) {
            throw new FileNotFoundException(file.getAbsolutePath());
        }

        AudioInputStream ais = AudioSystem.getAudioInputStream(file);

        format = ais.getFormat();
        
		audioInputStream = AudioSystem.getAudioInputStream(format, ais);
		
		buffer = new byte[format.getFrameSize()];
		
        ais = AudioSystem.getAudioInputStream(file);

        framesCount = ais.getFrameLength();

        sampleRate = (int) format.getSampleRate();

        sampleSize = format.getSampleSizeInBits() / 8;

        channelsNum = format.getChannels();

        long dataLength = framesCount * format.getSampleSizeInBits() * format.getChannels() / 8;

        data = new byte[(int) dataLength];
        ais.read(data);

        AudioInputStream aisForPlay = AudioSystem.getAudioInputStream(file);
        try {
            clip = AudioSystem.getClip();
            clip.open(aisForPlay);
            clip.setFramePosition(0);
            canPlay = true;
        } catch (LineUnavailableException e) {
            canPlay = false;
            System.out.println("I can play only 8bit and 16bit music.");
        }
    }

    public boolean isCanPlay() {
        return canPlay;
    }

    public void play() {
        clip.start();
    }

    public void stop() {
        clip.stop();
    }

    public AudioFormat getAudioFormat() {
        return format;
    }

    public int getSampleSize() {
        return sampleSize;
    }

    public double getDurationTime() {
        return getFramesCount() / getAudioFormat().getFrameRate();
    }

    public long getFramesCount() {
        return framesCount;
    }


    /**
     * Returns sample (amplitude value). Note that in case of stereo samples
     * go one after another. I.e. 
     * 0 - first sample of left channel, 
     * 1 - first sample of the right channel, 
     * 2 - second sample of the left channel, 
     * 3 - second sample of the rigth channel, etc.
     */
    public int getSampleInt(int sampleNumber) {

        if (sampleNumber < 0 || sampleNumber >= data.length / sampleSize) {
            throw new IllegalArgumentException(
                    "sample number can't be < 0 or >= data.length/"
                            + sampleSize);
        }

        byte[] sampleBytes = new byte[4]; //4byte = int

        for (int i = 0; i < sampleSize; i++) {
            sampleBytes[i] = data[sampleNumber * sampleSize * channelsNum + i];
        }

        int sample = ByteBuffer.wrap(sampleBytes)
                .order(ByteOrder.LITTLE_ENDIAN).getInt();
        return sample;
    }
    
    /**** CUSTOM ***/
    public double read()
    {
    	try {
			int read = audioInputStream.read(buffer);
			if (read == -1) {
				return Double.NaN;
			} else {
				short sample = (short) ((((int) buffer[1] & 0xff) << 8)
						+ ((int) buffer[0] & 0xff));
				return (sample) / 32767.0;
			}
		} catch (Exception e) {
			return 0;
		}
    }
    
    public double[] getAmplitudeArray() {
        double[] chunk = new double[this.CHUNKSIZE];
		for (int i = 0; i < chunk.length; i++)
		{
			double read = read();
			if(Double.isNaN(read))
			{
				readable = false;
				//System.err.println("EndDetected");
				chunk[i] = 0;
			}
			else
			{
				chunk[i] = read;
			}
			//System.out.println(i + " -> " + chunk[i]);
		}
		return chunk;
	}
    
    public boolean isReadable()
    {
    	return readable;
    }
    
    /***************/

    public int getSampleRate() {
        return sampleRate;
    }

    public Clip getClip() {
        return clip;
    }
}
