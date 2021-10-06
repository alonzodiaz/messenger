package mini.miniView;

import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.function.Supplier;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;

/**
 * Shared scrolling component
 * 
 * @author Jacob Lu
 *
 */
public class VerticalListPanel extends JPanel {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 5168191996461717724L;

	/**
	 * The timer
	 */
	private Timer revalidateTimer; 

	/**
	 * 
	 * @param revalidateInterval  Auto-revalidate interval in milliseconds.  revalidateInterval <= 0 means don't auto-revalidate. 
	 */
	public VerticalListPanel(int revalidateInterval) {

		initGUI();
		if (0<revalidateInterval) {
			revalidateTimer = new Timer(50, (e) -> {
				revalidate();
			});
			revalidateTimer.start();
		}
	}

	/**
	 * Initialize GUI
	 */
	private void initGUI() {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		add(Box.createVerticalGlue());
	}

	/**
	 * @param title : The title to be added
	 * @param fac : The supplier
	 */
	public void addComponent(String title, Supplier<JComponent> fac){
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(new TitledBorder(title));
		Component newComponent = fac.get();
		panel.add(newComponent, BorderLayout.CENTER);
		this.add(panel);
		this.revalidate();  // seems to work better than validate()
		this.repaint();  
	}

}