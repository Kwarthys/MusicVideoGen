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

	private static double[] window = buildFrameMask(882 * 2);

	private static double[] buildFrameMask(int size)
	{
		double[] window = new double[size];

		double inc = -4.0/size;
		
		int i;
		for (i=0; i<size/4; ++i)
		{
			double val = 1-inc*(i/10-size/4);
			window[i] = val;
			window[size-1-i] = val;

			inc = 4*(val-1)/size;
		}

		for(i=size/4; i<size/2; ++i)
		{
			window[i] = 1;
			window[size-1-i] = 1; 
		}
		
		return window;
	}

	public List<AudioSample> getAmplitudesFreqFromAudio(File file)
	{		
		ArrayList<AudioSample> data = new ArrayList<>();

		try {
			FFTAnalysor fftMaker = new FFTAnalysor();

			WaveFile wav = new WaveFile(file);

			System.out.println(wav.getSampleRate());

			int Fe = 882;

			int frameIndexA = Fe;
			int frameIndexB = 0;
			double[] samplesA = new double[4*Fe];
			double[] samplesB = new double[4*Fe];

			System.out.println("Starting music file Read...");

			while(wav.isReadable())
			{
				double[] array = wav.getAmplitudeArray();

				for(int i = 0; i < array.length; ++i)
				{			
					if(frameIndexA == 2*Fe)
					{
						frameIndexA = 0;
						fftMaker.doFFT(samplesA);

						data.add(new AudioSample(fftMaker.getFullFtt()));

						samplesA = new double[4*Fe];						
					}			
					if(frameIndexB == 2*Fe)
					{
						frameIndexB = 0;
						fftMaker.doFFT(samplesB);

						data.add(new AudioSample(fftMaker.getFullFtt()));

						samplesB = new double[4*Fe];						
					}

					samplesA[frameIndexA] = array[i] * window[frameIndexA];
					samplesB[frameIndexB] = array[i] * window[frameIndexB];					

					++frameIndexA;
					++frameIndexB;
				}				
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		System.out.println("Music read");

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
			e.printStackTrace();
			return null;
		}
	}
}
