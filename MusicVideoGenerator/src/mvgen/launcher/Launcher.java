package mvgen.launcher;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import mvgen.model.AudioSample;
import mvgen.process.audioread.AudioReader;
import mvgen.process.videogen.VideoElementsGenerator;

public class Launcher {

	public static void main(String[] args) {

		//File file = new File("deathsound.wav");
		//File file = new File("HeavyFire.wav");
		File file = new File("Bambino.wav");
		//File file = new File("test.wav");
		//File file = new File("RockingHells.wav");

		//List<Double> samples = new AudioReader().getAmplitudeFromAudio();
		List<AudioSample> samples = new AudioReader().getAmplitudesFreqFromAudio(file);
		ArrayList<Double> list = new ArrayList<>();
		double[] signal = new double[samples.size()];
		for(int i = 0; i < samples.size(); i++)
		{
			signal[i] = samples.get(i).getHigh();
			list.add(signal[i]);
		}

		//new Video3VoicesGenerator().generateVideo(samples);
		//new VideoGenWavesAndCircle().generateVideo(samples);*
		//new VideoGenPlanetary().generateVideo(samples);
		new VideoElementsGenerator().generateVideo(samples);
		//Fenetre f = new Fenetre();
		//f.setData(signal);
	}

}
