package mvgen.model;

import java.util.ArrayList;
import java.util.List;

public class Simple3VoicesSample {

	double[] amplitudes;

	public Simple3VoicesSample(double[] data)
	{
		amplitudes = data.clone();
	}

	public Simple3VoicesSample(AudioSample sample)
	{
		amplitudes = new double[3];
		amplitudes[0] = sample.getBass();
		amplitudes[1] = sample.getMid();
		amplitudes[2] = sample.getHigh();
	}
	
	public double getGlobalAmp() {return (amplitudes[0] + amplitudes[1] + amplitudes[1]) / 3;}

	public double[] getData() {return amplitudes;}

	public static void liftData(List<Simple3VoicesSample> data)
	{
		int lissageNb = 5;

		int sampleNumber = 3;

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

	public static void normalizeData(List<Simple3VoicesSample> data)
	{
		int sampleNumber = 3;
		//Getting max for all 3 bands
		double[] max = new double[sampleNumber];
		for(int vi = 0; vi < sampleNumber; vi++)
		{
			for(Simple3VoicesSample sample : data)
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
			System.out.println(vi + " " + max[vi]);
			for(Simple3VoicesSample sample : data)
			{
				sample.getData()[vi] /= max[vi];
				if(sample.getData()[vi] > 1) System.err.println(vi + " Hz TOO HIGH");
			}
		}

		System.out.println("Normalize Done");
	}
	
	public static ArrayList<Simple3VoicesSample> getSamples(List<AudioSample> rawSamples)
	{
		ArrayList<Simple3VoicesSample> dataSamples = new ArrayList<>();
		for(AudioSample sample : rawSamples)
		{
			dataSamples.add(new Simple3VoicesSample(sample));
		}
		return dataSamples;
	}
}

