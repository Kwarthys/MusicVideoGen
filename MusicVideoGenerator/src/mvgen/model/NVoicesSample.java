package mvgen.model;

import java.util.ArrayList;
import java.util.List;

import mvgen.utils.FFTAnalysor;

public class NVoicesSample {

	protected int sampleSize;

	protected double[] amplitudes;
	
	protected static double[] fill10Amps(AudioSample data)
	{
		double[] amplitudes = new double[10];
		amplitudes[0] = data.getFromTo(50, 100);
		amplitudes[1] = data.getFromTo(100, 200);
		amplitudes[2] = data.getFromTo(200, 400);
		amplitudes[3] = data.getFromTo(400, 800);
		amplitudes[4] = data.getFromTo(800, 1600);
		amplitudes[5] = data.getFromTo(1600, 3200);
		amplitudes[6] = data.getFromTo(3200, 6400);
		amplitudes[7] = data.getFromTo(6400, 12800);
		amplitudes[8] = data.getFromTo(12800, 17000);
		amplitudes[9] = data.getFromTo(17000, 22000);
		
		return amplitudes;
	}

	public NVoicesSample(AudioSample data, int size)
	{
		double[] tempAmplitudes = new double[size];
		this.sampleSize = size;
		
		int margin = 4;
		
		ArrayList<Integer> bandToIgnore = new ArrayList<>();
		
		double step = Math.pow(22000.0, 1.0/(sampleSize+margin));	
		for(int i = 0; i < sampleSize; i++)
		{
			int lowerFreq = (int)Math.pow(step,i+margin);
			int higherFreq = (int)Math.pow(step,i+1+margin);
			
			int[] predictedActualBand = { (int)(lowerFreq/FFTAnalysor.DSF), (int)(higherFreq / FFTAnalysor.DSF) };
			
			if(predictedActualBand[0] == predictedActualBand[1])
			{
				bandToIgnore.add(new Integer(i));
			}
			else
			{
				//System.out.println(lowerFreq + " -> " + higherFreq);
				tempAmplitudes[i] = data.getFromTo(lowerFreq, higherFreq);
			}
		}
		
		this.sampleSize -=  bandToIgnore.size();
		this.amplitudes = new double[this.sampleSize];
		for(int i = 0, c = 0; i < size; ++i)
		{
			if(!bandToIgnore.contains(i))
			{
				this.amplitudes[c++] = tempAmplitudes[i];
			}
		}
	}

	public NVoicesSample(double[] amplitudes)
	{
		this.amplitudes = amplitudes.clone();
		sampleSize = this.amplitudes.length;
	}
	
	public int getSampleSize() {return sampleSize;}
	
	public double getGlobalAmp()
	{
		double globalAmp = 0;
		for(int i = 0; i < this.amplitudes.length; i++)
		{
			globalAmp += this.amplitudes[i];
		}
		
		return globalAmp / this.amplitudes.length;
	}
	
	public double[] getData()
	{
		return this.amplitudes;
	}
	
	public static void liftData(List<NVoicesSample> data)
	{
		int lissageNb = 5;

		int sampleNumber = data.get(0).sampleSize;

		for(int i = 0; i < data.size(); i++)
		{
			double nb = 0;
			double[] average = new double[sampleNumber];

			for(int xi = -lissageNb; xi <= lissageNb; xi++)
			{
				if(i + xi > 0 && i + xi < data.size())
				{
					nb++;
					for(int fi = 0; fi < sampleNumber; fi++)
					{
						average[fi] += data.get(xi + i).getData()[fi];						
					}
				}
			}

			for(int fi = 0; fi < sampleNumber; fi++)	
			{
				data.get(i).getData()[fi] = average[fi]/nb;
			}
		}		

		System.out.println("Lift done");
	}

	public static void normalizeData(List<NVoicesSample> data)
	{
		int sampleNumber = data.get(0).sampleSize;
		//Getting max for all 3 bands
		double[] max = new double[sampleNumber];
		for(int vi = 0; vi < sampleNumber; vi++)
		{
			for(NVoicesSample sample : data)
			{
				double current = 0;

				current = sample.getData()[vi];

				if(current > max[vi])
				{
					max[vi] = current;
				}
			}
		}

		for(int vi = 0; vi < sampleNumber; vi++)
		{
			System.out.print(vi + " : " + max[vi] + " | ");
			for(NVoicesSample sample : data)
			{
				sample.getData()[vi] /= max[vi];
				if(sample.getData()[vi] > 1) System.err.println(vi + " Hz TOO HIGH");
			}
		}
		System.out.println();
		System.out.println("Normalize Done");
	}
	
	public static ArrayList<NVoicesSample> getSamples(List<AudioSample> rawSamples, int size)
	{
		ArrayList<NVoicesSample> dataSamples = new ArrayList<>();
		for(AudioSample sample : rawSamples)
		{
			dataSamples.add(new NVoicesSample(sample, size));
		}
		return dataSamples;
	}
}
