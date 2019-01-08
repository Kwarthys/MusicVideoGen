package mvgen.omi;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Fenetre extends JFrame{
	
	protected Grapher grapher;
	
	public Fenetre()
	{
		
		setLayout(new BorderLayout());
		
		JPanel container = new JPanel();

		container.setLayout(new BorderLayout());
		
		grapher = new Grapher();
		
		container.add(grapher);
		
		setSize(1300,800);

		setContentPane(container);
		
		addWindowListener(new WindowAdapter()
		{
		    public void windowClosing(WindowEvent e)
		    {
		         System.out.println("exited");
		    }
		});	

		
		setLocationRelativeTo(null);		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		setTitle("WAOW");

		setVisible(true);		
		
		System.out.println("lancement !");
		//go();
	}
/*
	private void go()
	{
		while(true)
		{
			//this.repaint();
			
			try{
				Thread.sleep(10);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}
	*/
	public void setData(List<Double> data)
	{
		grapher.data = new ArrayList<>(data);
		
		System.out.println("grapher.data.size() " + grapher.data.size());
		repaint();
	}

	public void setData(double[] data) {
		grapher.data = new ArrayList<Double>();
		System.out.println();
		for(int i = 0; i < data.length; i++)
		{
			grapher.data.add(data[i]);
			if(data[i] != 0)System.out.println("At " + i + " : " + data[i]);
		}
		System.out.println();
		
		System.out.println("grapher.data.size() " + grapher.data.size());
		repaint();
	}
}
