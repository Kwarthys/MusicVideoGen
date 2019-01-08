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
	
	public double getFromTo(int fmin, int fmax)
    {
        double amplitude = 0;

        if(rawFFT == null)return 0;

        int indexToStart = (int) Math.floor(fmin / DSF);
        int indexToEnd = (int) Math.ceil(fmax / DSF);

        if((indexToStart > rawFFT.length) || (indexToEnd > rawFFT.length))
        {
        	return 0;        	
        }

        for(int i = indexToStart; i < indexToEnd; ++i)
        {
            amplitude +=  rawFFT[i];
        }

        return amplitude/(indexToEnd-indexToStart);
    }
	/*
	public double getFromTo(int fmin, int fmax)
	{
		double amplitude = 0;

		for(int i = fmin; i < fmax; i+=DSF)
		{
			amplitude += getBand(i) / ((fmax - fmin) / DSF);
		}

		return amplitude;
	}
	*/
	public double getBass() // 0 -> 500Hz
	{
		return getFromTo(50,500);
	}
	
	public double getMid() // 500 -> 3kHz
	{
		return getFromTo(500, 3000);
	}
	
	public double getHigh() // 3kHz -> 22kHz
	{
		return getFromTo(3000, 22000);
	}
}
