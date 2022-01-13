import java.awt.EventQueue;

import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;


public class Startup {
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(new NimbusLookAndFeel());
					new Main();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
