package mvgen.process.audioread;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import mvgen.model.AudioSample;
import mvgen.utils.FFTAnalysor;
import mvgen.utils.WaveFile;

public class AudioReader {

	ArrayList<Double> allSamples = new ArrayList<>();
	ArrayList<Double> data = new ArrayList<>();
	
	int FRAME_RATE = 50;
	
	public List<AudioSample> getAmplitudesFreqFromAudio(File file)
	{		
		ArrayList<AudioSample> data = new ArrayList<>();

		try {
			FFTAnalysor fftMaker = new FFTAnalysor();
			
			WaveFile wav = new WaveFile(file);

			System.out.println(wav.getSampleRate());
			
			int Fe = 882;

			int frameIndex = 0;
			
			double[] samples = new double[Fe];

			while(wav.isReadable())
			{
				double[] array = wav.getAmplitudeArray();

				for(int i = 0; i < array.length; i++)
				{			
					if(frameIndex == Fe)
					{
						frameIndex = 0;
						fftMaker.doFFT(samples);
						
						data.add(new AudioSample(fftMaker.getFullFtt()));
						
						samples = new double[Fe];
						
					}
					else
					{
						samples[frameIndex] = array[i];
					}
					
					frameIndex++;
				}				
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return data;
	}

	public List<Double> getAmplitudeFromAudio()
	{		
		//File file = new File("deathsound.wav");
		//File file = new File("HeavyFire.wav");
		File file = new File("Bambino.wav");

		try {
			WaveFile wav = new WaveFile(file);

			System.out.println(wav.getSampleRate());
			
			boolean first = true;
			
			double localMax = Double.MIN_VALUE;
			
			int frameIndex = 0;
			
			while(wav.isReadable())
			{
				double[] array = wav.getAmplitudeArray();

				for(int i = 0; i < array.length; i++)
				{					
					if(!first)
					{
						//Update the localMax
						if(Math.abs(array[i]) > localMax)
						{
							localMax = Math.abs(array[i]);
						}

						if(frameIndex % (wav.getSampleRate() / FRAME_RATE) == 0)   //Going from wav.getSampleRate() Audio "Sample Rate" to a video frame rate
						{
							//it's Time to take a sample !
							data.add(localMax);

							localMax = Double.MIN_VALUE;
						}
					}
					else
					{						
						//taking the first sample without asking questions
						data.add(array[i]);
						first = false;
					}
					
					frameIndex ++;

					allSamples.add(array[i]);
				}				
			}
			
			return data;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
