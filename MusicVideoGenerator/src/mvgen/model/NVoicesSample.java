package mvgen.model;

import java.util.ArrayList;
import java.util.List;

public class NVoicesSample {

	protected int sampleSize;

	protected double[] amplitudes;

	/** THIS IS TEMPORARY, I DO NOT MASTER THE MATHS BEHIND THAT BLACK AUDIO MAGIC AND REQUIRE A WIZZARD **/
	public NVoicesSample(AudioSample data, int size)
	{
		
		//Music is logarithmic
		//i want size bands of equal logarithmic size
		
		sampleSize = 10;
		this.amplitudes = new double[sampleSize];
		this.amplitudes[0] = data.getFromTo(50, 100);
		this.amplitudes[1] = data.getFromTo(100, 200);
		this.amplitudes[2] = data.getFromTo(200, 400);
		this.amplitudes[3] = data.getFromTo(400, 800);
		this.amplitudes[4] = data.getFromTo(800, 1600);
		this.amplitudes[5] = data.getFromTo(1600, 3200);
		this.amplitudes[6] = data.getFromTo(3200, 6400);
		this.amplitudes[7] = data.getFromTo(6400, 12800);
		this.amplitudes[8] = data.getFromTo(12800, 17000);
		this.amplitudes[9] = data.getFromTo(17000, 22000);
		
		/*
		
		int lastCut = 0;
		double step = Math.pow(22000.0, 1.0/sampleSize);	
		for(int i = 1; i < sampleSize; i++)
		{	
			
			System.out.println((int)Math.pow(step,i) + " -> " + (int)Math.pow(step,i+1));
			this.amplitudes[i] = data.getFromTo((int)Math.pow(step,i), (int)Math.pow(step,i+1));
			
			lastCut = (int)Math.pow(step,i+1);
		}
		
		this.amplitudes[this.amplitudes.length-1] = data.getFromTo(lastCut, 22000);
		
		for(int i = 0; i < this.amplitudes.length; i++)
		{
			System.out.print(this.amplitudes[i] + " ");
		}
		System.out.println();
		*/
	}

	public NVoicesSample(double[] amplitudes)
	{
		this.amplitudes = amplitudes.clone();
		sampleSize = this.amplitudes.length;
	}
	
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
