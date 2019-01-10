package mvgen.launcher;

import java.io.File;
import java.util.List;

import mvgen.model.AudioSample;
import mvgen.omi.Drawer;
import mvgen.process.audioread.AudioReader;
import mvgen.process.videogen.VideoElementsGenerator;

public class Launcher {
	
	static Drawer d;

	public static void main(String[] args) {

		//File file = new File("deathsound.wav");
		//File file = new File("HeavyFire.wav");
		File file = new File("Bambino.wav");
		//File file = new File("test.wav");
		//File file = new File("RockingHells.wav");	

		//List<Double> samples = new AudioReader().getAmplitudeFromAudio();
		List<AudioSample> samples = new AudioReader().getAmplitudesFreqFromAudio(file);

		//new Video3VoicesGenerator().generateVideo(samples);
		//new VideoGenWavesAndCircle().generateVideo(samples);
		//new VideoGenPlanetary().generateVideo(samples);
		
		
		new VideoElementsGenerator().generateVideo(samples);
		
		//new QuickTester();
	}

}
