package qwe;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * 模式询问窗口
 * @author Administrator
 *
 */
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
				// 棋盘窗口
				JFrame frame = new JFrame("fir");
				frame.setSize(VsWorld.WIDTH, VsWorld.HEIGHT);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setLocationRelativeTo(null);
				frame.setResizable(false);

				World world = new CPUWorld();
				world.addListener();
				frame.add(world);
				askWin.dispose();
				frame.setVisible(true);
			}
		};
		buttonCPU.addMouseListener(cpu);

		MouseAdapter vs = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 棋盘窗口
				JFrame frame = new JFrame("fir");
				frame.setSize(VsWorld.WIDTH, VsWorld.HEIGHT);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setLocationRelativeTo(null);
				frame.setResizable(false);

				World world = new VsWorld();
				world.addListener();
				frame.add(world);
				askWin.dispose();
				frame.setVisible(true);
			}
		};
		buttonVS.addMouseListener(vs);
	}

	static JFrame askWin;

	public static void main(String[] args) {
		askWin = new JFrame("请选择");
		askWin.setSize(VsWorld.WIDTH, VsWorld.HEIGHT);
		askWin.setLocationRelativeTo(null);
		askWin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		askWin.setResizable(false);
		ChoseModePanel choseModePanel = new ChoseModePanel();
		askWin.add(choseModePanel);
		askWin.setVisible(true);
	}
}
