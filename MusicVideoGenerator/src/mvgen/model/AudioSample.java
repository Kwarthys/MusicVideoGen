package mvgen.model;

import mvgen.utils.FFTAnalysor;

public class AudioSample {
	
	protected double[] rawFFT;
	protected static final int DSF = FFTAnalysor.DSF;
	
	public AudioSample(double[] fft)
	{
		this.rawFFT = fft;
	}
	
	public double[] getSignal() {return rawFFT;}
	
	public double getBand(int frequency)
	{
		if(rawFFT == null)return 0;
		
		int indexToLookFor = frequency / DSF;
		
		if(indexToLookFor > rawFFT.length)return 0;
		
		return rawFFT[indexToLookFor];
	}
	
	protected double getFromTo(int fmin, int fmax)
	{
		double amplitude = 0;

		for(int i = fmin; i < fmax; i+=DSF)
		{
			amplitude += getBand(i) / ((fmax - fmin) / DSF);
		}

		return amplitude;
	}
	
	public double getBass() // 0 -> 200Hz
	{
		return getFromTo(50,500);
	}
	
	public double getMid() // 200 -> 1kHz
	{
		return getFromTo(500, 3000);
	}
	
	public double getHigh() // 1kHz -> 22kHz
	{
		return getFromTo(3000, 22000);
	}
}
