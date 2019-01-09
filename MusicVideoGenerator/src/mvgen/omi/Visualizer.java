package mvgen.omi;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Visualizer extends JFrame{

	public Visualizer(Drawer d)
	{		
		setLayout(new BorderLayout());
		
		JPanel container = new JPanel();

		container.setLayout(new BorderLayout());
		
		container.add(d);
		
		setSize(1300,800);

		setContentPane(container);
		
		setLocationRelativeTo(null);		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	

		setVisible(true);
	}
}
