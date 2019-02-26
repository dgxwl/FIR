package qwe;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ChoseModePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	public static boolean playWithCPU;

	public ChoseModePanel() {
		JButton buttonCPU = new JButton("单人模式");
		JButton buttonVS = new JButton("双人模式");
		this.add(buttonCPU);
		this.add(buttonVS);
		
		MouseAdapter cpu = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				playWithCPU = true;
			}
		};
		buttonCPU.addMouseListener(cpu);
		
		MouseAdapter vs = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				playWithCPU = false;
			}
		};
		buttonVS.addMouseListener(vs);
	}
}
