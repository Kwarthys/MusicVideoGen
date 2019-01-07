package mvgen.utils;

import org.jtransforms.fft.DoubleFFT_1D;

/**     //FFT 882pts  ---> amplitude fonction bande fréquences
		//nb de point correspond (882) -> 0-FeMax  -> Res 50Hz ----- Densité spectrale fréquence
		double[] tests = generateSinWave();
		addSinWave(tests, 50);
		addSinWave(tests, 50);
		addSinWave(tests, 50);
		addSinWave(tests, 500);
		addSinWave(tests, 500);
		addSinWave(tests, 5000);


		DoubleFFT_1D dfft1d = new DoubleFFT_1D(tests.length);

		dfft1d.realForward(tests);

		double[] fft = new double[tests.length / 2];

		for(int i = 0; 2*i < tests.length; i++)
		{
			double re = tests[i * 2];
			double im = tests[i * 2 + 1];
			fft[i] = Math.sqrt(re * re + im * im) / tests.length;
		}

 */
public class FFTAnalysor {
	
	protected double[] signalFFT = null;
	protected DoubleFFT_1D fftMaker;
	
	public static final int DSF = 50;

	public void doFFT(double[] samples)
	{
		double[] signalFullFFT = new double[samples.length];
		System.arraycopy(samples, 0, signalFullFFT, 0, samples.length);
		
		fftMaker = new DoubleFFT_1D(samples.length);
		fftMaker.realForward(signalFullFFT);
		
		signalFFT = new double[samples.length / 2];

		for(int i = 0; 2*i+1 < signalFullFFT.length; i++)
		{
			double re = signalFullFFT[i * 2];
			double im = signalFullFFT[i * 2 + 1];
			signalFFT[i] = Math.sqrt(re * re + im * im) / signalFullFFT.length;
		}		
	}
	
	public double[] getFullFtt()
	{
		return signalFFT.clone();
	}


	public static double[] generateSinWave()
	{
		int sampleRate = 44100;
        int signalFrequency = 22000;
        double[] signal = new double[882];
        for(int s = 0; s < 882; s++) {
            double t = s * (1 / (double)sampleRate);
            signal[s] = Math.sin(2 * Math.PI * (double)signalFrequency  * t);
        }
        
        return signal;
	}
	
	
	public static void addSinWave(double[] array, int freq)
	{
		int sampleRate = 44100;
        for(int s = 0; s < array.length; s++) {
            double t = s * (1 / (double)sampleRate);
            array[s] += Math.sin(2 * Math.PI * (double)freq  * t);
        }
	}
}
